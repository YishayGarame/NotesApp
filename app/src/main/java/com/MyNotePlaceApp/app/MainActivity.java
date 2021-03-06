package com.MyNotePlaceApp.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //assign variables
    private TextView signUp, forgotPassword;
    private EditText editTextEmail, editTextPassword;
    private Button login;
    CheckBox rememberMe;

    //defaule email to use in forget password
    String userEmailDefultForgot ;

    //firebase variables
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    //SharedPreferences and remember me
    SharedPreferences preferences;
    String checkbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        //userEmailDefultForgot= "yishaygra@gmail.com";
        setContentView(R.layout.activity_main);

        //progressBar
        progressBar = (ProgressBar) findViewById(R.id.progressBar);


        //init variables
        signUp = (TextView) findViewById(R.id.signUp);
        forgotPassword = (TextView) findViewById(R.id.forgotPassword);
        login = (Button) findViewById(R.id.login);

        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);

        rememberMe = findViewById(R.id.checkBoxbutton);

        //click listeners
        forgotPassword.setOnClickListener(this);
        signUp.setOnClickListener(this);
        login.setOnClickListener(this);

        //remember me ,SharedPreferences and
        preferences = getSharedPreferences("checkBox", MODE_PRIVATE);
        checkbox = preferences.getString("remember", "false");
        if (checkbox.equals("true")) {

            //saved user credentials
            String savedEmail = preferences.getString("userEmail", "noemail");
            String savesPassword = preferences.getString("userPassword", "nopass");

            //sign in with firebase
            mAuth.signInWithEmailAndPassword(savedEmail, savesPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(MainActivity.this, DashboardActivity.class));
                    } else {
                        Toast.makeText(MainActivity.this, "Failed to login! Please check your credentials", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else if (checkbox.equals("false")) {
            Toast.makeText(MainActivity.this, "Please Sign In", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signUp:
                startActivity(new Intent(this, SignUpActivity.class));
                break;
            case R.id.login:
                userLogin();
                break;
            case R.id.forgotPassword:

                //get user email
                userEmailDefultForgot = editTextEmail.getText().toString();
                if(userEmailDefultForgot.matches("")){
                    userEmailDefultForgot = "default@gmail.com";
                }
                        FirebaseAuth.getInstance().sendPasswordResetEmail(userEmailDefultForgot)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "Password sent to "+ userEmailDefultForgot, Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                break;
        }
    }

    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();


        //validation
        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please enter a valid email");
            editTextEmail.requestFocus();
            return;
        }
        if (password.length() < 6) {
            editTextPassword.setError("Min password length should be six characters");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    if (rememberMe.isChecked()) {
                        preferences = getSharedPreferences("checkBox", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("remember", "true");
                        editor.putString("userEmail", email);
                        editor.putString("userPassword", password);
                        editor.apply();
                    }
                    else{
                        preferences = getSharedPreferences("checkBox", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("remember", "false");
                    }
                    startActivity(new Intent(MainActivity.this, DashboardActivity.class));
                } else {
                    Toast.makeText(MainActivity.this, "Failed to login! Please check your credentials", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}