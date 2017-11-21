package com.idowolf.intervalquotesapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.MainFragmentListener {
    private boolean flag;
    private PendingIntent pendingIntent;
    private AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        HashMap<String, String> quotesHashMap = loadHashMap();
        Intent myIntent = QuotesIntentService.setData(this, quotesHashMap);
        pendingIntent = PendingIntent.getService(MainActivity.this, 0,
                myIntent, 0);
        alarmManager = (AlarmManager)this.getSystemService(this.ALARM_SERVICE);
    }

    @NonNull
    private HashMap<String, String> loadHashMap() {
        HashMap<String, String> quotesHashMap = new HashMap<>();
        try {
            JSONArray obj = new JSONArray(loadJSONFromAsset());
            for (int i = 0; i < obj.length(); i++) {
                JSONObject e = obj.getJSONObject(i);
                quotesHashMap.put(e.getString("quoteText"), e.getString("quoteAuthor"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return quotesHashMap;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this, "No settings here!", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStartClicked(View view, double minutes) {
        if (flag) {
            flag = false;
            ((Button)view).setText(R.string.button_start);
            alarmManager.cancel(pendingIntent);
        } else {
            flag = true;
            ((Button)view).setText(getString(R.string.button_stop));
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                    (long)(minutes * 1000), pendingIntent);
        }
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = this.getAssets().open("quotes.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
