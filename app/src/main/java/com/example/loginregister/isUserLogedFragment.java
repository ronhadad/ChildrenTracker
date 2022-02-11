package com.example.loginregister;

import android.Manifest;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link isUserLogedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class isUserLogedFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public isUserLogedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment isUserLogedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static isUserLogedFragment newInstance(String param1, String param2) {
        isUserLogedFragment fragment = new isUserLogedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

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
        MainActivity m = (MainActivity)getActivity();
        View view = inflater.inflate(R.layout.fragment_is_user_loged, container, false);
        Log.i("Ronnnnnnnnnn", "ACCESS_FINE_LOCATION Permission:" + ActivityCompat.checkSelfPermission(m, Manifest.permission.ACCESS_FINE_LOCATION));
        Log.i("Ronnnnnnnnnn", "ACCESS_COARSE_LOCATION Permission:" + ActivityCompat.checkSelfPermission(m, Manifest.permission.ACCESS_COARSE_LOCATION));
        Log.i("Ronnnnnnnnnn", "ACCESS_BACKGROUND_LOCATION Permission:" + ActivityCompat.checkSelfPermission(m, Manifest.permission.ACCESS_BACKGROUND_LOCATION));

        checkIfTheUserSaved(m, view);

        /*Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    while(true) {
                        if(ActivityCompat.checkSelfPermission(m, Manifest.permission.ACCESS_FINE_LOCATION)!=0)
                        {
                            ((TextView)view.findViewById(R.id.btPermission)).setVisibility(View.VISIBLE);
                            ActivityCompat.requestPermissions(m,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},0);
                        }
                        if(ActivityCompat.checkSelfPermission(m, Manifest.permission.ACCESS_FINE_LOCATION)==0) {
                            checkIfTheUserSaved(m, view);
                            break;
                        }

                        sleep(2000);

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();*/
        return view;
    }

    public void checkIfTheUserSaved(MainActivity m,View view){
        SharedPreferences sp1=m.getSharedPreferences("Login",0);
        String unm=sp1.getString("Unm", null);
        String pass = sp1.getString("Psw", null);
        Log.i("Ron", "checkIfTheUserSaved() - SharedPreferences - user:"+unm+", password:"+pass);
        if(unm !=null && pass !=null){
            boolean a =funcLogin(unm,pass,m,view);
            Log.i("Ron", "checkIfTheUserSaved() move to firstpage fragment");
        }
        else{
            // waiting for the view initialaized
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    Navigation.findNavController(view).navigate(R.id.action_isUserLogedFragment_to_login);
                }
            }, 00);
        }
    }
    public boolean funcLogin(String mail, String password,MainActivity m,View view) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users").child(mail).child("settings");
        // Read from the database
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                try {
                    Person value = dataSnapshot.getValue(Person.class);
                    if (value.getUsername().equals(mail) && value.getPass().equals(password)) {
                        m.userName = mail;
                        FlagClass.setFlag(true);
                        Log.i("Ron", "Login() - Login OK");
                        Log.i("Ron", "Login() - move to firstpage fragment");
                        m.refreshLists(m.userName);
                        Navigation.findNavController(view).navigate(R.id.action_isUserLogedFragment_to_firstPage2);

                    } else {
                        FlagClass.setFlag(false);
                        Log.i("Ron", "Login() - Login NOT OK");
                        m.alert2("Try again"," username or password wrong.");
                        Navigation.findNavController(view).navigate(R.id.action_isUserLogedFragment_to_login);

                    }
                } catch (Exception e) {
                    FlagClass.setFlag(false);
                    Log.i("Ron", "Login() - Login NOT OK - Exception");
                    m.alert2("Try again"," username or password wrong.");
                    Navigation.findNavController(view).navigate(R.id.action_isUserLogedFragment_to_login);

                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
        return FlagClass.isFlag();
    }
}