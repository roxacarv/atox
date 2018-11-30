package com.atox.navegacao.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.atox.infra.AtoxException;
import com.atox.usuario.dominio.Pessoa;
import com.atox.usuario.dominio.SessaoUsuario;
import com.atox.usuario.dominio.Usuario;
import com.atox.usuario.gui.LoginActivity;
import com.atox.usuario.negocio.PessoaNegocio;
import com.atox.usuario.negocio.UsuarioNegocio;

import java.util.concurrent.ExecutionException;

public class SplashActivity extends AppCompatActivity {

    private SessaoUsuario sessaoUsuario;
    private UsuarioNegocio usuarioNegocio;
    private Usuario usuario;
    private Pessoa pessoa;
    private String TAG = SplashActivity.class.getName();
    private PessoaNegocio pessoaNegocio;

    //Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessaoUsuario = SessaoUsuario.getSessao();
        usuarioNegocio = ViewModelProviders.of(this).get(UsuarioNegocio.class);
        pessoaNegocio = ViewModelProviders.of(this).get(PessoaNegocio.class);

        try {
            usuario = usuarioNegocio.restaurarSessao();
            if(usuario == null){
                throw new AtoxException ("Esse usuário não existe");
            }
            pessoa = pessoaNegocio.buscarPorIdDeUsuario(usuario.getUid());
            if(pessoa == null){
                throw new AtoxException ("O usuário não está associado com nenhuma pessoa");
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (AtoxException e) {
            e.printStackTrace();
        }

        if(usuario != null) {
            Log.i(TAG, "Entrou porque usuário havia logado anteriormente");
            sessaoUsuario.setUsuarioLogado(usuario);
            sessaoUsuario.setPessoaLogada(pessoa);
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
