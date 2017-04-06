package com.unmcelearning.android.thyroidpathology;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    TextView homeTotalScore;
    TextView firstPlace;
    TextView secondPlace;
    TextView thirdPlace;
    TextView fourthPlace;
    TextView fifthPlace;
    TextView topScores;
    ImageView logoImageView;

    private DatabaseReference mDatabase;
    private DatabaseReference mUsersDatabase;
    Query getTopScores;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mDatabase = FirebaseDatabase.getInstance().getReference();
        mUsersDatabase = mDatabase.child("users").child(HomeActivity.currentUser.username)
                .child("totalScore");

    }

    static <K, V extends Comparable<? super V>>
    List<Map.Entry<K, V>> entriesSortedByValues(Map<K, V> map) {

        List<Map.Entry<K, V>> sortedEntries = new ArrayList<Map.Entry<K, V>>(map.entrySet());

        Collections.sort(sortedEntries,
                new Comparator<Map.Entry<K, V>>() {
                    @Override
                    public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2) {
                        return e2.getValue().compareTo(e1.getValue());
                    }
                }
        );

        return sortedEntries;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        homeTotalScore = (TextView) rootView.findViewById(R.id.total_score_home);

        firstPlace = (TextView) rootView.findViewById(R.id.first_place);
        secondPlace = (TextView) rootView.findViewById(R.id.second_place);
        thirdPlace = (TextView) rootView.findViewById(R.id.third_place);
        fourthPlace = (TextView) rootView.findViewById(R.id.fourth_place);
        fifthPlace = (TextView) rootView.findViewById(R.id.fifth_place);
        logoImageView = (ImageView) rootView.findViewById(R.id.logo_image_view);
        topScores = (TextView) rootView.findViewById(R.id.top_scores_title);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        getTopScores = mDatabase.child("users").orderByChild("totalScore").limitToLast(5);

        getTopScores.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                HashMap scoreHashMap = new HashMap();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    DataSnapshot scoreDS = postSnapshot.child("totalScore");

                    Long scoreLong = scoreDS.getValue(Long.class);

                    DataSnapshot usernameDS = postSnapshot.child("username");

                    String usernameString = usernameDS.getValue(String.class);

                    String check = postSnapshot.toString();

                    scoreHashMap.put(usernameString, scoreLong);
                }
                List orderedScores = entriesSortedByValues(scoreHashMap);

                //if user is in top five, change color to pink in scoreboard
                setUsernameColor(orderedScores);

                String firstPlaceString = orderedScores.get(0).toString().replaceAll("=", " ");
                String secondPlaceString = orderedScores.get(1).toString().replaceAll("=", " ");
                String thirdPlaceString = orderedScores.get(2).toString().replaceAll("=", " ");
                String fourthPlaceString = orderedScores.get(3).toString().replaceAll("=", " ");
                String fifthPlaceString = orderedScores.get(4).toString().replaceAll("=", " ");


                firstPlace.setText(firstPlaceString);
                secondPlace.setText(secondPlaceString);
                thirdPlace.setText(thirdPlaceString);
                fourthPlace.setText(fourthPlaceString);
                fifthPlace.setText(fifthPlaceString);

                firstPlace.setVisibility(View.VISIBLE);
                secondPlace.setVisibility(View.VISIBLE);
                thirdPlace.setVisibility(View.VISIBLE);
                fourthPlace.setVisibility(View.VISIBLE);
                fifthPlace.setVisibility(View.VISIBLE);
                homeTotalScore.setVisibility(View.VISIBLE);

                topScores.setVisibility(View.VISIBLE);
                logoImageView.setVisibility(View.VISIBLE);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });


        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long totalScore = dataSnapshot.getValue(Long.class);
                String totalScoreString = String.valueOf(totalScore);
                homeTotalScore.setText("Your Score: " + totalScoreString);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setUsernameColor(List topFive) {

        firstPlace.setTextColor(Color.parseColor("#AD122A"));
        secondPlace.setTextColor(Color.parseColor("#AD122A"));
        thirdPlace.setTextColor(Color.parseColor("#AD122A"));
        fourthPlace.setTextColor(Color.parseColor("#AD122A"));
        fifthPlace.setTextColor(Color.parseColor("#AD122A"));

        int i = 0;

        for (Object items : topFive) {

            String topScorer = items.toString();

            int atIndex = topScorer.indexOf("=");
            topScorer = topScorer.substring(0, atIndex);

            if (HomeActivity.currentUser.getUsername().equals(topScorer)) {

                switch (i) {
                    case 0:
                        firstPlace.setTextColor(Color.parseColor("#000000"));
                        break;
                    case 1:
                        secondPlace.setTextColor(Color.parseColor("#000000"));
                        break;
                    case 2:
                        thirdPlace.setTextColor(Color.parseColor("#000000"));
                        break;
                    case 3:
                        fourthPlace.setTextColor(Color.parseColor("#000000"));
                        break;
                    case 4:
                        fifthPlace.setTextColor(Color.parseColor("#000000"));
                        break;
                }

            }

            i++;
        }

    }

}
