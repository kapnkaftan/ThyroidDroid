package com.unmcelearning.android.thyroidpathology;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TypefaceSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

import static com.unmcelearning.android.thyroidpathology.LearnMenuFragment.subjectArrayList;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    private DatabaseReference mDatabase;
    private DatabaseReference mUsersDatabase;
    private DatabaseReference mSubjectsDatabase;
    ProgressDialog progressDialog;
    static TopicAdapter topicAdapter;
    ArrayList<Topic> topicArrayList;
    ArrayList<Level> levelArrayList;
    static SubjectAdapter subjectAdapter;
    private DatabaseReference mTopicReference;
    private DatabaseReference mCaseReference;
    private DatabaseReference mLevelGraduated;
    TabLayout tabLayout;
    ViewFlipper viewFlipper;
    ViewPager viewPager;

    static Users currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        viewFlipper = (ViewFlipper) findViewById(R.id.view_flipper);

        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {

                    getSupportActionBar().show();
                    viewFlipper.setDisplayedChild(0);
                    continueSetUpOfHomeActivity();

                } else {

                    viewFlipper.setDisplayedChild(1);
                    //getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
                    getSupportActionBar().hide();

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }

    public void continueSetUpOfHomeActivity() {

        topicArrayList = new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();

        currentUser = new Users(user.getEmail(), false);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mUsersDatabase = mDatabase.child("users").child(currentUser.username)
                .child("totalScore");


        ActionBar actionBar = getSupportActionBar();

        SpannableString title = new SpannableString("Thyroid Droid");

// Add a span for the sans-serif-light font
        title.setSpan(
                new TypefaceSpan("sans-serif-light"),
                0,
                title.length(),
                Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        actionBar.setTitle(title);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.drawable.ic_action_true_white_logo);
        actionBar.setDisplayUseLogoEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);

        HomeFragmentPagerAdapter adapter = new HomeFragmentPagerAdapter(getSupportFragmentManager());

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        changeTabsFont();

        viewPager.setCurrentItem(0);


        subjectArrayList = new ArrayList<String>();

        mSubjectsDatabase = mDatabase.child("Subjects");

        mTopicReference = mDatabase.child("Topics").child("Uterine Pathology");

        progressDialog = new ProgressDialog(HomeActivity.this);
        progressDialog.setMessage("Loading Courses...");
        progressDialog.show();

        mTopicReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> topicIndexesIterable = dataSnapshot.getChildren();
                Iterator iterateIndexesOfTopics = topicIndexesIterable.iterator();

                while (iterateIndexesOfTopics.hasNext()) {

                    Object orderNumberOfTopic = iterateIndexesOfTopics.next();
                    DataSnapshot orderNumberDataSnap = (DataSnapshot) orderNumberOfTopic;

                    Iterable<DataSnapshot> topicStringsIterable = orderNumberDataSnap.getChildren();
                    Iterator iterateStringsOfTopics = topicStringsIterable.iterator();

                    while (iterateStringsOfTopics.hasNext()) {

                        levelArrayList = new ArrayList<>();

                        levelArrayList.clear();

                        Object stringOfTopic = iterateStringsOfTopics.next();

                        DataSnapshot topicDataSnap = (DataSnapshot) stringOfTopic;

                        String topicString = topicDataSnap.getKey();

                        Iterable<DataSnapshot> topicLevels = topicDataSnap.getChildren();
                        Iterator iterateLevels = topicLevels.iterator();

                        while (iterateLevels.hasNext()) {
                            Object level = iterateLevels.next();
                            DataSnapshot levelDataSnap = (DataSnapshot) level;
                            String levelString = levelDataSnap.getKey();
                            String photoCreditString = levelDataSnap.child("PhotoCred").getValue(String.class);

                            levelArrayList.add(new Level(levelString, false, photoCreditString));
                        }

                        Topic currentTopic = new Topic(topicString, levelArrayList);

                        topicArrayList.add(currentTopic);

                    }

                }


                checkLevelGraduatedStatus();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_log_out:
                signOut();
                return true;
            case R.id.see_references:
                showReferences();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        //TODO not sure what to do about this
        //return super.onOptionsItemSelected(item);
    }

    private void signOut() {
        firebaseAuth.signOut();
        Intent signOutIntent = new Intent(this, LoginActivity.class);
        signOutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        finish();
        startActivity(signOutIntent);
    }

    private void showReferences() {

        Intent showReferencesIntent = new Intent(this, References.class);
        startActivity(showReferencesIntent);
    }

    private void changeTabsFont() {

        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                }
            }
        }
    }

    public void checkLevelGraduatedStatus() {

        mLevelGraduated = mDatabase.child("Graduated").child(currentUser.username);

        mLevelGraduated.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    for (Topic topic : topicArrayList) {

                        for (Level currentLevel : topic.getTopicLevelsArrayList()) {

                            String levelStringNoSpace = currentLevel.getLevelString().replaceAll("\\s+", "");

                            if (dataSnapshot.child(topic.getTopicName()).child(levelStringNoSpace).exists()) {

                                boolean didGraduateBoolean = dataSnapshot.child(topic.getTopicName()).child(levelStringNoSpace).getValue(Boolean.class);

                                currentLevel.setLevelGraduated(didGraduateBoolean);

                            }

                        }

                    }

                }

                topicAdapter = new TopicAdapter(HomeActivity.this, topicArrayList);

                LearnMenuFragment.setAdapter(topicAdapter);

                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
