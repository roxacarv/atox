package com.atox.navegacao.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.atox.R;
import com.atox.network.gui.ProdutorCustomAdapter;
import com.atox.network.infra.ObtemServicoDados;
import com.atox.network.infra.RetrofitInstanciaCliente;
import com.atox.network.dominio.Produtor;
import com.atox.network.negocio.ProdutorNegocio;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProdutoresFragment extends Fragment {


    private RecyclerView customRecyclerViewProdutor;
    private ProdutorNegocio produtorNegocio;
    private Button btnAtualizar;
    private TextView textView6;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_produtores, container, false);
        inicializaVariaveis(view);
        List<Produtor> listaDeProdutores = produtorNegocio.buscarTodosProdutores();
        if(listaDeProdutores == null) {
            textView6.setText(R.string.precisa_pressionar_btnAtualizar);
        } else {
            generateDataList(listaDeProdutores, customRecyclerViewProdutor, getContext());
        }
        return view;
    }

    public void fazRequisicaoApi(){
        ObtemServicoDados service = RetrofitInstanciaCliente.getRetrofitInstance().create(ObtemServicoDados.class);
        Call<List<Produtor>> call = service.getAllProdutores();
        call.enqueue(new Callback<List<Produtor>>() {
            @Override
            public void onResponse(Call<List<Produtor>> call, Response<List<Produtor>> response) {
                generateDataList(response.body(), customRecyclerViewProdutor, getContext());
            }

            @Override
            public void onFailure(Call<List<Produtor>> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void generateDataList(List<Produtor> photoList, RecyclerView recycleViewProdutor, Context context) {
        textView6.setText(R.string.resumo_produtores);
        List<Long> idDeProdutores = produtorNegocio.inserirProdutores(photoList);
        customRecyclerViewProdutor = recycleViewProdutor;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        customRecyclerViewProdutor.setLayoutManager(layoutManager);
        customRecyclerViewProdutor.setAdapter(new ProdutorCustomAdapter(context,photoList));
    }

    public void inicializaVariaveis(View view){
        customRecyclerViewProdutor =  view.findViewById(R.id.customRecyclerViewProdutor);
        produtorNegocio = new ProdutorNegocio(this.getActivity());
        textView6 = view.findViewById(R.id.textView6);
        btnAtualizar = view.findViewById(R.id.btnAtualizarProdutores);
        btnAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fazRequisicaoApi();
            }
        });
    }
}
