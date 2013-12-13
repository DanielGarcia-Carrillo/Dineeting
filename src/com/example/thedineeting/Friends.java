package com.example.thedineeting;


import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
 
public class Friends extends Activity implements
        OnClickListener {
    Button button;
    ListView listView;
    ArrayAdapter<String> adapter;
    String[] resultArr;
    String yo;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurants);
 
		
		String name = "friendsupdate";
		
		String data = sendHttpRequest(MainActivity.url, name);
		System.out.println("Data ["+data+"]");
		
		String[] flists= data.split("::");
		
		ArrayList<String> fliststatus= new ArrayList<String>();
		for(int i=0; i < flists.length; i++){
			Log.e("Friends", i + " " + flists[i]);
			
			String[] mice = flists[i].split(",");
			for(int j=0; j < mice.length;j++){
				Log.e("mice", mice[j]);
				fliststatus.add(mice[j]);
			}
		}
		
		String[] realflist = fliststatus.toArray(new String[fliststatus.size()]);
		
		/*

		*/
		
        Bundle b = getIntent().getExtras();
        resultArr = b.getStringArray("selectedItems");
        yo = b.getString("login");
        Log.e("friends", yo);
        
        findViewsById();
 
        String[] sports = {"wade","jordan","lebron"};
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice, realflist);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(adapter);
 
        button.setOnClickListener(this);
    }
 
    private void findViewsById() {
        listView = (ListView) findViewById(R.id.list);
        button = (Button) findViewById(R.id.testbutton);
    }
 
    public void onClick(View v) {
        SparseBooleanArray checked = listView.getCheckedItemPositions();
        ArrayList<String> selectedItems = new ArrayList<String>();
        for (int i = 0; i < checked.size(); i++) {
            // Item position in adapter
            int position = checked.keyAt(i);
            // Add sport if it is checked i.e.) == TRUE!
            if (checked.valueAt(i))
                selectedItems.add(adapter.getItem(position));
        }
 
        String[] outputStrArr = new String[selectedItems.size()];
 
        for (int i = 0; i < selectedItems.size(); i++) {
            outputStrArr[i] = selectedItems.get(i);
        }
 
        Intent intent = new Intent(getApplicationContext(),
                DateTime.class);
 
        // Create a bundle object
        Bundle b = new Bundle();
        b.putStringArray("selectedFriends", outputStrArr);
        b.putStringArray("selectedItems", resultArr);
        b.putString("login", yo);
        // Add the bundle to the intent.
        intent.putExtras(b);
 
        // start the ResultActivity
        startActivity(intent);
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


