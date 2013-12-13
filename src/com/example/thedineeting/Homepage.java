package com.example.thedineeting;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;


import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class Homepage extends ListActivity {

	String[] events;
	ListView list;
	AlertDialog alertDialog;
	final Context context = this;
	static final String[] FRUITS = new String[] { "==== EVENTS ", "Avocado", "Banana",
		"Blueberry", "Coconut", "Durian", "Guava", "Kiwifruit",
		"Jackfruit", "Mango", "Olive", "Pear", "Sugar-apple" };


	ListView listView;
	String yo;
	String eid;
	String veid;
	SendHttpRequestTask t;
	boolean backrunning = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent in = getIntent();
		yo = in.getExtras().getString("login");
		Log.e("homepage", yo);
		//	String[] array = b.getStringArray("hevents");

		//	Log.e("homepage", "yo " + array.length);
		/*
		Intent in = getIntent();

		ArrayList<String> messages = in.getStringArrayListExtra("hevents");
		Log.e("homepage", "ho" + messages.size());
		 */


		String cmd = "homeupdate " + yo;

		String data = sendHttpRequest(MainActivity.url, cmd);
		events = data.split("::");
		String[] finalevents = new String[events.length+1];

		for(int i=0; i < events.length; i++){
			finalevents[i] = events[i];
		}
		finalevents[finalevents.length-1] = "CREATE NEW EVENT";
		Log.e("homepage", data);
		//System.out.println("Data ["+data+"]");

		// no more this
		// setContentView(R.layout.list_fruit);

		setListAdapter(new ArrayAdapter<String>(this, R.layout.list_fruit, finalevents));

		listView = getListView();
		listView.setTextFilterEnabled(true);

		/*
		ArrayList<String> foo = new ArrayList<String>();
		foo.add("manu");
		foo.add("kobe");
		foo.add("toby");
		 */		







		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				String selected = (String)listView.getItemAtPosition(position);
				String[] params = selected.split(" ");
				Log.e("homepage", selected);

				veid = params[2];

				if(veid.equals("EVENT")){
					backrunning = false;
					t.cancel(true);
					Intent in = new Intent(getApplicationContext(), Restaurant.class);
					in.putExtra("login", yo);
					startActivity(in);
				}
				else{
					Intent in = new Intent(getApplicationContext(),EventsStatus.class);
					in.putExtra("veid", veid);
					startActivity(in);
				}

				//   setContentView(R.layout.list_fruit);
				// When clicked, show a toast with the TextView text
				//   Toast.makeText(getApplicationContext(),
				//	((TextView) view).getText(), Toast.LENGTH_SHORT).show();
			}
		});


		t = new SendHttpRequestTask();
		cmd = "notificationupdate " + yo ;
		String[] params = new String[]{MainActivity.url, cmd};
		t.execute(params);


		/*
		Button btn = (Button) findViewById(R.id.neweventbutton);
		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(getApplicationContext(), Restaurant.class);
				//intent.putExtra("login", s);

				startActivity(intent);
			}
		});
		 */


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.homepage, menu);
		return true;
	}

	public void populateEvents()
	{	
		setListAdapter(new ArrayAdapter<String>(this, R.layout.list_fruit,events));

		ListView listView = getListView();
		listView.setTextFilterEnabled(true);

		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent in = new Intent(getApplicationContext(),EventsStatus.class);
				String message = "this is batman";

				startActivity(in);


				//   setContentView(R.layout.list_fruit);
				// When clicked, show a toast with the TextView text
				//   Toast.makeText(getApplicationContext(),
				//	((TextView) view).getText(), Toast.LENGTH_SHORT).show();
			}
		});
	}

	private class SendHttpRequestTask extends AsyncTask<String, Void, String> {


		@Override
		protected String doInBackground(String... params) {


			String url = params[0];
			String name = params[1];
			String data ="";

			while(backrunning){
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(!isFinishing()){
					data = sendHttpRequest(url, name);
					Log.e("looping", data);
					if(!data.equals("none")){
						break;
					}
				}
			}
			//System.out.println("Data ["+data+"]");

			return data;
		}

		@Override
		protected void onPostExecute(String result) {

			/*
			events = result.split("::");
			populateEvents();

			for(int i=0; i < events.length; i++){
				Log.e("homepage", events[i]);

			}
			 */

			if(!result.equals("none")){
				String[] foo = result.split(" ");
				if(foo[0].equals("newvoting")){
					newvotingNotification(result);
				}
				else if(foo[0].equals("votingover")){
					votingoverNotification(result);
				}

				SendHttpRequestTask t = new SendHttpRequestTask();
				String cmd = "notificationupdate " + yo ;
				String[] params = new String[]{MainActivity.url, cmd};
				t.execute(params);
			}
			//	item.setActionView(null);

		}



	}

	public void newvotingNotification(String data){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);


		String[] notes = data.split(" ");
		eid = notes[1];

		StringBuffer buf = new StringBuffer();
		for(int i=2; i < notes.length; i++){
			buf.append(notes[i] + " ");
		}
		String mesg = buf.toString();
		// set title
		alertDialogBuilder.setTitle("Dining Invite");

		// set dialog message
		alertDialogBuilder
		.setMessage(mesg)
		.setCancelable(false)
		.setPositiveButton("Vote",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				// if this button is clicked, close
				// current activity
				String response = "decision " + yo + " " + eid + " " + "vote";
				String data = sendHttpRequest(MainActivity.url, response);

				//Intent in = new Intent(getApplicationContext(), Restaurant.class);
				//startActivity(in);
			}
		})
		.setNegativeButton("Decline",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				// if this button is clicked, just close
				// the dialog box and do nothing
				String response = "decision " + yo + " " + eid + " " + "decline";
				String data = sendHttpRequest(MainActivity.url, response);
				dialog.cancel();
			}
		});

		// create alert dialog

		alertDialog = alertDialogBuilder.create();

		alertDialog.show();

	}

	public void votingoverNotification(String data){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);

		String[] notes = data.split(" ");
		eid = notes[1];

		StringBuffer buf = new StringBuffer();
		for(int i=2; i < notes.length; i++){
			buf.append(notes[i] +" ");
		}
		String mesg = buf.toString();

		// set title
		alertDialogBuilder.setTitle("Voting over");

		// set dialog message
		alertDialogBuilder
		.setMessage(mesg)
		.setCancelable(false)
		.setPositiveButton("Accept",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				// if this button is clicked, close
				// current activity

				String response = "finaldecision " + yo + " " + eid + " " + "accept";
				String data = sendHttpRequest(MainActivity.url, response);

				dialog.cancel();
			}
		})
		.setNegativeButton("Decline",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				// if this button is clicked, just close
				// the dialog box and do nothing
				String response = "finaldecision " + yo + " " + eid + " " + "decline";
				String data = sendHttpRequest(MainActivity.url, response);

				dialog.cancel();
			}
		});

		// create alert dialog
		alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}

	// establishes connection to server
	// sends message with name variable to server at url 
	//returns string response from server
	private String sendHttpRequest(String url, String name) {
		StringBuffer buffer = new StringBuffer();
		try {
			System.out.println("URL ["+url+"] - Name ["+name+"]");

			HttpURLConnection con = (HttpURLConnection) ( new URL(url)).openConnection();
			con.setRequestMethod("POST");
			con.setDoInput(true);
			con.setDoOutput(true);
			con.connect();
			con.getOutputStream().write( (name).getBytes());

			InputStream is = con.getInputStream();
			byte[] b = new byte[1024];
			byte[] b2;

			int bytesRead = 0;
			while ( (bytesRead = is.read(b)) >= 0){
				b2 = new byte[bytesRead];
				for(int i=0; i < bytesRead; i++){
					b2[i] = b[i];
				}
				buffer.append(new String(b2));
			}
			con.disconnect();
		}
		catch(Throwable t) {
			t.printStackTrace();
		}

		return buffer.toString();
	}

}
