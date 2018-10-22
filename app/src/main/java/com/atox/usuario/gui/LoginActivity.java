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
import com.atox.usuario.dominio.Sessao;
import com.atox.usuario.negocio.UsuarioNegocio;
import com.atox.usuario.dominio.Endereco;
import com.atox.usuario.dominio.Usuario;
import com.atox.utils.Encryption;
import com.atox.utils.ValidaCadastro;

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
        try {
            attemptLogin();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    private void attemptLogin() throws NoSuchAlgorithmException, ExecutionException, InterruptedException {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        ValidaCadastro validaCadastro = new ValidaCadastro();
        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        usuarioNegocio = ViewModelProviders.of(this).get(UsuarioNegocio.class);
        Usuario novo_usuario = new Usuario();
        novo_usuario.setEmail("xavier@gmail.com");
        novo_usuario.setLogin("roxac");
        novo_usuario.setCpf("00000000000");
        novo_usuario.setSenha("12345");
        Endereco novo_endereco = new Endereco();
        novo_endereco.setBairro("Santana");
        novo_endereco.setCidade("Recife");
        novo_usuario.setEndereco(novo_endereco);
        long idDeRetorno = usuarioNegocio.inserirUsuario(novo_usuario);

        usuarioNegocio.buscarUsuarioPorCpf("00000000000").observe(LoginActivity.this, new Observer<Usuario>() {
            @Override
            public void onChanged(@Nullable Usuario usuario) {
                getData(usuario);
                sessao = Sessao.getSessao();
                sessao.setUsuario(usuario);
                Log.i(TAG, "usuario: " + usuario.getEmail() + " id do objeto: " + usuario.getUid() + "endereco: " + usuario.getEndereco().getBairro());
            }
        });

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
            String realPassword = Encryption.encryptPassword(password);
            alert("login bem sucedido");
        }
    }

    public void getData(Usuario usuario) {
        Log.i(TAG, "Nome: " + usuario.getEmail());
    }


    private void alert(String s){
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }

}
