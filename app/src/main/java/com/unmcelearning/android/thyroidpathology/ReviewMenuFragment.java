package com.unmcelearning.android.thyroidpathology;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

import static com.unmcelearning.android.thyroidpathology.HomeActivity.currentUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewMenuFragment extends Fragment {

    static ListView listViewReview;
    private DatabaseReference mCaseDatabase;
    private DatabaseReference mCaseReference;
    ArrayList<Case> caseArrayList;
    CaseAdapter caseAdapter;

    public ReviewMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCaseDatabase = FirebaseDatabase.getInstance().getReference();

        mCaseReference = mCaseDatabase.child("Cases").child(currentUser.getUsername());

        caseArrayList = new ArrayList<>();

        String caseString = "Case";
        for (int i = 1; i < 11; i++) {

            String specificCaseString = caseString + " " + Integer.toString(i);
            Case specificCase = new Case(specificCaseString, false);
            caseArrayList.add(specificCase);

        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_review, container, false);
        listViewReview = (ListView) rootView.findViewById(R.id.listview2);

        mCaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot caseDataSnap) {

                if (!caseDataSnap.exists()) {

                    mCaseReference.setValue(caseArrayList);
                    caseAdapter = new CaseAdapter(getActivity(), caseArrayList);
                    ReviewMenuFragment.setAdapter(caseAdapter);

                } else {

                    caseArrayList.clear();
                    Iterable<DataSnapshot> cases = caseDataSnap.getChildren();
                    Iterator iterateCases = cases.iterator();

                    while (iterateCases.hasNext()) {
                        Object caseObject = iterateCases.next();
                        DataSnapshot currentCase = (DataSnapshot) caseObject;
                        String currentCaseString = currentCase.child("caseName").getValue(String.class);
                        boolean currentCaseCompleted = currentCase.child("completed").getValue(Boolean.class);

                        Case addNewCase = new Case(currentCaseString, currentCaseCompleted);

                        caseArrayList.add(addNewCase);
                    }

                    caseAdapter = new CaseAdapter(getActivity(), caseArrayList);
                    ReviewMenuFragment.setAdapter(caseAdapter);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return rootView;

    }

    public static void setAdapter(CaseAdapter caseAdapter) {

        listViewReview.setAdapter(caseAdapter);


    }

}
