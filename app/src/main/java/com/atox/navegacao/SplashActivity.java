package com.atox.navegacao;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.atox.R;
import com.atox.usuario.gui.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    //Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=new Intent(SplashActivity.this,LoginActivity.class);
        startActivity(intent);
        /*setContentView(R.layout.activity_splash);

        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashActivity.this,OnboardingActivity.class);
                startActivity(intent);
                finish();
            }
        },2000);*/

    }
}
