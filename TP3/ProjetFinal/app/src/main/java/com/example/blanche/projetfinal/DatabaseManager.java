package com.example.blanche.projetfinal;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


public class DatabaseManager {


    private static StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    private static DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
    private static DatabaseReference usersRef = databaseRef.child("users");

    private DatabaseManager() {
    }

    // Fonction qui verifie si le user existe deja et s'il n'existe pas, l'ajoute a la BD
    static void addUser(final String password, final EditText etUsername, final Context context, final Context appContext, final Bitmap profilePicture) {
        usersRef.child(etUsername.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    etUsername.setError("A user with the same username already exists!");
                    Toast done = Toast.makeText(appContext, "Some fields are invalid!", Toast.LENGTH_SHORT);
                    done.show();
                } else {
                    LoginActivity.setCurrentUser(etUsername.getText().toString());
                    DatabaseManager.addUserToBD(etUsername.getText().toString(),
                            password);
                    DatabaseManager.addPhotoToBD(etUsername.getText().toString(), profilePicture);
                    //Intent goToGroupSelection = new Intent(context, GroupSelectionActivity.class);
                    //context.startActivity(goToGroupSelection);
                    Toast done = Toast.makeText(appContext, "You have been successfully registered!", Toast.LENGTH_SHORT);
                    done.show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    // Fonction pour ajouter un user a la base de donnee
    static void addUserToBD(String userName, String password) {
        usersRef.child(userName)
                .child("password")
                .setValue(password);
    }

    // Fonction qui verifie les infos du login
    static void userIsValid(final String username, final String password, final Context context) {
        DatabaseReference user = usersRef.child(username);
        if (user.getRoot() != null) {
            user.child("password").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String pwd = (String) dataSnapshot.getValue();
                        if (pwd.equals(password)) {
                            LoginActivity.setCurrentUser(username);
                            //Intent goToGroupSelection = new Intent(context, GroupSelectionActivity.class);
                            //context.startActivity(goToGroupSelection);
                            return;
                        }
                    }
                    Toast error = Toast.makeText(context, "The username or password do not match any existing user!", Toast.LENGTH_SHORT);
                    error.show();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }

    // Fonction pour ajouter la photo de profil de l'utilisateur a la BD
    static void addPhotoToBD(String username, Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask task = storageRef.child(username).putBytes(data);
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Photo","Failure");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("Photo","success");
            }
        });
    }
}