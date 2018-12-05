package com.atox.navegacao.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.atox.infra.AtoxException;
import com.atox.usuario.dominio.Pessoa;
import com.atox.usuario.dominio.SessaoUsuario;
import com.atox.usuario.dominio.Usuario;
import com.atox.usuario.gui.LoginActivity;
import com.atox.usuario.negocio.SessaoNegocio;
import com.atox.usuario.persistencia.dao.PessoaDao;

import java.util.concurrent.ExecutionException;

public class SplashActivity extends AppCompatActivity {

    private SessaoUsuario sessaoUsuario;
    private Usuario usuario;
    private Pessoa pessoa;
    private String TAG = SplashActivity.class.getName();
    private SessaoNegocio sessaoNegocio;

    //Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessaoNegocio = new SessaoNegocio(this);
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
            alert("Seja bem vindo de volta");
            goToHomeScreen();
        } else{
            alert("Seja bem vindo");
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

    private void alert(String s){
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }
}
