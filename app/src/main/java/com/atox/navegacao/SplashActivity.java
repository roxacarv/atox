package com.atox.navegacao;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.atox.usuario.dominio.SessaoUsuario;
import com.atox.usuario.dominio.Usuario;
import com.atox.usuario.gui.LoginActivity;
import com.atox.usuario.negocio.UsuarioNegocio;

import java.util.concurrent.ExecutionException;

public class SplashActivity extends AppCompatActivity {

    private SessaoUsuario sessaoUsuario;
    private UsuarioNegocio usuarioNegocio;
    private Usuario usuario;
    private String TAG = SplashActivity.class.getName();

    //Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessaoUsuario = SessaoUsuario.getSessao();
        usuarioNegocio = ViewModelProviders.of(this).get(UsuarioNegocio.class);
        try {
            usuario = usuarioNegocio.restaurarSessao();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(usuario != null) {
            Log.i(TAG, "Entrou porque usuário havia logado anteriormente");
            sessaoUsuario.setUsuarioLogado(usuario);
            goToHomeScreen();
        } else {
            Log.i(TAG, "Não entrou pois não havia usuário logado");
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

    protected void goToHomeScreen() {
        Intent intent = new Intent(SplashActivity.this, MenuActivity.class);
        startActivity(intent);
    }

    protected void goToLoginScreen() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
