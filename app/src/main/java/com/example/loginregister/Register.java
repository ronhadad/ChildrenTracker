package com.example.loginregister;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Register#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class Register extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Register.
     */
    // TODO: Rename and change types and number of parameters
    public static Register newInstance(String param1, String param2) {
        Register fragment = new Register();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Register() {
        // Required empty public constructor
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
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        MainActivity m = (MainActivity) getActivity();
        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(view).navigate(R.id.action_register_to_login);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(m, callback);

        ArrayAdapter<String> arrayAdapter;
        String [] str = {"male", "female"};
        Spinner genderSpinner = (Spinner) view.findViewById (R.id.genderSpinner);

        arrayAdapter = new ArrayAdapter <String> (m, android.R.layout.simple_spinner_item, str);
        arrayAdapter.setDropDownViewResource (android.R.layout.simple_spinner_item);
        genderSpinner.setAdapter (arrayAdapter);

        ImageView back = view.findViewById(R.id.SignUpBackButton);
        TextView signUp = view.findViewById(R.id.signUpButtonSignUp);
        EditText usernameEditText = ((EditText) view.findViewById(R.id.textSignUpUserName));
        EditText passEditText = ((EditText) view.findViewById(R.id.textSignUpPassword));
        EditText phoneEditText = ((EditText) view.findViewById(R.id.textSignUpPhone));
        EditText idEditText = ((EditText) view.findViewById(R.id.textSignUpId));
        idEditText.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                String id = ((EditText) view.findViewById(R.id.textSignUpId)).getText().toString();

                if (id.length()>=9) {
                    ((RelativeLayout)(view.findViewById(R.id.idRelativeLayout))).setBackgroundResource(R.drawable.green_border_rounded_cornwe);
                }
                else {
                    ((RelativeLayout)(view.findViewById(R.id.idRelativeLayout))).setBackgroundResource(R.drawable.red_border_rounded_cornwe);
                }
                // TODO: the editText has just been left
            }
        });

        phoneEditText.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                String phone = ((EditText) view.findViewById(R.id.textSignUpPhone)).getText().toString();

                if (phone.length()>=10) {
                    ((RelativeLayout)(view.findViewById(R.id.phoneRelativeLayout))).setBackgroundResource(R.drawable.green_border_rounded_cornwe);
                }
                else {
                    ((RelativeLayout)(view.findViewById(R.id.phoneRelativeLayout))).setBackgroundResource(R.drawable.red_border_rounded_cornwe);
                }
                // TODO: the editText has just been left
            }
        });

        passEditText.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                String pass = ((EditText) view.findViewById(R.id.textSignUpPassword)).getText().toString();

                if (pass.length()>=8) {
                    ((RelativeLayout)(view.findViewById(R.id.passwordRelativeLayout))).setBackgroundResource(R.drawable.green_border_rounded_cornwe);
                }
                else {
                    ((RelativeLayout)(view.findViewById(R.id.passwordRelativeLayout))).setBackgroundResource(R.drawable.red_border_rounded_cornwe);
                }
                // TODO: the editText has just been left
            }
        });

        usernameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                String username = ((EditText) view.findViewById(R.id.textSignUpUserName)).getText().toString();
                if (username.length()>=4) {
                    ((RelativeLayout)(view.findViewById(R.id.usernameRelativeLayout))).setBackgroundResource(R.drawable.green_border_rounded_cornwe);
                }
                else {
                    ((RelativeLayout)(view.findViewById(R.id.usernameRelativeLayout))).setBackgroundResource(R.drawable.red_border_rounded_cornwe);
                }
                // TODO: the editText has just been left
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_register_to_login);
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = ((EditText) view.findViewById(R.id.textSignUpUserName)).getText().toString();
                String pass = ((EditText) view.findViewById(R.id.textSignUpPassword)).getText().toString();
                String phone = ((EditText) view.findViewById(R.id.textSignUpPhone)).getText().toString();
                String id = ((EditText) view.findViewById(R.id.textSignUpId)).getText().toString();
                String gender = ((Spinner) view.findViewById(R.id.genderSpinner)).getSelectedItem().toString();
                funcRegister(username,pass,phone,id,gender,view);
            }
        });
        return view;
    }
    public void funcRegister(String username,String pass, String phone,String id,String gender, View view) {
        MainActivity m = (MainActivity) getActivity();

        if(username.length()>=4 && pass.length()>=8 && phone.length()>=10 && id.length()>=9) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("users").child(username);
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        m.alert2("This username already exist", "try another username");
                    } else {

                        Person p = new Person(username, pass, phone, id, gender);
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("users").child(p.getUsername()).child("settings");
                        myRef.setValue(p);
                        Toast.makeText(m, "Successfully registered", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(view).navigate(R.id.action_register_to_login);

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
        else
        {
            if (username.length()<4) {
                ((RelativeLayout)(view.findViewById(R.id.usernameRelativeLayout))).setBackgroundResource(R.drawable.red_border_rounded_cornwe);
            }
            if (pass.length()<8) {
                ((RelativeLayout)(view.findViewById(R.id.passwordRelativeLayout))).setBackgroundResource(R.drawable.red_border_rounded_cornwe);
            }
            if (phone.length()<10) {
                ((RelativeLayout)(view.findViewById(R.id.phoneRelativeLayout))).setBackgroundResource(R.drawable.red_border_rounded_cornwe);
            }
            if (id.length()<9) {
                ((RelativeLayout)(view.findViewById(R.id.idRelativeLayout))).setBackgroundResource(R.drawable.red_border_rounded_cornwe);
            }

            m.alert2("Something got wrong", "fill all the red boxes");

        }
    }

}