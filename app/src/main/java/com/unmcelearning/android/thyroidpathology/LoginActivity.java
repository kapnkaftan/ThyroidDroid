package com.unmcelearning.android.thyroidpathology;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TypefaceSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextEmailLogin;
    private EditText editTextPasswordLogin;
    private Button buttonLogin;
    private Button buttonGoBack;

    private FirebaseAuth firebaseAuth;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();

        SpannableString title = new SpannableString("Thyroid Droid");

// Add a span for the sans-serif-light font
        title.setSpan(
                new TypefaceSpan("sans-serif-light"),
                0,
                title.length(),
                Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        actionBar.setTitle(title);

        //initialize
        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            //start home activity

            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        }
        editTextEmailLogin = (EditText) findViewById(R.id.edit_text_email_login);
        editTextPasswordLogin = (EditText) findViewById(R.id.edit_text_password_login);
        buttonLogin = (Button) findViewById(R.id.button_sign_in);
        buttonGoBack = (Button) findViewById(R.id.button_go_back_register);
        progressDialog = new ProgressDialog(this);

        buttonLogin.setOnClickListener(this);
        buttonGoBack.setOnClickListener(this);

    }

    private void userLogin() {
        String email = editTextEmailLogin.getText().toString().trim();
        String password = editTextPasswordLogin.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            //email is empty
            Toast.makeText(this, "Please enter your email.", Toast.LENGTH_SHORT);
            return;
        }


        if (TextUtils.isEmpty(password)) {
            //password is empty
            Toast.makeText(this, "Please enter a password.", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Signing in...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if (task.isSuccessful()) {
                            //start home activity

                            finish();
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        }

                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Incorrect email or password.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        if (view == buttonLogin) {
            userLogin();
        }

        if (view == buttonGoBack) {

            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
