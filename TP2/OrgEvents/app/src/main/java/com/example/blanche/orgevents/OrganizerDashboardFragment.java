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
import android.widget.Toast;

/**
 * Created by alain.trandang on 2017-03-23.
 */

public class OrganizerDashboardFragment extends Fragment {

    //static View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Inflating the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_organizerdashboard, null);

        DatabaseManager.setupDashboard(this.getView());

        Button bCreateEvent = (Button) view.findViewById(R.id.bCreateEvent);
        Button bVote = (Button) view.findViewById(R.id.bVote);


        bVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View w){
                if(MapsActivity.locationHashMapMarker.size() == 3) {
                    //DatabaseManager.getAllInfoForOrganizerDashboard(view);
                    Intent goToVote = new Intent(getActivity(), LocationVoteActivity.class);
                    startActivityForResult(goToVote, 7);
                }
                else
                {
                    Toast.makeText(getContext(), "You must have 3 locations before you can vote", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        DatabaseManager.getAllInfoForOrganizerDashboard(this.getView());
        this.getView().findViewById(R.id.bVote).setVisibility(Button.GONE);
    }
}
