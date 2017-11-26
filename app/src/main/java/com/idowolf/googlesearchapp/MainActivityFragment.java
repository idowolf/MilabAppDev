package com.idowolf.googlesearchapp;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public interface MainFragmentListener {
        String onSearchResult(String response, String searchText);
        String getUrl();
    }

    private MainFragmentListener listener;

    public MainActivityFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (MainFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement MainFragmentListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        setSearchButtonListener(view);
        return view;
    }

    /**
     * Set the search button listener using Volley
     * @param view The root view of the current fragment
     */
    private void setSearchButtonListener(View view) {
        final RequestQueue queue = Volley.newRequestQueue(getContext());

        Button searchButton = view.findViewById(R.id.searchButton);
        final EditText searchText = view.findViewById(R.id.searchEditText);
        final TextView resultTextView = view.findViewById(R.id.resultTextView);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest req = new StringRequest(Request.Method.GET, listener.getUrl() + searchText.getText().toString(), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        resultTextView.setText(listener.onSearchResult(response, searchText.getText().toString()));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("MainActivityFragment", "Encountered error - " + error);
                    }
                });
                queue.add(req);
            }
        });
    }
}
