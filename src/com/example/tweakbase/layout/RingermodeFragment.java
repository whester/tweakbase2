package com.example.tweakbase.layout;

import java.util.ArrayList;
import java.util.List;

import com.example.tweakbase.DatabaseHandler;
import com.example.tweakbase.R;
import com.example.tweakbase.TBRingermodeProfiles;

import android.support.v4.app.ListFragment;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class RingermodeFragment extends ListFragment {
	
	final String TAG = "RingermodeFragment";
	List<TBRingermodeProfiles> rmprofiles = new ArrayList<TBRingermodeProfiles>();
	ListView listView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.ringermode_list, container, false);
		listView = (ListView) rootView.findViewById(android.R.id.list);
		
		// This is where I need to get the values for ringermode profiles
		DatabaseHandler db = new DatabaseHandler(getActivity());
		
		rmprofiles = db.getAllActiveRMP();
		if(!rmprofiles.isEmpty()){
		    
			ArrayAdapter<TBRingermodeProfiles> adapter = new ArrayAdapter<TBRingermodeProfiles>(getActivity(),
			        R.layout.ringermode_list_item, rmprofiles);
		    listView.setAdapter(adapter);
		    
		}else{
			Log.d(TAG, "No Ringermode profiles");
			
			String [] message = new String[] {"There are currently no ringer mode profiles"};
		    ArrayAdapter<String> emptyadapter = new ArrayAdapter<String>(getActivity(),
			        R.layout.ringermode_list_item, message);
		    listView.setAdapter(emptyadapter);
		}
		
		db.close();
		
		return rootView;
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
		listView.invalidateViews();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		listView.invalidateViews();
	}
	
	@Override
    public void onListItemClick(ListView l, View v, final int position, long id) {
		
		final DatabaseHandler db = new DatabaseHandler(getActivity());		

		if(!db.getAllActiveRMP().isEmpty()){
			final long dbid = db.getAllActiveRMP().get(position).getID();
			
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
			alertDialog.setTitle("Deactivate Ringermode");
			alertDialog.setMessage("Are you sure you want to deactivate this profile?");
			alertDialog.setCancelable(true);
			alertDialog.setIcon(R.drawable.ic_action_volume_on);

			// OK button action
			alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
						db.setProfileAsNotInUse(dbid);
						rmprofiles.remove(position);
						listView.invalidateViews();
		           }
		       });
			
			// Cancel button action
			alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   // DO NOTHING
		           }
		       });
			
			AlertDialog confirm = alertDialog.create();
			confirm.show();
		}else{
			Log.d(TAG, "No entries in the table");
		}
		
		db.close();
    }

}
