package com.unmcelearning.android.thyroidpathology;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TypefaceSpan;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
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

import static android.view.View.VISIBLE;
import static com.unmcelearning.android.thyroidpathology.HomeActivity.currentUser;
import static com.unmcelearning.android.thyroidpathology.HomeActivity.currentUser;
import static com.unmcelearning.android.thyroidpathology.Quizzer.currentScore;

public class CaseActivity extends AppCompatActivity {

    String caseNameId;
    boolean caseCompleted;
    ProgressDialog progressDialog;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private DatabaseReference mDatabase;
    private DatabaseReference mCaseDatabase;
    private DatabaseReference mUsersCaseDatabase;
    StorageReference caseRef;
    TextView caseIdTextView;
    TextView caseTextTextView;
    TextView caseQuestionTextView;
    TextView currentScoreTextView;
    TextView plusThreeTextView;
    RadioButton radioButton1;
    RadioButton radioButton2;
    RadioButton radioButton3;
    RadioButton radioButton4;
    RadioGroup radioGroupCase;
    ImageView caseImageView;
    Button enterButton;
    Button pickNewCaseButton;
    TextView correctCaseAnsTextView;
    Animation animation;
    TextView casePhotoCredTextView;

    String caseCorrectAns;
    String firstIncAns;
    String secondIncAns;
    String thirdIncAns;
    StorageReference caseImageRef;
    ViewFlipper viewFlipperCase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case);

        viewFlipperCase = (ViewFlipper) findViewById(R.id.view_flipper_case);

        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {

                    getSupportActionBar().show();
                    viewFlipperCase.setDisplayedChild(0);
                    continueSetUpOfCaseActivity();

                } else {

                    viewFlipperCase.setDisplayedChild(1);
                    getSupportActionBar().hide();

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }

    public void continueSetUpOfCaseActivity() {

        caseIdTextView = (TextView) findViewById(R.id.case_id);
        casePhotoCredTextView = (TextView) findViewById(R.id.case_photo_cred);
        casePhotoCredTextView.setVisibility(View.INVISIBLE);
        pickNewCaseButton = (Button) findViewById(R.id.pick_new_case_button);
        pickNewCaseButton.setVisibility(View.GONE);
        caseIdTextView.setVisibility(View.INVISIBLE);
        correctCaseAnsTextView = (TextView) findViewById(R.id.case_correct_ans_text_view);
        correctCaseAnsTextView.setVisibility(View.GONE);
        caseTextTextView = (TextView) findViewById(R.id.case_text);
        caseTextTextView.setVisibility(View.INVISIBLE);
        caseQuestionTextView = (TextView) findViewById(R.id.case_question);
        caseQuestionTextView.setVisibility(View.INVISIBLE);
        currentScoreTextView = (TextView) findViewById(R.id.score_display_case);
        currentScoreTextView.setVisibility(View.INVISIBLE);
        plusThreeTextView = (TextView) findViewById(R.id.plus_three);
        plusThreeTextView.setVisibility(View.INVISIBLE);
        caseImageView = (ImageView) findViewById(R.id.case_image);
        caseImageView.setVisibility(View.INVISIBLE);
        radioButton1 = (RadioButton) findViewById(R.id.radio_button1_case);
        radioButton2 = (RadioButton) findViewById(R.id.radio_button2_case);
        radioButton3 = (RadioButton) findViewById(R.id.radio_button3_case);
        radioButton4 = (RadioButton) findViewById(R.id.radio_button4_case);
        enterButton = (Button) findViewById(R.id.enter_button_radio_group_case);
        radioGroupCase = (RadioGroup) findViewById(R.id.radio_group_case);
        radioGroupCase.setVisibility(View.INVISIBLE);
        enterButton.setVisibility(View.INVISIBLE);

        animation = AnimationUtils.loadAnimation(this,
                R.anim.clockwise);

        setTheme(R.style.AppTheme);

        if ((this).isFinishing()) {
            return;
        }

        progressDialog = new ProgressDialog(CaseActivity.this);
        progressDialog.setMessage("Preparing case...");
        progressDialog.show();

        StorageReference storageRef = storage.getReferenceFromUrl("gs://thyroid-pathology.appspot.com");

        caseNameId = getIntent().getExtras().getString("caseString");
        String caseNameOnlyFalseId = caseNameId.replaceAll("Case ", "");
        int idInt = Integer.parseInt(caseNameOnlyFalseId);
        idInt--;
        String caseNameOnlyTrueId = String.valueOf(idInt);
        caseIdTextView.setText(caseNameId);
        caseCompleted = getIntent().getExtras().getBoolean("caseCompleted");
        String caseNameNoSpace = caseNameId.replaceAll("\\s+", "");

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mCaseDatabase = mDatabase.child("Cases").child(currentUser.getUsername()).child(caseNameOnlyTrueId)
                .child("completed");
        mUsersCaseDatabase = mDatabase.child("users").child(currentUser.username)
                .child("totalScore");

        String caseStringTxt = caseNameNoSpace + ".txt";
        String caseImagePng = caseNameNoSpace + ".png";

        caseRef = storageRef.child("Cases").child(caseStringTxt);
        caseImageRef = storageRef.child("Cases").child(caseImagePng);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.drawable.ic_action_true_white_logo);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        SpannableString title = new SpannableString("Thyroid Droid");

        title.setSpan(
                new TypefaceSpan("sans-serif-light"),
                0,
                title.length(),
                Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        actionBar.setTitle(title);
        mUsersCaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                currentScore = dataSnapshot.getValue(Long.class);
                String currentScoreString = String.valueOf(currentScore);
                currentScoreTextView.setText("Total Score: " + currentScoreString);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        downloadAndDistributeText();

    }

    public void downloadAndShowImage() {

        long ONE_MEGABYTE = 1024 * 1024;

        caseImageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {

                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                caseImageView.setImageBitmap(Bitmap.createScaledBitmap(bmp, caseImageView.getWidth(),
                        caseImageView.getHeight(), false));

                RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);

                p.addRule(RelativeLayout.BELOW, R.id.case_photo_cred);
                p.setMargins(30, 20, 30, 0);

                caseQuestionTextView.setLayoutParams(p);

                casePhotoCredTextView.setVisibility(VISIBLE);

                caseImageView.setVisibility(VISIBLE);
                caseIdTextView.setVisibility(VISIBLE);
                caseTextTextView.setVisibility(VISIBLE);
                caseQuestionTextView.setVisibility(VISIBLE);
                radioGroupCase.setVisibility(VISIBLE);
                enterButton.setVisibility(VISIBLE);
                currentScoreTextView.setVisibility(VISIBLE);

                progressDialog.dismiss();

            }
        });
    }

    public void downloadAndDistributeText() {

        long ONE_MEGABYTE = 1024 * 1024;

        caseRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {

            @Override
            public void onSuccess(byte[] bytes) {

                try {
                    String fullCase = new String(bytes, "UTF-8");

                    String caseTextString = fullCase.substring(fullCase.indexOf("TEXT<") + 5,
                            fullCase.indexOf("./>") + 1);
                    String caseQuestionString = fullCase.substring(fullCase.indexOf("QUESTION<")
                            + 9, fullCase.indexOf("?") + 1);
                    String hasImageString = fullCase.substring(fullCase.indexOf("HAS_IMAGE<") + 10,
                            fullCase.indexOf("/>HAS"));
                    String casePhotoCredString = fullCase.substring(fullCase.indexOf("PHOTO_CREDIT<") + 13,
                            fullCase.indexOf("/>PHOTO"));

                    caseCorrectAns = getCorrectAns(fullCase);

                    firstIncAns = getFirstIncAns(fullCase);

                    secondIncAns = getSecondIncAns(fullCase);

                    thirdIncAns = getThirdIncAns(fullCase);

                    caseTextTextView.setText(caseTextString);
                    caseQuestionTextView.setText(caseQuestionString);
                    casePhotoCredTextView.setText("Image courtesy of " + casePhotoCredString + ".");

                    enterButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (radioGroupCase.getCheckedRadioButtonId() == -1) {
                                // no radio buttons are checked
                                Toast.makeText(CaseActivity.this, "Oops! No answer is selected.", Toast.LENGTH_SHORT).show();
                            } else {
                                // one of the radio buttons is checked
                                int radioButtonID = radioGroupCase.getCheckedRadioButtonId();
                                RadioButton radioButton = (RadioButton) radioGroupCase.findViewById(radioButtonID);
                                String proposedAns = (String) radioButton.getText();
                                checkCaseAns(proposedAns);

                            }

                        }
                    });

                    randomizeButtonCaseAnswerOrder(Quizzer.randomObject.nextInt(4));


                    if (hasImageString.equals("y")) {

                        downloadAndShowImage();

                    } else {

                        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);

                        p.addRule(RelativeLayout.BELOW, R.id.case_text);
                        p.setMargins(30, 20, 30, 0);

                        caseQuestionTextView.setLayoutParams(p);

                        caseImageView.setVisibility(View.GONE);
                        caseIdTextView.setVisibility(VISIBLE);
                        caseTextTextView.setVisibility(VISIBLE);
                        caseQuestionTextView.setVisibility(VISIBLE);
                        radioGroupCase.setVisibility(VISIBLE);
                        enterButton.setVisibility(VISIBLE);
                        currentScoreTextView.setVisibility(VISIBLE);

                        progressDialog.dismiss();

                    }


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

    public String getCorrectAns(String caseInput) {

        String[] parts = caseInput.split("COR_ANSWER<");
        String correctAnsString = parts[1].substring(parts[1].indexOf("COR_ANSWER<") + 1,
                parts[1].indexOf("/>"));

        return correctAnsString;

    }

    public String getFirstIncAns(String fullCase) {
        String[] parts = fullCase.split("INC_ANSWER2<");
        String incAns1String = parts[0].substring(parts[0].indexOf("INC_ANSWER1<") + 12,
                parts[0].lastIndexOf("/>"));
        return incAns1String;
    }

    public String getSecondIncAns(String fullCase) {
        String[] parts = fullCase.split("INC_ANSWER2<");
        String incCorAns2String = parts[1].substring(parts[1].indexOf("INC_ANSWER2<") + 1,
                parts[1].indexOf("/>"));
        return incCorAns2String;
    }

    public String getThirdIncAns(String fullCase) {
        String[] parts = fullCase.split("INC_ANSWER3<");
        String incCorAns3String = parts[1].substring(parts[1].indexOf("INC_ANSWER3<") + 1,
                parts[1].indexOf("/>"));
        return incCorAns3String;
    }

    public void randomizeButtonCaseAnswerOrder(int randomCase) {

        switch (randomCase) {
            case 0:
                radioButton1.setText(caseCorrectAns);
                radioButton2.setText(firstIncAns);
                radioButton3.setText(secondIncAns);
                radioButton4.setText(thirdIncAns);
                break;
            case 1:
                radioButton2.setText(caseCorrectAns);
                radioButton1.setText(firstIncAns);
                radioButton3.setText(secondIncAns);
                radioButton4.setText(thirdIncAns);
                break;
            case 2:
                radioButton3.setText(caseCorrectAns);
                radioButton2.setText(firstIncAns);
                radioButton1.setText(secondIncAns);
                radioButton4.setText(thirdIncAns);
                break;
            case 3:
                radioButton4.setText(caseCorrectAns);
                radioButton3.setText(firstIncAns);
                radioButton2.setText(secondIncAns);
                radioButton1.setText(thirdIncAns);
                break;
        }
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

    public void checkCaseAns(String proposedAns) {

        if (caseCorrectAns.equals(proposedAns)) {

            if (!caseCompleted) {

                Quizzer.currentScore = addThreeToTotalCurrentScore(Quizzer.currentScore);
                clockwise();
                mUsersCaseDatabase.setValue(currentScore);
                mCaseDatabase.setValue(true);

            }

            correctCaseAnsDisplay();


        } else {

            Toast.makeText(CaseActivity.this, "Try again.", Toast.LENGTH_LONG).show();

        }

    }

    public long addThreeToTotalCurrentScore(long totalScore) {
        totalScore = totalScore + 3;
        return totalScore;
    }

    public void correctCaseAnsDisplay() {

        enterButton.setVisibility(View.GONE);
        radioGroupCase.setVisibility(View.GONE);
        correctCaseAnsTextView.setVisibility(VISIBLE);
        pickNewCaseButton.setVisibility(View.VISIBLE);
        correctCaseAnsTextView.setText("Correct!" + "\n" + "The answer was:" + "\n" + caseCorrectAns);

        pickNewCaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NavUtils.navigateUpFromSameTask(CaseActivity.this);

            }
        });

    }

    public void clockwise() {


        plusThreeTextView.setVisibility(VISIBLE);
        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                plusThreeTextView.setVisibility(View.INVISIBLE);

            }
        });
        plusThreeTextView.startAnimation(animation);
    }
}
