package com.example.loginregister;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateRedZone#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateRedZone extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CreateRedZone() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RedZone.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateRedZone newInstance(String param1, String param2) {
        CreateRedZone fragment = new CreateRedZone();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    GoogleMap mygoogleMap;
    private SupportMapFragment mSupportMapFragment;
    LatLng redZone [];
    int redZoneInteger;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        MainActivity m = (MainActivity) getActivity();
        View view = inflater.inflate(R.layout.fragment_create_red_zone, container, false);
        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(view).navigate(R.id.action_creatRedZone_to_redZone);

            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(m, callback);

        ImageView btSave =(ImageView) view.findViewById(R.id.btSave);
        ImageView btBack =(ImageView ) view.findViewById(R.id.btBack);
        ImageView btClearMarkers = (ImageView) view.findViewById(R.id.btClearMarkers);
        redZone = new LatLng[4];
        redZoneInteger=0;
        mSupportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map2);
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
                        mygoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                            @Override
                            public void onMapClick(final LatLng point) {
                                //if (PolyUtil.isLocationOnPath(point, finalPolylines.getPoints(), true)) {
                                // user clicked on polyline
                                if(redZoneInteger==4)
                                    redZoneInteger=0;
                                redZone[redZoneInteger]=point;
                                redZoneInteger++;
                                updateRedZoneMarkers();
                            }
                            //}
                        });
                    }

                }
            });
        }
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_creatRedZone_to_redZone);

            }
        });
        btClearMarkers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redZoneInteger=0;
                redZone = new LatLng[4];
                updateRedZoneMarkers();
            }
        });
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String redZoneName = ((EditText)view.findViewById(R.id.editTextCreateRedZone)).getText().toString();
                if(!redZoneName.equals("")) {
                    if(redZone[3]!=null) {
                        saveRedZone(redZoneName, m.userName, m.currentChildren, view);
                        Log.i("Ron", "btSave:" + m.currentChildren);
                    }
                    else{
                        m.alert2("Problem","Please choose 4 points for your red zone.");
                    }
                }
                else{
                    m.alert2("Problem","Please fill the red zone name.");
                    ((RelativeLayout)(view.findViewById(R.id.redZoneNameRelativeLayout))).setBackgroundResource(R.drawable.red_border_rounded_cornwe);

                }
            }
        });
        return view;
    }
    void updateRedZoneMarkers()
    {

        mygoogleMap.clear();
        for(int i=0;i<4;i++) {
            if (redZone[i] != null) {
                LatLng mamam = redZone[i];
                mygoogleMap.addMarker(new MarkerOptions()
                        .position(mamam)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            }
        }

        if(redZone[3]!=null) {
            PolygonOptions p = new PolygonOptions()
                    .add(new LatLng(redZone[0].latitude, redZone[0].longitude),
                            new LatLng(redZone[1].latitude, redZone[1].longitude),
                            new LatLng(redZone[2].latitude, redZone[2].longitude),
                            new LatLng(redZone[3].latitude, redZone[3].longitude),
                            new LatLng(redZone[0].latitude, redZone[0].longitude))
                    .fillColor(0x33FF0000)
                    .strokeColor(Color.RED)
                    .strokeWidth(3);
            mygoogleMap.addPolygon(p);

        }
    }
    public void saveRedZone(String redZoneName,String user,String child,View v){
        Log.i("Ron", "saveRedZone():"+user+","+child+","+redZoneName);
        MainActivity m = (MainActivity)getActivity();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users").child(user).child("children").child(child).child("RedZone").child(redZoneName);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(m);
                    builder1.setTitle("This name is alredy exist");
                    builder1.setMessage("Do you want to overwrite the existing name?");
                    builder1.setCancelable(true);
                    builder1.setPositiveButton(
                            "Overwrite",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference myRef = database.getReference("users").child(user).child("children").child(child).child("RedZone").child(redZoneName);
                                    for(int i=0;i<4;i++) {
                                        myRef.child(i+"").child("latitude").setValue(redZone[i].latitude);
                                        myRef.child(i+"").child("longitude").setValue(redZone[i].longitude);
                                    }
                                    Navigation.findNavController(v).navigate(R.id.action_creatRedZone_to_redZone);
                                }
                            });

                    builder1.setNegativeButton(
                            "Decline",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
                else
                {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("users").child(user).child("children").child(child).child("RedZone").child(redZoneName);
                    for(int i=0;i<4;i++) {
                        myRef.child(i+"").child("latitude").setValue(redZone[i].latitude);
                        myRef.child(i+"").child("longitude").setValue(redZone[i].longitude);
                    }
                    Navigation.findNavController(v).navigate(R.id.action_creatRedZone_to_redZone);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        Log.i("Ron", "saveRedZone():"+user+","+child+","+redZoneName);

    }
}