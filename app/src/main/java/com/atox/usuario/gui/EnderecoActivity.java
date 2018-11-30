package com.atox.usuario.gui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.atox.usuario.negocio.UsuarioNegocio;
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

    private Pessoa registraNoBancoDeDados() throws ExecutionException, InterruptedException {
        usuarioNegocio = ViewModelProviders.of(this).get(UsuarioNegocio.class);
        pessoaNegocio = ViewModelProviders.of(this).get(PessoaNegocio.class);
        Usuario usuario = criarUsuario();
        Long idDeUsuario = usuarioNegocio.inserirUsuario(usuario);
        Log.i(TAG, "Id de usuário do banco: " + idDeUsuario);
        Pessoa pessoa = criarPessoa(idDeUsuario);
        Long idDePessoa = pessoaNegocio.inserirPessoa(pessoa);
        Log.i(TAG, "Id de pessoa do banco: " + idDePessoa);
        //Endereco endereco = criarEndereco(idDePessoa);
        //Long idDeEndereco = usuarioNegocio.inserirEndereco(endereco);
        //pessoa.setEndereco(endereco);
        return pessoa;
    }

    private Pessoa criarPessoa(Long idDoUsuario) {
        Pessoa pessoa = new Pessoa();
        pessoa.setTelefone(dadosPessoais.getString("TELEFONE"));
        pessoa.setNome(dadosPessoais.getString("NOME"));
        pessoa.setUsuarioId(idDoUsuario);
        DateFormat dataFormatada = new SimpleDateFormat("dd/MM/yyyy");
        Date data;
        try {
            data = dataFormatada.parse(dadosPessoais.getString("DATA_NASCIMENTO"));
            pessoa.setDataNascimento(data);
        } catch (ParseException pe) {
            System.out.println("ERRO_NA_DATA_NASCIMENTO: " + pe);
        }
        return pessoa;
    }

    private Usuario criarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setSenha(dadosPessoais.getString("SENHA"));
        usuario.setEmail(dadosPessoais.getString("EMAIL"));
        return usuario;
    }

    private Endereco criarEndereco(Long idDeUsuario) {
        Address testLocation = getAddress(editTextAddress.getText().toString());
        Endereco endereco = new Endereco();
        endereco.setUsuarioId(idDeUsuario);
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



    public void finalizarRegistro(View view) throws ExecutionException, InterruptedException {
        Pessoa pessoa = registraNoBancoDeDados();
        Log.i(TAG, "Id apos insercao final : " + pessoa.getUsuarioId());
        Toast.makeText(this,R.string.success_register,Toast.LENGTH_LONG).show();
        Intent loginScreen = new Intent(EnderecoActivity.this, LoginActivity.class);
        startActivity(loginScreen);
    }
}
