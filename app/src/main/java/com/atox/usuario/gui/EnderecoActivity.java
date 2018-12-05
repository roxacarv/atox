package com.atox.usuario.gui;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
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
import java.util.concurrent.ExecutionException;

public class EnderecoActivity extends AppCompatActivity implements OnQueryCompleteListener {

    private static final String TAG = EnderecoActivity.class.getName();

    private Pessoa pessoa;
    private Usuario usuario;
    private PessoaNegocio pessoaNegocio;
    private SessaoUsuario sessaoUsuario;
    private Bundle dadosPessoais;
    private Button buttonRegistrar;
    private AutoCompleteTextView editTextAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endereco);
        buttonRegistrar = (Button) findViewById(R.id.buttonRegistrar);
        editTextAddress =  (AutoCompleteTextView) findViewById(R.id.editTextAddress);
        dadosPessoais = getIntent().getExtras();
        pessoaNegocio = new PessoaNegocio(this);

        try {
            pessoa = pessoaNegocio.recuperarPessoaPorId(dadosPessoais.getLong("ID_USUARIO"));
        } catch (ExecutionException e) {
            alert("Ocorreu um erro na hora de realizar o cadastro");
            irParaTelaDeLogin();
        } catch (InterruptedException e) {
            alert("O programa parou de funcionar inesperadamente");
            irParaTelaDeLogin();
        }

    }

    public void backToRegistroScreen(View view){

        Intent registerScreen = new Intent(EnderecoActivity.this, RegistroActivity.class);
        startActivity(registerScreen);


    }

    private Pessoa registraNoBancoDeDados() throws ExecutionException, InterruptedException {
        //Long idDePessoa = pessoa.getPid();
        //Log.i(TAG, "Id de pessoa do banco: " + idDePessoa);
        //Endereco endereco = criarEndereco(idDePessoa);
        //Long idDeEndereco = pessoaNegocio.registrarEndereco(endereco);
        //pessoa.setEndereco(endereco);
        return pessoa;
    }


    private Endereco criarEndereco(Long idDePessoa) {
        Address testLocation = getAddress(editTextAddress.getText().toString());
        Endereco endereco = new Endereco();
        endereco.setPessoaId(idDePessoa);
        endereco.setCidade(testLocation.getSubAdminArea());
        endereco.setBairro(testLocation.getSubLocality());
        endereco.setCep(testLocation.getPostalCode());
        endereco.setEstado(testLocation.getAdminArea());
        endereco.setPais(testLocation.getCountryName());
        endereco.setLogradouro(testLocation.getThoroughfare());
        return endereco;
    }

    public Address getAddress(String completeAddress)
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

    public void finalizarRegistro(View view) throws ExecutionException, InterruptedException {
        Pessoa pessoa = registraNoBancoDeDados();
        Toast.makeText(this,R.string.success_register,Toast.LENGTH_LONG).show();
        Intent loginScreen = new Intent(EnderecoActivity.this, LoginActivity.class);
        startActivity(loginScreen);
    }

    public void irParaTelaDeLogin() {
        Intent loginScreen = new Intent(EnderecoActivity.this, LoginActivity.class);
        startActivity(loginScreen);
    }
}
