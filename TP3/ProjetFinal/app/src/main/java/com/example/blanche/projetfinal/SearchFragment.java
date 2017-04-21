package com.example.blanche.projetfinal;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.text.Line;
import com.google.firebase.database.DatabaseReference;

import org.w3c.dom.Text;

import java.util.List;

public class SearchFragment extends Fragment {

    public SearchFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        final AutoCompleteTextView actvSearch = (AutoCompleteTextView)view.findViewById(R.id.actvSearchUsers);
        final View l = view.findViewById(R.id.infoUserLayout);
        final Button b = (Button)view.findViewById(R.id.bFollow);
        final TextView tv = (TextView)view.findViewById(R.id.tvUsername);

        // Mettre dans le AutoCompleteTextView la liste des users
        List<String> users = DatabaseManager.getUsers();
        final PreferencesManager pm = DatabaseManager.getPreferencesManager();
        users.remove(pm.getCurrentUser());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, users);
        actvSearch.setAdapter(adapter);

        // Actualiser le profil qu'on voit au bas de la page lorsque le user clique sur un nom
        actvSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos,
                                    long id) {
                String user = (String)parent.getItemAtPosition(pos);
                tv.setText(user);
                ImageView iv = (ImageView)getView().findViewById(R.id.ivUser);
                //Set the image view to the profile pic of the user
                iv.setImageResource(R.mipmap.ic_profile_black);
                l.setBackgroundResource(R.drawable.border_background);
                DatabaseManager.isFollowing(getActivity(), pm.getCurrentUser(), user);
            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (b.getText().equals("Follow"))
                    DatabaseManager.addFollow(getActivity(), tv.getText().toString(), DatabaseManager.getPreferencesManager().getCurrentUser());
                else
                    DatabaseManager.removeFollow(getActivity(), tv.getText().toString(), DatabaseManager.getPreferencesManager().getCurrentUser());
            }
        });

        actvSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER)
                    return true;
                return false;
            }
        });

        return view;
    }
}
