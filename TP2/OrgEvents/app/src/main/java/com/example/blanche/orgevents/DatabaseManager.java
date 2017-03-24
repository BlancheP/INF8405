package com.example.blanche.orgevents;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static android.content.Context.LOCATION_SERVICE;
import static com.example.blanche.orgevents.MapsActivity.mMap;


public class DatabaseManager {



    private static StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    private static DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
    private static DatabaseReference usersRef = databaseRef.child("users");
    public static DatabaseReference groupsRef = databaseRef.child("groups");
    private static DatabaseReference eventsRef = databaseRef.child("events");
    final public static List<String> groups = new ArrayList<>();
    final public static List<String> locationNames = new ArrayList<>();
    final public static List<String> locNotes = new ArrayList<>();

    private DatabaseManager(){}

    // Fonction qui verifie si le user existe deja et s'il n'existe pas, l'ajoute a la BD
    static void addUser(final String password, final EditText etUsername, final Context context, final Context appContext, final Bitmap profilePicture) {
        usersRef.child(etUsername.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    etUsername.setError("A user with the same username already exists!");
                    Toast done = Toast.makeText(appContext, "Some fields are invalid!", Toast.LENGTH_SHORT);
                    done.show();
                }
                else {
                    LoginActivity.setCurrentUser(etUsername.getText().toString());
                    DatabaseManager.addUserToBD(etUsername.getText().toString(),
                            password);
                    DatabaseManager.addPhotoToBD(etUsername.getText().toString(), profilePicture);
                    Intent goToGroupSelection = new Intent(context, GroupSelectionActivity.class);
                    context.startActivity(goToGroupSelection);
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
    static void addUserToBD(String userName, String password){
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
                            Intent goToGroupSelection = new Intent(context, GroupSelectionActivity.class);
                            context.startActivity(goToGroupSelection);
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

    // Fonction qui determine si on peut se joindre au groupe
    static void groupSelection(final String group, final Context context) {
        final String currentUser = LoginActivity.getCurrentUser();
        usersRef.child(currentUser).child("group").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    final String groupName = dataSnapshot.getValue().toString();
                    Toast r = Toast.makeText(context, group, Toast.LENGTH_SHORT);
                    r.show();
                    if (groupName.equals(group)) {
                        Toast t = Toast.makeText(context, "On est ici", Toast.LENGTH_SHORT);
                        t.show();
                        GroupSelectionActivity.setGroup(group);
                        Intent goToMain = new Intent(context, MapsActivity.class);
                        context.startActivity(goToMain);
                    }
                    else {
                        groupsRef.child(groupName).child("managerName").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    if (dataSnapshot.getValue().toString().equals(currentUser)) {
                                        Toast error = Toast.makeText(context, "You have to select the group from which you are the owner.", Toast.LENGTH_SHORT);
                                        error.show();
                                    }
                                    else {
                                        groupsRef.child(groupName).child("users").child(currentUser).removeValue();
                                        usersRef.child(currentUser).child("group").setValue(group);
                                        GroupSelectionActivity.setGroup(group);
                                        addGroup(group, context);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
                else {
                    usersRef.child(currentUser).child("group").setValue(group);
                    GroupSelectionActivity.setGroup(group);
                    addGroup(group, context);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // Fonction qui verifie si le groupe existe deja et s'il n'existe pas, l'ajoute a la BD
    static void addGroup(final String groupName, final Context context) {
        groupsRef.child(groupName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    DatabaseManager.addGroupToBD(groupName, LoginActivity.getCurrentUser());
                }
                else {
                    DatabaseManager.addUserToGroup(LoginActivity.getCurrentUser(), groupName);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    //Fonction qui verifie si un utilisateur est le manager de son groupe et change l'activite pour qu'elle corresponde a la bonne.
    static void chooseManagerActivity(final Context context) {
        String group = GroupSelectionActivity.getGroup();
        groupsRef.child(group).child("managerName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                if(dataSnapshot.exists()){
                    String user = (String) dataSnapshot.getValue();
                    if(user.equals(LoginActivity.getCurrentUser())){
                        Intent goToManagerDash = new Intent(context, OrganizerDashboardActivity.class);
                        context.startActivity(goToManagerDash);
                    }
                    else{
                        Intent goToMain = new Intent(context, MapsActivity.class);
                        context.startActivity(goToMain);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError){

            }
        });
    }

    static void addNewLocationToMap(final LatLng latLng, final Context context){

        String group = GroupSelectionActivity.getGroup();
        groupsRef.child(group).child("managerName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String user = (String) dataSnapshot.getValue();
                    if (user.equals(LoginActivity.getCurrentUser())) {

                        if (MapsActivity.locationHashMapMarker.size() < 3) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Enter Location Name");

                            // Set up the input
                            final EditText input = new EditText(context);

                            // Specify the type of input expected
                            input.setInputType(InputType.TYPE_CLASS_TEXT);
                            builder.setView(input);

                            // Set up the buttons
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    String newLocationName = "";
                                    newLocationName = input.getText().toString();

                                    final String finalNewLocationName = newLocationName;
                                    new AlertDialog.Builder(context)
                                            .setTitle("Confirmation")
                                            .setMessage("Would you like to send this location to your guests?")
                                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    //Add marker on LongClick position
                                                    MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(finalNewLocationName);
                                                    Marker marker = mMap.addMarker(markerOptions);
                                                    MapsActivity.locationHashMapMarker.put(finalNewLocationName, marker);
                                                    marker.showInfoWindow();

                                                    //send location name and coords to Firebase
                                                    DatabaseManager.addLocationToCurrentGroup(
                                                            GroupSelectionActivity.getGroup(),
                                                            finalNewLocationName,
                                                            latLng.latitude,
                                                            latLng.longitude);
                                                }
                                            })
                                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    //do nothing
                                                }
                                            })
                                            .show();
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                            builder.show();

                        } else {
                            new AlertDialog.Builder(context)
                                    .setTitle("Alert")
                                    .setMessage("You cannot add more than 3 locations")
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }
                    } else {
                        Toast.makeText(context, "Only the admin of this group can add locations", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // Fonction qui ajoute le user au groupe s'il n'y etait pas deja
    static void addUserToGroup(final String username, final String group) {
        groupsRef.child(group).child("users").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    groupsRef.child(group).child("users").child(username).child("hasVoted").setValue(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // Fonction qui ajoute le groupe a la BD
    static void addGroupToBD(String groupName, String userName){
        groupsRef.child(groupName)
                .child("managerName")
                .setValue(userName);
        groupsRef.child(groupName)
                .child("users")
                .child(userName).child("hasVoted").setValue(false);
    }

    // Fonction qui permet de recuperer tous les groupes existants
    // Cette fonction est appelee lors de la creation du GroupSelectionActivity pour initialiser le AutoCompleteTextView
    static void getGroups(final Context context, final AutoCompleteTextView actv) {
        DatabaseManager.groupsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> values = (Map<String, Object>)dataSnapshot.getValue();
                groups.clear();
                for (Map.Entry<String, Object> entry : values.entrySet()) {
                    groups.add(entry.getKey());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                        android.R.layout.simple_dropdown_item_1line, groups);
                actv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    //Fonction qui va chercher les noms des lieux et les ecrit dans 3 textviews
    static void getLocationsName(final String groupName, final TextView loc1, final TextView loc2, final TextView loc3) {
        locationNames.clear();
        groupsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> currentGroupLocations =
                        (Map<String, Object>) dataSnapshot.child(groupName).child("Locations").getValue();

                if (currentGroupLocations != null) {

                    for (Map.Entry<String, Object> entry : currentGroupLocations.entrySet()) {

                        LocationVoteActivity.locationsName.add(entry.getKey());
                        locationNames.add(entry.getKey());
                    }

                    loc1.setText(locationNames.get(0));
                    loc2.setText(locationNames.get(1));
                    loc3.setText(locationNames.get(2));

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    static void getLocationsSpinner(final String groupName, final Context context, final Spinner spinner){
        locationNames.clear();
        groupsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> currentGroupLocations =
                        (Map<String, Object>) dataSnapshot.child(groupName).child("Locations").getValue();

                if (currentGroupLocations != null) {

                    for (Map.Entry<String, Object> entry : currentGroupLocations.entrySet()) {

                        LocationVoteActivity.locationsName.add(entry.getKey());
                        locationNames.add(entry.getKey());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                            android.R.layout.simple_dropdown_item_1line, locationNames);
                    spinner.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // Fonction appele lorsque le user veut quitter le groupe
    static void quitGroup(final Context context) {
        usersRef.child(LoginActivity.getCurrentUser()).child("group").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    final String group = dataSnapshot.getValue().toString();
                    groupsRef.child(group).child("managerName").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                if (dataSnapshot.getValue().toString().equals(LoginActivity.getCurrentUser())) {
                                    Toast error = Toast.makeText(context, "You can't quit your own group", Toast.LENGTH_SHORT);
                                    error.show();
                                }
                                else {
                                    groupsRef.child(group).child("users").child(LoginActivity.getCurrentUser()).removeValue();
                                    usersRef.child(LoginActivity.getCurrentUser()).child("group").removeValue();
                                    Intent goToGroupSelection = new Intent(context, GroupSelectionActivity.class);
                                    context.startActivity(goToGroupSelection);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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


    //fonction pour sauvegarder un evenement dans la base de donnees.
    //TODO: probablement ajouter la liste des participants a l'evenement.
    static void addEventToBD(String groupName, String eventName, String location, String description,
                             String startDate, String startTime, String endDate, String endTime) {
        groupsRef.child(groupName)
                .child("event")
                .child("Name")
                .setValue(eventName);
        groupsRef.child(groupName)
                .child("event")
                .child("Location")
                .setValue(location);
        groupsRef.child(groupName)
                .child("event")
                .child("description")
                .setValue(description);
        groupsRef.child(groupName)
                .child("event")
                .child("startDate")
                .setValue(startDate);
        groupsRef.child(groupName)
                .child("event")
                .child("startTime")
                .setValue(startTime);
        groupsRef.child(groupName)
                .child("event")
                .child("endDate")
                .setValue(endDate);
        groupsRef.child(groupName)
                .child("event")
                .child("endTime")
                .setValue(endTime);
    }

    static void addLocationToCurrentGroup(String groupName,
                                          String locationName,
                                          double latitude,
                                          double longitude) {

        groupsRef.child(groupName)
                .child("Locations")
                .child(locationName).child("Coords")
                .child("latitude").setValue(latitude);

        groupsRef.child(groupName)
                .child("Locations")
                .child(locationName).child("Coords")
                .child("longitude").setValue(longitude);

        groupsRef.child(groupName)
                .child("Locations")
                .child(locationName).child("Note").setValue(-1);
    }

    static void getAllLocationsCurrentGroup(final String groupName){

        groupsRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        MapsActivity.locationHashMapMarker.clear();

                        Map<String, Object> currentGroupLocations =
                                (Map<String, Object>) dataSnapshot.child(groupName).child("Locations").getValue();

                        if(currentGroupLocations != null){

                            //Log.d("DatabaseManager", "CURRENT GROUP OBJECT " + currentGroupLocations.toString());

                            //iterate through each location coordinates, ignoring their names
                            for (Map.Entry<String, Object> entry : currentGroupLocations.entrySet()) {

                                String locationName = entry.getKey();


                                double lat = (double) dataSnapshot
                                        .child(groupName)
                                        .child("Locations")
                                        .child(entry.getKey())
                                        .child("Coords")
                                        .child("latitude").getValue();

                                double lgt = (double) dataSnapshot
                                        .child(groupName)
                                        .child("Locations")
                                        .child(entry.getKey())
                                        .child("Coords")
                                        .child("longitude").getValue();

                                MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(lat, lgt)).title(locationName);
                                Marker marker = mMap.addMarker(markerOptions);
                                MapsActivity.locationHashMapMarker.put(locationName, marker);
                                marker.showInfoWindow();
                            }
                            Log.d("DatabaseManager", "locationHashMap size: " + MapsActivity.locationHashMapMarker.size());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });

        Log.d("DatabaseManager", "getAllLocationsCurrentGroup called()");
    }


    static void sendCurrentUserCoords(String username, double latitude, double longitude) {

        usersRef.child(username)
                .child("Coords")
                .child("latitude").setValue(latitude);

        usersRef.child(username)
                .child("Coords")
                .child("longitude").setValue(longitude);
    }

    //TODO: getter of current group's current users
    static void getAllCoordsUsersCurrentGroup(final String groupName){

        groupsRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot groupDataSnapshot) {

                        //remove all current markers
                        for (Map.Entry<String, Marker> entry : MapsActivity.userHashMapMarker.entrySet())
                        {
                            entry.getValue().remove();
                        }

                        Map<String, Object> currentGroupUsersLocations =
                                (Map<String, Object>) groupDataSnapshot.child(groupName).child("users").getValue();

                        //Log.d("DatabaseManager", "CURRENT GROUP OBJECT " + currentGroupLocations.toString());

                        if(currentGroupUsersLocations != null){

                            for (final Map.Entry<String, Object> entry : currentGroupUsersLocations.entrySet()) {

                                final String userName = entry.getKey();

                                Log.d("DatabaseManger", "User in " + groupName + ": " + userName);

                                usersRef.addListenerForSingleValueEvent(new ValueEventListener() {

                                    @Override
                                    public void onDataChange(DataSnapshot usersDataSnapshot) {

                                        Double lat = (Double) usersDataSnapshot
                                                .child(userName)
                                                .child("Coords")
                                                .child("latitude").getValue();

                                        Double lgt = (Double) usersDataSnapshot
                                                .child(userName)
                                                .child("Coords")
                                                .child("longitude").getValue();

                                        if(lat != null && lgt != null) {

                                            MarkerOptions markerOptions = new MarkerOptions().position(
                                                    new LatLng(lat, lgt)).title(userName).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                                            Marker marker = mMap.addMarker(markerOptions);
                                            MapsActivity.userHashMapMarker.put(userName, marker);
                                            marker.showInfoWindow();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        //handle databaseError
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });

        Log.d("DatabaseManager", "getAllCoordsUsersCurrentGroup called()");
    }

    public static void computeNote(final Context context, final String userName, final String groupName, final RatingBar rbLoc1, final RatingBar rbLoc2, final RatingBar rbLoc3) {

        groupsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //si le user n'a pas deja voté
                if(dataSnapshot.child(groupName).child("users").child(userName).child("hasVoted").getValue().equals(false)){
                    Map<String, Object> currentGroupLocations =
                            (Map<String, Object>) dataSnapshot.child(groupName).child("Locations").getValue();

                    if (currentGroupLocations != null) {
                        int counter =0;
                        for (Map.Entry<String, Object> entry : currentGroupLocations.entrySet()) {

                            Long newNote = 0L;
                            Long previousNote = (Long) dataSnapshot.child(groupName).child("Locations")
                                    .child(entry.getKey()).child("Note").getValue();

                            if(previousNote == -1) {
                                if (counter == 0) {
                                    newNote =  (new Float (rbLoc1.getRating())).longValue();
                                } else if (counter == 1) {
                                    newNote =  (new Float (rbLoc2.getRating())).longValue();
                                } else if (counter == 2) {
                                    newNote =  (new Float (rbLoc3.getRating())).longValue();
                                }

                            }
                            else{
                                if (counter == 0) {
                                    newNote = (previousNote + (new Float (rbLoc1.getRating())).longValue() ) / 2;
                                } else if (counter == 1) {
                                    newNote = (previousNote + (new Float (rbLoc2.getRating())).longValue()) / 2;
                                } else if (counter == 2) {
                                    newNote = (previousNote + (new Float (rbLoc3.getRating())).longValue()) / 2;
                                }
                            }

                            groupsRef.child(groupName).child("Locations")
                                    .child(entry.getKey()).child("Note").setValue(newNote);

                            counter++;
                        }
                    }
                    groupsRef.child(groupName).child("users").child(userName).child("hasVoted").setValue(true);
                }
                else{
                    Toast message = Toast.makeText(context, "You have already voted!", Toast.LENGTH_SHORT);
                    message.show();
                }


            }
            @Override
            public void onCancelled(DatabaseError databaseError){

            }
        });
    }

    static void showLocNote(final String groupName, final TextView loc1, final TextView loc2, final TextView loc3) {
        groupsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> currentGroupLocations =
                        (Map<String, Object>) dataSnapshot.child(groupName).child("Locations").getValue();

                if (currentGroupLocations != null) {

                    for (Map.Entry<String, Object> entry : currentGroupLocations.entrySet()) {

                        locNotes.add(dataSnapshot.child(groupName).child("Locations")
                                .child(entry.getKey()).child("Note").getValue().toString());
                    }

                    loc1.setText(" : " + locNotes.get(0));
                    loc2.setText(" : " + locNotes.get(1));
                    loc3.setText(" : " + locNotes.get(2));

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    static void getAllInfoForOrganizerDashboard(View view){

        final TextView tvDashLoc1 = (TextView) view.findViewById(R.id.tvDashLoc1);
        final TextView tvDashLoc2 = (TextView) view.findViewById(R.id.tvDashLoc2);
        final TextView tvDashLoc3 = (TextView) view.findViewById(R.id.tvDashLoc3);

        final TextView  locNote1 = (TextView) view.findViewById(R.id.locNote1);
        final TextView  locNote2 = (TextView) view.findViewById(R.id.locNote2);
        final TextView  locNote3 = (TextView) view.findViewById(R.id.locNote3);

        groupsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> currentGroupLocations =
                        (Map<String, Object>) dataSnapshot.child(GroupSelectionActivity.getGroup()).child("Locations").getValue();

                if (currentGroupLocations != null) {

                    for (Map.Entry<String, Object> entry : currentGroupLocations.entrySet()) {

                        locNotes.add(dataSnapshot.child(GroupSelectionActivity.getGroup()).child("Locations")
                                .child(entry.getKey()).child("Note").getValue().toString());
                    }

                    locNote1.setText(" : " + locNotes.get(0));
                    locNote2.setText(" : " + locNotes.get(1));
                    locNote3.setText(" : " + locNotes.get(2));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        groupsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> currentGroupLocations =
                        (Map<String, Object>) dataSnapshot.child(GroupSelectionActivity.getGroup()).child("Locations").getValue();

                if (currentGroupLocations != null) {

                    for (Map.Entry<String, Object> entry : currentGroupLocations.entrySet()) {

                        LocationVoteActivity.locationsName.add(entry.getKey());
                        locationNames.add(entry.getKey());
                    }

                    tvDashLoc1.setText(locationNames.get(0));
                    tvDashLoc2.setText(locationNames.get(1));
                    tvDashLoc3.setText(locationNames.get(2));

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void isGoing() {
        groupsRef.child(GroupSelectionActivity.getGroup())
                .child("event").child("participants")
                .child("going")
                .child(LoginActivity.getCurrentUser())
                .setValue(LoginActivity.getCurrentUser());
    }

    public static void isNotGoing() {
        groupsRef.child(GroupSelectionActivity.getGroup())
                .child("event").child("participants")
                .child("not_going")
                .child(LoginActivity.getCurrentUser())
                .setValue(LoginActivity.getCurrentUser());
    }

    public static void isMaybeGoing() {
        groupsRef.child(GroupSelectionActivity.getGroup())
                .child("event").child("participants")
                .child("maybe")
                .child(LoginActivity.getCurrentUser())
                .setValue(LoginActivity.getCurrentUser());
    }

    public static void populateEvent(final View v) {
        groupsRef.child(GroupSelectionActivity.getGroup())
                .child("event").child("Name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    TextView tvName = (TextView) v.findViewById(R.id.tvName);
                    tvName.setText(dataSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        groupsRef.child(GroupSelectionActivity.getGroup())
                .child("event").child("description").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    TextView tvDescription = (TextView) v.findViewById(R.id.tvDescription);
                    tvDescription.setText(dataSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        groupsRef.child(GroupSelectionActivity.getGroup())
                .child("event").child("startDate").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    TextView tvStart = (TextView) v.findViewById(R.id.tvStart);
                    tvStart.setText(dataSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        groupsRef.child(GroupSelectionActivity.getGroup())
                .child("event").child("startTime").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    TextView tvStart = (TextView) v.findViewById(R.id.tvStart);
                    tvStart.append(" " + dataSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        groupsRef.child(GroupSelectionActivity.getGroup())
                .child("event").child("endDate").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    TextView tvEnd = (TextView) v.findViewById(R.id.tvEnd);
                    tvEnd.setText(dataSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        groupsRef.child(GroupSelectionActivity.getGroup())
                .child("event").child("endTime").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    TextView tvEnd = (TextView) v.findViewById(R.id.tvEnd);
                    tvEnd.append(" " + dataSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
