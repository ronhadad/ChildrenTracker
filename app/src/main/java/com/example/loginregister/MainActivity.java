package com.example.loginregister;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.loginregister.polygon.Point;
import com.example.loginregister.polygon.Polygon;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements LocationListener {
    String userName = null;
    String currentChildren = null;
    int currentChildrenSpinnerInteger = 0;

    List<Child> myChildren = new ArrayList<>();
    List<String> wantToBeMyParents = new ArrayList<>();
    List<String> myParents = new ArrayList<>();
    protected LocationManager locationManager;
    double lat, lon;

    Spinner childrenSpinner;
    Spinner parentsSpinner;


    ListView listview;
    ListView listview2;
    ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        /*if(getSupportActionBar()!=null)
            this.getSupportActionBar().hide();*/

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (userName != null) {
            lat = location.getLatitude();
            lon = location.getLongitude();
            //Log.i("Ron", "onLocationChanged:" + lat + "," + lon);
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Ron", "Latitude disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Ron", "Latitude enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude", "status");
    }


    public void refreshLists(String userMail) {
        Log.i("Ron", "refreshLists(): start");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users").child(userMail).child("children");
        // My top posts by number of stars
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myChildren.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if (postSnapshot.child("isMyChildren").getValue().toString().equals("true")) {
                        ArrayList<RedZoneClass> redZone = new ArrayList<>();
                        for (DataSnapshot redZoneSnapshot : postSnapshot.child("RedZone").getChildren()) {
                            LatLng[] points = new LatLng[4];
                            String redZoneName = redZoneSnapshot.getKey();
                            //Log.i("Ronnnn", "refreshLists(): red zone name:"+redZoneName);
                            //Log.i("Ron", "red zone"+ redZoneSnapshot.getValue().toString());
                            for (int i = 0; i < 4; i++) {
                                double lat = 0;
                                double lon = 0;
                                try {
                                    lat = (double) redZoneSnapshot.child(i + "").child("latitude").getValue();
                                    lon = (double) redZoneSnapshot.child(i + "").child("longitude").getValue();
                                } catch (Exception e) {
                                }
                                points[i] = new LatLng(lat, lon);
                            }
                            redZone.add(new RedZoneClass(redZoneName, points));
                        }
                        double lat = 0;
                        double lon = 0;
                        myChildren.add(new Child(postSnapshot.getKey() + "", lat, lon, "", "", "", "", redZone));
                    }
                }
                Log.i("Ron", "refreshLists(): " + userMail + " children size:" + myChildren.size());
                refreshSpinners();
                refreshViewList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                //Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });
        myRef = database.getReference("users").child(userMail).child("parent");
        // My top posts by number of stars
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myParents.clear();
                wantToBeMyParents.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if (postSnapshot.getValue().toString().equals("true")) {
                        myParents.add(postSnapshot.getKey() + "");
                    } else {
                        boolean con =false;
                        for(int i=0;i<wantToBeMyParents.size();i++)
                        {
                            if(wantToBeMyParents.get(i)==postSnapshot.getKey())
                                con=true;
                        }
                        if(!con) {
                            wantToBeMyParents.add(postSnapshot.getKey() + "");
                            alert();
                            Log.i("alerttttttttttttttt", "aaaaaaaaaaaaaaa");
                        }

                    }
                }
                refreshSpinners();
                refreshViewList();
                Log.i("Ron", "refreshLists(): " + userMail + " parents size:" + myParents.size());
                Log.i("Ron", "refreshLists(): " + userMail + " wanrToBeParents size:" + wantToBeMyParents.size());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }


    public void addChild() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Add a child");
        alert.setMessage("Please write your child username");
        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String child = input.getText().toString();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("users").child(child);
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getInstance().getReference("users").child(child).child("parent").child(userName);
                            myRef.setValue(false);
                            myRef = database.getReference("users").child(userName).child("children").child(child).child("isMyChildren");
                            myRef.setValue(false);
                            Log.i("Ron", "addChild():");
                            Log.d("Ron", "Pin Value : " + child);
                            alert2("You Added " + child + " to be your child", "Now all that remains is to wait for " + child + "'s approval.");

                        } else {
                            alert2("Problem!", "We can't find this mail, please try again");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
                return;
            }
        });
        alert.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        return;
                    }
                });
        alert.show();

    }

    public void acceptParent(String child, String parent) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users").child(child).child("parent").child(parent);
        myRef.setValue(true);
        myRef = database.getReference("users").child(parent).child("children").child(child).child("isMyChildren");
        myRef.setValue(true);
        Log.i("Ron", "acceptParent(): " + child + " added " + parent + " to be his parent");
        //refreshLists(child);
    }

    private DatabaseReference mDatabase;

    public void removeChildParent(String child, String parent) {
        Log.i("Ron", "removeChildParent(): " + parent + " try to remove " + child + " to be his child");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users").child(parent).child("children").child(child);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("Ron", "removeChildParent(): " + parent + " removing " + child + " to be his child");
                dataSnapshot.getRef().removeValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        myRef = database.getReference("users").child(child).child("parent").child(parent);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().removeValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        Log.i("Ron", "removeChildParent(): " + parent + " removed " + child + " to be his child");
        //refreshLists(userMail);
    }

    public void alert() {
        Log.i("Ron", "alert()");
        if (!wantToBeMyParents.isEmpty()) {
            String parent = wantToBeMyParents.get(0);
            wantToBeMyParents.remove(0);
            Log.i("Ron", "inside alert()");
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setTitle(parent + " want to be your parent");
            builder1.setMessage("Note, Parent cannot be deleted. Only a parent can delete a child.");
            builder1.setCancelable(true);
            builder1.setPositiveButton(
                    "Accept",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Log.i("Ron", "alert():" + userName + " accept " + parent);
                            acceptParent(userName, parent);
                            myParents.add(parent);
                            alert2("Update!", "Congratulations! Now " + parent + " is your parent.");
                            dialog.cancel();
                            alert();
                        }
                    });

            builder1.setNegativeButton(
                    "Decline",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Log.i("Ron", "alert():" + userName + " decline " + parent);
                            removeChildParent(userName, parent);
                            dialog.cancel();
                            alert();
                        }
                    });
            builder1.setNeutralButton(
                    "Remind me next time",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Log.i("Ron", "alert():" + userName + " ask to remind next time for " + parent);
                            dialog.cancel();
                            alert();
                        }
                    });
            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
    }

    public void alert2(String title, String text) {
        Log.i("Ron", "alert()");
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle(title);
        builder1.setMessage(text);
        builder1.setCancelable(true);
        builder1.setNeutralButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void refreshSpinners() {
        if (childrenSpinner != null && parentsSpinner != null) {
            List<String> children = new ArrayList<>();
            for (int i = 0; i < myChildren.size(); i++) {
                children.add(myChildren.get(i).username);
            }
            List<String> parents = new ArrayList<>();
            for (int i = 0; i < myParents.size(); i++) {
                parents.add(myParents.get(i));
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, children);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            childrenSpinner.setAdapter(adapter);
            ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, parents);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            parentsSpinner.setAdapter(adapter2);
            Log.i("Ron", "refreshSpinners(): children list size: " + children.size());
            Log.i("Ron", "refreshSpinners(): parents list size: " + parents.size());
        }
    }

    public void keepMeSignIn(String user, String pass) {
        SharedPreferences sp = getSharedPreferences("Login", 0);
        SharedPreferences.Editor Ed = sp.edit();
        Ed.putString("Unm", user);
        Ed.putString("Psw", pass);
        Ed.commit();
    }


    public void refreshViewList() {
        Log.i("Ron", "refreshViewList(): start");
        ArrayList<RedZoneClass> arr = new ArrayList<>();
        int childrenInteger = 0;
        for (int i = 0; i < myChildren.size(); i++) {
            Log.i("Ron", "refreshViewList():" + myChildren.get(i).username + " red zone size: " + myChildren.get(i).redZones.size());
            if (myChildren.get(i).username.equals(currentChildren)) {
                childrenInteger = i;
                Log.i("Ron", "refreshViewList():" + myChildren.get(i).username + " is the current children: ");
            }

        }
        if (listview != null) {
            listview.setAdapter(new ChildrenAdapter(myChildren, this));
            Log.i("Ron", "refreshViewList(): currentChildren:" + currentChildren);
        }
        if (listview2 != null) {
            listview2.setAdapter(new RedZoneAdapter(myChildren.get(childrenInteger).redZones, this));
            Log.i("Ron", "refreshViewList(): arr:" + myChildren.get(childrenInteger).redZones.size());
        }
    }

    public void removeRedZone(String redZoneName) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users").child(userName).child("children").child(currentChildren).child("RedZone").child(redZoneName);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().removeValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    public void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "CHANNEL_NAME";
            String description = "CHANNEL_DESC";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("CHANNEL_ID", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    public void addNotification(String title, String description) {
        createNotificationChannel();
        //Log.d(TAG,"Add Notification");
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, "CHANNEL_ID")
                        .setSmallIcon(R.drawable.icon)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon))
                        //.setDefaults(Notification.DEFAULT_SOUND)
                        //.setSound(soundUri)
                        .setContentTitle(title)
                        .setContentText(description)
                        .setAutoCancel(true)
                        .setContentText(description);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;
        manager.notify(m, builder.build());
    }
    boolean doubleBackToExitPressedOnce=false;
    public void exitt() {
        if (!doubleBackToExitPressedOnce) {
            Toast.makeText(this, "Click again to exit", Toast.LENGTH_SHORT).show();
            doubleBackToExitPressedOnce = true;
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        } else {
            super.onBackPressed();
            return;
        }
    }

}