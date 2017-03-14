package com.example.blanche.orgevents;

import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

public class GroupSelectionActivity extends ActivityWithMenu {

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
