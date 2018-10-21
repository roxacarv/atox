package com.atox.usuario.gui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.atox.R;
import com.atox.infra.Mascara;
import com.atox.utils.Encryption;
import com.atox.utils.ValidaCadastro;

import java.security.NoSuchAlgorithmException;

public class RegistroActivity extends AppCompatActivity {
    private EditText mNome, mTelefone, mData, mEmail, mSenha, mSenhaConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        mNome = findViewById(R.id.editTextNome);
        mTelefone = findViewById(R.id.editTextTelefone);
        mData = findViewById(R.id.editTextDataNascimento);
        mData.addTextChangedListener(Mascara.insert("##/##/####", mData));
        mEmail = findViewById(R.id.editTextEmail);
        mSenha = findViewById(R.id.editTextSenha);
        mSenhaConfirm = findViewById(R.id.editTextConfirmeSenha);
    }

    private void validarRegistro() throws NoSuchAlgorithmException {
        mEmail.setError(null);
        mTelefone.setError(null);
        mSenha.setError(null);
        mSenhaConfirm.setError(null);
        mNome.setError(null);
        mData.setError(null);

        String telefone         = mTelefone.getText().toString();
        String senha            = mSenha.getText().toString();
        String confirmSenha     = mSenhaConfirm.getText().toString();
        String nome             = mNome.getText().toString();
        String dataNasc         = mData.getText().toString();
        String email            = mEmail.getText().toString();
        View focusView = null;

        ValidaCadastro validaCadastro = new ValidaCadastro();
        boolean valido = true;

        if(validaCadastro.isCampoVazio(nome)){
            mNome.requestFocus();
            mNome.setError(getString(R.string.error_invalid_name));
            valido = false;
        }

        if(validaCadastro.isCampoVazio(telefone)){
            mTelefone.requestFocus();
            mTelefone.setError(getString(R.string.error_invalid_tel));
            valido = false;
        }

        if(!validaCadastro.isDataNascimento(dataNasc)){
            mData.requestFocus();
            mData.setError(getString(R.string.error_invalid_date));
            valido = false;
        }

        if(!validaCadastro.isEmail(email)){
            mEmail.requestFocus();
            mEmail.setError(getString(R.string.error_invalid_email));
            valido = false;
        }

        if(!validaCadastro.isSenhaValida(senha)){
            mSenha.requestFocus();
            mSenha.setError(getString(R.string.error_invalid_password));
            valido = false;
        }

        if(!validaCadastro.isSenhaValida(confirmSenha)){
            mSenhaConfirm.requestFocus();
            mSenhaConfirm.setError(getString(R.string.error_invalid_password));
            valido = false;
        }

        if(!(senha.equals(confirmSenha))){
            mSenhaConfirm.requestFocus();
            mSenhaConfirm.setError(getString(R.string.error_invalid_password));
            valido = false;
        }


        if(valido){
            alert("Registro bem sucedido");
            String RealSenha = Encryption.encryptPassword(senha);
        } else {
            alert("Preencha os campos corretamente");
        }

    }
    private void alert(String s){
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }



    public void backToLoginScreen(View view){

        Intent registerScreen = new Intent(RegistroActivity.this, LoginActivity.class);
        startActivity(registerScreen);

    }

    public void goToEnderecoScreen(View view){

        //try {
          //  validarRegistro();
        //} catch (NoSuchAlgorithmException e) {
          //  e.printStackTrace();
        //}

        Intent registerScreen = new Intent(RegistroActivity.this, EnderecoActivity.class);
        startActivity(registerScreen);

    }

}
