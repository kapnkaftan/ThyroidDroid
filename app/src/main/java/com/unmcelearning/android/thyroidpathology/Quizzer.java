package com.unmcelearning.android.thyroidpathology;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.TypefaceSpan;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static android.view.View.GONE;

public class Quizzer extends AppCompatActivity {

    static ArrayList<Question> questionArrayList = new ArrayList<Question>();
    static Map<String, String> stringQuestionHashMap = new HashMap<>();
    static Random randomObject = new Random();
    static int questionCount;
    static Question currentQuestion;
    static Question previousQuestion;
    static TextView questionTextView;
    static TextView scoreTextView;
    static TextView questionTextViewInvisible;
    static TextView correctAnsTextView;
    static TextView ansExplanationTextView;
    static String photoCreditStringQuizzer;
    static boolean graduated;

    static RadioGroup radioGroup;
    static RadioButton radioButton1;
    static RadioButton radioButton2;
    static RadioButton radioButton3;
    static RadioButton radioButton4;
    static Button multipleChoiceSubmitButton;
    static Button continueLessonButton;
    static Button showAnswerButton;
    static LinearLayout gradeSelfLinearLayout;
    static Button gradeSelfCorrect;
    static Button gradeSelfIncorrect;
    static long currentQuestionTimesAnsCorrect;
    static long currentQuestionTimesAnsIncorrect;

    static EditText answerEditText;
    static String subjectString;
    static String levelString;
    static String[] textStringArray;
    ProgressDialog quizzerProgressDialog;
    static Button enterButton;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference subjectRef;
    StorageReference levelRef;
    StorageReference imageRef;
    static TextView[] textViewArray;
    static long currentScore;
    private DatabaseReference mDatabase;
    private DatabaseReference mQuestionDatabase;
    private DatabaseReference mUsersDatabase;
    ViewPager viewPager;
    static boolean questionOrAnswer;
    static boolean correctOrIncorrect;
    final long ONE_MEGABYTE = 1024 * 1024;
    public static final String PREFS_NAME = "Directions";
    static boolean understandReadDirections;
    static boolean understandImageDirections;
    static String currentLevelName;
    ViewFlipper viewFlipperQuizzer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        // Set the content of the activity to use the activity_main.xml layout file
        setContentView(R.layout.quiz_activity);

        viewFlipperQuizzer = (ViewFlipper) findViewById(R.id.view_flipper_quizzer);

        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {

                    getSupportActionBar().show();
                    viewFlipperQuizzer.setDisplayedChild(0);
                    continueSetUpOfQuizzerActivity();

                } else {

                    viewFlipperQuizzer.setDisplayedChild(1);
                    getSupportActionBar().hide();

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }

    public void continueSetUpOfQuizzerActivity() {

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        understandReadDirections = settings.getBoolean("understandRead", false);
        understandImageDirections = settings.getBoolean("understandImage", false);

        graduated = false;

        questionCount = 0;
        questionOrAnswer = false;
        correctOrIncorrect = false;

        if ((this).isFinishing()) {
            return;
        }

        quizzerProgressDialog = new ProgressDialog(Quizzer.this);
        quizzerProgressDialog.setMessage("Preparing questions...");
        quizzerProgressDialog.show();

        questionArrayList.clear();

        textStringArray = new String[10];
        textViewArray = new TextView[10];

        StorageReference storageRef = storage.getReferenceFromUrl("gs://thyroid-pathology.appspot.com");

        stringQuestionHashMap.clear();

        subjectString = getIntent().getExtras().getString("topicString");
        currentLevelName = getIntent().getExtras().getString("levelString");
        levelString = currentLevelName.replaceAll("\\s+", "");
        photoCreditStringQuizzer = getIntent().getExtras().getString("photoCredString");

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mQuestionDatabase = mDatabase.child("Questions").child(subjectString).child(levelString)
                .child(HomeActivity.currentUser.username);
        mUsersDatabase = mDatabase.child("users").child(HomeActivity.currentUser.username)
                .child("totalScore");

        String levelStringTxt = levelString + ".txt";
        String levelStringSvg = levelString + ".png";

        subjectRef = storageRef.child(subjectString);
        levelRef = subjectRef.child(levelStringTxt);
        imageRef = subjectRef.child(levelStringSvg);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.drawable.ic_action_true_white_logo);
        actionBar.setDisplayUseLogoEnabled(true);

        SpannableString title = new SpannableString("Thyroid Droid");

        title.setSpan(
                new TypefaceSpan("sans-serif-light"),
                0,
                title.length(),
                Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        actionBar.setTitle(title);
        // Find the view pager that will allow the user to swipe between fragments
        viewPager = (ViewPager) findViewById(R.id.viewpager2);

        // Create an adapter that knows which fragment should be shown on each page
        LearnFragmentPagerAdapter adapter = new LearnFragmentPagerAdapter(getSupportFragmentManager());

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        viewPager.setCurrentItem(0);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                if (position == 1 || position == 0) {

                    hideKeyboard(Quizzer.this);

                }
            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs2);
        tabLayout.setupWithViewPager(viewPager);


        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {

                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                ImageFragment.image = (ImageView) findViewById(R.id.image);
                ImageFragment.photoCreditTV = (TextView) findViewById(R.id.photo_courtesy_text_view);
                ImageFragment.photoCreditTV.setText("Photo courtesy of " + photoCreditStringQuizzer + ".");

                ImageFragment.image.setImageBitmap(Bitmap.createScaledBitmap(bmp, ImageFragment.image.getWidth(),
                        ImageFragment.image.getHeight(), false));
                ImageFragment.image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

                retrieveAndDistributeText();

            }
        });

    }

    public void retrieveAndDistributeText() {

        levelRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {

            @Override
            public void onSuccess(byte[] bytes) {

                try {
                    String textFileOfLesson = new String(bytes, "UTF-8");


                    String[] getCaption = textFileOfLesson.split("/>END_CAPTION");
                    String caption = getCaption[0].substring(9);

                    String textFileToRemoveCaption = textFileOfLesson;
                    String find = "/>END_CAPTION";

                    String updatedTextFile = textFileToRemoveCaption.substring(textFileToRemoveCaption.indexOf(find) + find.length());

                    String[] parts = updatedTextFile.split("END_MINI_LESSON");

                    int count = 0;

                    for (String miniLesson : parts) {

                        Question currentQuestionObject;

                        String currentQuestionFirstAltAns = null;
                        String currentQuestionSecondAltAns = null;
                        String currentQuestionThirdAltAns = null;

                        String currentTextStringNoNewLines = miniLesson.substring(miniLesson.indexOf("TEXT<") + 5,
                                miniLesson.indexOf("./>") + 1);
                        String currentTextString = currentTextStringNoNewLines.replaceAll("\\\\n", System.getProperty("line.separator"));
                        String currentQuestionString = miniLesson.substring(miniLesson.indexOf("QUESTION<")
                                + 9, miniLesson.indexOf("?") + 1);
                        String currentQuestionMC = miniLesson.substring(miniLesson.indexOf("MULTIPLE_CHOICE<")
                                + 16, miniLesson.indexOf("MULTIPLE_CHOICE<") + 17);
                        String currentQuestionGS = miniLesson.substring(miniLesson.indexOf("GRADE_SELF<")
                                + 11, miniLesson.indexOf("GRADE_SELF<") + 12);
                        String currentQuestionFIB = miniLesson.substring(miniLesson.indexOf("FILL_IN_BLANK<")
                                + 14, miniLesson.indexOf("FILL_IN_BLANK<") + 15);

                        String currentQuestionCorrectAns = getCorrectAns(miniLesson);
                        int numberOfAltAns = checkIfAltCorAnsExists(miniLesson);

                        if (numberOfAltAns == 1) {
                            currentQuestionFirstAltAns = getFirstAltAns(miniLesson);
                        }

                        if (numberOfAltAns == 2) {
                            currentQuestionSecondAltAns = getSecondAltAns(miniLesson);
                        }
                        if (numberOfAltAns == 3) {
                            currentQuestionThirdAltAns = getThirdAltAns(miniLesson);
                        }

                        textStringArray[count] = "    " + currentTextString;


                        if (currentQuestionMC.equals("y")) {

                            String firstIncAns = getFirstIncAns(miniLesson);

                            String secondIncAns = getSecondIncAns(miniLesson);

                            String thirdIncAns = getThirdIncAns(miniLesson);

                            if (currentQuestionFIB.equals("y") && currentQuestionGS.equals("y")) {

                                if (numberOfAltAns < 1) {
                                    currentQuestionObject = new Question(subjectString, levelString,
                                            count, currentQuestionString, currentQuestionCorrectAns, firstIncAns,
                                            secondIncAns, thirdIncAns, true, true, true);
                                } else if (numberOfAltAns == 1) {
                                    currentQuestionFirstAltAns = getFirstAltAns(miniLesson);
                                    currentQuestionObject = new Question(subjectString, levelString,
                                            count, currentQuestionString, currentQuestionCorrectAns, currentQuestionFirstAltAns,
                                            firstIncAns, secondIncAns, thirdIncAns, true, true, true);
                                } else if (numberOfAltAns == 2) {
                                    currentQuestionFirstAltAns = getFirstAltAns(miniLesson);
                                    currentQuestionSecondAltAns = getSecondAltAns(miniLesson);
                                    currentQuestionObject = new Question(subjectString, levelString,
                                            count, currentQuestionString, currentQuestionCorrectAns, currentQuestionFirstAltAns,
                                            currentQuestionSecondAltAns, firstIncAns, secondIncAns, thirdIncAns, true, true, true);
                                } else {
                                    currentQuestionFirstAltAns = getFirstAltAns(miniLesson);
                                    currentQuestionSecondAltAns = getSecondAltAns(miniLesson);
                                    currentQuestionThirdAltAns = getThirdAltAns(miniLesson);
                                    currentQuestionObject = new Question(subjectString, levelString,
                                            count, currentQuestionString, currentQuestionCorrectAns, currentQuestionFirstAltAns,
                                            currentQuestionSecondAltAns, firstIncAns, secondIncAns, thirdIncAns, true, true, currentQuestionThirdAltAns, true);
                                }

                            } else if (currentQuestionFIB.equals("y") && currentQuestionGS.equals("n")) {

                                if (numberOfAltAns < 1) {
                                    currentQuestionObject = new Question(subjectString, levelString,
                                            count, currentQuestionString, currentQuestionCorrectAns, firstIncAns,
                                            secondIncAns, thirdIncAns, true, true, false);
                                } else if (numberOfAltAns == 1) {
                                    currentQuestionFirstAltAns = getFirstAltAns(miniLesson);
                                    currentQuestionObject = new Question(subjectString, levelString,
                                            count, currentQuestionString, currentQuestionCorrectAns, currentQuestionFirstAltAns,
                                            firstIncAns, secondIncAns, thirdIncAns, true, true, false);
                                } else if (numberOfAltAns == 2) {
                                    currentQuestionFirstAltAns = getFirstAltAns(miniLesson);
                                    currentQuestionSecondAltAns = getSecondAltAns(miniLesson);
                                    currentQuestionObject = new Question(subjectString, levelString,
                                            count, currentQuestionString, currentQuestionCorrectAns, currentQuestionFirstAltAns,
                                            currentQuestionSecondAltAns, firstIncAns, secondIncAns, thirdIncAns, true, true, false);
                                } else {
                                    currentQuestionFirstAltAns = getFirstAltAns(miniLesson);
                                    currentQuestionSecondAltAns = getSecondAltAns(miniLesson);
                                    currentQuestionThirdAltAns = getThirdAltAns(miniLesson);
                                    currentQuestionObject = new Question(subjectString, levelString,
                                            count, currentQuestionString, currentQuestionCorrectAns, currentQuestionFirstAltAns,
                                            currentQuestionSecondAltAns, firstIncAns, secondIncAns, thirdIncAns, true, true, currentQuestionThirdAltAns, false);
                                }

                            } else if (currentQuestionFIB.equals("n") && currentQuestionGS.equals("y")) {

                                if (numberOfAltAns < 1) {
                                    currentQuestionObject = new Question(subjectString, levelString,
                                            count, currentQuestionString, currentQuestionCorrectAns, firstIncAns,
                                            secondIncAns, thirdIncAns, false, true, true);
                                } else if (numberOfAltAns == 1) {
                                    currentQuestionFirstAltAns = getFirstAltAns(miniLesson);
                                    currentQuestionObject = new Question(subjectString, levelString,
                                            count, currentQuestionString, currentQuestionCorrectAns, currentQuestionFirstAltAns,
                                            firstIncAns, secondIncAns, thirdIncAns, false, true, true);
                                } else if (numberOfAltAns == 2) {
                                    currentQuestionFirstAltAns = getFirstAltAns(miniLesson);
                                    currentQuestionSecondAltAns = getSecondAltAns(miniLesson);
                                    currentQuestionObject = new Question(subjectString, levelString,
                                            count, currentQuestionString, currentQuestionCorrectAns, currentQuestionFirstAltAns,
                                            currentQuestionSecondAltAns, firstIncAns, secondIncAns, thirdIncAns, false, true, true);
                                } else {
                                    currentQuestionFirstAltAns = getFirstAltAns(miniLesson);
                                    currentQuestionSecondAltAns = getSecondAltAns(miniLesson);
                                    currentQuestionThirdAltAns = getThirdAltAns(miniLesson);
                                    currentQuestionObject = new Question(subjectString, levelString,
                                            count, currentQuestionString, currentQuestionCorrectAns, currentQuestionFirstAltAns,
                                            currentQuestionSecondAltAns, firstIncAns, secondIncAns, thirdIncAns, false, true, currentQuestionThirdAltAns, true);

                                }

                            } else {

                                if (numberOfAltAns < 1) {
                                    currentQuestionObject = new Question(subjectString, levelString,
                                            count, currentQuestionString, currentQuestionCorrectAns, firstIncAns,
                                            secondIncAns, thirdIncAns, false, true, false);
                                } else if (numberOfAltAns == 1) {
                                    currentQuestionFirstAltAns = getFirstAltAns(miniLesson);
                                    currentQuestionObject = new Question(subjectString, levelString,
                                            count, currentQuestionString, currentQuestionCorrectAns, currentQuestionFirstAltAns,
                                            firstIncAns, secondIncAns, thirdIncAns, false, true, false);
                                } else if (numberOfAltAns == 2) {
                                    currentQuestionFirstAltAns = getFirstAltAns(miniLesson);
                                    currentQuestionSecondAltAns = getSecondAltAns(miniLesson);
                                    currentQuestionObject = new Question(subjectString, levelString,
                                            count, currentQuestionString, currentQuestionCorrectAns, currentQuestionFirstAltAns,
                                            currentQuestionSecondAltAns, firstIncAns, secondIncAns, thirdIncAns, false, true, false);
                                } else {
                                    currentQuestionFirstAltAns = getFirstAltAns(miniLesson);
                                    currentQuestionSecondAltAns = getSecondAltAns(miniLesson);
                                    currentQuestionThirdAltAns = getThirdAltAns(miniLesson);
                                    currentQuestionObject = new Question(subjectString, levelString,
                                            count, currentQuestionString, currentQuestionCorrectAns, currentQuestionFirstAltAns,
                                            currentQuestionSecondAltAns, firstIncAns, secondIncAns, thirdIncAns, false, true, currentQuestionThirdAltAns, false);
                                }
                            }

                            questionArrayList.add(currentQuestionObject);

                        } else {

                            if (currentQuestionFIB.equals("y") && currentQuestionGS.equals("y")) {

                                if (numberOfAltAns < 1) {
                                    currentQuestionObject = new Question(subjectString, levelString,
                                            count, currentQuestionString, currentQuestionCorrectAns, true, false, true);
                                } else if (numberOfAltAns == 1) {
                                    currentQuestionFirstAltAns = getFirstAltAns(miniLesson);
                                    currentQuestionObject = new Question(subjectString, levelString,
                                            count, currentQuestionString, currentQuestionCorrectAns, currentQuestionFirstAltAns,
                                            true, false, true);
                                } else if (numberOfAltAns == 2) {
                                    currentQuestionFirstAltAns = getFirstAltAns(miniLesson);
                                    currentQuestionSecondAltAns = getSecondAltAns(miniLesson);
                                    currentQuestionObject = new Question(subjectString, levelString,
                                            count, currentQuestionString, currentQuestionCorrectAns, currentQuestionFirstAltAns,
                                            currentQuestionSecondAltAns, true, false, true);
                                } else {
                                    currentQuestionFirstAltAns = getFirstAltAns(miniLesson);
                                    currentQuestionSecondAltAns = getSecondAltAns(miniLesson);
                                    currentQuestionThirdAltAns = getThirdAltAns(miniLesson);
                                    currentQuestionObject = new Question(subjectString, levelString,
                                            count, currentQuestionString, currentQuestionCorrectAns, currentQuestionFirstAltAns,
                                            currentQuestionSecondAltAns, true, false, currentQuestionThirdAltAns, true);
                                }

                            } else if (currentQuestionFIB.equals("y") && currentQuestionGS.equals("n")) {

                                if (numberOfAltAns < 1) {
                                    currentQuestionObject = new Question(subjectString, levelString,
                                            count, currentQuestionString, currentQuestionCorrectAns, true, false, false);
                                } else if (numberOfAltAns == 1) {
                                    currentQuestionFirstAltAns = getFirstAltAns(miniLesson);
                                    currentQuestionObject = new Question(subjectString, levelString,
                                            count, currentQuestionString, currentQuestionCorrectAns, currentQuestionFirstAltAns,
                                            true, false, false);
                                } else if (numberOfAltAns == 2) {
                                    currentQuestionFirstAltAns = getFirstAltAns(miniLesson);
                                    currentQuestionSecondAltAns = getSecondAltAns(miniLesson);
                                    currentQuestionObject = new Question(subjectString, levelString,
                                            count, currentQuestionString, currentQuestionCorrectAns, currentQuestionFirstAltAns,
                                            currentQuestionSecondAltAns, true, false, false);
                                } else {
                                    currentQuestionFirstAltAns = getFirstAltAns(miniLesson);
                                    currentQuestionSecondAltAns = getSecondAltAns(miniLesson);
                                    currentQuestionThirdAltAns = getThirdAltAns(miniLesson);
                                    currentQuestionObject = new Question(subjectString, levelString,
                                            count, currentQuestionString, currentQuestionCorrectAns, currentQuestionFirstAltAns,
                                            currentQuestionSecondAltAns, true, false, currentQuestionThirdAltAns, false);
                                }

                            } else if (currentQuestionFIB.equals("n") && currentQuestionGS.equals("y")) {

                                if (numberOfAltAns < 1) {
                                    currentQuestionObject = new Question(subjectString, levelString,
                                            count, currentQuestionString, currentQuestionCorrectAns, false, false, true);
                                } else if (numberOfAltAns == 1) {
                                    currentQuestionFirstAltAns = getFirstAltAns(miniLesson);
                                    currentQuestionObject = new Question(subjectString, levelString,
                                            count, currentQuestionString, currentQuestionCorrectAns, currentQuestionFirstAltAns,
                                            false, false, true);
                                } else if (numberOfAltAns == 2) {
                                    currentQuestionFirstAltAns = getFirstAltAns(miniLesson);
                                    currentQuestionSecondAltAns = getSecondAltAns(miniLesson);
                                    currentQuestionObject = new Question(subjectString, levelString,
                                            count, currentQuestionString, currentQuestionCorrectAns, currentQuestionFirstAltAns,
                                            currentQuestionSecondAltAns, false, false, true);
                                } else {
                                    currentQuestionFirstAltAns = getFirstAltAns(miniLesson);
                                    currentQuestionSecondAltAns = getSecondAltAns(miniLesson);
                                    currentQuestionThirdAltAns = getThirdAltAns(miniLesson);
                                    currentQuestionObject = new Question(subjectString, levelString,
                                            count, currentQuestionString, currentQuestionCorrectAns, currentQuestionFirstAltAns,
                                            currentQuestionSecondAltAns, false, false, currentQuestionThirdAltAns, true);
                                }

                            } else {

                                if (numberOfAltAns < 1) {
                                    currentQuestionObject = new Question(subjectString, levelString,
                                            count, currentQuestionString, currentQuestionCorrectAns, false, false, false);
                                } else if (numberOfAltAns == 1) {
                                    currentQuestionFirstAltAns = getFirstAltAns(miniLesson);
                                    currentQuestionObject = new Question(subjectString, levelString,
                                            count, currentQuestionString, currentQuestionCorrectAns, currentQuestionFirstAltAns, false, false, false);
                                } else if (numberOfAltAns == 2) {
                                    currentQuestionFirstAltAns = getFirstAltAns(miniLesson);
                                    currentQuestionSecondAltAns = getSecondAltAns(miniLesson);
                                    currentQuestionObject = new Question(subjectString, levelString,
                                            count, currentQuestionString, currentQuestionCorrectAns, currentQuestionFirstAltAns,
                                            currentQuestionSecondAltAns, false, false, false);
                                } else {

                                    currentQuestionFirstAltAns = getFirstAltAns(miniLesson);
                                    currentQuestionSecondAltAns = getSecondAltAns(miniLesson);
                                    currentQuestionThirdAltAns = getThirdAltAns(miniLesson);
                                    currentQuestionObject = new Question(subjectString, levelString,
                                            count, currentQuestionString, currentQuestionCorrectAns, currentQuestionFirstAltAns,
                                            currentQuestionSecondAltAns, false, false, currentQuestionThirdAltAns, false);

                                }

                            }

                            questionArrayList.add(currentQuestionObject);

                        }

                        count++;

                    }


                    if (textStringArray[0] != null) {

                        ReadFragment.readTextView1.setText(Html.fromHtml(textStringArray[0]));

                    }
                    if (textStringArray[1] != null) {

                        ReadFragment.readTextView2.setText(Html.fromHtml(textStringArray[1]));

                    }
                    if (textStringArray[2] != null) {

                        ReadFragment.readTextView3.setText(Html.fromHtml(textStringArray[2]));

                    }
                    if (textStringArray[3] != null) {

                        ReadFragment.readTextView4.setText(Html.fromHtml(textStringArray[3]));

                    }
                    if (textStringArray[4] != null) {

                        ReadFragment.readTextView5.setText(Html.fromHtml(textStringArray[4]));

                    }
                    if (textStringArray[5] != null) {

                        ReadFragment.readTextView6.setText(Html.fromHtml(textStringArray[5]));

                    }
                    if (textStringArray[6] != null) {

                        ReadFragment.readTextView7.setText(Html.fromHtml(textStringArray[6]));

                    }
                    if (textStringArray[7] != null) {

                        ReadFragment.readTextView8.setText(Html.fromHtml(textStringArray[7]));

                    }
                    if (textStringArray[8] != null) {

                        ReadFragment.readTextView9.setText(Html.fromHtml(textStringArray[8]));

                    }
                    if (textStringArray[9] != null) {

                        ReadFragment.readTextView10.setText(Html.fromHtml(textStringArray[9]));

                    }

                    ImageFragment.photoCaptionTV.setText(caption);

                    textViewArray[0] = ReadFragment.readTextView1;
                    textViewArray[1] = ReadFragment.readTextView2;
                    textViewArray[2] = ReadFragment.readTextView3;
                    textViewArray[3] = ReadFragment.readTextView4;
                    textViewArray[4] = ReadFragment.readTextView5;
                    textViewArray[5] = ReadFragment.readTextView6;
                    textViewArray[6] = ReadFragment.readTextView7;
                    textViewArray[7] = ReadFragment.readTextView8;
                    textViewArray[8] = ReadFragment.readTextView9;
                    textViewArray[9] = ReadFragment.readTextView10;

                    for (TextView currentTextView : textViewArray) {

                        if (currentTextView.getText().length() > 1) {

                            SpannedString currentString = (SpannedString) currentTextView.getText();

                            if (currentString == null || currentString.length() < 2) {
                                currentTextView.setVisibility(GONE);
                            } else {
                                currentTextView.setVisibility(View.VISIBLE);
                            }

                        } else {
                            currentTextView.setVisibility(GONE);
                        }

                    }


                    addDatabaseListenerWhenComplete();

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

    }


    public void addDatabaseListenerWhenComplete() {

        mQuestionDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (!dataSnapshot.exists()) {

                    for (int i = 0; i < questionArrayList.size(); i++) {

                        String iString = Integer.toString(i);

                        mQuestionDatabase.child("QuestionIDs").child(iString).child("Correct").setValue(0);
                        mQuestionDatabase.child("QuestionIDs").child(iString).child("Incorrect").setValue(0);
                        mQuestionDatabase.child("Graduated").setValue(false);

                    }
                }
                quizzerProgressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {

        super.onStart();

    }


    public String getCorrectAns(String miniLessonInput) {

        String[] parts = miniLessonInput.split("COR_ANSWER<");
        String correctAnsString = parts[1].substring(parts[1].indexOf("COR_ANSWER<") + 1,
                parts[1].indexOf("/>"));


        return correctAnsString;

    }


    //if none exist, return 0; if 1 exists, return 1; if two exist, return 2
    public int checkIfAltCorAnsExists(String miniLessonInput) {

        int numberOfAltAns = 0;
        String[] parts = miniLessonInput.split("ALT_COR_ANSWER2<");
        String altCorAns1String = parts[0].substring(parts[0].indexOf("ALT_COR_ANSWER1<") + 16,
                parts[0].lastIndexOf("/>"));
        String altCorAns2String = parts[1].substring(parts[1].indexOf("ALT_COR_ANSWER2<") + 1,
                parts[1].indexOf("/>"));
        String altCorAns3String = null;

        if (parts[1].contains("ALT_COR_ANSWER3<")) {

            altCorAns3String = parts[1].substring(parts[1].indexOf("ALT_COR_ANSWER3<") + 16,
                    parts[1].indexOf("INC_ANS") - 4);


        }

        if (altCorAns1String != null && altCorAns1String.length() > 0) {
            numberOfAltAns++;
        }
        if (altCorAns2String != null && altCorAns2String.length() > 0) {
            numberOfAltAns++;
        }
        if (altCorAns3String != null && altCorAns3String.length() > 0) {
            numberOfAltAns++;
        }

        return numberOfAltAns;
    }

    public String getFirstAltAns(String miniLessonInput) {
        String[] parts = miniLessonInput.split("ALT_COR_ANSWER2<");
        String altCorAns1String = parts[0].substring(parts[0].indexOf("ALT_COR_ANSWER1<") + 16,
                parts[0].lastIndexOf("/>"));

        return altCorAns1String;
    }

    public String getSecondAltAns(String miniLessonInput) {
        String[] parts = miniLessonInput.split("ALT_COR_ANSWER2<");
        String altCorAns2String = parts[1].substring(parts[1].indexOf("ALT_COR_ANSWER2<") + 1,
                parts[1].indexOf("/>"));
        return altCorAns2String;
    }

    public String getThirdAltAns(String miniLessonInput) {
        String[] parts = miniLessonInput.split("ALT_COR_ANSWER2<");
        String altCorAns3String = parts[1].substring(parts[1].indexOf("ALT_COR_ANSWER3<") + 16,
                parts[1].indexOf("INC_ANS") - 4);
        return altCorAns3String;
    }

    public String getFirstIncAns(String miniLessonInput) {
        String[] parts = miniLessonInput.split("INC_ANSWER2<");
        String incAns1String = parts[0].substring(parts[0].indexOf("INC_ANSWER1<") + 12,
                parts[0].lastIndexOf("/>"));
        return incAns1String;
    }

    public String getSecondIncAns(String miniLessonInput) {
        String[] parts = miniLessonInput.split("INC_ANSWER2<");
        String incCorAns2String = parts[1].substring(parts[1].indexOf("INC_ANSWER2<") + 1,
                parts[1].indexOf("/>"));
        return incCorAns2String;
    }

    public String getThirdIncAns(String miniLessonInput) {
        String[] parts = miniLessonInput.split("INC_ANSWER3<");
        String incCorAns3String = parts[1].substring(parts[1].indexOf("INC_ANSWER3<") + 1,
                parts[1].indexOf("/>"));
        return incCorAns3String;
    }


    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("understandRead", understandReadDirections);
        editor.putBoolean("understandImage", understandImageDirections);

        // Commit the edits!
        editor.commit();
    }

}