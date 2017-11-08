package com.example.matthew.fogdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

/**
 * Created by Matt on 11/4/2017.
 */

public class LoginActivity extends Activity {
    private EditText mUsernameView;
    private EditText mRaspIpView;
    private static final Pattern IP_REGEX = Pattern.compile(
            "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);;

        // Set up the login form.
        mUsernameView = (EditText) findViewById(R.id.username_input);
        mRaspIpView = (EditText) findViewById(R.id.rpi_input);

        Button signInButton = (Button) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * Attempts to sign in the account specified by the login form.
     * If there are form errors (invalid username, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        String username = mUsernameView.getText().toString().trim();
        String raspIp = mRaspIpView.getText().toString().trim();
        if (!IP_REGEX.matcher(raspIp).find()) {
            Toast.makeText(getApplicationContext(), "You must input a valid Raspberry Pi IPv4 Address",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (username.isEmpty()) {
            Toast.makeText(getApplicationContext(), "You must input a username", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check for a valid username.
        // Store values at the time of the login attempt.
        SessionInfo.getInstance().setUsername(username);

        Intent intent = new Intent(this, ChatActivity.class);
        startActivity(intent);


    }

}
