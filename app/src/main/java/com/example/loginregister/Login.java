package com.example.loginregister;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Login#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Login extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Login() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Login.
     */
    // TODO: Rename and change types and number of parameters
    public static Login newInstance(String param1, String param2) {
        Login fragment = new Login();
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
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                m.exitt();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(m, callback);

        TextView signIn = view.findViewById(R.id.signInButtonSignin);
        TextView signUp = view.findViewById(R.id.signInButtonSignUp);
        //checkIfTheUserSaved(m,view);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_login_to_register);
            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FlagClass.isFlag()) {
                }
                else{
                    String user =((EditText)view.findViewById(R.id.TextsignInMail)).getText().toString();
                    String pass =((EditText)view.findViewById(R.id.TextsignInPassword)).getText().toString();
                    boolean a =funcLogin(user,pass,m,v);
                    m.keepMeSignIn(user,pass);
                    Log.i("Ron", "boolean of login:"+FlagClass.isFlag());
                }
            }
        });
        return view;
    }
    public boolean logInStatus = false;

    public boolean funcLogin(String username, String password,MainActivity m,View view) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users").child(username).child("settings");
        // Read from the database
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                try {
                    Person value = dataSnapshot.getValue(Person.class);
                    Log.i("Ron", "Login(): "+value.getUsername()+" ,"+value.getPass());
                    if (value.getUsername().equals(username) && value.getPass().equals(password)) {
                        m.userName = username;
                        logInStatus = true;
                        FlagClass.setFlag(true);
                        Log.i("Ron", "Login OK");
                        m.refreshLists(m.userName);
                        Log.i("Ron", "Login: move to firstpage fragment");
                        Navigation.findNavController(view).navigate(R.id.action_login_to_firstPage2);

                    } else {
                        logInStatus = false;
                        FlagClass.setFlag(false);
                        Log.i("Ron", "Login NOT OK");
                        m.alert2("Try again"," username or password wrong.");

                    }
                } catch (Exception e) {
                    logInStatus = false;
                    FlagClass.setFlag(false);
                    Log.i("Ron", "Login NOT OK - Exception");
                    m.alert2("Try again"," username or password wrong.");
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