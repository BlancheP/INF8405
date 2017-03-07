package com.example.blanche.orgevents;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by Alexis on 02/03/2017.
 */

public class DatabaseManager {
    private static StorageReference mStorageRef;
    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static DatabaseReference rootRef = database.getReference();
    private static DatabaseReference userRef = rootRef.child("users");


   private DatabaseManager(){
     }

    static void addUser(String userName, String password){
         userRef.child(userName).child("password").setValue(password);
    }



}
