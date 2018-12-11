package com.atox.navegacao.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.atox.usuario.dominio.Pessoa;
import com.atox.usuario.dominio.SessaoUsuario;
import com.atox.usuario.dominio.Usuario;
import com.atox.usuario.gui.LoginActivity;
import com.atox.usuario.negocio.SessaoNegocio;

import java.util.concurrent.ExecutionException;

public class SplashActivity extends AppCompatActivity {

    private SessaoUsuario sessaoUsuario;
    private Usuario usuario;
    private Pessoa pessoa;
    private String TAG = SplashActivity.class.getName();

    //Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SessaoNegocio sessaoNegocio = new SessaoNegocio(this);
        //TODO verifica se já existe usuário logado.
        Pessoa pessoaJaLogada = null;

        try {
            pessoaJaLogada = sessaoNegocio.restaurarSessao();
        } catch (ExecutionException e) {
            alert("");
        } catch (InterruptedException e) {
            alert("");
        }

        if (pessoaJaLogada != null){
            alert(getString(com.atox.R.string.bem_vindo_de_volta));
            goToHomeScreen();
        } else{
            alert(getString(com.atox.R.string.bem_vindo));
            goToLoginScreen();
        }

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

    private void goToHomeScreen() {
        Intent intent = new Intent(SplashActivity.this, MenuActivity.class);
        startActivity(intent);
    }

    private void goToLoginScreen() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void alert(String s){
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }
}
