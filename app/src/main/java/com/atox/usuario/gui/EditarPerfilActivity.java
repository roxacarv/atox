package com.atox.usuario.gui;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.AutoCompleteTextView;

import com.atox.R;
import com.atox.usuario.dominio.Endereco;
import com.google.android.gms.location.places.Place;
import com.shishank.autocompletelocationview.interfaces.OnQueryCompleteListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class EditarPerfilActivity extends AppCompatActivity implements OnQueryCompleteListener {

    private AutoCompleteTextView editTextAddress;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);
        editTextAddress =  (AutoCompleteTextView) findViewById(R.id.editTextEditarEndereco);
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

}
