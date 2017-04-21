package com.example.blanche.projetfinal;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DatabaseManager {

    private static StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    private static DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
    private static DatabaseReference usersRef = databaseRef.child("users");
    private static DatabaseReference markerRef = databaseRef.child("markers");
    private static PreferencesManager pm;
    private static DatabaseReference picturesRef = databaseRef.child("pictures");


    final static long ONE_MEGABYTE = 1024 * 1024;


    private DatabaseManager() {}

    // Fonction qui verifie si le user existe deja et s'il n'existe pas, l'ajoute a la BD
    static void addUser(final String password, final EditText etUsername, final Context context, final Bitmap profilePicture) {
        final String username = etUsername.getText().toString();
        usersRef.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    etUsername.setError("A user with the same username already exists!");
                    Toast done = Toast.makeText(context, "Some fields are invalid! Please try again", Toast.LENGTH_SHORT);
                    done.show();
                } else {
                    usersRef.child(username).child("password").setValue(password);

                    Bitmap emptyBitmap = Bitmap.createBitmap(profilePicture.getWidth(), profilePicture.getHeight(), profilePicture.getConfig());
                    if (!profilePicture.sameAs(emptyBitmap)) {
                        DatabaseManager.addProfilePhotoToBD(username, profilePicture);
                    }

                    Toast done = Toast.makeText(context, "You have been successfully registered!", Toast.LENGTH_SHORT);
                    done.show();
                    storeDataInfo(username, password, (Activity)context);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast done = Toast.makeText(context, "Error while connecting to the database" + databaseError, Toast.LENGTH_SHORT);
                done.show();
            }
        });
    }

    static void addMarker(final LatLng latLng, final Context context /*TODO: eventually a picture*/)
    {
        markerRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Enter Marker Name");

                    // Set up the input
                    final EditText input = new EditText(context);

                    // Specify the type of input expected
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setView(input);

                    // Set up the buttons
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            String newMarkerName = "";
                            newMarkerName = input.getText().toString();

                            final String finalNewMarkerName = newMarkerName;

                            //Add marker on LongClick position
                            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(finalNewMarkerName);
                            Marker marker = MapFragment.mGoogleMap.addMarker(markerOptions);
                            marker.showInfoWindow();

                            //send location name and coords to Firebase
                            markerRef.child(finalNewMarkerName)
                                    .child("Coords")
                                    .child("latitude").setValue(latLng.latitude);

                            markerRef.child(finalNewMarkerName)
                                    .child("Coords")
                                    .child("longitude").setValue(latLng.longitude);

                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    // Fonction pour ajouter la photo de profil de l'utilisateur a la BD
    static void addProfilePhotoToBD(String username, Bitmap bitmap) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);
        byte[] data = b.toByteArray();
        UploadTask task = storageRef.child(username + "/profile").putBytes(data);
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

    static void userIsValid(String username, String password, Context context) {
        userIsValid(username, password, context, false);
    }

    // Fonction qui verifie les infos du login
    static void userIsValid(final String username, final String password, final Context context, final boolean isUserFromPreferences) {
        usersRef.child(username).child("password").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String pwd = (String) dataSnapshot.getValue();
                    if (pwd.equals(password)) {
                        if (isUserFromPreferences){
                            Intent goToDashboard = new Intent(context, MainActivity.class);
                            context.startActivity(goToDashboard);
                            ((Activity)context).finishAffinity();
                        }
                        else {
                            storeDataInfo(username, password, (Activity) context);
                        }
                        return;
                    }
                }
                if (isUserFromPreferences){
                    Intent goToLogin = new Intent(context, LoginActivity.class);
                    context.startActivity(goToLogin);
                    ((Activity)context).finish();
                    return;
                }
                Toast error = Toast.makeText(context, "The username or password do not match any existing user!", Toast.LENGTH_SHORT);
                error.show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    static void setPreferencesManager(Context context) {
        pm = new PreferencesManager(context);
    }

    static PreferencesManager getPreferencesManager() {
        return pm;
    }

    static void showExplanation(String title,
                                 String message,
                                 final String permission,
                                 final int permissionRequestCode,
                                 final Activity activity)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        requestPermission(permission, permissionRequestCode, activity);
                    }
                });

        builder.create().show();
    }

    static void requestPermission(String permissionName, int permissionRequestCode, Activity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[]{permissionName}, permissionRequestCode);
    }

    static void storeDataInfo(final String username, final String password, final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.storeUserInfo).setMessage(R.string.storeUserInfoMessage);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                pm.updateCurrentUser(username, password);
                Intent goToDashboard = new Intent(activity, MainActivity.class);
                activity.startActivity(goToDashboard);
                activity.finishAffinity();
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent goToDashboard = new Intent(activity, MainActivity.class);
                activity.startActivity(goToDashboard);
                activity.finishAffinity();
            }
        });
        builder.create().show();
    }

    static void loadProfilePhoto( final Context context) {
        String username = pm.getCurrentUser();
        storageRef.child(username + "/profile").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(context)
                        .load(uri)
                        .memoryPolicy(MemoryPolicy.NO_CACHE )
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .into((ImageView) ((Activity) context).findViewById(R.id.ivProfilePic));
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }

        });
    }
    @SuppressWarnings("VisibleForTests")
    static void addPhotoToBD(final String filename, final String date, final String description, Bitmap bitmap) {
        final String username = pm.getCurrentUser();
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);
        byte[] data = b.toByteArray();
        //Pour la sauvegarde des photos avec Firebase storage
        UploadTask task = storageRef.child(username + "/" + filename).putBytes(data);
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Photo","Failure");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("Photo","success");
                //Pour pouvoir avoir les liens vers les photos avec toutes les informations pertinentes dans
                // Firebase database
                picturesRef.child(filename).child("username").setValue(username);
                picturesRef.child(filename).child("filename").setValue(filename);
                picturesRef.child(filename).child("url").setValue( taskSnapshot.getDownloadUrl().toString());
                picturesRef.child(filename).child("date").setValue(date);
                picturesRef.child(filename).child("description").setValue(description);
            }
        });
    }

    static void loadDashboardPhoto(final Context context, final int index) {

        picturesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List< Map<String, String>> pictures = new ArrayList<>();
                Map<String, String> infos = new HashMap<>();
                Map<String, String> individualInfo = new HashMap<>();
                if(dataSnapshot.exists()) {
                    //For all the pictures
                    for (DataSnapshot picturesIter: dataSnapshot.getChildren()) {
                        //For all the informations of each picture
                        /*for (DataSnapshot detailsIter : dataSnapshot.child("pictures").child(picturesIter.getKey()).getChildren()) {
                            individualInfo = (Map<String, String>) detailsIter.getValue();
                            infos.put picturesIter.getValue();
                        }*/
                        String key = picturesIter.getKey();
                        infos = (Map<String, String>) picturesIter.getValue();
                        pictures.add(infos);
                    }
                    TextView username = (TextView) ((Activity) context).findViewById(R.id.tvDashUsername);
                    TextView filename = (TextView) ((Activity) context).findViewById(R.id.tvDashFilename);
                    TextView date = (TextView) ((Activity) context).findViewById(R.id.tvDashDate);
                    TextView description = (TextView) ((Activity) context).findViewById(R.id.tvDashDescr);
                    ImageView iv = (ImageView) ((Activity) context).findViewById(R.id.ivDashPhoto);



                    filename.setText(pictures.get(index).get("filename"));
                    username.setText(pictures.get(index).get("username"));
                    date.setText(pictures.get(index).get("date"));
                    description.setText(pictures.get(index).get("description"));
                    Uri uri = Uri.parse(pictures.get(index).get("url"));
                    Picasso.with(context)
                            .load(uri)
                            .memoryPolicy(MemoryPolicy.NO_CACHE )
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .into(iv);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}