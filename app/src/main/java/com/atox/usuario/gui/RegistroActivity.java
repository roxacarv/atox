package com.atox.usuario.gui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.atox.R;

public class RegistroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
    }


    public void backToLoginScreen(View view){

        Intent registerScreen = new Intent(RegistroActivity.this, LoginActivity.class);
        startActivity(registerScreen);

    }

    public void goToEnderecoScreen(View view){

        Intent registerScreen = new Intent(RegistroActivity.this, EnderecoActivity.class);
        startActivity(registerScreen);

    }

}
