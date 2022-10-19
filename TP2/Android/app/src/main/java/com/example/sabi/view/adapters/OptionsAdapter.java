package com.example.sabi.view.adapters;

import android.app.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sabi.R;
import com.example.sabi.view.static_data.MainOptions;

public class OptionsAdapter extends ArrayAdapter<String> {

    private final Activity context;

    public OptionsAdapter(final Activity context) {
        super(context, R.layout.option_item);
        this.context = context;
    }

    public View getView(int position, View view, ViewGroup parent) {
        final LayoutInflater inflater = context.getLayoutInflater();
        final View rowView = inflater.inflate(R.layout.option_item, null, true);

        TextView titleText = rowView.findViewById(R.id.title);
        ImageView imageView = rowView.findViewById(R.id.icon);
        TextView subtitleText = rowView.findViewById(R.id.subtitle);

        titleText.setText(MainOptions.list[position].titleResId);
        imageView.setImageResource(MainOptions.list[position].imageId);
        subtitleText.setText(MainOptions.list[position].subtitleResId);

        return rowView;

    }

    @Override
    public int getCount() {
        return MainOptions.list.length;
    }
}