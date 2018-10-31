package com.atox.usuario.gui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.atox.R;
import com.atox.infra.Mascara;
import com.atox.utils.Criptografia;
import com.atox.utils.ValidaCadastro;

import java.security.NoSuchAlgorithmException;

public class RegistroActivity extends AppCompatActivity {
    private EditText mNome, mTelefone, mData, mEmail, mSenha, mSenhaConfirm;
    private boolean valido = true;
    private Intent registerScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        mNome = findViewById(R.id.editTextRegistroNome);
        mTelefone = findViewById(R.id.editTextRegistroTelefone);
        mTelefone.addTextChangedListener(Mascara.insert("(##)#####-####",mTelefone));
        mData = findViewById(R.id.editTextDataNascimento);
        mData.addTextChangedListener(Mascara.insert("##/##/####", mData));
        mEmail = findViewById(R.id.editTextRegistroEmail);
        mSenha = findViewById(R.id.editTextRegistroSenha);
        mSenhaConfirm = findViewById(R.id.editTextConfirmeSenha);
    }

    public boolean validarRegistro() throws NoSuchAlgorithmException {
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
        String dataNascimento         = mData.getText().toString();
        String email            = mEmail.getText().toString();
        View focusView = null;

        ValidaCadastro validaCadastro = new ValidaCadastro();


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

        else if(!validaCadastro.isDataNascimento(dataNascimento)){
            mData.requestFocus();
            mData.setError(getString(R.string.error_invalid_date));
            valido = false;
        }

        else if(!validaCadastro.isEmail(email)){
            mEmail.requestFocus();
            mEmail.setError(getString(R.string.error_invalid_email));
            valido = false;
        }

        else if(!validaCadastro.isSenhaValida(senha)){
            mSenha.requestFocus();
            mSenha.setError(getString(R.string.error_invalid_password));
            valido = false;
        }

        else if(!validaCadastro.isSenhaValida(confirmSenha)){
            mSenhaConfirm.requestFocus();
            mSenhaConfirm.setError(getString(R.string.error_invalid_password));
            valido = false;
        }

        else if(!(senha.equals(confirmSenha))){
            mSenhaConfirm.requestFocus();
            mSenhaConfirm.setError(getString(R.string.error_invalid_password));
            valido = false;
        }
        else{
            valido = true;
        }


        if(valido){
            alert("Registro bem sucedido");
            String realSenha = Criptografia.encryptPassword(senha);

            registerScreen.putExtra("TELEFONE", telefone);
            registerScreen.putExtra("SENHA", realSenha);
            registerScreen.putExtra("NOME", nome);
            registerScreen.putExtra("DATA_NASCIMENTO", dataNascimento);
            registerScreen.putExtra("EMAIL", email);
        } else {
            alert("Preencha os campos corretamente");

        }
        return valido;
    }


    private void alert(String s){
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }



    public void backToLoginScreen(View view){

        registerScreen = new Intent(RegistroActivity.this, LoginActivity.class);
        startActivity(registerScreen);

    }

    public void goToEnderecoScreen(View view){

        try {
            if(validarRegistro()){
                registerScreen = new Intent(RegistroActivity.this, EnderecoActivity.class);
                startActivity(registerScreen);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

}
