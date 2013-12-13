package com.example.thedineeting;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
	
	public static final String url = "http://192.17.237.126:8080/DineServer/DoubleMeServlet";
	
	ArrayList<String> etss;
	EditText e;

	String[] events;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		e = (EditText) findViewById(R.id.login);
		Button login = (Button) findViewById(R.id.button);
		login.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String s = e.getText().toString();
				String name = "login ".concat(s);
			//	MenuItemCompat.setActionView(item, R.layout.progress);
	//			item.setActionView(R.layout.progress);
				SendHttpRequestTask t = new SendHttpRequestTask();
				
				String[] params = new String[]{url, name};
				t.execute(params);
				

				Intent intent = new Intent(getApplicationContext(), Homepage.class);
				intent.putExtra("login", s);
				
				startActivity(intent);
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	

private class SendHttpRequestTask extends AsyncTask<String, Void, String> {

	
	@Override
	protected String doInBackground(String... params) {
		String url = params[0];
		String name = params[1];
		
		String data = sendHttpRequest(url, name);
		System.out.println("Data ["+data+"]");
		return data;
	}

	@Override
	protected void onPostExecute(String result) {
	//	edtResp.setText(result);
	//	item.setActionView(null);
		
	}
	
	
	
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
