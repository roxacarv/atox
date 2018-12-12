package com.atox.usuario.gui;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.atox.R;
import com.atox.usuario.dominio.Endereco;
import com.atox.usuario.dominio.Pessoa;
import com.atox.usuario.dominio.SessaoUsuario;
import com.atox.usuario.dominio.Usuario;
import com.google.android.gms.location.places.Place;
import com.shishank.autocompletelocationview.interfaces.OnQueryCompleteListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class EditarPerfilActivity extends AppCompatActivity implements OnQueryCompleteListener {
    private SimpleDateFormat sdf;
    private EditText mNome;
    private EditText mTelefone;
    private EditText mData;
    private EditText mEmail;
    private EditText mSenha;
    private EditText mSenhaConfirm;
    private AutoCompleteTextView editTextAddress;
    private SessaoUsuario sessaoUsuario;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);
        inicializarCampos();

        Pessoa pessoaLogada = sessaoUsuario.getPessoaLogada();
        mNome.setText(pessoaLogada.getNome());
        mTelefone.setText(pessoaLogada.getTelefone());
        String dataNascimentoFormatada = sdf.format(pessoaLogada.getDataNascimento());
        mData.setText(dataNascimentoFormatada);

        Usuario usuarioLogado = pessoaLogada.getUsuario();
        mEmail.setText(usuarioLogado.getEmail());


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

    public void inicializarCampos(){
        sdf = new SimpleDateFormat("dd/MM/yyyy");
        mNome = (EditText)findViewById(R.id.editTextEditarNome);
        mTelefone = (EditText)findViewById(R.id.editTextEditarTelefone);
        mData = (EditText)findViewById(R.id.editTextEditarDataNascimento);
        mEmail = (EditText)findViewById(R.id.editTextEditarEmail);
        mSenha = (EditText)findViewById(R.id.editTextEditarSenha);
        mSenhaConfirm = (EditText)findViewById(R.id.editTextEditarConfirmacaoSenha);
        editTextAddress =  (AutoCompleteTextView) findViewById(R.id.editTextEditarEndereco);
        sessaoUsuario = SessaoUsuario.getSessao();
    }

    @Override
    public void onTextClear() {

    }

    @Override
    public void onPlaceSelected(Place place) {

    }

}
