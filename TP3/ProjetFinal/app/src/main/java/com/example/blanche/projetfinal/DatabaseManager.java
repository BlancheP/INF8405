package com.example.blanche.projetfinal;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
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
    final public static List<String> users = new ArrayList<>();
    final public static List< Map<String, String>> picturesGlob = new ArrayList<>();
    final public static List< Map<String, String>> myPicturesGlob = new ArrayList<>();
    final public static List<String> myURLS = new ArrayList<>();
    final public static List<ImageItem> myImageItems = new ArrayList<>();

    private DatabaseManager() {}

    static void Init(Context context) {

        pm = new PreferencesManager(context);

        // Permet de recuperer les users et de modifier la liste de tous les users
        DatabaseManager.usersRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                users.add(dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                users.remove(s);
                users.add(dataSnapshot.getKey());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                users.remove(dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        usersRef.child(pm.getCurrentUser()).child("Following").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final String username = dataSnapshot.getKey().toString();
                 usersRef.child(username).child("pictures").addChildEventListener(new ChildEventListener() {
                     @Override
                     public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                         Map<String, String> infos = (Map<String, String>) dataSnapshot.getValue();
                         infos.put("username", username);
                         picturesGlob.add(infos);
                     }

                     @Override
                     public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                         int index = getPhotoIndex(picturesGlob, username, s);
                         if(index != -1){
                             picturesGlob.remove(index);

                         }
                         Map<String, String> infos = (Map<String, String>) dataSnapshot.getValue();
                         infos.put("username", username);
                         picturesGlob.add(infos);

                     }

                     @Override
                     public void onChildRemoved(DataSnapshot dataSnapshot) {
                         int index = getPhotoIndex(picturesGlob, username, dataSnapshot.child("filename").getValue().toString());
                         if(index != -1){
                             picturesGlob.remove(index);
                         }
                     }

                     @Override
                     public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                     }

                     @Override
                     public void onCancelled(DatabaseError databaseError) {

                     }
                 });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                deletePhotoFollower(dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        usersRef.child(pm.getCurrentUser()).child("pictures").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                               final long ONE_MEGABYTE = 1024 * 1024;
                final String filename = dataSnapshot.getKey().toString();
                storageRef.child(pm.getCurrentUser() + "/"+filename).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        myImageItems.add(new ImageItem(bitmap, filename));

                    }
                });

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // Fonction qui verifie si le user existe deja et s'il n'existe pas, l'ajoute a la BD
    static void addUser(final String password, final EditText etUsername, final Context context, final boolean hasPhoto, final Bitmap profilePicture) {
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

                    if (hasPhoto)
                        DatabaseManager.addProfilePhotoToBD(username, profilePicture);

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
        pm.updateCurrentUser(username, password);
        Intent goToDashboard = new Intent(activity, MainActivity.class);
        activity.startActivity(goToDashboard);
        activity.finishAffinity();
    }

    static void loadProfilePhoto( final Activity context) {
        String username = pm.getCurrentUser();
        storageRef.child(username + "/profile").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(context)
                        .load(uri)
                        .memoryPolicy(MemoryPolicy.NO_CACHE )
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .into((ImageView)context.findViewById(R.id.ivProfilePic));
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                ImageView iv = (ImageView)context.findViewById(R.id.ivProfilePic);
                iv.setImageResource(R.mipmap.ic_profile_black);
            }

        });
    }

    static void loadProfile( final Activity context, final View view) {

        loadProfilePhoto(context);

        final TextView followers = (TextView)view.findViewById(R.id.tvNbFollowers);
        final TextView following = (TextView)view.findViewById(R.id.tvNbFollowing);
        final TextView posts = (TextView)view.findViewById(R.id.tvNbPost);

        usersRef.child(DatabaseManager.getPreferencesManager().getCurrentUser()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long nb = dataSnapshot.child("Followers").getChildrenCount();
                followers.setText(String.valueOf(nb));
                nb = dataSnapshot.child("Following").getChildrenCount();
                following.setText(String.valueOf(nb));
                nb = dataSnapshot.child("pictures").getChildrenCount();
                posts.setText(String.valueOf(nb));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
                Map<String, String> photo = new HashMap<String, String>();
                photo.put("filename", filename);
                photo.put("date", date);
                photo.put("url", taskSnapshot.getDownloadUrl().toString());
                photo.put("description", description);
                usersRef.child(username).child("pictures").child(filename).setValue(photo);



            }
        });
    }

    static void loadHomePhoto(final View view, final Context context) {

        TextView username = (TextView) view.findViewById(R.id.tvDashUsername);
        TextView filename = (TextView) view.findViewById(R.id.tvDashFilename);
        TextView date = (TextView) view.findViewById(R.id.tvDashDate);
        TextView description = (TextView) view.findViewById(R.id.tvDashDescr);
        ImageView iv = (ImageView) view.findViewById(R.id.ivDashPhoto);


        if(!picturesGlob.isEmpty()) {
            if (HomeFragment.index >= picturesGlob.size()) {
                //on retourne au debut
                HomeFragment.index = 0;
            }


            filename.setText(picturesGlob.get(HomeFragment.index).get("filename"));
            username.setText(picturesGlob.get(HomeFragment.index).get("username"));
            date.setText(picturesGlob.get(HomeFragment.index).get("date"));
            description.setText(picturesGlob.get(HomeFragment.index).get("description"));
            Uri uri = Uri.parse(picturesGlob.get(HomeFragment.index).get("url"));
            Picasso.with(context)
                    .load(uri)
                    .memoryPolicy(MemoryPolicy.NO_CACHE )
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(iv);
            HomeFragment.justChanged = false;

        }
        else{
            filename.setText("No Pictures!");
        }
    }

    // Fonction qui permet de recuperer tous les users existants
    // Cette fonction est appelee lors de la creation du SearchFragment pour initialiser le AutoCompleteTextView
    static List<String> getUsers() {
        return users;
    }

    // Fonction qui permet de follow un user
    static void addFollow(Activity activity, String user, String follower) {
        usersRef.child(user).child("Followers").child(follower).setValue("true");
        usersRef.child(follower).child("Following").child(user).setValue("true");
        Toast.makeText(activity, "You are now following " + user + "!", Toast.LENGTH_SHORT).show();
        activity.findViewById(R.id.infoUserLayout).setVisibility(View.INVISIBLE);
        ((AutoCompleteTextView)activity.findViewById(R.id.actvSearchUsers)).setText("");
    }

    // Fonction qui permet de unfollow un user
    static void removeFollow(Activity activity, String user, String follower) {
        usersRef.child(user).child("Followers").child(follower).removeValue();
        usersRef.child(follower).child("Following").child(user).removeValue();
        Toast.makeText(activity, "You are now no more following " + user + "!", Toast.LENGTH_SHORT).show();
        activity.findViewById(R.id.infoUserLayout).setVisibility(View.INVISIBLE);
        ((AutoCompleteTextView)activity.findViewById(R.id.actvSearchUsers)).setText("");
    }

    static void isFollowing(final Activity activity, String user, String possibleFollowing) {
        usersRef.child(user).child("Following").child(possibleFollowing).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                    ((Button)activity.findViewById(R.id.bFollow)).setText("Unfollow");
                else
                    ((Button)activity.findViewById(R.id.bFollow)).setText("Follow");
                activity.findViewById(R.id.infoUserLayout).setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    static int getPhotoIndex(List<Map<String, String>> photos, String username, String filename){
        int index = 0;
        for(; index < photos.size(); index++){
           if(photos.get(index).get("username").equals(username)
                   && photos.get(index).get("filename").equals(filename)){
               return index;
           }
        }
        return -1;
    }

    static void deletePhotoFollower(String username){
        for(int i = 0; i < picturesGlob.size(); i++ ){
            if(picturesGlob.get(i).get("username").equals(username)){
                picturesGlob.remove(i);
                --i;
            }
        }
    }

    static boolean changePassword(final Activity activity, final String oldPwd, final String newPwd1, final String newPwd2, final View view, final DialogInterface dialog) {
        usersRef.child(pm.getCurrentUser()).child("password").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue().toString().equals(oldPwd)) {
                    if (newPwd1.equals(newPwd2)) {
                        usersRef.child(pm.getCurrentUser()).child("password").setValue(newPwd1);
                        dialog.cancel();
                    }
                    else {
                        ((TextView)view.findViewById(R.id.etConfirmPassword)).setError("Please enter the same password");
                    }
                }
                else {
                    ((TextView)view.findViewById(R.id.etOldPassword)).setError("You entered the wrong password");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return true;
    }

}