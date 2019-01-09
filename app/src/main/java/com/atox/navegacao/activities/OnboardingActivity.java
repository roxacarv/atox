package com.atox.navegacao.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.atox.R;
import com.atox.usuario.gui.LoginActivity;
import com.hololo.tutorial.library.Step;
import com.hololo.tutorial.library.TutorialActivity;

public class OnboardingActivity extends TutorialActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addFragment(new Step.Builder().setTitle("ORGÂNICOS POR PERTO")
                .setContent("Veja no mapa os mais próximos restaurantes, mercados e feirinhas com produtos orgânicos!")
                .setBackgroundColor(Color.parseColor("#61987B")) // int background color
                .setDrawable(R.drawable.onboarding_1) // int top drawable
                //.setSummary("This is summary 1")
                .build());
        addFragment(new Step.Builder().setTitle("DICAS E RECEITAS")
                .setContent("Receba dicas de como começar sua \n" +
                        "horta em casa e também um montão \n" +
                        "de receitas de comidas saudáveis!")
                .setBackgroundColor(Color.parseColor("#61987B")) // int background color
                .setDrawable(R.drawable.onboarding_2) // int top drawable
                //.setSummary("This is summary 2")
                .build());
        addFragment(new Step.Builder().setTitle("DIRETO DA FONTE")
                .setContent("Tenha acesso ao contato dos produtores \n" +
                        "cadastrados nas cooperativas mais \n" +
                        "próximas!")
                .setBackgroundColor(Color.parseColor("#61987B")) // int background color
                .setDrawable(R.drawable.onboarding_3) // int top drawable
                //.setSummary("This is summary 3")
                .build());
    }

    @Override
    public void finishTutorial() {
        // Your implementation
        Intent intent=new Intent(OnboardingActivity.this,LoginActivity.class);
        startActivity(intent);
    }
}
