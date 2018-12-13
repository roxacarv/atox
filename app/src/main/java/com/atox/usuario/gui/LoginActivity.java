package com.atox.usuario.gui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.atox.R;
import com.atox.atoxlogs.AtoxException;
import com.atox.navegacao.activities.MenuActivity;
import com.atox.usuario.dominio.Pessoa;
import com.atox.usuario.negocio.PessoaNegocio;
import com.atox.usuario.dominio.Usuario;
import com.atox.infra.negocio.ValidaCadastro;
import com.atox.usuario.negocio.SessaoNegocio;

import java.util.concurrent.ExecutionException;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getName();
    private EditText mEmailView;
    private EditText mPasswordView;
    private PessoaNegocio pessoaNegocio;
    private SessaoNegocio sessaoNegocio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmailView = (EditText) findViewById(R.id.editTextEmail);
        mPasswordView = (EditText) findViewById(R.id.editTextSenha);
        pessoaNegocio = new PessoaNegocio(this);
        sessaoNegocio = new SessaoNegocio(this);
    }

    public void goToRegisterScreen(View view) {

        Intent registerScreen = new Intent(LoginActivity.this, RegistroActivity.class);
        startActivity(registerScreen);

    }

    public void goToHomeScreen(View view) {

        Intent homeScrenn = new Intent(LoginActivity.this, MenuActivity.class);
        startActivity(homeScrenn);

    }

    public void logar(View view) throws ExecutionException, InterruptedException, AtoxException {

        Usuario usuario = null;
        Pessoa pessoa = null;

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean camposValidos = validarCamposLoginNaGui(email, password);

        if (camposValidos) {
            usuario = pessoaNegocio.efetuarLogin(email, password);
            if (usuario == null) {
                //usuario não cadastrado ou senha incorreta
                alert(getString(R.string.error_no_such_user_or_pass_incorrect));
            } else {
                pessoa = pessoaNegocio.recuperarPessoaPorId(usuario.getUid());
                if (pessoa == null) {
                    alert("O usuário digitado não existe ou a senha está incorreta.");
                }
                Log.i(TAG, "Pessoa existe.");
                alert(getString(R.string.act_successful_login));
            }
        }

        if(pessoa != null) {
            sessaoNegocio.iniciarNovaSessao(pessoa);
            goToHomeScreen(view);
        }
    }


    private boolean validarCamposLoginNaGui(String email, String password)  {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        ValidaCadastro validaCadastro = new ValidaCadastro();
        // Store values at the time of the login attempt
        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (validaCadastro.isCampoVazio(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }
        else if(!validaCadastro.isSenhaValida(password)){
            mPasswordView.setError(getString(R.string.error_password_too_short));
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
            focusView.requestFocus();
            alert("Email ou senha inválido");
        }
        return !cancel;

    }

    private void alert(String s){
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }



}
