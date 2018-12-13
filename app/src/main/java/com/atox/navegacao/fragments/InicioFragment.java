package com.atox.navegacao.fragments;

import android.os.Bundle;
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
    private SessaoUsuario sessaoUsuario;
    private TextView textViewNomeUsuario;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_inicio, container, false);
        sessaoUsuario = SessaoUsuario.getInstance();

        textViewNomeUsuario = (TextView) view.findViewById(R.id.textViewMsgBoasVindas);

        Pessoa pessoaLogada = sessaoUsuario.getPessoaLogada();

        if (pessoaLogada != null){
            textViewNomeUsuario.setText(view.getContext().getResources().getString(R.string.texto_bemvindo) +
                    " " +
                    pessoaLogada.getNome());
        }


        // Inflate the layout for this fragment
        return view;
    }


}
