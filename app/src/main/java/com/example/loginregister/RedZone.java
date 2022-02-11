package com.example.loginregister;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RedZone#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RedZone extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RedZone() {
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
    public static RedZone newInstance(String param1, String param2) {
        RedZone fragment = new RedZone();
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
        View view = inflater.inflate(R.layout.fragment_red_zone, container, false);
        MainActivity m = (MainActivity) getActivity();
        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(view).navigate(R.id.action_redZone_to_children);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(m, callback);

        m.listview2 = (ListView) view.findViewById(R.id.listview3);
        ArrayList<RedZoneClass> arr = new ArrayList<>();
        for(int i=0;i<m.myChildren.size();i++)
        {
            if(m.myChildren.get(i).username==m.currentChildren)
                arr = m.myChildren.get(i).redZones;
        }
        Log.i("Ron", "RedZone:" + arr);
        Log.i("Ron", "RedZone:" + m.currentChildren);
        Log.i("Ron", "RedZone:" + arr);
        m.listview2.setAdapter(new RedZoneAdapter(arr, m));
        ((TextView)view.findViewById(R.id.redZoneTitlePage)).setText(m.currentChildren+"'s red zone");

        ImageView imgCreateRedZone = (ImageView) view.findViewById(R.id.imgCreateRedZone);
        ImageView btBackToChildren = (ImageView) view.findViewById(R.id.btBackToChildren);
        imgCreateRedZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_redZone_to_creatRedZone);
            }
        });
        btBackToChildren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_redZone_to_children);
            }
        });

        return view;
    }


}