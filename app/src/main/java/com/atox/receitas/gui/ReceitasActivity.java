package com.atox.receitas.gui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.atox.R;
import com.atox.navegacao.fragments.PerfilFragment;
import com.atox.network.dominio.Produtor;
import com.atox.network.gui.ProdutorCustomAdapter;
import com.atox.network.negocio.ProdutorNegocio;
import com.atox.receitas.dominio.Receita;
import com.atox.receitas.negocio.ReceitaNegocio;

import java.util.ArrayList;
import java.util.List;

public class ReceitasActivity extends AppCompatActivity {

    private ReceitaCustomAdapter receitaCustomAdapter;
    private RecyclerView customRecyclerViewReceita;
    private ReceitaNegocio receitaNegocio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receitas);

        receitaNegocio = new ReceitaNegocio(this);

        Receita boloChocolate = new Receita();
        boloChocolate.setNome("Bolo de Chocolate");

        Receita frangoAssado = new Receita();
        frangoAssado.setNome("Frango Assado");

        Receita tortaLimao = new Receita();
        tortaLimao.setNome("Torta de Limão");

        Receita feijaoTropeiro = new Receita();
        feijaoTropeiro.setNome("Feijão Tropeiro");

        ArrayList<Receita> arrayListTesteReceitas = new ArrayList<Receita>();
        arrayListTesteReceitas.add(boloChocolate);
        arrayListTesteReceitas.add(frangoAssado);
        arrayListTesteReceitas.add(tortaLimao);
        arrayListTesteReceitas.add(feijaoTropeiro);


        customRecyclerViewReceita = (RecyclerView) findViewById(R.id.customRecycleViewReceitas);

        // generateDataList(listaDeProdutores, customRecyclerViewProdutor, getContext(), pAdapter);
        gerandoListaDeReceitas(receitaNegocio.buscarTodasReceitas(),customRecyclerViewReceita, this,receitaCustomAdapter);




    }

    /*Method to generate List of data using RecyclerView with custom adapter*/
    private void gerandoListaDeReceitas(List<Receita> listaReceitas, RecyclerView recycleViewReceitas, Context context, ReceitaCustomAdapter receitaCustomAdapter) {
        customRecyclerViewReceita = recycleViewReceitas;
        receitaCustomAdapter = new ReceitaCustomAdapter(context,listaReceitas);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recycleViewReceitas.getContext(),
                ((LinearLayoutManager) layoutManager).getOrientation());
        recycleViewReceitas.addItemDecoration(dividerItemDecoration);
        customRecyclerViewReceita.setLayoutManager(layoutManager);
        customRecyclerViewReceita.setAdapter(receitaCustomAdapter);
    }

}
