package com.atox.receitas.gui;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.atox.R;
import com.atox.receitas.dominio.Receita;
import com.atox.receitas.negocio.ReceitaNegocio;
import java.util.List;

public class ReceitasActivity extends AppCompatActivity {

    private ReceitaCustomAdapter receitaCustomAdapter;
    private RecyclerView customRecyclerViewReceita;
    private ReceitaNegocio receitaNegocio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receitas);
        inicializarVariaveis();
        gerandoListaDeReceitas(receitaNegocio.buscarTodasReceitas(),customRecyclerViewReceita, this,receitaCustomAdapter);
    }

    public void inicializarVariaveis(){
        receitaNegocio = new ReceitaNegocio(this);
        customRecyclerViewReceita = (RecyclerView) findViewById(R.id.customRecycleViewReceitas);
    }

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