package com.unmcelearning.android.thyroidpathology;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.text.SpannedString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

import static com.unmcelearning.android.thyroidpathology.Quizzer.ansExplanationTextView;
import static com.unmcelearning.android.thyroidpathology.Quizzer.answerEditText;
import static com.unmcelearning.android.thyroidpathology.Quizzer.continueLessonButton;
import static com.unmcelearning.android.thyroidpathology.Quizzer.correctAnsTextView;
import static com.unmcelearning.android.thyroidpathology.Quizzer.currentQuestion;
import static com.unmcelearning.android.thyroidpathology.Quizzer.currentQuestionTimesAnsCorrect;
import static com.unmcelearning.android.thyroidpathology.Quizzer.currentQuestionTimesAnsIncorrect;
import static com.unmcelearning.android.thyroidpathology.Quizzer.currentScore;
import static com.unmcelearning.android.thyroidpathology.Quizzer.enterButton;
import static com.unmcelearning.android.thyroidpathology.Quizzer.gradeSelfCorrect;
import static com.unmcelearning.android.thyroidpathology.Quizzer.gradeSelfIncorrect;
import static com.unmcelearning.android.thyroidpathology.Quizzer.gradeSelfLinearLayout;
import static com.unmcelearning.android.thyroidpathology.Quizzer.graduated;
import static com.unmcelearning.android.thyroidpathology.Quizzer.levelString;
import static com.unmcelearning.android.thyroidpathology.Quizzer.multipleChoiceSubmitButton;
import static com.unmcelearning.android.thyroidpathology.Quizzer.questionArrayList;
import static com.unmcelearning.android.thyroidpathology.Quizzer.questionCount;
import static com.unmcelearning.android.thyroidpathology.Quizzer.questionOrAnswer;
import static com.unmcelearning.android.thyroidpathology.Quizzer.questionTextView;
import static com.unmcelearning.android.thyroidpathology.Quizzer.questionTextViewInvisible;
import static com.unmcelearning.android.thyroidpathology.Quizzer.radioGroup;
import static com.unmcelearning.android.thyroidpathology.Quizzer.scoreTextView;
import static com.unmcelearning.android.thyroidpathology.Quizzer.showAnswerButton;
import static com.unmcelearning.android.thyroidpathology.Quizzer.subjectString;
import static com.unmcelearning.android.thyroidpathology.Quizzer.textViewArray;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuizFragment extends Fragment {

    String currentQuestionIdString1;
    private DatabaseReference mDatabaseFrag;
    private DatabaseReference mQuestionDatabaseFrag;
    private DatabaseReference mUsersDatabaseFrag;
    private DatabaseReference mGraduatedDatabaseFrag;
    ScrollView scrollView;
    ProgressDialog quizFragProgressDialog;
    TextView plusOne;
    Animation animation;

    public QuizFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        quizFragProgressDialog = new ProgressDialog(getActivity());

        mDatabaseFrag = FirebaseDatabase.getInstance().getReference();

        mQuestionDatabaseFrag = mDatabaseFrag.child("Questions").child(subjectString).child(levelString)
                .child(HomeActivity.currentUser.username);

        mUsersDatabaseFrag = mDatabaseFrag.child("users").child(HomeActivity.currentUser.username)
                .child("totalScore");

        mGraduatedDatabaseFrag = mDatabaseFrag.child("Graduated").child(HomeActivity.currentUser.username)
                .child(Quizzer.subjectString).child(Quizzer.levelString);

    }


    public long addOneToTotalCurrentScore(long totalScore) {
        totalScore++;
        return totalScore;
    }

    @Override
    public void onStart() {
        super.onStart();


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_quiz, container, false);

        mUsersDatabaseFrag.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                currentScore = dataSnapshot.getValue(Long.class);
                String currentScoreString = String.valueOf(currentScore);
                scoreTextView.setText("Total Score: " + currentScoreString);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        scrollView = (ScrollView) rootView.findViewById(R.id.quiz_fragment_scrollview);

        scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = scrollView.getRootView().getHeight() - scrollView.getHeight();

                if (heightDiff > 100 && Quizzer.questionOrAnswer == false) {

                    scrollView.fullScroll(View.FOCUS_DOWN);
                }
            }

        });

        scoreTextView = (TextView) rootView.findViewById(R.id.score_display);
        questionTextViewInvisible = (TextView) rootView.findViewById(R.id.question_answer_count);
        scoreTextView.setText("score: " + " ");
        answerEditText = (EditText) rootView.findViewById(R.id.answer_edit_text);
        enterButton = (Button) rootView.findViewById(R.id.enter_button);
        radioGroup = (RadioGroup) rootView.findViewById(R.id.radio_group);
        Quizzer.radioButton1 = (RadioButton) rootView.findViewById(R.id.radio_button1);
        Quizzer.radioButton2 = (RadioButton) rootView.findViewById(R.id.radio_button2);
        Quizzer.radioButton3 = (RadioButton) rootView.findViewById(R.id.radio_button3);
        Quizzer.radioButton4 = (RadioButton) rootView.findViewById(R.id.radio_button4);
        Quizzer.gradeSelfLinearLayout = (LinearLayout) rootView.findViewById(R.id.grade_self_linear_layout);
        Quizzer.gradeSelfCorrect = (Button) rootView.findViewById(R.id.grade_self_correct_button);
        Quizzer.gradeSelfIncorrect = (Button) rootView.findViewById(R.id.grade_self_incorrect_button);
        Quizzer.multipleChoiceSubmitButton = (Button) rootView.findViewById(R.id.enter_button_radio_group);
        Quizzer.continueLessonButton = (Button) rootView.findViewById(R.id.continue_button);
        Quizzer.showAnswerButton = (Button) rootView.findViewById(R.id.show_ans_button);
        Quizzer.questionTextView = (TextView) rootView.findViewById(R.id.question_text_view);
        Quizzer.correctAnsTextView = (TextView) rootView.findViewById(R.id.correct_ans_text_view);
        Quizzer.ansExplanationTextView = (TextView) rootView.findViewById(R.id.correct_ans_explanation);
        Quizzer.radioButton1.setVisibility(View.GONE);
        Quizzer.radioButton2.setVisibility(View.GONE);
        Quizzer.radioButton3.setVisibility(View.GONE);
        Quizzer.radioButton4.setVisibility(View.GONE);
        multipleChoiceSubmitButton.setVisibility(View.GONE);
        continueLessonButton.setVisibility(View.GONE);
        showAnswerButton.setVisibility(View.GONE);
        correctAnsTextView.setVisibility(View.GONE);
        ansExplanationTextView.setVisibility(View.GONE);

        Quizzer.currentQuestion = Quizzer.questionArrayList.get(Quizzer.questionCount);

        questionOrAnsDisplay(Quizzer.questionOrAnswer, Quizzer.correctOrIncorrect);


        plusOne = (TextView) rootView.findViewById(R.id.plus_one);
        plusOne.setVisibility(View.INVISIBLE);

        animation = AnimationUtils.loadAnimation(getActivity(),
                R.anim.clockwise);

        return rootView;

    }

    public void setupGraduatedDisplay() {

        Quizzer.radioButton1.setVisibility(View.GONE);
        Quizzer.radioButton2.setVisibility(View.GONE);
        Quizzer.radioButton3.setVisibility(View.GONE);
        Quizzer.radioButton4.setVisibility(View.GONE);
        multipleChoiceSubmitButton.setVisibility(View.GONE);
        correctAnsTextView.setVisibility(View.GONE);
        ansExplanationTextView.setVisibility(View.GONE);
        showAnswerButton.setVisibility(View.GONE);
        enterButton.setVisibility(View.GONE);
        answerEditText.setVisibility(View.GONE);
        continueLessonButton.setVisibility(View.GONE);
        gradeSelfCorrect.setText("Retake Quiz");
        gradeSelfIncorrect.setText("New Lesson");

        questionTextView.setVisibility(View.VISIBLE);
        gradeSelfLinearLayout.setVisibility(View.VISIBLE);
        questionTextView.setText("Congratulations on completing this lesson!");

        gradeSelfCorrect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetStatsForQuiz();
            }
        });

        gradeSelfIncorrect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((Activity) view.getContext()).finish();

                NavUtils.navigateUpFromSameTask(getActivity());

            }
        });

    }

    public void setupFillInTheBlankDisplay() {

        continueLessonButton.setVisibility(View.GONE);
        gradeSelfLinearLayout.setVisibility(View.GONE);
        showAnswerButton.setVisibility(View.GONE);
        Quizzer.radioButton1.setVisibility(View.GONE);
        Quizzer.radioButton2.setVisibility(View.GONE);
        Quizzer.radioButton3.setVisibility(View.GONE);
        Quizzer.radioButton4.setVisibility(View.GONE);
        multipleChoiceSubmitButton.setVisibility(View.GONE);
        correctAnsTextView.setVisibility(View.GONE);
        ansExplanationTextView.setVisibility(View.GONE);
        enterButton.setVisibility(View.VISIBLE);
        answerEditText.setVisibility(View.VISIBLE);
        questionTextView.setVisibility(View.VISIBLE);
        questionTextView.setText(currentQuestion.getQuestion());


        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkEditTextAns();
            }
        });

    }

    public void setupGradeSelfDisplay() {

        Quizzer.radioButton1.setVisibility(View.GONE);
        Quizzer.radioButton2.setVisibility(View.GONE);
        Quizzer.radioButton3.setVisibility(View.GONE);
        Quizzer.radioButton4.setVisibility(View.GONE);
        multipleChoiceSubmitButton.setVisibility(View.GONE);
        correctAnsTextView.setVisibility(View.GONE);
        ansExplanationTextView.setVisibility(View.GONE);
        enterButton.setVisibility(View.GONE);
        answerEditText.setVisibility(View.GONE);
        continueLessonButton.setVisibility(View.GONE);
        gradeSelfLinearLayout.setVisibility(View.GONE);
        questionTextView.setText(currentQuestion.getQuestion());

        showAnswerButton.setText("Show answer");
        showAnswerButton.setVisibility(View.VISIBLE);
        showAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setUpGradeSelfAnswerDisplay();

            }
        });


    }

    public void questionOrAnsDisplay(boolean qOrA, boolean corOrInc) {

        if (graduated) {

            setupGraduatedDisplay();
            return;
        }

        if (qOrA) {

            if (corOrInc) {

                correctAnsDisplay();

            } else {

                incorrectAnsDisplay();

            }
        } else {

            checkForDisplayStyle(currentQuestion);

        }

    }

    public void setUpGradeSelfAnswerDisplay() {

        showAnswerButton.setVisibility(View.GONE);
        gradeSelfCorrect.setText("Knew it");
        gradeSelfIncorrect.setText("Try again");
        correctAnsTextView.setVisibility(View.VISIBLE);
        correctAnsTextView.setText(currentQuestion.getCorrectAns());
        correctAnsTextView.setBackgroundResource(R.color.standardBackground);
        gradeSelfLinearLayout.setVisibility(View.VISIBLE);
        gradeSelfCorrect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Quizzer.correctOrIncorrect = true;
                questionOrAnswer = true;
                Quizzer.currentScore = addOneToTotalCurrentScore(Quizzer.currentScore);
                mUsersDatabaseFrag.setValue(currentScore);
                correctAnsDisplayPostGS();

            }
        });
        gradeSelfIncorrect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Quizzer.correctOrIncorrect = false;
                questionOrAnswer = true;

                incorrectAnsDisplayPostGS();

            }
        });


    }

    public void setupMultipleChoiceDisplay() {

        Quizzer.radioButton1.setVisibility(View.VISIBLE);
        Quizzer.radioButton2.setVisibility(View.VISIBLE);
        Quizzer.radioButton3.setVisibility(View.VISIBLE);
        Quizzer.radioButton4.setVisibility(View.VISIBLE);
        gradeSelfLinearLayout.setVisibility(View.GONE);
        multipleChoiceSubmitButton.setVisibility(View.VISIBLE);
        correctAnsTextView.setVisibility(View.GONE);
        ansExplanationTextView.setVisibility(View.GONE);
        enterButton.setVisibility(View.GONE);
        answerEditText.setVisibility(View.GONE);
        continueLessonButton.setVisibility(View.GONE);
        showAnswerButton.setVisibility(View.GONE);

        randomizeButtonAnswerOrder(Quizzer.randomObject.nextInt(4));
        questionTextView.setText(currentQuestion.getQuestion());


        multipleChoiceSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (radioGroup.getCheckedRadioButtonId() == -1) {
                    // no radio buttons are checked
                    Toast.makeText(getContext(), "Oops! No answer is selected.", Toast.LENGTH_SHORT).show();
                } else {
                    // one of the radio buttons is checked
                    int radioButtonID = radioGroup.getCheckedRadioButtonId();
                    RadioButton radioButton = (RadioButton) radioGroup.findViewById(radioButtonID);
                    String proposedAns = (String) radioButton.getText();
                    checkMultipleChoiceAns(proposedAns);

                }

            }
        });


    }

    public void correctAnsDisplayPostGS() {

        Quizzer.radioButton1.setVisibility(View.GONE);
        Quizzer.radioButton2.setVisibility(View.GONE);
        Quizzer.radioButton3.setVisibility(View.GONE);
        Quizzer.radioButton4.setVisibility(View.GONE);
        gradeSelfLinearLayout.setVisibility(View.GONE);
        multipleChoiceSubmitButton.setVisibility(View.GONE);
        enterButton.setVisibility(View.GONE);
        answerEditText.setVisibility(View.GONE);

        questionTextView.setVisibility(View.VISIBLE);
        correctAnsTextView.setVisibility(View.VISIBLE);
        ansExplanationTextView.setVisibility(View.VISIBLE);
        continueLessonButton.setVisibility(View.VISIBLE);

        String correctString = "Nice job!";

        questionTextView.setText(currentQuestion.getQuestion());
        correctAnsTextView.setText(correctString);
        correctAnsTextView.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.rounded_text_view_corner_correct));
        SpannedString currentQuestionExplanation = (SpannedString) textViewArray[Quizzer.currentQuestion.getQuestionId()].getText();
        ansExplanationTextView.setText(currentQuestionExplanation);

        continueLessonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                quizFragProgressDialog.setMessage("Loading next question...");
                quizFragProgressDialog.show();

                Quizzer.questionOrAnswer = false;

                correctUpdateQuestionScore();

            }
        });

        scrollView.fullScroll(ScrollView.FOCUS_UP);
        clockwise();

    }

    public void correctAnsDisplay() {

        Quizzer.radioButton1.setVisibility(View.GONE);
        Quizzer.radioButton2.setVisibility(View.GONE);
        Quizzer.radioButton3.setVisibility(View.GONE);
        Quizzer.radioButton4.setVisibility(View.GONE);
        gradeSelfLinearLayout.setVisibility(View.GONE);
        multipleChoiceSubmitButton.setVisibility(View.GONE);
        enterButton.setVisibility(View.GONE);
        answerEditText.setVisibility(View.GONE);

        questionTextView.setVisibility(View.VISIBLE);
        correctAnsTextView.setVisibility(View.VISIBLE);
        if (Quizzer.currentQuestion.getSubjectString().equals("Tutorial and Objectives")) {

            ansExplanationTextView.setVisibility(View.GONE);
            continueLessonButton.setVisibility(View.VISIBLE);
            String correctString = "Great!" +"\n"+"Click below to continue!";
            correctAnsTextView.setText(correctString);

        } else {

            ansExplanationTextView.setVisibility(View.VISIBLE);
            continueLessonButton.setVisibility(View.VISIBLE);
            String correctString = "Correct!" + "\n" + "The answer was:" + "\n" +
                    Quizzer.currentQuestion.getCorrectAns();
            correctAnsTextView.setText(correctString);
        }




        questionTextView.setText(currentQuestion.getQuestion());

        correctAnsTextView.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.rounded_text_view_corner_correct));
        SpannedString currentQuestionExplanation = (SpannedString) textViewArray[Quizzer.currentQuestion.getQuestionId()].getText();
        ansExplanationTextView.setText(currentQuestionExplanation);

        continueLessonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                quizFragProgressDialog.setMessage("Loading next question...");
                quizFragProgressDialog.show();

                Quizzer.questionOrAnswer = false;

                correctUpdateQuestionScore();

            }
        });

        scrollView.fullScroll(ScrollView.FOCUS_UP);
        clockwise();

    }

    public void incorrectAnsDisplayPostGS() {

        Quizzer.radioButton1.setVisibility(View.GONE);
        Quizzer.radioButton2.setVisibility(View.GONE);
        Quizzer.radioButton3.setVisibility(View.GONE);
        Quizzer.radioButton4.setVisibility(View.GONE);
        gradeSelfLinearLayout.setVisibility(View.GONE);
        multipleChoiceSubmitButton.setVisibility(View.GONE);
        enterButton.setVisibility(View.GONE);
        answerEditText.setVisibility(View.GONE);

        questionTextView.setVisibility(View.VISIBLE);
        correctAnsTextView.setVisibility(View.VISIBLE);
        ansExplanationTextView.setVisibility(View.VISIBLE);
        continueLessonButton.setVisibility(View.VISIBLE);

        String correctString = "No worries." + "\n" + "Just keep practicing for better recall.";

        questionTextView.setText(currentQuestion.getQuestion());
        correctAnsTextView.setText(correctString);
        correctAnsTextView.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.rounded_text_view_corner_incorrect));
        SpannedString currentQuestionExplanation = (SpannedString) textViewArray[Quizzer.currentQuestion.getQuestionId()].getText();
        ansExplanationTextView.setText(currentQuestionExplanation);

        continueLessonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                quizFragProgressDialog.setMessage("Loading next question...");
                quizFragProgressDialog.show();

                Quizzer.questionOrAnswer = false;

                incorrectUpdateQuestionScore();


            }
        });

        scrollView.fullScroll(ScrollView.FOCUS_UP);

    }

    public void incorrectAnsDisplay() {

        Quizzer.radioButton1.setVisibility(View.GONE);
        Quizzer.radioButton2.setVisibility(View.GONE);
        Quizzer.radioButton3.setVisibility(View.GONE);
        Quizzer.radioButton4.setVisibility(View.GONE);
        gradeSelfLinearLayout.setVisibility(View.GONE);
        multipleChoiceSubmitButton.setVisibility(View.GONE);
        enterButton.setVisibility(View.GONE);
        answerEditText.setVisibility(View.GONE);

        questionTextView.setVisibility(View.VISIBLE);
        correctAnsTextView.setVisibility(View.VISIBLE);
        ansExplanationTextView.setVisibility(View.VISIBLE);
        continueLessonButton.setVisibility(View.VISIBLE);

        String correctString = "Nice try!" + "\n" + "Here's the correct answer:" +
                "\n" + Quizzer.currentQuestion.getCorrectAns();

        questionTextView.setText(currentQuestion.getQuestion());
        correctAnsTextView.setText(correctString);
        correctAnsTextView.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.rounded_text_view_corner_incorrect));
        SpannedString currentQuestionExplanation = (SpannedString) textViewArray[Quizzer.currentQuestion.getQuestionId()].getText();
        ansExplanationTextView.setText(currentQuestionExplanation);

        continueLessonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                quizFragProgressDialog.setMessage("Loading next question...");
                quizFragProgressDialog.show();

                Quizzer.questionOrAnswer = false;

                incorrectUpdateQuestionScore();

            }
        });

        scrollView.fullScroll(ScrollView.FOCUS_UP);

    }


    public void checkMultipleChoiceAns(String proposedAns) {

        if (Quizzer.currentQuestion.getCorrectAns().equals(proposedAns)) {

            Quizzer.questionOrAnswer = true;
            Quizzer.correctOrIncorrect = true;

            correctAnsDisplay();
            Quizzer.currentScore = addOneToTotalCurrentScore(Quizzer.currentScore);
            mUsersDatabaseFrag.setValue(currentScore);


        } else {

            Quizzer.questionOrAnswer = true;
            Quizzer.correctOrIncorrect = false;

            incorrectAnsDisplay();

        }

    }

    public void correctUpdateQuestionScore() {

        Quizzer.currentQuestion.setTimesAnsweredCorrectly(Quizzer.currentQuestion.getTimesAnsweredCorrectly() + 1);
        String currentQIDString = String.valueOf(Quizzer.currentQuestion.getQuestionId());

        if (Quizzer.questionCount < (Quizzer.questionArrayList.size()) - 1) {
            Quizzer.questionCount++;
        } else {
            Quizzer.questionCount = 0;
        }

        Quizzer.previousQuestion = Quizzer.currentQuestion;

        Quizzer.currentQuestion = questionArrayList.get(questionCount);

        if (animation.hasStarted()) {

            stopAnimation();

        }

        mQuestionDatabaseFrag.child("QuestionIDs").child(currentQIDString).child("Correct").
                setValue(Quizzer.previousQuestion.getTimesAnsweredCorrectly());

    }

    public void incorrectUpdateQuestionScore() {

        Quizzer.currentQuestion.setTimesAnsweredIncorrectly(Quizzer.currentQuestion.getTimesAnsweredIncorrectly() + 1);
        String currentQIDString = String.valueOf(Quizzer.currentQuestion.getQuestionId());

        if (Quizzer.questionCount < (Quizzer.questionArrayList.size()) - 1) {
            Quizzer.questionCount++;
        } else {
            Quizzer.questionCount = 0;
        }

        Quizzer.previousQuestion = Quizzer.currentQuestion;

        Quizzer.currentQuestion = questionArrayList.get(questionCount);

        mQuestionDatabaseFrag.child("QuestionIDs").child(currentQIDString).child("Incorrect").
                setValue(Quizzer.previousQuestion.getTimesAnsweredIncorrectly());

    }

    public void checkEditTextAns() {

        hideKeyboardFrom(getContext(), getView());

        String answer = answerEditText.getText().toString().toLowerCase().trim();
        String removeTheS = answer;

        String altAnswer1 = "altAnswer1";
        String altAnswer2 = "altAnswer2";
        String altAnswer3 = "altAnswer3";

        if (!(answer.indexOf(',') == -1)) {

            answer = answer.replaceAll(",", "");

        }

        if (!(answer.indexOf(';') == -1)) {

            answer = answer.replaceAll(";", "");

        }

        if (answer.contains(".")) {

            answer = answer.replaceAll("\\.", "");

        }

        if (answer.startsWith("the ")) {

            answer = answer.replaceAll("the ", "");

        } else if (answer.startsWith("a ")) {

            answer = answer.replaceFirst("a ", "");

        } else if (answer.startsWith("an ")) {

            answer = answer.replaceFirst("an ", "");

        }

        if (Quizzer.currentQuestion.getAltCorrectAns1() != null) {

            altAnswer1 = Quizzer.currentQuestion.getAltCorrectAns1().toLowerCase();

        }

        if (Quizzer.currentQuestion.getAltCorrectAns2() != null) {

            altAnswer2 = Quizzer.currentQuestion.getAltCorrectAns2().toLowerCase();

        }

        if (Quizzer.currentQuestion.getAltCorrectAns3() != null) {

            altAnswer3 = Quizzer.currentQuestion.getAltCorrectAns3().toLowerCase();

        }

        if (answer.endsWith("s")) {

            removeTheS = answer.substring(0, (answer.length() - 1));

        }


        if (answer.equals(Quizzer.currentQuestion.getCorrectAns().toLowerCase())
                || answer.equals(altAnswer1) || answer.equals(altAnswer2)
                || answer.equals(altAnswer3) || removeTheS.equals(Quizzer.currentQuestion.getCorrectAns().toLowerCase())
                || removeTheS.equals(altAnswer1) || removeTheS.equals(altAnswer2) || removeTheS.equals(altAnswer3)) {

            Quizzer.currentScore = addOneToTotalCurrentScore(Quizzer.currentScore);
            mUsersDatabaseFrag.setValue(currentScore);
            String currentScoreString = String.valueOf(Quizzer.currentScore);
            scoreTextView.setText("Score: " + currentScoreString);

            Quizzer.questionOrAnswer = true;
            Quizzer.correctOrIncorrect = true;

            correctAnsDisplay();

        } else {

            Quizzer.questionOrAnswer = true;
            Quizzer.correctOrIncorrect = false;

            incorrectAnsDisplay();

        }

        answerEditText.setText("");

    }

    public void checkForDisplayStyle(Question currentQuestion) {

        mQuestionDatabaseFrag.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot qData) {

                DataSnapshot dataSnapshot = qData.child("QuestionIDs");
                DataSnapshot isGraduated = qData.child("Graduated");
                boolean isGraduatedBoolean = isGraduated.getValue(Boolean.class);

                if (isGraduatedBoolean) {

                    setupGraduatedDisplay();
                    quizFragProgressDialog.dismiss();
                    return;

                }

                graduated = true;


                String currentQuestionIdString = String.valueOf(Quizzer.currentQuestion.getQuestionId());
                Object timesAnsCorOb = dataSnapshot.child(currentQuestionIdString).child("Correct").getValue();
                Object timesAnsIncOb = dataSnapshot.child(currentQuestionIdString).child("Incorrect").getValue();
                currentQuestionTimesAnsCorrect = (long) timesAnsCorOb;
                currentQuestionTimesAnsIncorrect = (long) timesAnsIncOb;
                int currentQTimesAnsCorrectInt = (int) currentQuestionTimesAnsCorrect;
                int currentQTimesAnsIncorrectInt = (int) currentQuestionTimesAnsIncorrect;
                Quizzer.currentQuestion.setTimesAnsweredCorrectly(currentQTimesAnsCorrectInt);
                Quizzer.currentQuestion.setTimesAnsweredIncorrectly(currentQTimesAnsIncorrectInt);

                Iterable<DataSnapshot> questionsIterable = dataSnapshot.getChildren();
                Iterator iterateQuestions = questionsIterable.iterator();


                while (iterateQuestions.hasNext()) {

                    Object question = iterateQuestions.next();
                    DataSnapshot questionDataSnap = (DataSnapshot) question;
                    String questionIdStringFromKey = questionDataSnap.getKey();
                    int questionIdIntFromKey = Integer.parseInt(questionIdStringFromKey);

                    Long timesAnsweredCorrect = questionDataSnap.child("Correct").getValue(Long.class);
                    Long timesAnsweredIncorrect = questionDataSnap.child("Incorrect").getValue(Long.class);

                    double timesAnsweredCorrectDouble = timesAnsweredCorrect.doubleValue();
                    double timesAnsweredIncorrectDouble = timesAnsweredIncorrect.doubleValue();
                    double percentCorrect = timesAnsweredCorrectDouble / (timesAnsweredCorrectDouble + timesAnsweredIncorrectDouble);

                    if (timesAnsweredCorrectDouble < 1) {

                        graduated = false;

                    } else if (percentCorrect < 0.50) {

                        graduated = false;

                    }


                }

                if (graduated) {

                    mQuestionDatabaseFrag.child("Graduated").setValue(true);
                    mGraduatedDatabaseFrag.setValue(true);

                    setupGraduatedDisplay();
                    quizFragProgressDialog.dismiss();
                    return;

                } else {

                    if (!Quizzer.currentQuestion.getMultipleChoice()) {
                        if (Quizzer.currentQuestion.getFillInTheBlank()) {
                            setupFillInTheBlankDisplay();
                            quizFragProgressDialog.dismiss();
                            return;
                        } else {
                            setupGradeSelfDisplay();
                            quizFragProgressDialog.dismiss();
                            return;
                        }
                    }

                    int currentQuestionIdInt = Quizzer.currentQuestion.getQuestionId();
                    currentQuestionIdString1 = Integer.toString(currentQuestionIdInt);

                    Long timesAnsIncorrectly = dataSnapshot.child(currentQuestionIdString1).
                            child("Incorrect").getValue(Long.class);
                    Long timesAnsCorrectly = dataSnapshot.child(currentQuestionIdString1).child("Correct")
                            .getValue(Long.class);
                    double timesAnsCorrectlyDouble = (double) timesAnsCorrectly;
                    if (timesAnsCorrectly == 0) {
                        if (Quizzer.currentQuestion.getMultipleChoice()) {

                            setupMultipleChoiceDisplay();
                            quizFragProgressDialog.dismiss();
                            return;

                        } else if (Quizzer.currentQuestion.getFillInTheBlank()) {

                            setupFillInTheBlankDisplay();
                            quizFragProgressDialog.dismiss();
                            return;

                        } else {
                            setupGradeSelfDisplay();
                            quizFragProgressDialog.dismiss();
                            return;
                        }
                    }

                    if (timesAnsCorrectly > 0) {
                        if (timesAnsCorrectlyDouble / (timesAnsIncorrectly + timesAnsCorrectly) > 0.34) {
                            if (Quizzer.currentQuestion.getFillInTheBlank()) {
                                setupFillInTheBlankDisplay();
                                quizFragProgressDialog.dismiss();
                                return;
                            } else if (Quizzer.currentQuestion.getGradeSelf()) {
                                setupGradeSelfDisplay();
                                quizFragProgressDialog.dismiss();
                                return;
                            } else {
                                setupMultipleChoiceDisplay();
                                quizFragProgressDialog.dismiss();
                                return;
                            }
                        } else if (Quizzer.currentQuestion.getMultipleChoice()) {
                            setupMultipleChoiceDisplay();
                            quizFragProgressDialog.dismiss();
                            return;
                        } else if (Quizzer.currentQuestion.getFillInTheBlank()) {
                            setupFillInTheBlankDisplay();
                            quizFragProgressDialog.dismiss();
                            return;
                        } else {
                            setupGradeSelfDisplay();
                            quizFragProgressDialog.dismiss();
                            return;
                        }
                    } else if (Quizzer.currentQuestion.getMultipleChoice()) {
                        setupMultipleChoiceDisplay();
                        quizFragProgressDialog.dismiss();
                        return;
                    } else if (Quizzer.currentQuestion.getFillInTheBlank()) {
                        setupFillInTheBlankDisplay();
                        quizFragProgressDialog.dismiss();
                        return;
                    } else {
                        setupGradeSelfDisplay();
                        quizFragProgressDialog.dismiss();
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void randomizeButtonAnswerOrder(int randomCase) {

        radioGroup.clearCheck();

        switch (randomCase) {
            case 0:
                Quizzer.radioButton1.setText(Quizzer.currentQuestion.getCorrectAns());
                Quizzer.radioButton2.setText(Quizzer.currentQuestion.getIncorrectAns1());
                Quizzer.radioButton3.setText(Quizzer.currentQuestion.getIncorrectAns2());
                Quizzer.radioButton4.setText(Quizzer.currentQuestion.getIncorrectAns3());
                break;
            case 1:
                Quizzer.radioButton2.setText(Quizzer.currentQuestion.getCorrectAns());
                Quizzer.radioButton1.setText(Quizzer.currentQuestion.getIncorrectAns1());
                Quizzer.radioButton3.setText(Quizzer.currentQuestion.getIncorrectAns2());
                Quizzer.radioButton4.setText(Quizzer.currentQuestion.getIncorrectAns3());
                break;
            case 2:
                Quizzer.radioButton3.setText(Quizzer.currentQuestion.getCorrectAns());
                Quizzer.radioButton2.setText(Quizzer.currentQuestion.getIncorrectAns1());
                Quizzer.radioButton1.setText(Quizzer.currentQuestion.getIncorrectAns2());
                Quizzer.radioButton4.setText(Quizzer.currentQuestion.getIncorrectAns3());
                break;
            case 3:
                Quizzer.radioButton4.setText(Quizzer.currentQuestion.getCorrectAns());
                Quizzer.radioButton3.setText(Quizzer.currentQuestion.getIncorrectAns1());
                Quizzer.radioButton2.setText(Quizzer.currentQuestion.getIncorrectAns2());
                Quizzer.radioButton1.setText(Quizzer.currentQuestion.getIncorrectAns3());
                break;
        }
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void clockwise() {


        plusOne.setVisibility(View.VISIBLE);
        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Animation fadeOut = new AlphaAnimation(1, 0);
                fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
                fadeOut.setDuration(1000);
                fadeOut.setStartOffset(500);
                fadeOut.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        plusOne.setVisibility(View.INVISIBLE);

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                plusOne.startAnimation(fadeOut);


            }
        });
        plusOne.startAnimation(animation);
    }

    public static boolean canCancelAnimation() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    public void stopAnimation() {
        plusOne.clearAnimation();
        if (canCancelAnimation()) {
            plusOne.animate().cancel();
        }
    }

    public void resetStatsForQuiz() {

        for (int i = 0; i < questionArrayList.size(); i++) {

            String iString = Integer.toString(i);

            mQuestionDatabaseFrag.child("QuestionIDs").child(iString).child("Correct").setValue(0);
            mQuestionDatabaseFrag.child("QuestionIDs").child(iString).child("Incorrect").setValue(0);
            mQuestionDatabaseFrag.child("Graduated").setValue(false);


        }

    }

}
