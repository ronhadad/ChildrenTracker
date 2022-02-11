package com.example.loginregister;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.loginregister.polygon.Point;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FirstPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FirstPage extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FirstPage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FirstPage.
     */
    // TODO: Rename and change types and number of parameters
    public static FirstPage newInstance(String param1, String param2) {
        FirstPage fragment = new FirstPage();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    GoogleMap mygoogleMap;
    boolean firstGetGps=true;
    private SupportMapFragment mSupportMapFragment;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    Thread thread;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity m = (MainActivity) getActivity();
        View view = inflater.inflate(R.layout.fragment_first_page, container, false);
        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                m.exitt();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(m, callback);

        TextView btLogOut = view.findViewById(R.id.btLogOut);
        TextView btChildren = view.findViewById(R.id.btChildren);
        m.childrenSpinner = (Spinner) view.findViewById(R.id.spinnerChildren);
        m.parentsSpinner = (Spinner) view.findViewById(R.id.spinnerParents);
        m.refreshSpinners();
        m.childrenSpinner.setSelection(m.currentChildrenSpinnerInteger);

        mSupportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mSupportMapFragment == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            mSupportMapFragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace(R.id.map, mSupportMapFragment).commit();
        }

        if (mSupportMapFragment != null) {
            mSupportMapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    mygoogleMap = googleMap;
                    if (mygoogleMap != null) {
                        mygoogleMap.getUiSettings().setAllGesturesEnabled(true);
                        getGps(mygoogleMap,view);
                        mygoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(32.030960,34.768093),5));
                    }
                }
            });
        }

        thread = new Thread() {
            @Override
            public void run() {
                try {
                    while(true) {
                        boolean a = SendLocation(view);
                        getGps(mygoogleMap,view);
                        Log.i("Ron", "thread()");
                        if (a) {
                            ((TextView) view.findViewById(R.id.textView12)).setText("Sending loocation status: good");
                            ((TextView) view.findViewById(R.id.textView12)).setTextColor(Color.GREEN);

                        }
                        else {
                            ((TextView) view.findViewById(R.id.textView12)).setText("Sending loocation status: bad");
                            ((TextView) view.findViewById(R.id.textView12)).setTextColor(Color.RED);

                        }
                        sleep(6000);

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();

        CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);
        SharedPreferences sp1=m.getSharedPreferences("Follow",0);
        String f=sp1.getString("follow", null);
        if(f!=null && f.equals("true"))
            checkBox.setChecked(true);
        else
            checkBox.setChecked(false);


        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    focusMapsOnCurrentChildren(m);
                }
                SharedPreferences sp = m.getSharedPreferences("Follow", 0);
                SharedPreferences.Editor Ed = sp.edit();
                Ed.putString("follow", isChecked+"");
                Ed.commit();
            }
        });

        m.childrenSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView adapter, View v, int i, long lng) {
                updateMarkersAndPolygon(mygoogleMap);
                focusMapsOnCurrentChildren(m);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView)
            {

            }
        });
        btChildren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_firstPage2_to_children);
            }
        });

        btLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
                Navigation.findNavController(view).navigate(R.id.action_firstPage2_to_login);
            }
        });
        return view;
    }
    public void focusMapsOnCurrentChildren(MainActivity m){
        for(int z=0;z<m.myChildren.size();z++) {
            if (m.myChildren.get(z).username == m.childrenSpinner.getSelectedItem().toString()) {
                LatLng mamam = new LatLng(m.myChildren.get(z).lat,m.myChildren.get(z).lon);
                mygoogleMap.moveCamera(CameraUpdateFactory.newLatLng(mamam));
            }
        }
    }
    public boolean check5Minutes(String dateTimeFormat) {
        SimpleDateFormat format = new SimpleDateFormat("ddMMyyyyHHmmss");
        Date x = new Date();
        try {
            x = format.parse(dateTimeFormat);
        } catch (ParseException e) {
            Log.i("Ron", "CATCH!!!!!!!!!!");
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(x);
        cal.add(Calendar.MINUTE, 5);
        Calendar now = Calendar.getInstance();
        if (cal.after(now)) {
            return true;
        }
        return false;
    }

    public boolean SendLocation(View v) {
        MainActivity m = (MainActivity) getActivity();
        if((m.lat!=0 || m.lon!=0)&&m.userName!=null) {
            String mail = m.userName;
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat Format = new SimpleDateFormat("ddMMyyyyHHmmss");
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

            String dateTimeFormat = Format.format(calendar.getTime());
            String date = dateFormat.format(calendar.getTime());
            String time = timeFormat.format(calendar.getTime());
            Log.i("Ron", "SendLocation(): date:" + date+",time:"+time + ",user:"+mail);
            MyLocation l = new MyLocation(mail, m.lat, m.lon, date, time, dateTimeFormat);
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("users").child(l.getMail()).child("location").child(dateTimeFormat);
            DatabaseReference myRef2 = database.getReference("users").child(l.getMail()).child("location").child("last time");
            myRef.setValue(l);
            myRef2.setValue(l);
            return true;
        }
        else{
            return false;
        }
    }

    public void getGps(GoogleMap mygoogleMap,View view) {
        MainActivity m = (MainActivity) getActivity();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        // Read from the database
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                try {
                    for (int i = 0; i < m.myChildren.size(); i++) {
                        String gender = dataSnapshot.child(m.myChildren.get(i).username).child("settings").child("gender").getValue() + "";
                        double lat=-999.9;
                        double lon= -999.9;
                        String date=null;
                        String time=null;
                        String dateTimeFormat="never";
                        try {
                            lat = (double) dataSnapshot.child(m.myChildren.get(i).username).child("location").child("last time").child("latitude").getValue();
                            lon = (double) dataSnapshot.child(m.myChildren.get(i).username).child("location").child("last time").child("longitude").getValue();
                            date = (String) dataSnapshot.child(m.myChildren.get(i).username).child("location").child("last time").child("date").getValue();
                            time = (String) dataSnapshot.child(m.myChildren.get(i).username).child("location").child("last time").child("time").getValue();
                            dateTimeFormat = (String) dataSnapshot.child(m.myChildren.get(i).username).child("location").child("last time").child("dateTimeFormat").getValue();
                        }
                        catch (Exception e){}
                        m.myChildren.set(i, new Child(m.myChildren.get(i).username, lat, lon, date, time, dateTimeFormat,gender,m.myChildren.get(i).redZones));
                        for(int z=0;z<m.myChildren.get(i).redZones.size();z++)
                        {
                            if(m.myChildren.get(i).redZones.get(z).polygon.contains(new Point(lat,lon)) && !m.myChildren.get(i).redZones.get(z).insideRedZone) {
                                m.myChildren.get(i).redZones.get(z).insideRedZone = true;
                                Log.i("Ron", m.myChildren.get(i).username+" inside "+m.myChildren.get(i).redZones.get(z).redZoneName+" RED ZONE!");
                                m.addNotification("Update!",m.myChildren.get(i).username+" inside "+m.myChildren.get(i).redZones.get(z).redZoneName+" RED ZONE!");

                            }
                            if(!m.myChildren.get(i).redZones.get(z).polygon.contains(new Point(lat,lon)) && m.myChildren.get(i).redZones.get(z).insideRedZone) {
                                m.myChildren.get(i).redZones.get(z).insideRedZone = false;
                                Log.i("Ron", m.myChildren.get(i).username+" outside "+m.myChildren.get(i).redZones.get(z).redZoneName+" RED ZONE!");
                                m.addNotification("Update!",m.myChildren.get(i).username+" outside "+m.myChildren.get(i).redZones.get(z).redZoneName+" RED ZONE!");

                            }

                        }

                    }
                    if(firstGetGps || ((CheckBox)view.findViewById(R.id.checkBox)).isChecked()){
                        firstGetGps=false;
                        focusMapsOnCurrentChildren(m);
                    }
                    updateMarkersAndPolygon(mygoogleMap);
                } catch (Exception e) {
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });

        Log.i("Ron", "GetGps(): end");
    }
    void updateMarkersAndPolygon(GoogleMap mygoogleMap)
    {
        MainActivity m = (MainActivity) getActivity();
        String child =  m.childrenSpinner.getSelectedItem().toString();
        if(mygoogleMap!=null && m.myChildren!=null)
        {
            mygoogleMap.clear();
            for(int i=0;i<m.myChildren.size();i++){
                if(!m.myChildren.get(i).dateTimeFormat.equals("never")&&!m.myChildren.get(i).dateTimeFormat.equals(""))
                {
                    LatLng mamam = new LatLng(m.myChildren.get(i).lat, m.myChildren.get(i).lon);
                    if (check5Minutes(m.myChildren.get(i).dateTimeFormat)) {
                        mygoogleMap.addMarker(new MarkerOptions()
                                .position(mamam)
                                .title(m.myChildren.get(i).username + " last seen: less than 5 minutes")
                                .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.greenmarker)));
                        //mygoogleMap.moveCamera(CameraUpdateFactory.newLatLng(mamam));
                    } else {
                        mygoogleMap.addMarker(new MarkerOptions()
                                .position(mamam)
                                .title(m.myChildren.get(i).username + " last seen: more than 5 minutes")
                                .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.redmarker)));
                        //mygoogleMap.moveCamera(CameraUpdateFactory.newLatLng(mamam));
                    }
                }
            }
            int childInteger=0;
            for(int z=0;z<m.myChildren.size();z++)
            {
                if(m.myChildren.get(z).username.equals(child)){
                    childInteger=z;
                }
            }
            for(int z=0;z<m.myChildren.get(childInteger).redZones.size();z++)
            {
                PolygonOptions p = new PolygonOptions()
                        .add(new LatLng(m.myChildren.get(childInteger).redZones.get(z).points[0].latitude, m.myChildren.get(childInteger).redZones.get(z).points[0].longitude),
                                new LatLng(m.myChildren.get(childInteger).redZones.get(z).points[1].latitude, m.myChildren.get(childInteger).redZones.get(z).points[1].longitude),
                                new LatLng(m.myChildren.get(childInteger).redZones.get(z).points[2].latitude, m.myChildren.get(childInteger).redZones.get(z).points[2].longitude),
                                new LatLng(m.myChildren.get(childInteger).redZones.get(z).points[3].latitude, m.myChildren.get(childInteger).redZones.get(z).points[3].longitude),
                                new LatLng(m.myChildren.get(childInteger).redZones.get(z).points[0].latitude, m.myChildren.get(childInteger).redZones.get(z).points[0].longitude))
                        .fillColor(0x33FF0000)
                        .strokeColor(Color.RED)
                        .strokeWidth(3);
                mygoogleMap.addPolygon(p);
            }
            Log.i("Ron", "updateMarkersAndPolygon(): end");
        }
        else
        {
        }
    }
    public  void logOut(){
        MainActivity m = (MainActivity) getActivity();
        SharedPreferences sp= m.getSharedPreferences("Login", 0);
        SharedPreferences.Editor Ed=sp.edit();
        Ed.putString("Unm", null);
        Ed.putString("Psw",null);
        Ed.commit();
        sp= m.getSharedPreferences("Follow", 0);
        Ed=sp.edit();
        Ed.putString("follow", null);
        Ed.commit();

        m.userName=null;
        FlagClass.setFlag(false);
    }
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}