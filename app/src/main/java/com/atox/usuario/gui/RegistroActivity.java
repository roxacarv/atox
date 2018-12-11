package com.atox.usuario.gui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.atox.R;
import com.atox.infra.persistencia.Mascara;
import com.atox.infra.negocio.Criptografia;
import com.atox.infra.negocio.ValidaCadastro;
import com.atox.usuario.dominio.Pessoa;
import com.atox.usuario.dominio.Usuario;
import com.atox.usuario.negocio.PessoaNegocio;

import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class RegistroActivity extends AppCompatActivity {
    private EditText mNome;
    private EditText mTelefone;
    private EditText mData;
    private EditText mEmail;
    private EditText mSenha;
    private EditText mSenhaConfirm;
    private Intent registerScreen;
    private PessoaNegocio pessoaNegocio;

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
        pessoaNegocio = new PessoaNegocio(this);
        registerScreen = new Intent(RegistroActivity.this, EnderecoActivity.class);
    }

    private boolean validarRegistro() throws NoSuchAlgorithmException, ExecutionException, InterruptedException {
        boolean valido = true;
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

        else if(validaCadastro.isEmail(email)){
            mEmail.requestFocus();
            mEmail.setError(getString(R.string.error_invalid_email));
            valido = false;
        }

        else if(validaCadastro.isSenhaValida(senha)){
            mSenha.requestFocus();
            mSenha.setError(getString(R.string.error_invalid_password));
            valido = false;
        }

        else if(validaCadastro.isSenhaValida(confirmSenha)){
            mSenhaConfirm.requestFocus();
            mSenhaConfirm.setError(getString(R.string.error_invalid_password));
            valido = false;
        }

        else if(!(senha.equals(confirmSenha))){
            mSenhaConfirm.requestFocus();
            mSenhaConfirm.setError(getString(R.string.error_invalid_password));
            valido = false;
        }


        if(valido){
            String realSenha = Criptografia.encryptPassword(senha);
            Pessoa pessoa = montarPessoa(email, realSenha, nome, telefone, dataNascimento);
            pessoaNegocio.setPessoa(pessoa);
            Long idUsuario = pessoaNegocio.cadastrar();
            if(idUsuario == -1) {
                valido = false;
                alert(getString(R.string.email_already_exists));
            } else{
                alert(getString(R.string.select_address));
                registerScreen.putExtra("ID_USUARIO", idUsuario);
            }

        }
        else {
            alert(getString(com.atox.R.string.preencha_corretamente_os_campos));

        }
        return valido;
    }


    private void alert(String s){
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }



    public void backToLoginScreen(View view){

        Intent loginScreen = new Intent(RegistroActivity.this, LoginActivity.class);
        startActivity(loginScreen);

    }

    public void goToEnderecoScreen(View view){

        try {
            if(validarRegistro()){

                startActivity(registerScreen);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
    private static Pessoa montarPessoa(String email, String senha, String nome, String telefone, String dataNascimento){
        Pessoa pessoa = new Pessoa();
        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setSenha(senha);
        pessoa.setUsuario(usuario);
        pessoa.setNome(nome);
        pessoa.setTelefone(telefone);
        DateFormat dataFormatada = new SimpleDateFormat("dd/MM/yyyy");
        Date data;
        try {
            data = dataFormatada.parse(dataNascimento);
            pessoa.setDataNascimento(data);
        } catch (ParseException pe) {
            System.out.println("ERRO_NA_DATA_NASCIMENTO: " + pe);
        }
        return pessoa;
    }
}

