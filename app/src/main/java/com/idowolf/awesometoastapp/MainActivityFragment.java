package com.idowolf.awesometoastapp;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * A fragment with a button and an edittext.
 * Pressing the button toasts the edittext text.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate root view (instantiate all tags and turn them to "tangible" objects)
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Get reference to edittext and button
        final EditText editText = rootView.findViewById(R.id.editText);
        Button toastButton = rootView.findViewById(R.id.toastButton);

        // Set button onClick listener
        toastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a new toast and show it
                Toast.makeText(getContext(), editText.getText(), Toast.LENGTH_SHORT).show();
            }
        });
        return rootView;
    }
}
