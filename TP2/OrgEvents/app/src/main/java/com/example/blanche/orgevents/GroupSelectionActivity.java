package com.example.blanche.orgevents;

import android.content.Intent;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.constraint.solver.widgets.Snapshot;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GroupSelectionActivity extends AppCompatActivity {

    private static String currentGroup = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_selection);

        final AutoCompleteTextView actvGroupSelection = (AutoCompleteTextView)findViewById(R.id.actvGroupSelection);
        final Button bSelectGroup = (Button)findViewById(R.id.bSelectGroup);

        // On initialise le widget AutoCompleteTextView
        DatabaseManager.getGroups(this, actvGroupSelection);

        bSelectGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentGroup = actvGroupSelection.getText().toString();
                DatabaseManager.addGroup(currentGroup, GroupSelectionActivity.this);
            }
        });
    }

    public static String getGroup() {
        return currentGroup;
    }

    public static void setGroup(String groupName) {
        currentGroup = groupName;
    }
}
