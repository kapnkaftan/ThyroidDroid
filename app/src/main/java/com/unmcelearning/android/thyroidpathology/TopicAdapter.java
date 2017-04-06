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
 * Created by adamk_000 on 11/30/2016.
 */

public class TopicAdapter extends ArrayAdapter<Topic> {

    Topic currentTopic;

    public TopicAdapter(Activity context, ArrayList<Topic> topicArrayList) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for subject_view_for_adapter, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        //The final parameter is the list to be used.

        super(context, 0, topicArrayList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = null;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext()
                    .getSystemService(LAYOUT_INFLATER_SERVICE);
            itemView = inflater.inflate(R.layout.topic_view_for_adapter, null);
        } else {
            itemView = convertView;
        }

        currentTopic = getItem(position);
        final String currentTopicName = currentTopic.getTopicName();
        final ArrayList<Level> currentTopicLevelsArrayList = currentTopic.getTopicLevelsArrayList();
        int arraySize = currentTopicLevelsArrayList.size();

        TextView levelOneTV = (TextView) itemView.findViewById(R.id.level_one);
        TextView levelTwoTV = (TextView) itemView.findViewById(R.id.level_two);
        TextView levelThreeTV = (TextView) itemView.findViewById(R.id.level_three);
        TextView levelFourTV = (TextView) itemView.findViewById(R.id.level_four);
        TextView levelFiveTV = (TextView) itemView.findViewById(R.id.level_five);
        TextView levelSixTV = (TextView) itemView.findViewById(R.id.level_six);

        levelOneTV.setVisibility(View.GONE);
        levelTwoTV.setVisibility(View.GONE);
        levelThreeTV.setVisibility(View.GONE);
        levelFourTV.setVisibility(View.GONE);
        levelFiveTV.setVisibility(View.GONE);
        levelSixTV.setVisibility(View.GONE);

        ArrayList<TextView> textViewArrayList = new ArrayList<>();
        textViewArrayList.add(levelOneTV);
        textViewArrayList.add(levelTwoTV);
        textViewArrayList.add(levelThreeTV);
        textViewArrayList.add(levelFourTV);
        textViewArrayList.add(levelFiveTV);
        textViewArrayList.add(levelSixTV);

        for (int i = 0; i < arraySize; i++) {

            final TextView currentTextView = textViewArrayList.get(i);
            final int INDEX = i;

            currentTextView.setVisibility(View.VISIBLE);
            currentTextView.setText(currentTopicLevelsArrayList.get(i).getLevelString());
            currentTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    TextView b = (TextView) view;
                    String currentLevelName = b.getText().toString();


                    Intent openQuizzerActivity = new Intent(view.getContext(), Quizzer.class);
                    openQuizzerActivity.putExtra("topicString", currentTopicName);
                    openQuizzerActivity.putExtra("levelString", currentLevelName);
                    openQuizzerActivity.putExtra("photoCredString", currentTopicLevelsArrayList.get(INDEX).getPhotoCred());
                    view.getContext().startActivity(openQuizzerActivity);
                }
            });

            if (currentTopicLevelsArrayList.get(i).getLevelGraduated()) {

                currentTextView.setBackground(ContextCompat.getDrawable(getContext(),
                        R.drawable.rounded_text_view_corner_subjects_menu));

            } else {

                currentTextView.setBackground(ContextCompat.getDrawable(getContext(),
                        R.drawable.rounded_text_view_corner_case_incomplete));

            }

        }

        TextView subjectTextView = (TextView) itemView.findViewById(R.id.topic_text_view);
        subjectTextView.setText(currentTopicName);

        return itemView;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

}
