package com.milab.ido.timezoneapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    public void fetchWeather(final View view) {
        final TimeFetcher fetcher = new TimeFetcher(view.getContext());
        final Integer timezone = Integer.parseInt(((Spinner)findViewById(R.id.utc_offfset)).getSelectedItem().toString());
        final boolean useOffset = ((CheckBox)findViewById(R.id.check_box)).isChecked();
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching time...");
        progressDialog.show();

        fetcher.dispatchRequest(useOffset, timezone, new TimeFetcher.TimeResponseListener() {
            @Override
            public void onResponse(TimeFetcher.TimeResponse response) {
                progressDialog.hide();

                if (response.isError) {
                    Toast.makeText(view.getContext(), "Error while fetching weather", Toast.LENGTH_LONG).show();
                    return;
                }

                ((TextView)MainActivity.this.findViewById(R.id.time_view)).setText(response.time);
            }
        });
    }
}
