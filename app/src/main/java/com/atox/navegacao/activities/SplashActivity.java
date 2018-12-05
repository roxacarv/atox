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
import com.atox.usuario.negocio.SessaoNegocio;
import com.atox.usuario.persistencia.dao.PessoaDao;

import java.util.concurrent.ExecutionException;

public class SplashActivity extends AppCompatActivity {

    private SessaoUsuario sessaoUsuario;
    private Usuario usuario;
    private Pessoa pessoa;
    private String TAG = SplashActivity.class.getName();
    private PessoaDao pessoaDao;
    private SessaoNegocio sessaoNegocio;

    //Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessaoUsuario = SessaoUsuario.getSessao();
        pessoaDao = ViewModelProviders.of(this).get(PessoaDao.class);
        sessaoNegocio = new SessaoNegocio(this);

        try {
            if(usuario == null){
                throw new AtoxException ("Esse usuário não existe");
            }
            pessoa = pessoaDao.buscarPorIdDeUsuario(usuario.getUid());
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

        //TODO verifica se já existe usuário logado.
        Pessoa pessoaJaLogada = sessaoNegocio.obterPessoaLogada();
        if (pessoaJaLogada != null){
            goToHomeScreen();
        } else{
            goToLoginScreen();
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
