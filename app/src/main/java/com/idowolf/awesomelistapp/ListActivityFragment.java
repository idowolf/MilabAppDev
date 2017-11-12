package com.idowolf.awesomelistapp;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.idowolf.awesomelistapp.R;

import java.util.List;

import static java.util.Arrays.asList;

public class ListActivityFragment extends Fragment {

    private static final List<String> STARK = asList("ned", "catelyn", "robb", "jon", "sansa",
            "arya", "bran");
    private static final List<String> LANNISTER = asList("tywin", "jaime",
            "cersei", "tyrion");

    public ListActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.main_list);
        ListActivity.House result = (ListActivity.House) getActivity().getIntent().getSerializableExtra("HOUSE_ID");
        MyListAdapter adapter = new MyListAdapter(getContext(), result == ListActivity.House.STARK ? STARK : LANNISTER);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }
}
