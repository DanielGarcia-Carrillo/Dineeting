package com.example.thedineeting;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class Voting extends ListActivity {

	String yo;
	String veid;
	boolean backrunning = true;
	ListView listView;
	String data;
	String[] vcounts;
	SendHttpRequestTask t;
	ArrayAdapter<String> vadapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.voting);
		
		Bundle b = getIntent().getExtras();
		veid = b.getString("veid");
		Log.e("voting", veid);
		yo = b.getString("login");
		
		String cmd = "eventvoting " + veid;
		data = sendHttpRequest(MainActivity.url, cmd);
		
		String[] finalevents = {"Ikenberry-3", "Par-0", "Chipotle-1"};
		String[] foo = data.split("::");

		vcounts = foo[0].split(",");
		
		vadapter = new ArrayAdapter<String>(this, R.layout.list_fruit, vcounts);
		setListAdapter(vadapter);

		//listView = (ListView) findViewById(R.id.list);
		listView = getListView();
		listView.setTextFilterEnabled(true);
		
		Button timingsubmit = (Button) findViewById(R.id.message_submit);
		timingsubmit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

		        // Add the bundle to the intent.
		      				
				Intent intent = new Intent(getApplicationContext(), Messagess.class);

				startActivity(intent);
			}
		});
		/*
		t = new SendHttpRequestTask();
		cmd = "eventvoting " + veid ;
		String[] params = new String[]{MainActivity.url, cmd};
		t.execute(params);
		*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.voting, menu);
		return true;
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
					String curdata = sendHttpRequest(url, name);
					data = sendHttpRequest(url, name);
					Log.e("looping", data);
					if(!data.equals(curdata)){
						data = curdata;
						break;
					}
				}
			}

			return data;
		}

		@Override
		protected void onPostExecute(String result) {
				String[] foo = result.split("::");

				vcounts = foo[0].split(",");
				vadapter.notifyDataSetChanged();
				SendHttpRequestTask t = new SendHttpRequestTask();
				String cmd = "eventvoting " + veid ;
				String[] params = new String[]{MainActivity.url, cmd};
				t.execute(params);
			
		}
	}
	
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
