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

    //private static DatabaseReference


   private DatabaseManager(){
     }

    static void addUser(String userName, String password){
        FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(userName)
                .child("password")
                .setValue(password);
    }

    static void addGroup(String groupName, String userName){
        FirebaseDatabase.getInstance().getReference()
                .child("groups")
                .child(groupName)
                .child("managerName")
                .setValue(userName);

    }




}
