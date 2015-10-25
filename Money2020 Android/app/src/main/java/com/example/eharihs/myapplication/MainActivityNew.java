package com.example.eharihs.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;


import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class MainActivityNew extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reply);
    
    }

   /* class GetList extends AsyncTask<String, String, String> {

        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... args) {

        try {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            JSONArray j =  (JSONArray)new JSONArrayParser().getJsonObject("https://www.healthcare.gov/api/index.json");

            for(int it=0;it< 3;++it){
                Intent i = new Intent();
                i.setClassName("com.example.eharihs.myapplication", "com.example.eharihs.myapplication.SpeakActivity");
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setType((String)j.getJSONObject(it).get("title"));
                startActivity(i);
            }

        }
        catch(Exception e)
        {

        }

            return null;

        }

        protected void onPostExecute(String file_url) {



        }

    }*/
}
