package com.example.francescop.cul8r;

import android.graphics.Color;
import android.util.Log;
import android.widget.ArrayAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

class MyArrayAdapter extends ArrayAdapter<Event> {
    private final Context context;
    private final Event[] values;
    private String now;

    MyArrayAdapter(Context context, ArrayList<Event> values) {
        super(context, R.layout.rowlayout, values);
        this.context = context;
        Log.d("***","values: "+values);
        this.values = values.toArray(new Event[values.size()]);
        Log.d("***","values[0]"+this.values[0]);
        final Date nowDate = Calendar.getInstance().getTime();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        now = format1.format(nowDate);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.rowlayout, parent, false);
        TextView description = (TextView) rowView.findViewById(R.id.firstLine);
        TextView date = (TextView) rowView.findViewById(R.id.secondLine);
        ImageView image = (ImageView) rowView.findViewById(R.id.icon);
        description.setText(values[position].getDescription());
        date.setText("Start:"+values[position].getStart()+" - End:"+values[position].getEnd());
        //expired events in light gray
        if(values[position].getEnd().compareTo(now)<0) {
            description.setTextColor(Color.LTGRAY);
            date.setTextColor(Color.LTGRAY);
        }
        // Change the icon for verified events
        if (values[position].isVerified()) {
            image.setImageResource(R.drawable.ic_today_black);
        } else {
            image.setImageResource(R.drawable.ic_today_white);
        }

        return rowView;
    }
}
