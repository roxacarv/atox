package com.atox.http.gui.fragments;
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
import com.atox.http.dominio.Produtor;
import com.atox.http.gui.adapter.ProdutorCustomAdapter;
import com.atox.http.infra.RetrofitInstanciaCliente;
import com.atox.http.infra.ObtemServicoDados;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NotificationsFragment extends Fragment{
    private ProdutorCustomAdapter pAdapter;
    private RecyclerView recyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_produtores, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.customRecyclerViewProdutor);

        /*Create handle for the RetrofitInstance interface*/
        ObtemServicoDados service = RetrofitInstanciaCliente.getRetrofitInstance().create(ObtemServicoDados.class);

        Call<List<Produtor>> call = service.getAllProdutores();
        call.enqueue(new Callback<List<Produtor>>() {

            @Override
            public void onResponse(Call<List<Produtor>> call, Response<List<Produtor>> response) {
                generateDataList(response.body(),recyclerView, getContext(), pAdapter);
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
    private void generateDataList(List<Produtor> photoList, RecyclerView rc, Context context, ProdutorCustomAdapter pAdapter) {
        recyclerView = rc;
        pAdapter = new ProdutorCustomAdapter(context,photoList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(pAdapter);
    }
}
