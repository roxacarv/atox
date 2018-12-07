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
import com.atox.usuario.negocio.SessaoNegocio;

import java.text.SimpleDateFormat;

public class PerfilFragment extends Fragment {

    private static final String TAG = InicioFragment.class.getName();
    private SessaoUsuario sessaoUsuario;
    private TextView textViewPerfilNomeUsuario;
    private TextView textViewPerfilDataNascimento;
    private TextView textViewPerfilEndereco;
    private TextView textViewPerfilEmail;
    private String dataFinal;
    private SessaoNegocio sessaoNegocio;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        sessaoNegocio = new SessaoNegocio(this.getActivity());
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        sessaoUsuario = SessaoUsuario.getSessao();

        textViewPerfilNomeUsuario = (TextView) view.findViewById(R.id.textViewPerfilNomeUsuario);
        textViewPerfilDataNascimento = (TextView) view.findViewById(R.id.textViewPerfilDataNascimento);
        textViewPerfilEndereco = (TextView) view.findViewById(R.id.textViewPerfilEndereco);
        textViewPerfilEmail = (TextView) view.findViewById(R.id.textViewPerfilEmail);

        textViewPerfilNomeUsuario.setText(sessaoUsuario.getPessoaLogada().getNome());
        textViewPerfilEmail.setText(sessaoUsuario.getUsuarioLogado().getEmail());

        if(sessaoUsuario.getPessoaLogada().getEndereco() != null) {
            String bairro = sessaoUsuario.getPessoaLogada().getEndereco().getBairro();
            String cidade = sessaoUsuario.getPessoaLogada().getEndereco().getCidade();
            String estado = sessaoUsuario.getPessoaLogada().getEndereco().getEstado();
            String endereco = bairro + ", " + cidade + " - " + estado;
            textViewPerfilEndereco.setText(endereco);
        }

        dataFinal = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy").format(sessaoUsuario.getPessoaLogada().getDataNascimento());
        textViewPerfilDataNascimento.setText(dataFinal);
        // Inflate the layout for this fragment
        return view;

    }

    public void encerrarSessao() {
        sessaoNegocio.encerrarSessao();
        //mudar de tela
    }


}
