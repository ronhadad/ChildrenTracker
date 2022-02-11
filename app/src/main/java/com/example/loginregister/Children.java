package com.example.loginregister;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Children#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Children extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Children() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Children.
     */
    // TODO: Rename and change types and number of parameters
    public static Children newInstance(String param1, String param2) {
        Children fragment = new Children();
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
        View view = inflater.inflate(R.layout.fragment_children, container, false);
        MainActivity m = (MainActivity) getActivity();
        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(view).navigate(R.id.action_children_to_firstPage2);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(m, callback);

        // The callback can be enabled or disabled here or in handleOnBackPressed()

        m.listview = (ListView) view.findViewById(R.id.listview);
        m.listview.setAdapter(new ChildrenAdapter(m.myChildren, m));
        ImageView imgAddChild = (ImageView) view.findViewById(R.id.imgAddChild);
        m.listview = (ListView) view.findViewById(R.id.listview);
        imgAddChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m.addChild();
            }
        });
        ImageView btBackToHomePage = (ImageView) view.findViewById(R.id.btBackToHomePage);
        btBackToHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_children_to_firstPage2);
            }
        });
        return view;
    }

}