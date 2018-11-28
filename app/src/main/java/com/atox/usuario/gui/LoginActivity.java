package com.atox.usuario.gui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.atox.R;
import com.atox.navegacao.MenuActivity;
import com.atox.usuario.dominio.Sessao;
import com.atox.usuario.negocio.UsuarioNegocio;
import com.atox.usuario.dominio.Usuario;
import com.atox.infra.negocio.Criptografia;
import com.atox.infra.negocio.ValidaCadastro;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getName();
    private EditText mEmailView;
    private EditText mPasswordView;
    private UsuarioNegocio usuarioNegocio;
    private Sessao sessao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmailView = (EditText) findViewById(R.id.editTextEmail);
        mPasswordView = (EditText) findViewById(R.id.editTextSenha);
        usuarioNegocio = ViewModelProviders.of(this).get(UsuarioNegocio.class);
    }

    public void goToRegisterScreen(View view){

        Intent registerScreen = new Intent(LoginActivity.this, RegistroActivity.class);
        startActivity(registerScreen);

    }

    public void goToHomeScreen(View view) {

        Intent homeScrenn = new Intent(LoginActivity.this, MenuActivity.class);
        startActivity(homeScrenn);

    }

    public void logar(View view) throws ExecutionException, InterruptedException {

        Usuario usuario = null;

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        try {
            int resposta = validarCamposLoginNaGui(email, password);
            if(resposta == 1) {
                String senhaCriptografa = Criptografia.encryptPassword(password);
                usuario = usuarioNegocio.buscarUsuarioPorEmail(email, senhaCriptografa);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        if(usuario != null) {
            sessao = Sessao.getSessao();
            sessao.setUsuarioId(usuario.getUid());
            sessao.setUsuario(usuario);
            usuarioNegocio.salvarSessao(sessao);
            goToHomeScreen(view);
        } else {
            mEmailView.setError(getString(R.string.error_invalid_email));
            View focusView = mEmailView;
            focusView.requestFocus();
            alert(getString(R.string.error_no_such_user_or_pass_incorrect));
        }

    }


    private int validarCamposLoginNaGui(String email, String password) throws NoSuchAlgorithmException, ExecutionException, InterruptedException {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        ValidaCadastro validaCadastro = new ValidaCadastro();
        // Store values at the time of the login attempt
        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (validaCadastro.isCampoVazio(password) || !validaCadastro.isSenhaValida(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (validaCadastro.isCampoVazio(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!validaCadastro.isEmail(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
            alert(getString(R.string.error_email_or_pass_invalid));
            return -1;
        } else {
            // chamar classe para validar o login(criptografado) no DB
            String realPassword = Criptografia.encryptPassword(password);
            alert(getString(R.string.act_successful_login));
            return 1;
        }

    }

    private void alert(String s){
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }



}
