package com.idowolf.googlesearchapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.MainFragmentListener {

    public static final String FILTER_START = "class=\"_H1m _ees\">";
    public static final String FILTER_END = "</div>";
    public static final String GOOGLE_URL = "https://www.google.com/search?pws=0&gl=us&gws_rd=cr&q=";

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this, "No settings here", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handle search result URL and find the title of the first result
     * @param response A string containing the source of the Google search
     * @param searchText The input text to the Google search
     * @return Title of the first result
     */
    @Override
    public String onSearchResult(String response, String searchText) {
        if(searchText.equals(""))
            return getString(R.string.err_no_text);
        String filterStart = FILTER_START;
        int n1 = response.indexOf(filterStart) + filterStart.length();
        response = response.substring(n1);
        return response.substring(0, response.indexOf(FILTER_END));
    }

    /**
     * Return the URL to Google.com's main page
     * @return Non-local (US) Google search page with query handle
     */
    @Override
    public String getUrl() {
        return GOOGLE_URL;
    }
}
