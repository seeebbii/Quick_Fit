package com.example.quickfit.Services;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.quickfit.R;

import java.util.ArrayList;

public class ChatAdapter  extends ArrayAdapter<Object> {

    private static class ViewHolder {
        TextView name;
        TextView home;
    }



    public ChatAdapter(Context context, ArrayList<Object> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        // Get the data item for this position
        Object user = getItem(position);
        //boolean b = user.getClass().getDeclaredField("IsAdmin").getBoolean(user);
        //Log.d("tt", b ? "aa" : "bb");


        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_model, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.user);
            viewHolder.home = (TextView) convertView.findViewById(R.id.chatmsg);
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data from the data object via the viewHolder object
        // into the template view.


        Class c = user.getClass();

        try {
            Object fieldValue = c.getField("IsAdmin").get(user);
            viewHolder.name.setText( (Boolean)fieldValue ? "Admin" : "User");

            Object fieldValue2 = c.getField("msg").get(user);
            viewHolder.home.setText( (String)fieldValue2);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }


        return convertView;
    }

}
