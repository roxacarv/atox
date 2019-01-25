package com.atox.receitas.gui;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.atox.R;
import com.atox.infra.negocio.SlopeOne;
import com.atox.receitas.dominio.Receita;
import com.atox.receitas.negocio.ReceitaNegocio;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecomendacaoActivity extends AppCompatActivity {

    private static final String TAG = RecomendacaoActivity.class.getName();
    private SlopeOne slopeOne;
    private ReceitaNegocio receitaNegocio;
    private String[] receitas;
    private Map<String, Map<String, Double>> dados;
    private TextView txtViewRecomendTeste;
    private RecyclerView recycleRecomendacoes;
    private ReceitaCustomAdapter receitaCustomAdapter;


    private String role_frango = "Rolê de Frango";
    private String empanado_frango = "Empanado de Frango";
    private String sanduiche_carne = "Sanduíche de Carne";
    private String torta_pudim = "Torta de Pudim";
    private String cookie_chocolate = "Cookie crocante de chocolate";
    private String bolo_tapioca = "Bolo de Tapioca";
    private String bolo_terremoto = "Bolo Terremoto";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recomendacao);
        iniciarVariaveis();

        //método vai inserir os favoritos de terceiros
        preparaDadosParaAlgoritmo();


        //cria o usuário para o qual queremos recomendar
        //e define as receitas que foram favoritadas por ele
        HashMap<String, Double> usuarioPrincipal = new HashMap<>();
        usuarioPrincipal.put(empanado_frango, 1.0);
        usuarioPrincipal.put(bolo_terremoto, 1.0);

        //método chama o algoritmo pra recomendar e devolve o nome das receitas recomendadas
        List<String> nomesReceitasRecomendadas = obtemNomesReceitasRecomendadas(usuarioPrincipal);

        //chamada ao método que vai buscar no banco as receitas recomendadas (busca por nome)
        List<Receita> receitasRecomendadas = obtemListaObjetosReceita(nomesReceitasRecomendadas);

        //método que preenche recycler view com as receitas que foram recomendadas
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

        //faz a recomendacao
        Map<String, Double> recomendacao = so.predict(usuario);
        List receitasRecomendadas = new ArrayList<String>();

        //adiciona os nomes das receitas recomendadas numa lista
        for (String chave : recomendacao.keySet()){
            receitasRecomendadas.add(chave);
        }

        return receitasRecomendadas;
    }


    private void preparaDadosParaAlgoritmo() {
        //Seleciona as receitas
        role_frango = "Rolê de Frango";
        empanado_frango = "Empanado de Frango";
        sanduiche_carne = "Sanduíche de Carne";
        torta_pudim = "Torta de Pudim";
        cookie_chocolate = "Cookie crocante de chocolate";
        bolo_tapioca = "Bolo de Tapioca";
        bolo_terremoto = "Bolo Terremoto";

        receitas = new String[]{role_frango, empanado_frango, sanduiche_carne, torta_pudim, cookie_chocolate, bolo_tapioca, bolo_terremoto};

        //Pré-seleciona as avaliações
        HashMap<String, Double> user1 = new HashMap<>();
        HashMap<String, Double> user2 = new HashMap<>();
        HashMap<String, Double> user3 = new HashMap<>();
        HashMap<String, Double> user4 = new HashMap<>();
        HashMap<String, Double> user5 = new HashMap<>();

        //Avaliações usuario1
        user1.put(empanado_frango, 1.0);
        user1.put(bolo_tapioca, 0.0);
        user1.put(sanduiche_carne, 1.0);
        dados.put("Jonas", user1);

        //Avaliações usuario2
        user2.put(role_frango, 1.0);
        user2.put(bolo_tapioca, 1.0);
        dados.put("Bob", user2);

        //Avaliações usuario3
        user3.put(sanduiche_carne, 1.0);
        user3.put(cookie_chocolate, 0.0);
        dados.put("Maria", user3);

        //Avaliações usuario4
        user4.put(torta_pudim, 0.0);
        user4.put(sanduiche_carne, 1.0);
        user4.put(empanado_frango, 0.0);
        dados.put("Julia", user4);

        //Avaliações usuario5
        user5.put(torta_pudim, 1.0);
        user5.put(sanduiche_carne, 0.0);
        user5.put(role_frango, 1.0);
        dados.put("Juvenal", user5);

    }



    private void iniciarVariaveis() {
        receitaNegocio = new ReceitaNegocio(this);
        dados = new HashMap<>();
        txtViewRecomendTeste = (TextView)findViewById(R.id.textViewRecomendTeste);
        recycleRecomendacoes = (RecyclerView)findViewById(R.id.recycleViewRecomendacao);
    }
}
