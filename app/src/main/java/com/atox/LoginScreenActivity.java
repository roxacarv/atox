package com.atox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;


public class LoginScreenActivity extends AppCompatActivity {

    private EditText mEmailView;
    private EditText mPasswordView;
    final int TAMANHO_SENHA = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        mEmailView = (EditText) findViewById(R.id.editTextEmail);
        mPasswordView = (EditText) findViewById(R.id.editTextPassword);
        try {
            attemptLogin();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public void goToRegisterScreen(View view){

        Intent registerScreen = new Intent(LoginScreenActivity.this, RegisterScreenActivity.class);
        startActivity(registerScreen);

    }
    private void attemptLogin() throws NoSuchAlgorithmException {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
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
        }
    }

    private boolean isEmailValid(String email) {
        return (Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    public boolean isCampoVazio(String texto){
        return (texto.trim().isEmpty() || TextUtils.isEmpty(texto));
    }

    private boolean isPasswordValid(String password) {

        return !isCampoVazio(password) && password.length() >= TAMANHO_SENHA;
    }

    private void alert(String s){
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }

}
