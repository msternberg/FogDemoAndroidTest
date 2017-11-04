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

/**
 * Created by Matt on 11/4/2017.
 */

public class LoginActivity extends Activity {
    private EditText mUsernameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);;

        // Set up the login form.
        mUsernameView = (EditText) findViewById(R.id.username_input);

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
