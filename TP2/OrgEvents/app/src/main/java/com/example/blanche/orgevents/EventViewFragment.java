package com.example.blanche.orgevents;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EventViewFragment extends Fragment {

    private static String name;
    private static String description;
    private static String location;
    private static String startDate;
    private static String startTime;
    private static String endDate;
    private static String endTime;

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
                tvParticipation.setText("Going");
                bGoing.setTextColor(Color.BLUE);
                bNotGoing.setTextColor(Color.BLACK);
                bMaybeGoing.setTextColor(Color.BLACK);
                addEventToCalendar(v);
            }
        });

        bNotGoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseManager.isNotGoing();
                tvParticipation.setText("Not going");
                bGoing.setTextColor(Color.BLACK);
                bNotGoing.setTextColor(Color.BLUE);
                bMaybeGoing.setTextColor(Color.BLACK);
            }
        });

        bMaybeGoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseManager.isMaybeGoing();
                tvParticipation.setText("Maybe going");
                bGoing.setTextColor(Color.BLACK);
                bNotGoing.setTextColor(Color.BLACK);
                bMaybeGoing.setTextColor(Color.BLUE);
                addEventToCalendar(v);
            }
        });

        DatabaseManager.populateEvent(v);
        return v;
    }

    public static void setName(String n) {
        name = n;
    }

    public static void setDescription(String des) {
        description = des;
    }

    public static void setLocation(String loc) {
        location = loc;
    }

    public static void setStartDate(String s) {
        startDate = s;
    }

    public static void setStartTime(String t) {
        startTime = t;
    }

    public static void setEndDate(String e) {
        endDate = e;
    }

    public static void setEndTime(String t) {
        endTime = t;
    }

    private void addEventToCalendar(View v) {
        Calendar cal = Calendar.getInstance();
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra("allDay", false);
        intent.putExtra("description", description);
        intent.putExtra("title", name);
        intent.putExtra("eventLocation", location);

        String start = startDate.concat(" ").concat(startTime);
        String end = endDate.concat(" ").concat(endTime);
        Long startT = null, endT = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date date = format.parse(start);
            startT = date.getTime();
            date = format.parse(end);
            endT = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        intent.putExtra("beginTime", startT);
        intent.putExtra("endTime", endT);

        v.getContext().startActivity(intent);
    }
}
