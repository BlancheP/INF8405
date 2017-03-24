package com.example.blanche.orgevents;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by alain.trandang on 2017-03-23.
 */

public class OrganizerDashboardFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Inflating the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_organizerdashboard, null);

        /*

        TextView tvDashLoc1 = (TextView) view.findViewById(R.id.tvDashLoc1);
        TextView tvDashLoc2 = (TextView) view.findViewById(R.id.tvDashLoc2);
        TextView tvDashLoc3 = (TextView) view.findViewById(R.id.tvDashLoc3);

        TextView  locNote1 = (TextView) view.findViewById(R.id.locNote1);
        TextView  locNote2 = (TextView) view.findViewById(R.id.locNote2);
        TextView  locNote3 = (TextView) view.findViewById(R.id.locNote3);

        DatabaseManager.showLocNote(GroupSelectionActivity.getGroup(), locNote1, locNote2, locNote3);
        DatabaseManager.getLocationsName(GroupSelectionActivity.getGroup(),tvDashLoc1, tvDashLoc2, tvDashLoc3);
        */


        Button bCreateEvent = (Button) view.findViewById(R.id.bCreateEvent);
        Button bVote = (Button) view.findViewById(R.id.bVote);


        bVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View w){
                Intent goToVote = new Intent(getActivity(), LocationVoteActivity.class);
                OrganizerDashboardFragment.this.startActivity(goToVote);
            }
        });

        bCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View w) {

                if (MapsActivity.locationHashMapMarker.size() == 3) {
                    Intent goToEventCreation = new Intent(getActivity(), EventCreationActivity.class);
                    OrganizerDashboardFragment.this.startActivity(goToEventCreation);
                }
                else
                {
                    Toast.makeText(getContext(), "Please enter 3 locations before creating an event", Toast.LENGTH_SHORT).show();
                }
            }
        });

        DatabaseManager.getAllInfoForOrganizerDashboard(view);
        return view;
    }


}
