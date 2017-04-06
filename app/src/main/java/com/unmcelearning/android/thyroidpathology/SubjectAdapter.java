package com.unmcelearning.android.thyroidpathology;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by adamk_000 on 11/28/2016.
 */

public class SubjectAdapter extends ArrayAdapter<String> {

    String currentString;

    public SubjectAdapter(Activity context, ArrayList<String> subjectArrayList) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for subject_view_for_adapter, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        //The final parameter is the list to be used.
        super(context, 0, subjectArrayList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = null;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext()
                    .getSystemService(LAYOUT_INFLATER_SERVICE);
            itemView = inflater.inflate(R.layout.subject_view_for_adapter, null);
        } else {
            itemView = convertView;
        }

        currentString = getItem(position);

        final TextView subjectTextView = (TextView) itemView.findViewById(R.id.subject_text_view);
        subjectTextView.setText(currentString);

        return itemView;
    }

}