package com.atox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LoginScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
    }

    public void goToRegisterScreen(View view){

        Intent registerScreen = new Intent(LoginScreenActivity.this, RegisterScreenActivity.class);
        startActivity(registerScreen);

    }
}
