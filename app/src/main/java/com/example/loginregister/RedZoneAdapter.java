package com.example.loginregister;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.navigation.Navigation;

import java.util.ArrayList;
import java.util.List;

public class RedZoneAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<RedZoneClass> list = new ArrayList<RedZoneClass>();
    private Context context;

    public RedZoneAdapter(ArrayList<RedZoneClass> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 7;
        //just return 0 if your list items do not have an Id variable.
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.red_zone_list_layout, null);
        }
        MainActivity m = (MainActivity) context;

        //Handle TextView and display string from your list
        TextView redZoneNameTextView= (TextView)view.findViewById(R.id.redZoneNameTextView);
        redZoneNameTextView.setText(list.get(position).redZoneName);
        ImageView imgRemoveRedZone = (ImageView) view.findViewById(R.id.imgRemoveRedZone);
        imgRemoveRedZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m.removeRedZone(list.get(position).redZoneName);
            }
        });
        //Handle buttons and add onClickListeners
        return view;
    }
}
