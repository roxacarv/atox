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

    }

    public void goToRegisterScreen(View view){

        Intent registerScreen = new Intent(LoginActivity.this, RegistroActivity.class);
        startActivity(registerScreen);

    }
    public void logar(View view) throws ExecutionException, InterruptedException {

        // apenas colocando um bypass para testar a entrada no app! pode remover dps!
        Intent homeScrenn = new Intent(LoginActivity.this, MenuActivity.class);
        startActivity(homeScrenn);

        /*try {
            validarCamposLogin();


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }*/
    }


    private void validarCamposLogin() throws NoSuchAlgorithmException, ExecutionException, InterruptedException {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        ValidaCadastro validaCadastro = new ValidaCadastro();
        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (validaCadastro.isCampoVazio(password) || validaCadastro.isSenhaValida(password) ) {
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
            alert("Login ou senha inválida");
        } else {
            // chamar classe para validar o login(criptografado) no DB
            String realPassword = Criptografia.encryptPassword(password);
            alert("login bem sucedido");
        }

        usuarioNegocio = ViewModelProviders.of(this).get(UsuarioNegocio.class);
        usuarioNegocio.buscarUsuarioPorId(1).observe(this, new Observer<Usuario>() {
            @Override
            public void onChanged(@Nullable Usuario usuario) {

            }
        });

    }


    public void getData(Usuario usuario) {
        Log.i(TAG, "Nome: " + usuario.getEmail());
    }


    private void alert(String s){
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }

}
