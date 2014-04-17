package com.example.tweakbase.layout;

import com.example.tweakbase.R;
import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ApplicationFragment extends ListFragment {
	
	final String TAG = "ApplicationFragment";
	ListView listView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.applications_list, container, false);
		listView = (ListView) rootView.findViewById(android.R.id.list);
		
		// This is where I need to get the values for ringermode profiles
	    String[] values = new String[] { "Application 1", "Application 2", "Application 3", "Application 4" };
	    
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
	        R.layout.applications_list_item, values);
	    listView.setAdapter(adapter);
		
		return rootView;
	}
}
