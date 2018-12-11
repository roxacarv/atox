package com.atox.navegacao.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.atox.R;
import com.atox.usuario.dominio.Pessoa;
import com.atox.usuario.dominio.SessaoUsuario;

public class InicioFragment extends Fragment {

    private static final String TAG = InicioFragment.class.getName();

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_inicio, container, false);
        SessaoUsuario sessaoUsuario = SessaoUsuario.getSessao();

        TextView textViewNomeUsuario = view.findViewById(R.id.textViewMsgBoasVindas);

        Pessoa pessoaLogada = sessaoUsuario.getPessoaLogada();

        if (pessoaLogada != null){
            textViewNomeUsuario.setText(view.getContext().getResources().getString(R.string.texto_bemvindo) +
                    " " + pessoaLogada.getNome());
        }


        // Inflate the layout for this fragment
        return view;
    }


}
