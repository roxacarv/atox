package com.atox.usuario.gui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.atox.R;
import com.atox.usuario.dominio.Endereco;
import com.atox.usuario.dominio.Sessao;
import com.atox.usuario.dominio.Usuario;
import com.atox.usuario.negocio.UsuarioNegocio;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.shishank.autocompletelocationview.interfaces.OnQueryCompleteListener;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class EnderecoActivity extends AppCompatActivity implements OnQueryCompleteListener {

    private static final String TAG = EnderecoActivity.class.getName();
    private UsuarioNegocio usuarioNegocio;
    private Sessao sessao;
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

    }

    public void showTestToast(View view){
        Address testLocation = getAddress(editTextAddress.getText().toString());

        Toast.makeText(this,testLocation.getSubAdminArea(),Toast.LENGTH_LONG).show();
        Log.i(TAG, "Pais: " + testLocation.getCountryName());
        Log.i(TAG, "Estado: " + testLocation.getAdminArea());
        Log.i(TAG, "Cidade: " + testLocation.getSubAdminArea());
        Log.i(TAG, "Bairro: " + testLocation.getSubLocality());
        Log.i(TAG, "Rua: " + testLocation.getThoroughfare());
        Log.i(TAG, "CEP: " + testLocation.getPostalCode());




    }
    public void backToRegistroScreen(View view){

        Intent registerScreen = new Intent(EnderecoActivity.this, RegistroActivity.class);
        startActivity(registerScreen);

    }

    private void registraNoBancoDeDados() throws ExecutionException, InterruptedException {
        usuarioNegocio = ViewModelProviders.of(this).get(UsuarioNegocio.class);
        Usuario usuario = criarUsuario();
        Long idDeUsuario = usuarioNegocio.inserirUsuario(usuario);
        Endereco endereco = criarEndereco(idDeUsuario);
        Long idDeEndereco = usuarioNegocio.inserirEndereco(endereco);
    }

    private Usuario criarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setTelefone(dadosPessoais.getString("TELEFONE"));
        usuario.setSenha(dadosPessoais.getString("SENHA"));
        usuario.setNome(dadosPessoais.getString("NOME"));
        DateFormat dataFormatada = new SimpleDateFormat("dd/MM/yyyy");
        Date data;
        try {
            data = dataFormatada.parse(dadosPessoais.getString("DATA_NASCIMENTO"));
            usuario.setDataNascimento(data);
        } catch (ParseException pe) {
            System.out.println("ERRO_NA_DATA_NASCIMENTO: " + pe);
        }
        usuario.setEmail(dadosPessoais.getString("EMAIL"));
        return usuario;
    }

    private Endereco criarEndereco(Long idDeUsuario) {
        Endereco endereco = new Endereco();
        endereco.setUsuarioId(idDeUsuario);
        return endereco;
    }

    @Override
    public void onTextClear() {

    }

    @Override
    public void onPlaceSelected(Place place) {


    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

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


}
