package com.example.thedineeting;



import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class DateTime extends Activity {

    EditText etype;
    EditText edate;
    EditText etime;
    String yo;
    String[] resultArr;
    String[] friendsArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_time);

        Bundle b = getIntent().getExtras();
        resultArr = b.getStringArray("selectedItems");
        friendsArr = b.getStringArray("selectedFriends");
        yo = b.getString("login");


        etype = (EditText) findViewById(R.id.edit_type);
        edate = (EditText) findViewById(R.id.edit_date);
        etime = (EditText) findViewById(R.id.edit_time);

        Button timingsubmit = (Button) findViewById(R.id.timing_submit);
        timingsubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String bld = etype.getText().toString();
                String mdate = edate.getText().toString();
                String mtime = etime.getText().toString();


                //	MenuItemCompat.setActionView(item, R.layout.progress);
                //			item.setActionView(R.layout.progress);


                String cmd = "sendvotingeventinfo " + yo.trim()  +"::" + bld.trim() + "::";
                for(int i=0; i < resultArr.length; i++){
                    cmd = cmd + resultArr[i] + ",";
                }
                cmd = cmd.substring(0, cmd.length()-1);

                cmd = cmd + "::";
                for(int i=0; i < friendsArr.length; i++){
                    cmd = cmd + friendsArr[i] + ",";
                }
                cmd = cmd.substring(0, cmd.length()-1);

                cmd = cmd + "::" + mdate.trim() + "::" + mtime.trim() + "::" + mdate.trim() + "::" + mtime.trim();


                String data = sendHttpRequest(MainActivity.url, cmd);
                System.out.println("Data ["+data+"]");

                Bundle b = new Bundle();
                b.putString("login", yo);
                b.putString("veid", data);
                // Add the bundle to the intent.

                Intent intent = new Intent(getApplicationContext(), Voting.class);
                intent.putExtras(b);

                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.date_time, menu);
        return true;
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
