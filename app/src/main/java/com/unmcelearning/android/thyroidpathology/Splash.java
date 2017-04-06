package com.unmcelearning.android.thyroidpathology;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.R.attr.animation;

/**
 * Created by adamk_000 on 1/6/2017.
 */

public class Splash extends Activity {

    private FirebaseAuth firebaseAuthSplash;

    private FirebaseAuth.AuthStateListener mAuthListenerSplash;

    private DatabaseReference mDatabaseSplash;


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        firebaseAuthSplash = FirebaseAuth.getInstance();

        mDatabaseSplash = FirebaseDatabase.getInstance().getReference();

        setContentView(R.layout.splash);

        final TextView sponsorshipTV = (TextView) findViewById(R.id.sponsor_text_view);
        sponsorshipTV.setVisibility(View.INVISIBLE);
        final ImageView unmcLogoImageView = (ImageView)findViewById(R.id.unmc_logo);
        final Animation removeLogo = AnimationUtils.loadAnimation(getBaseContext(), R.anim.logo_fade_out);
        final Animation sponsorAnimation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.splash_res);
        final Animation stayAnimation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.splash_stay);

        mAuthListenerSplash = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                final FirebaseUser user = firebaseAuth.getCurrentUser();
                removeLogo.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                        unmcLogoImageView.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                        unmcLogoImageView.setVisibility(View.INVISIBLE);
                        sponsorAnimation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                                sponsorshipTV.setText("Funding provided by an award from the Office of the Vice " +
                                        "Chancellor for Academic Affairs at the University of Nebraska Medical Center 2017.");

                                sponsorshipTV.setVisibility(View.VISIBLE);

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {

                                stayAnimation.setAnimationListener(new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationStart(Animation animation) {


                                    }

                                    @Override
                                    public void onAnimationEnd(Animation animation) {

                                        if (user != null) {
                                            // User is signed in
                                            Intent i = new Intent(Splash.this, HomeActivity.class);
                                            startActivity(i);

                                        } else {


                                            Intent i = new Intent(Splash.this, MainActivity.class);
                                            startActivity(i);

                                            // User is signed out
                                        }

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) {

                                    }
                                });

                                sponsorshipTV.startAnimation(stayAnimation);

                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        sponsorshipTV.startAnimation(sponsorAnimation);


                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                unmcLogoImageView.startAnimation(removeLogo);

            }
        };


        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

    }


    @Override
    public void onStart() {
        super.onStart();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        firebaseAuthSplash.addAuthStateListener(mAuthListenerSplash);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page")
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStop() {
        super.onStop();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        if (mAuthListenerSplash != null) {
            firebaseAuthSplash.removeAuthStateListener(mAuthListenerSplash);
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }
}
