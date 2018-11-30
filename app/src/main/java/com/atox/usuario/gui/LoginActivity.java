package com.atox.usuario.gui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.atox.R;
import com.atox.infra.AtoxException;
import com.atox.navegacao.activities.MenuActivity;
import com.atox.usuario.dominio.Pessoa;
import com.atox.usuario.dominio.SessaoUsuario;
import com.atox.usuario.negocio.PessoaNegocio;
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
    private PessoaNegocio pessoaNegocio;
    private SessaoUsuario sessaoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmailView = (EditText) findViewById(R.id.editTextEmail);
        mPasswordView = (EditText) findViewById(R.id.editTextSenha);
        usuarioNegocio = ViewModelProviders.of(this).get(UsuarioNegocio.class);
        pessoaNegocio = ViewModelProviders.of(this).get(PessoaNegocio.class);
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
        Pessoa pessoa = null;

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        try {
            validarCamposLoginNaGui(email,password);
            String senhaCriptografada = Criptografia.encryptPassword(password);
            usuario = usuarioNegocio.buscarUsuarioPorEmail(email, senhaCriptografada);
            if(usuario == null) {
                throw new AtoxException("Esse usuário não existe");
            }
            pessoa = pessoaNegocio.buscarPorIdDeUsuario(usuario.getUid());
            if(pessoa == null) {
                throw new AtoxException("O usuário não está associado com nenhuma pessoa");
            }
            Log.i(TAG, "Pessoa existe");
            alert(getString(R.string.act_successful_login));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (AtoxException e) {
            alert(e.getMessage());
        }

        if(usuario != null) {
            sessaoUsuario = SessaoUsuario.getSessao();
            sessaoUsuario.setUsuarioLogado(usuario);
            sessaoUsuario.setPessoaLogada(pessoa);
            usuarioNegocio.salvarSessao(sessaoUsuario);
            goToHomeScreen(view);
        }

    }


    private void validarCamposLoginNaGui(String email, String password) throws AtoxException, NoSuchAlgorithmException, ExecutionException, InterruptedException {

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
            focusView.requestFocus();
            throw new AtoxException(getString(R.string.error_email_or_pass_invalid));
        }

    }

    private void alert(String s){
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }



}
