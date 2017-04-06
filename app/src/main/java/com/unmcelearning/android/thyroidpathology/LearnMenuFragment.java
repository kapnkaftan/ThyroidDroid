package com.unmcelearning.android.thyroidpathology;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import static com.unmcelearning.android.thyroidpathology.HomeActivity.topicAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class LearnMenuFragment extends Fragment {


    static ArrayList<String> subjectArrayList;

    static ListView listView;

    public LearnMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_learn_menu, container, false);

        listView = (ListView) rootView.findViewById(R.id.listview);

        setAdapter(topicAdapter);

        return rootView;

    }

    public static void setAdapter(TopicAdapter topicAdapter) {

        listView.setAdapter(topicAdapter);
    }

}
