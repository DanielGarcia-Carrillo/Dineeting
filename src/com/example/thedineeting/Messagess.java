package com.example.thedineeting;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Messagess extends ListActivity {

	ListView listView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.messages);
		
		String[] finalevents = {"Tyler- I want to go to chipotle for Mexican food", "Jobin- I want to grab some burgers", "Alyssa- I've been to pizza hut too many times"};
		ArrayAdapter<String> vadapter = new ArrayAdapter<String>(this, R.layout.list_fruit, finalevents);
		setListAdapter(vadapter);

		//listView = (ListView) findViewById(R.id.list);
		listView = getListView();
		listView.setTextFilterEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.messagess, menu);
		return true;
	}

}
