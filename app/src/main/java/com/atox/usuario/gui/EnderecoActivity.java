package com.atox.usuario.gui;

import android.annotation.TargetApi;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.atox.R;
import com.atox.usuario.dominio.Endereco;
import com.atox.usuario.dominio.Pessoa;
import com.atox.usuario.dominio.SessaoUsuario;
import com.atox.usuario.dominio.Usuario;
import com.atox.usuario.negocio.PessoaNegocio;
import com.google.android.gms.location.places.Place;
import com.shishank.autocompletelocationview.interfaces.OnQueryCompleteListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class EnderecoActivity extends AppCompatActivity implements OnQueryCompleteListener {

    private static final String TAG = EnderecoActivity.class.getName();

    private Pessoa pessoa;
    private Usuario usuario;
    private SessaoUsuario sessaoUsuario;
    private AutoCompleteTextView editTextAddress;
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endereco);
        Button buttonRegistrar = findViewById(R.id.buttonRegistrar);
        editTextAddress = findViewById(R.id.editTextAddress);
        Bundle dadosPessoais = getIntent().getExtras();
        PessoaNegocio pessoaNegocio = new PessoaNegocio(this);

        try {
            dadosPessoais = Objects.requireNonNull(dadosPessoais);
            pessoa = pessoaNegocio.recuperarPessoaPorId(dadosPessoais.getLong("ID_USUARIO"));
        } catch (ExecutionException e) {
            alert(getString(com.atox.R.string.erro_na_hora_de_realizar_cadastro));
            irParaTelaDeLogin();
        } catch (InterruptedException e) {
            alert(getString(com.atox.R.string.parou_de_funcionar_inesperadamente));
            irParaTelaDeLogin();
        }

    }

    public void backToRegistroScreen(View view){

        Intent registerScreen = new Intent(EnderecoActivity.this, RegistroActivity.class);
        startActivity(registerScreen);


    }

    private Pessoa registraNoBancoDeDados() {
        //Long idDePessoa = pessoa.getPid();
        //Log.i(TAG, "Id de pessoa do banco: " + idDePessoa);
        //Endereco endereco = criarEndereco(idDePessoa);
        //Long idDeEndereco = pessoaNegocio.registrarEndereco(endereco);
        //pessoa.setEndereco(endereco);
        return pessoa;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private Endereco criarEndereco(Long idDePessoa) {
        Address testLocation = getAddress(editTextAddress.getText().toString());

        Endereco endereco = new Endereco();
        endereco.setPessoaId(idDePessoa);
        testLocation = Objects.requireNonNull(testLocation);
        endereco.setCidade(testLocation.getSubAdminArea());
        endereco.setBairro(testLocation.getSubLocality());
        endereco.setCep(testLocation.getPostalCode());
        endereco.setEstado(testLocation.getAdminArea());
        endereco.setPais(testLocation.getCountryName());
        endereco.setLogradouro(testLocation.getThoroughfare());
        return endereco;
    }

    private Address getAddress(String completeAddress)
    {
        Geocoder geocoder;
        List addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocationName(completeAddress, 1);
            return (Address) addresses.get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void onTextClear() {

    }

    @Override
    public void onPlaceSelected(Place place) {

    }

    private void alert(String s){
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }

    public void finalizarRegistro(View view) {
        Pessoa pessoa = registraNoBancoDeDados();
        Toast.makeText(this,R.string.success_register,Toast.LENGTH_LONG).show();
        Intent loginScreen = new Intent(EnderecoActivity.this, LoginActivity.class);
        startActivity(loginScreen);
    }

    private void irParaTelaDeLogin() {
        Intent loginScreen = new Intent(EnderecoActivity.this, LoginActivity.class);
        startActivity(loginScreen);
    }
}
