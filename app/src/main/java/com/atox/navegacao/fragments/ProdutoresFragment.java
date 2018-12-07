package com.atox.navegacao.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.atox.R;
import com.atox.network.gui.ProdutorCustomAdapter;
import com.atox.network.infra.ObtemServicoDados;
import com.atox.network.infra.RetrofitInstanciaCliente;
import com.atox.network.dominio.Produtor;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProdutoresFragment extends Fragment {

    private ProdutorCustomAdapter pAdapter;
    private RecyclerView customRecyclerViewProdutor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_produtores, container, false);
        customRecyclerViewProdutor = (RecyclerView) view.findViewById(R.id.customRecyclerViewProdutor);

        /*Create handle for the RetrofitInstance interface*/
        ObtemServicoDados service = RetrofitInstanciaCliente.getRetrofitInstance().create(ObtemServicoDados.class);

        Call<List<Produtor>> call = service.getAllProdutores();
        call.enqueue(new Callback<List<Produtor>>() {

            @Override
            public void onResponse(Call<List<Produtor>> call, Response<List<Produtor>> response) {
                generateDataList(response.body(),customRecyclerViewProdutor, getContext(), pAdapter);
                System.out.println("RESPOSTINHA");
                System.out.println(response.body().get(0).getNome());
                System.out.println(response.body().get(8).getAtividades());
            }

            @Override
            public void onFailure(Call<List<Produtor>> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }

    /*Method to generate List of data using RecyclerView with custom adapter*/
    private void generateDataList(List<Produtor> photoList, RecyclerView recycleViewProdutor, Context context, ProdutorCustomAdapter pAdapter) {
        customRecyclerViewProdutor = recycleViewProdutor;
        pAdapter = new ProdutorCustomAdapter(context,photoList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        customRecyclerViewProdutor.setLayoutManager(layoutManager);
        customRecyclerViewProdutor.setAdapter(pAdapter);
    }



}
