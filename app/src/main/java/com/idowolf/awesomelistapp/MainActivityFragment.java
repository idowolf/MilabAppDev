package com.idowolf.awesomelistapp;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        Button starkButton = rootView.findViewById(R.id.starkButton);
        Button lannisterButton = rootView.findViewById(R.id.lannisterButton);
        setGoTClickListener(starkButton, ListActivity.House.STARK);
        setGoTClickListener(lannisterButton, ListActivity.House.LANNISTER);
        return rootView;
    }

    private void setGoTClickListener(Button button, final ListActivity.House house) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ListActivity.class);
                intent.putExtra("HOUSE_ID", house);
                startActivity(intent);
            }
        });
    }
}
