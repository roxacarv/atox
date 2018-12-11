package com.atox.navegacao.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.atox.R;
import com.atox.usuario.dominio.Endereco;
import com.atox.usuario.dominio.Pessoa;
import com.atox.usuario.dominio.SessaoUsuario;
import com.atox.usuario.dominio.Usuario;
import com.atox.usuario.gui.LoginActivity;
import com.atox.usuario.negocio.SessaoNegocio;

import java.text.SimpleDateFormat;

public class PerfilFragment extends Fragment {

    private static final String TAG = InicioFragment.class.getName();
    private SessaoNegocio sessaoNegocio;
    private Intent homeScreen;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        sessaoNegocio = new SessaoNegocio(this.getActivity());
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        SessaoUsuario sessaoUsuario = SessaoUsuario.getSessao();
        Pessoa pessoaPerfil = sessaoUsuario.getPessoaLogada();
        Usuario usuarioPerfil = pessoaPerfil.getUsuario();
        Endereco enderecoPerfil = pessoaPerfil.getEndereco();

        TextView textViewPerfilNomeUsuario = view.findViewById(R.id.textViewPerfilNomeUsuario);
        TextView textViewPerfilDataNascimento = view.findViewById(R.id.textViewPerfilDataNascimento);
        TextView textViewPerfilEndereco = view.findViewById(R.id.textViewPerfilEndereco);
        TextView textViewPerfilEmail = view.findViewById(R.id.textViewPerfilEmail);
        TextView textViewPerfilTelefone = view.findViewById(R.id.textViewPerfilTelefone);


        String nome = pessoaPerfil.getNome();
        String telefone = pessoaPerfil.getTelefone();
        String email = usuarioPerfil.getEmail();

        textViewPerfilNomeUsuario.setText(nome);
        textViewPerfilEmail.setText(email);
        textViewPerfilTelefone.setText(telefone);

        homeScreen = new Intent(this.getActivity(), LoginActivity.class);
        Button btnSair = view.findViewById(R.id.btnSairDoApp);
        btnSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessaoNegocio.encerrarSessao();
                startActivity(homeScreen);
            }
        });


        if(sessaoUsuario.getPessoaLogada().getEndereco() != null) {
            String bairro = enderecoPerfil.getBairro();
            String cidade = enderecoPerfil.getCidade();
            String estado = enderecoPerfil.getEstado();
            String endereco = bairro + ", " + cidade + " - " + estado;
            textViewPerfilEndereco.setText(endereco);
        }

        String dataFinal = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy").format(pessoaPerfil.getDataNascimento());
        textViewPerfilDataNascimento.setText(dataFinal);
        // Inflate the layout for this fragment
        return view;

    }


}
