package com.example.tweakbase.layout;

import java.util.ArrayList;
import java.util.List;

import com.example.tweakbase.DatabaseHandler;
import com.example.tweakbase.R;
import com.example.tweakbase.TBRingermodeProfiles;

import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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
    public void onListItemClick(ListView l, View v, int position, long id) {
		
		DatabaseHandler db = new DatabaseHandler(getActivity());		

		if(!db.getAllActiveRMP().isEmpty()){
			Toast.makeText(getActivity(), "Deactiving profile...", Toast.LENGTH_LONG).show();
			long dbid = db.getAllActiveRMP().get(position).getID();
			db.setProfileAsNotInUse(dbid);
			rmprofiles.remove(position);
			listView.invalidateViews();
			Toast.makeText(getActivity(), "Successfully deactived profile", Toast.LENGTH_LONG).show();
		}else{
			Log.d(TAG, "No entries in the table");
		}
		
		db.close();
    }

}
