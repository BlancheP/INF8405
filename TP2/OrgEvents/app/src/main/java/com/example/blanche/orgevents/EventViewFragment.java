package com.example.blanche.orgevents;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

public class EventViewFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_event_view, null);

        final Button bGoing = (Button) v.findViewById(R.id.bGoing);
        final Button bNotGoing = (Button) v.findViewById(R.id.bNotGoing);
        final Button bMaybeGoing = (Button) v.findViewById(R.id.bMaybeGoing);
        final TextView tvParticipation = (TextView) v.findViewById(R.id.tvParticipation);

        bGoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseManager.isGoing();
                bGoing.setVisibility(View.GONE);
                bNotGoing.setVisibility(View.GONE);
                bMaybeGoing.setVisibility(View.GONE);
                tvParticipation.setText("Going");
                addEventToCalendar(v);
            }
        });

        bNotGoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseManager.isNotGoing();
                bGoing.setVisibility(View.GONE);
                bNotGoing.setVisibility(View.GONE);
                bMaybeGoing.setVisibility(View.GONE);
                tvParticipation.setText("Not going");
            }
        });

        bMaybeGoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseManager.isMaybeGoing();
                bGoing.setVisibility(View.GONE);
                bNotGoing.setVisibility(View.GONE);
                bMaybeGoing.setVisibility(View.GONE);
                tvParticipation.setText("Maybe going");
            }
        });

        DatabaseManager.populateEvent(v);
        return v;
    }

    private static void addEventToCalendar(View v) {
        Calendar cal = Calendar.getInstance();
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra("beginTime", cal.getTimeInMillis());
        intent.putExtra("allDay", false);
        intent.putExtra("description", "Reminder description");
        intent.putExtra("endTime", cal.getTimeInMillis()+60*60*1000);
        intent.putExtra("title", "Un event");
        intent.putExtra("eventLocation", "ici");
        v.getContext().startActivity(intent);
    }
}
