package com.unmcelearning.android.thyroidpathology;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import static android.view.View.GONE;
import static com.unmcelearning.android.thyroidpathology.Quizzer.understandImageDirections;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageFragment extends Fragment {

    static ImageView image;
    static TextView photoCreditTV;
    static TextView photoCaptionTV;
    LinearLayout imageInstruction;
    TextView instructionsTV;
    Button gotItImageButton;

    public ImageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_image, container, false);


        image = (ImageView) rootView.findViewById(R.id.image);
        photoCreditTV = (TextView) rootView.findViewById(R.id.photo_courtesy_text_view);
        photoCaptionTV = (TextView) rootView.findViewById(R.id.photo_caption_image_frag);


        instructionsTV = (TextView) rootView.findViewById(R.id.image_instructions_tv);
        gotItImageButton = (Button) rootView.findViewById(R.id.got_it_button_image);
        imageInstruction = (LinearLayout) rootView.findViewById(R.id.instructions_linear_layout_image);
        imageInstruction.setVisibility(View.INVISIBLE);
        imageInstruction.bringToFront();

        if (!understandImageDirections) {

            final Animation animationFadeInImage = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);

            setUpAnimationImage(animationFadeInImage);

        }


        return rootView;
    }

    public void setUpAnimationImage(Animation animExample) {

        animExample.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

                imageInstruction.setVisibility(View.VISIBLE);
                instructionsTV.setText("Swipe again to\ntake the quiz.");


            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                gotItImageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        understandImageDirections = true;
                        animationFadeOutImage();
                    }
                });

            }
        });
        imageInstruction.startAnimation(animExample);

    }

    public void animationFadeOutImage() {

        final Animation animationFadeOutImage = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
        animationFadeOutImage.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {


            }

            @Override
            public void onAnimationEnd(Animation animation) {

                imageInstruction.setVisibility(GONE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imageInstruction.startAnimation(animationFadeOutImage);

    }

}
