package com.unmcelearning.android.thyroidpathology;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by adamk_000 on 1/5/2017.
 */

public class CaseAdapter extends ArrayAdapter<Case> {

    Case currentCase;

    public CaseAdapter(Activity context, ArrayList<Case> caseArrayList) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for subject_view_for_adapter, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        //The final parameter is the list to be used.

        super(context, 0, caseArrayList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = null;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext()
                    .getSystemService(LAYOUT_INFLATER_SERVICE);
            itemView = inflater.inflate(R.layout.case_view_for_adapter, null);
        } else {
            itemView = convertView;
        }

        currentCase = getItem(position);

        TextView caseTV = (TextView) itemView.findViewById(R.id.case_text_view);


        final String currentCaseName = currentCase.getCaseName();
        final boolean currentCaseCompleted = currentCase.getCompleted();


        caseTV.setText(currentCaseName);
        if (currentCaseCompleted) {
            caseTV.setBackground(ContextCompat.getDrawable(getContext(),
                    R.drawable.rounded_text_view_corner_subjects_menu));

        } else {

            caseTV.setBackground(ContextCompat.getDrawable(getContext(),
                    R.drawable.rounded_text_view_corner_case_incomplete));
        }

        if (position == 9) {

            View lineView = (View) itemView.findViewById(R.id.line_view);
            lineView.setVisibility(View.GONE);

        }

        caseTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent openCaseActivity = new Intent(view.getContext(), CaseActivity.class);
                openCaseActivity.putExtra("caseString", currentCaseName);
                openCaseActivity.putExtra("caseCompleted", currentCaseCompleted);
                view.getContext().startActivity(openCaseActivity);

            }
        });


        return itemView;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }
}
