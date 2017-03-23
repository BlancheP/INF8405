package com.example.blanche.orgevents;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class EventViewFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_event_view, null);

        final Button bGoing = (Button) v.findViewById(R.id.bGoing);
        final Button bNotGoing = (Button) v.findViewById(R.id.bNotGoing);
        final Button bMaybeGoing = (Button) v.findViewById(R.id.bMaybeGoing);

        bGoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseManager.isGoing();
            }
        });

        bNotGoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseManager.isNotGoing();
            }
        });

        bMaybeGoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseManager.isMaybeGoing();
            }
        });

        DatabaseManager.populateEvent(v);
        return v;
    }
}
