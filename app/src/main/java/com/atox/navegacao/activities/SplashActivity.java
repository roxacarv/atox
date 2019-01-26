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

public class SplashActivity extends AppCompatActivity {


    private SessaoNegocio sessaoNegocio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessaoNegocio = new SessaoNegocio(this);
        Pessoa pessoaJaLogada = null;
        pessoaJaLogada = sessaoNegocio.restaurarSessao();
        if (pessoaJaLogada != null){
            alert("Seja bem vindo de volta");
            goToHomeScreen();
        } else{
            alert("Seja bem vindo");
            goToLoginScreen();
        }

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
