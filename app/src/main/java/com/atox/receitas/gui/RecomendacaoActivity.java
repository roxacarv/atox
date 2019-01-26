package com.atox.receitas.gui;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.atox.R;
import com.atox.infra.negocio.SlopeOne;
import com.atox.receitas.dominio.Receita;
import com.atox.receitas.negocio.ReceitaNegocio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecomendacaoActivity extends AppCompatActivity {

    private ReceitaNegocio receitaNegocio;
    private String[] receitas;
    private Map<String, Map<String, Double>> dados;
    private TextView txtViewRecomendTeste;
    private RecyclerView recycleRecomendacoes;
    private ReceitaCustomAdapter receitaCustomAdapter;
    private  String roleFrango = "Rolê de Frango";
    private  String empanadoFrango = "Empanado de Frango";
    private  String sanduicheCarne = "Sanduíche de Carne";
    private  String tortaPudim = "Torta de Pudim";
    private  String cookieChocolate = "Cookie crocante de chocolate";
    private  String boloTapioca = "Bolo de Tapioca";
    private  String boloTerremoto = "Bolo Terremoto";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recomendacao);
        iniciarVariaveis();
        preparaDadosParaAlgoritmo();

        HashMap<String, Double> usuarioPrincipal = new HashMap<>();
        usuarioPrincipal.put(empanadoFrango, 1.0);
        usuarioPrincipal.put(boloTerremoto, 1.0);
        List<String> nomesReceitasRecomendadas = obtemNomesReceitasRecomendadas(usuarioPrincipal);
        List<Receita> receitasRecomendadas = obtemListaObjetosReceita(nomesReceitasRecomendadas);
        gerandoListaDeReceitas(receitasRecomendadas, recycleRecomendacoes, this,receitaCustomAdapter);
    }

    public void gerandoListaDeReceitas(List<Receita> listaReceitas, RecyclerView recycleViewReceitas, Context context, ReceitaCustomAdapter receitaCustomAdapter){
        recycleRecomendacoes = recycleViewReceitas;
        receitaCustomAdapter = new ReceitaCustomAdapter(context,listaReceitas);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recycleViewReceitas.getContext(),
                ((LinearLayoutManager) layoutManager).getOrientation());
        recycleViewReceitas.addItemDecoration(dividerItemDecoration);
        recycleRecomendacoes.setLayoutManager(layoutManager);
        recycleRecomendacoes.setAdapter(receitaCustomAdapter);
    }

    public List<Receita> obtemListaObjetosReceita(List<String> nomesReceitas){
        List<Receita> listaDeReceitas = new ArrayList<>();
        for (String nomeReceita : nomesReceitas){
            Receita buscaReceita = receitaNegocio.buscarReceitaPorNome(nomeReceita);
            listaDeReceitas.add(buscaReceita);
        }
        return listaDeReceitas;
    }

    public List<String> obtemNomesReceitasRecomendadas(HashMap<String, Double> usuario){
        SlopeOne so = new SlopeOne(dados, receitas);
        Map<String, Double> recomendacao = so.predict(usuario);
        List receitasRecomendadas = new ArrayList<String>();
        for (String chave : recomendacao.keySet()){
            receitasRecomendadas.add(chave);
        }
        return receitasRecomendadas;
    }


    private void preparaDadosParaAlgoritmo() {
        roleFrango = "Rolê de Frango";
        empanadoFrango = "Empanado de Frango";
        sanduicheCarne = "Sanduíche de Carne";
        tortaPudim = "Torta de Pudim";
        cookieChocolate = "Cookie crocante de chocolate";
        boloTapioca = "Bolo de Tapioca";
        boloTerremoto = "Bolo Terremoto";
        receitas = new String[]{roleFrango, empanadoFrango, sanduicheCarne, tortaPudim, cookieChocolate, boloTapioca, boloTerremoto};
        HashMap<String, Double> user1 = new HashMap<>();
        HashMap<String, Double> user2 = new HashMap<>();
        HashMap<String, Double> user3 = new HashMap<>();
        HashMap<String, Double> user4 = new HashMap<>();
        HashMap<String, Double> user5 = new HashMap<>();
        user1.put(empanadoFrango, 1.0);
        user1.put(boloTapioca, 0.0);
        user1.put(sanduicheCarne, 1.0);
        dados.put("Jonas", user1);
        user2.put(roleFrango, 1.0);
        user2.put(boloTapioca, 1.0);
        dados.put("Bob", user2);
        user3.put(sanduicheCarne, 1.0);
        user3.put(cookieChocolate, 0.0);
        dados.put("Maria", user3);
        user4.put(tortaPudim, 0.0);
        user4.put(sanduicheCarne, 1.0);
        user4.put(empanadoFrango, 0.0);
        dados.put("Julia", user4);
        user5.put(tortaPudim, 1.0);
        user5.put(sanduicheCarne, 0.0);
        user5.put(roleFrango, 1.0);
        dados.put("Juvenal", user5);
    }

    private void iniciarVariaveis() {
        receitaNegocio = new ReceitaNegocio(this);
        dados = new HashMap<>();
        txtViewRecomendTeste = (TextView)findViewById(R.id.textViewRecomendTeste);
        recycleRecomendacoes = (RecyclerView)findViewById(R.id.recycleViewRecomendacao);
    }
}
