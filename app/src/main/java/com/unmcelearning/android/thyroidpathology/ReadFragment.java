package com.unmcelearning.android.thyroidpathology;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.SpannedString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import static android.view.View.GONE;
import static com.unmcelearning.android.thyroidpathology.Quizzer.currentLevelName;
import static com.unmcelearning.android.thyroidpathology.Quizzer.subjectString;
import static com.unmcelearning.android.thyroidpathology.Quizzer.textStringArray;
import static com.unmcelearning.android.thyroidpathology.Quizzer.textViewArray;
import static com.unmcelearning.android.thyroidpathology.Quizzer.understandReadDirections;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReadFragment extends Fragment {

    static TextView readTextView1;
    static TextView readTextView2;
    static TextView readTextView3;
    static TextView readTextView4;
    static TextView readTextView5;
    static TextView readTextView6;
    static TextView readTextView7;
    static TextView readTextView8;
    static TextView readTextView9;
    static TextView readTextView10;
    static TextView topicTextViewLabel;
    static TextView levelTextViewLabel;
    TextView instructionsRead;
    Button gotItReadButton;
    LinearLayout instructionsLinLay;

    public ReadFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_read, container, false);
        instructionsLinLay = (LinearLayout) rootView.findViewById(R.id.instructions_linear_layout);
        instructionsLinLay.bringToFront();
        instructionsRead = (TextView) rootView.findViewById(R.id.read_instructions);
        gotItReadButton = (Button) rootView.findViewById(R.id.got_it_read_button);

        readTextView1 = (TextView) rootView.findViewById(R.id.read_text_view_1);
        readTextView2 = (TextView) rootView.findViewById(R.id.read_text_view_2);
        readTextView3 = (TextView) rootView.findViewById(R.id.read_text_view_3);
        readTextView4 = (TextView) rootView.findViewById(R.id.read_text_view_4);
        readTextView5 = (TextView) rootView.findViewById(R.id.read_text_view_5);
        readTextView6 = (TextView) rootView.findViewById(R.id.read_text_view_6);
        readTextView7 = (TextView) rootView.findViewById(R.id.read_text_view_7);
        readTextView8 = (TextView) rootView.findViewById(R.id.read_text_view_8);
        readTextView9 = (TextView) rootView.findViewById(R.id.read_text_view_9);
        readTextView10 = (TextView) rootView.findViewById(R.id.read_text_view_10);
        topicTextViewLabel = (TextView) rootView.findViewById(R.id.topic_text_view_label);
        levelTextViewLabel = (TextView) rootView.findViewById(R.id.level_text_view_label);


        if (!understandReadDirections) {

            final Animation animationFadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);

            setUpAnimationRead(animationFadeIn);

        }


        if (textStringArray[0] != null) {

            readTextView1.setText(Html.fromHtml(textStringArray[0]));

        }
        if (textStringArray[1] != null) {

            readTextView2.setText(Html.fromHtml(textStringArray[1]));

        }
        if (textStringArray[2] != null) {

            readTextView3.setText(Html.fromHtml(textStringArray[2]));

        }
        if (textStringArray[3] != null) {

            readTextView4.setText(Html.fromHtml(textStringArray[3]));

        }
        if (textStringArray[4] != null) {

            readTextView5.setText(Html.fromHtml(textStringArray[4]));

        }
        if (textStringArray[5] != null) {

            readTextView6.setText(Html.fromHtml(textStringArray[5]));

        }
        if (textStringArray[6] != null) {

            readTextView7.setText(Html.fromHtml(textStringArray[6]));

        }
        if (textStringArray[7] != null) {

            readTextView8.setText(Html.fromHtml(textStringArray[7]));

        }
        if (textStringArray[8] != null) {

            readTextView9.setText(Html.fromHtml(textStringArray[8]));

        }
        if (textStringArray[9] != null) {

            readTextView10.setText(Html.fromHtml(textStringArray[9]));

        }
        topicTextViewLabel.setText(subjectString);
        levelTextViewLabel.setText(currentLevelName);

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

                if (currentString == null || currentString.length() < 6) {
                    currentTextView.setVisibility(GONE);
                } else {
                    currentTextView.setVisibility(View.VISIBLE);
                }

            } else {
                currentTextView.setVisibility(GONE);
            }
        }

        return rootView;
    }

    public void setUpAnimationRead(Animation animExample) {

        animExample.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

                instructionsLinLay.setVisibility(View.VISIBLE);
                instructionsRead.setText("Swipe left to\nview the image.");


            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                gotItReadButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        understandReadDirections = true;
                        animationFadeOut();

                    }
                });

            }
        });
        instructionsLinLay.startAnimation(animExample);


    }

    public void animationFadeOut() {

        final Animation animationFadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
        animationFadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {


            }

            @Override
            public void onAnimationEnd(Animation animation) {

                instructionsLinLay.setVisibility(GONE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        instructionsLinLay.startAnimation(animationFadeOut);

    }

}
