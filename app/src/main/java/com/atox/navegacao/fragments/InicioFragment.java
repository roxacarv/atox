package com.atox.navegacao.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.atox.R;
import com.atox.network.dominio.Feirinha;
import com.atox.network.dominio.Produtor;
import com.atox.network.gui.FeirinhaCustomAdapter;
import com.atox.network.gui.ProdutorCustomAdapter;
import com.atox.network.infra.ObtemServicoDados;
import com.atox.network.infra.RetrofitInstanciaCliente;
import com.atox.usuario.dominio.Pessoa;
import com.atox.usuario.dominio.SessaoUsuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InicioFragment extends Fragment {

    private static final String TAG = InicioFragment.class.getName();
    private SessaoUsuario sessaoUsuario;
    private TextView textViewNomeUsuario;
    // configurando a recycle view pra feirinhas
    private FeirinhaCustomAdapter fAdapter;
    private RecyclerView customRecycleViewFeirinha;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inicio, container, false);
        sessaoUsuario = SessaoUsuario.getInstance();
        textViewNomeUsuario = (TextView) view.findViewById(R.id.textViewMsgBoasVindas);
        customRecycleViewFeirinha = (RecyclerView) view.findViewById(R.id.customRecyclerViewFeirinha);
        Pessoa pessoaLogada = sessaoUsuario.getPessoaLogada();
        if (pessoaLogada != null){
            textViewNomeUsuario.setText(view.getContext().getResources().getString(R.string.texto_bemvindo) +
                    " " +
                    pessoaLogada.getNome());
        }
        /*Create handle for the RetrofitInstance interface*/
        ObtemServicoDados service = RetrofitInstanciaCliente.getRetrofitInstance().create(ObtemServicoDados.class);
        Call<List<Feirinha>> call = service.getAllFeirinhas();
        call.enqueue(new Callback<List<Feirinha>>() {
            @Override
            public void onResponse(Call<List<Feirinha>> call, Response<List<Feirinha>> response) {
                generateDataList(response.body(),customRecycleViewFeirinha, getContext(), fAdapter);
            }
            @Override
            public void onFailure(Call<List<Feirinha>> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    /*Method to generate List of data using RecyclerView with custom adapter*/
    private void generateDataList(List<Feirinha> listaFeirinhas, RecyclerView recycleViewProdutor, Context context, FeirinhaCustomAdapter fAdapter) {
        customRecycleViewFeirinha = recycleViewProdutor;
        fAdapter = new FeirinhaCustomAdapter(context, listaFeirinhas);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        customRecycleViewFeirinha.setLayoutManager(layoutManager);
        customRecycleViewFeirinha.setAdapter(fAdapter);
    }




}
