package com.example.loginregister;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.navigation.Navigation;

import java.util.ArrayList;
import java.util.List;

public class ChildrenAdapter extends BaseAdapter implements ListAdapter {
    private List<Child> list = new ArrayList<Child>();
    private Context context;

    public ChildrenAdapter(List<Child> list, Context context) {
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
            view = inflater.inflate(R.layout.children_list_layout, null);
        }
        MainActivity m = (MainActivity) context;

        //Handle TextView and display string from your list
        TextView childNameTextView= (TextView)view.findViewById(R.id.childNameTextView);
        TextView lastSeenDateTextView= (TextView)view.findViewById(R.id.lastSeenDateTextView);
        TextView lastSeenTimeTextView= (TextView)view.findViewById(R.id.lastSeenTimeTextView);
        ImageView childrenIcon= (ImageView)view.findViewById(R.id.childrenIcon);
        ImageView locatonIcon= (ImageView)view.findViewById(R.id.locatonIcon);
        if(list.get(position).gender.equals("male"))
            childrenIcon.setImageResource(R.drawable.boy);
        else
            childrenIcon.setImageResource(R.drawable.girl);

        childNameTextView.setText(list.get(position).username);
        if(list.get(position).date!=null)
            lastSeenDateTextView.setText(list.get(position).date);
        else
            lastSeenDateTextView.setText("Never");
        if(list.get(position).time!=null)
            lastSeenTimeTextView.setText(list.get(position).time);
        else
            lastSeenTimeTextView.setText("");
        boolean redZone = false;
        for (int i=0;i<list.get(position).redZones.size();i++)
        {
            if(list.get(position).redZones.get(i).insideRedZone)
            {
                redZone=true;
            }
        }
        if(redZone) {
            ((RelativeLayout) view.findViewById(R.id.ChildLayout)).setBackgroundColor(Color.parseColor("#65FF0000"));

        }
        else
            ((RelativeLayout)view.findViewById(R.id.ChildLayout)).setBackgroundColor(Color.parseColor("#FFFFFFFF"));
        //Handle buttons and add onClickListeners
        ImageView imgRemoveChildren = (ImageView)view.findViewById(R.id.imgRemoveChildren);
        ImageView imgRedZoneList= (ImageView)view.findViewById(R.id.imgRedZoneList);
        imgRedZoneList.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                m.currentChildren = childNameTextView.getText().toString();
                Navigation.findNavController(v).navigate(R.id.action_children_to_redZone2);
            }
        });
        imgRemoveChildren.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String child = childNameTextView.getText().toString();
                Log.i("Ron", "clicked on Remove "+child);
                m.removeChildParent(child, m.userName);
                m.alert2("Successfully removed",child +" is no longer on your children's list.");
                m.refreshViewList();
            }
        });
        locatonIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int i=0;
                String child = childNameTextView.getText().toString();
                for(i=0;i<m.myChildren.size();i++){
                    m.childrenSpinner.setSelection(i);
                    Log.i("Ronnnnnnnn", i+":"+m.childrenSpinner.getSelectedItem().toString());
                    if(m.childrenSpinner.getSelectedItem().toString().equals(child))
                        break;
                }
                m.currentChildrenSpinnerInteger=i;
                Navigation.findNavController(v).navigate(R.id.action_children_to_firstPage2);

            }
        });
        /*addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                notifyDataSetChanged();
            .
            }
        });*/

        return view;
    }
}
