package com.example.blanche.projetfinal;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.BatteryManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
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
    private static List<String> users = new ArrayList<>();
    private static List< Map<String, String>> picturesGlob = new ArrayList<>();
    private static List< Map<String, String>> myPicturesGlob = new ArrayList<>();
    private static List<String> myURLS = new ArrayList<>();
    private static List<ImageItem> myImageItems = new ArrayList<>();
    private static Map<String, ImageItem> markerImages = new HashMap<>();

    private DatabaseManager() {}

    static void Init(Context context) {

        pm = new PreferencesManager(context);
        users.clear();
        markerImages.clear();

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);

        PowerLevelReceiver.initialPowerLevel = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);



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

        // Get most recent picture of each user
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, String> infos;
                if (dataSnapshot.exists()) {
                    //For all users
                    for (DataSnapshot usersIter : dataSnapshot.getChildren()) {
                        final String username = usersIter.getKey();
                        for (final DataSnapshot picturesIter : dataSnapshot.child(username).child("pictures").getChildren()) {
                            String isMostRecentPhoto = picturesIter.child("current").getValue().toString();
                            if (isMostRecentPhoto.equals("true")) {
                                final long ONE_MEGABYTE = 1024 * 1024;
                                final String filename = picturesIter.getKey().toString();
                                storageRef.child(username + "/" + filename).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(byte[] bytes) {
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                                        ImageItem mostRecentPhoto =
                                                new ImageItem(
                                                        bitmap,
                                                        filename,
                                                        picturesIter.child("description").getValue().toString(),
                                                        picturesIter.child("date").getValue().toString(),
                                                        Double.parseDouble(picturesIter.child("Lat").getValue().toString()),
                                                        Double.parseDouble(picturesIter.child("Long").getValue().toString()));

                                        markerImages.put(username, mostRecentPhoto);
                                    }
                                });
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if (pm.getCurrentUser() != null && !pm.getCurrentUser().equals(""))
            InitUser(pm.getCurrentUser().toString());
    }

    static void InitUser(final String username) {

        if (username != null && username != "") {
            usersRef.child(username).child("Following").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    if (dataSnapshot.exists()) {
                        final String username = dataSnapshot.getKey().toString();
                        usersRef.child(username).child("pictures").addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                int index = getPhotoIndex(picturesGlob, username, s);
                                if(index == -1){
                                    Map<String, String> infos = (Map<String, String>) dataSnapshot.getValue();
                                    infos.put("username", username);
                                    picturesGlob.add(infos);
                                }
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

            usersRef.child(username).child("pictures").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(final DataSnapshot dataSnapshot, String s) {

                    final long FIVE_MEGABYTE = 1024 * 1024 * 5;
                    final String filename = dataSnapshot.getKey().toString();
                    storageRef.child(username + "/"+filename).getBytes(FIVE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            myImageItems.add(new ImageItem(
                                    bitmap,
                                    filename,dataSnapshot.child("description").getValue().toString(),
                                    dataSnapshot.child("date").getValue().toString(),
                                    Double.parseDouble(dataSnapshot.child("Lat").getValue().toString()),
                                    Double.parseDouble(dataSnapshot.child("Long").getValue().toString())));
                        }
                    });

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                    int index = getPhotoItemIndex(myImageItems, dataSnapshot.child("filename").getValue().toString());
                    if(index != -1) {
                        myImageItems.remove(index);
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
    }

    static void loadPhotoAfterLogin(final String currentUser, final Context context, final View view) {
        picturesGlob.clear();
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, String> infos;
                if (dataSnapshot.exists()) {
                    //For all users
                    for (DataSnapshot usersIter : dataSnapshot.getChildren()) {
                        if (dataSnapshot.child(currentUser).child("Following").exists()) {
                            //On affiche que les photos de ceux dont on est abonné.
                            if (dataSnapshot.child(currentUser).child("Following").getValue().toString().contains(usersIter.getKey())) {
                                //For all pictures
                                for (DataSnapshot picturesIter : usersIter.child("pictures").getChildren()) {
                                    infos = (Map<String, String>) picturesIter.getValue();
                                    infos.put("username", usersIter.getKey());
                                    picturesGlob.add(infos);
                                }
                            }
                        }
                    }
                    TextView username = (TextView) view.findViewById(R.id.tvDashUsername);
                    TextView filename = (TextView) view.findViewById(R.id.tvDashFilename);
                    TextView date = (TextView) view.findViewById(R.id.tvDashDate);
                    TextView description = (TextView) view.findViewById(R.id.tvDashDescr);
                    ImageView iv = (ImageView) view.findViewById(R.id.ivDashPhoto);

                    if (!picturesGlob.isEmpty()) {
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
                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                .networkPolicy(NetworkPolicy.NO_CACHE)
                                .into(iv);
                    } else {
                        filename.setText("No Pictures!");
                    }
                    //InitUser(currentUser);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    // checks if user already exists in DB, if not create an account
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

    // function that adds the profile picture a user to DB
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

    static void storeDataInfo(final String username, final String password, final Activity activity) {
        pm.updateCurrentUser(username, password);
        picturesGlob.clear();
        myImageItems.clear();
        myURLS.clear();
        myPicturesGlob.clear();
        DatabaseManager.Init(activity);
        Intent goToDashboard = new Intent(activity, MainActivity.class);
        activity.startActivity(goToDashboard);
        activity.finishAffinity();
    }

    //Function that gets the profile picture from Firebase Storage and sets the imageView with it.
    static void loadProfilePhoto(String username, final Activity context, final ImageView iv) {
        storageRef.child(username + "/profile").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(context)
                        .load(uri)
                        .memoryPolicy(MemoryPolicy.NO_CACHE )
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .into(iv);
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                iv.setImageResource(R.mipmap.ic_profile_black);
            }

        });
    }

    static void loadProfile( final Activity context, final View view) {

        loadProfilePhoto(pm.getCurrentUser().toString(), context, (ImageView)view.findViewById(R.id.ivProfilePic));

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

    //Function that saves a picture to Firebase storage and realtime database.
    @SuppressWarnings("VisibleForTests")
    static void addPhotoToBD(final String filename, final String date, final String description,
                             final Bitmap bitmap, final Context context, final View view, final Location currentLocation) {

        final String username = pm.getCurrentUser();
        usersRef.child(username).child("pictures").child(filename).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Toast.makeText(context, "A picture with the same name already exists!", Toast.LENGTH_SHORT).show();
                }
                else {
                    ByteArrayOutputStream b = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);
                    final byte[] data = b.toByteArray();
                    //To save pictures files with Firebase storage
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
                            //To save the informations about the pictures in Firebase database
                            usersRef.child(username).child("pictures").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.getChildrenCount() > 0 ) {
                                        for (DataSnapshot picturesIter : dataSnapshot.getChildren()) {
                                            String b = picturesIter.child("current").getValue().toString();
                                            if (b.equals("true"))
                                                picturesIter.child("current").getRef().setValue("false");
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            Map<String, String> photo = new HashMap<String, String>();
                            photo.put("filename", filename);
                            photo.put("date", date);
                            photo.put("url", taskSnapshot.getDownloadUrl().toString());
                            photo.put("description", description);
                            photo.put("current", "true");
                            photo.put("Lat", Double.toString(currentLocation.getLatitude()));
                            photo.put("Long", Double.toString(currentLocation.getLongitude()));

                            ImageItem newPhoto =
                                    new ImageItem(
                                            bitmap,
                                            filename,
                                            description,
                                            date,
                                            currentLocation.getLatitude(),
                                            currentLocation.getLongitude());

                            markerImages.put(username, newPhoto);
                            usersRef.child(username).child("pictures").child(filename).setValue(photo);


                            view.findViewById(R.id.targetimage).setVisibility(ImageView.GONE);
                            ((EditText)view.findViewById(R.id.etFileName)).getText().clear();
                            ((EditText)view.findViewById(R.id.etDescription)).getText().clear();
                            view.findViewById(R.id.layout_upload).setVisibility(View.GONE);
                            view.findViewById(R.id.loadphoto).setVisibility(View.VISIBLE);
                            view.findViewById(R.id.takephoto).setVisibility(View.VISIBLE);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    //Function that loads the picture from the list picturesGlob, and uses the url of the picture
    // in Firebase storage to load it.
    static void loadHomePhoto(final View view, final Context context) {

        TextView username = (TextView) view.findViewById(R.id.tvDashUsername);
        TextView filename = (TextView) view.findViewById(R.id.tvDashFilename);
        TextView date = (TextView) view.findViewById(R.id.tvDashDate);
        TextView description = (TextView) view.findViewById(R.id.tvDashDescr);
        ImageView iv = (ImageView) view.findViewById(R.id.ivDashPhoto);


        if(!picturesGlob.isEmpty()) {
            if (HomeFragment.index >= picturesGlob.size()) {
                //We go back to the start
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

        }
        else{
            if(!pm.equals(null)) {
                loadPhotoAfterLogin(pm.getCurrentUser(), context, view);
            }
        }
    }

    // Fonction qui permet de recuperer tous les users existants
    // Cette fonction est appelee lors de la creation du SearchFragment pour initialiser le AutoCompleteTextView
    static List<String> getUsers() {
        return users;
    }

    static List<Map<String, String>> getMyPicturesGlob() { return myPicturesGlob; }

    static List<Map<String, String>> getPicturesGlob() { return picturesGlob; }

    static List<ImageItem> getMyImageItems() { return myImageItems; }

    static Map<String, ImageItem> getMarkerImages() { return markerImages; }

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
    //Function that returns the index of a picture in the list passed as argument using the username and the
    //filename.
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
    //Function that removes the pictures of previously followed people from the list that shows pictures.
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

    static void getFollowers(final Activity activity) {
        usersRef.child(pm.getCurrentUser()).child("Followers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
                dialog.setTitle("Followers");
                LayoutInflater inflater = activity.getLayoutInflater();
                final List<String> followers = new ArrayList<>();
                for (DataSnapshot usersIter : dataSnapshot.getChildren()) {
                    followers.add(usersIter.getKey().toString());
                }
                if (followers.size() == 0)
                    followers.add("You do not have any followers");
                final View dialogView = inflater.inflate(R.layout.followers_following, null);
                ListView lv = (ListView)dialogView.findViewById(R.id.lvFollow);
                ArrayAdapter adapter = new ArrayAdapter<>(activity,
                        android.R.layout.simple_list_item_1,
                        followers);
                lv.setAdapter(adapter);

                dialog.setView(dialogView);

                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

                final AlertDialog d = dialog.create();
                d.show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    static void getFollowing(final Activity activity) {
        usersRef.child(pm.getCurrentUser()).child("Following").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
                dialog.setTitle("Following");
                LayoutInflater inflater = activity.getLayoutInflater();
                final List<String> followers = new ArrayList<>();
                for (DataSnapshot usersIter : dataSnapshot.getChildren()) {
                    followers.add(usersIter.getKey().toString());
                }
                if (followers.size() == 0)
                    followers.add("You are not following anybody");
                final View dialogView = inflater.inflate(R.layout.followers_following, null);
                ListView lv = (ListView)dialogView.findViewById(R.id.lvFollow);
                ArrayAdapter adapter = new ArrayAdapter<>(activity,
                        android.R.layout.simple_list_item_1,
                        followers);
                lv.setAdapter(adapter);

                dialog.setView(dialogView);

                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

                final AlertDialog d = dialog.create();
                d.show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //Function that removes a picture from Firebase storage using it's name.
    static void deletePhoto(String filename) {
        String username = pm.getCurrentUser();
        usersRef.child(username).child("pictures").child(filename).removeValue();
        storageRef.child(username + "/"+filename).delete();
    }

    //Function that gets the index from the list of ImageItems using the filename.
    static int getPhotoItemIndex(List<ImageItem> photos, String filename) {
        int index = 0;

        for(; index < photos.size(); index++) {
            if(photos.get(index).getTitle().equals(filename)){
                return index;
            }
        }
        return -1;
    }

}