package com.atox.navegacao.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.atox.R;
import com.atox.usuario.dominio.SessaoUsuario;

import java.text.SimpleDateFormat;

public class PerfilFragment extends Fragment {

    private static final String TAG = InicioFragment.class.getName();
    private SessaoUsuario sessaoUsuario;
    private TextView textViewPerfilNomeUsuario;
    private TextView textViewPerfilDataNascimento;
    private TextView textViewPerfilEndereco;
    private TextView textViewPerfilEmail;
    private TextView textViewPerfilTelefone;
    private String dataFinal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        sessaoUsuario = SessaoUsuario.getSessao();

        textViewPerfilNomeUsuario = (TextView) view.findViewById(R.id.textViewPerfilNomeUsuario);
        textViewPerfilDataNascimento = (TextView) view.findViewById(R.id.textViewPerfilDataNascimento);
        textViewPerfilEndereco = (TextView) view.findViewById(R.id.textViewPerfilEndereco);
        textViewPerfilEmail = (TextView) view.findViewById(R.id.textViewPerfilEmail);
        textViewPerfilTelefone = (TextView) view.findViewById(R.id.textViewPerfilTelefone);

        textViewPerfilNomeUsuario.setText(sessaoUsuario.getPessoaLogada().getNome());
        textViewPerfilEmail.setText(sessaoUsuario.getUsuarioLogado().getEmail());
        textViewPerfilTelefone.setText(sessaoUsuario.getPessoaLogada().getTelefone());


        dataFinal = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy").format(sessaoUsuario.getPessoaLogada().getDataNascimento());
        textViewPerfilDataNascimento.setText(dataFinal);
        // Inflate the layout for this fragment
        return view;


    }


}
