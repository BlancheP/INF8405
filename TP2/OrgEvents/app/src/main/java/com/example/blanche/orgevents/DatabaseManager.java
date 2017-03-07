package com.example.blanche.orgevents;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

/**
 * Created by Alexis on 02/03/2017.
 */

public class DatabaseManager {
    private static StorageReference mStorageRef;
    private static DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
    private static DatabaseReference userRef = databaseRef.child("users");

    private DatabaseManager(){}

    static void addUser(String userName, String password){
        userRef.child(userName).child("password").setValue(password);
    }

    static boolean userExists(String username) {
        if (userRef.child(username).getRoot() == null)
            return false;
        return true;
    }

    static void userIsValid(String username, final String password, final Context context) {
        DatabaseReference user = userRef.child(username);
        if (user.getRoot() != null) {
            user.child("password").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String pwd = (String) dataSnapshot.getValue();
                        if (pwd.equals(password)) {
                            Intent groupSelectionIntent = new Intent(context, MainActivity.class);
                            context.startActivity(groupSelectionIntent);
                            return;
                        }
                    }

                    Toast error = Toast.makeText(context, "The username or password do not match any existing user!", Toast.LENGTH_SHORT);
                    error.show();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        }
    }
}
