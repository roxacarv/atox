package com.atox.receitas.persistencia;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.atox.infra.persistencia.DaoBase;
import com.atox.receitas.dominio.Receita;

import java.util.List;

@Dao
public interface ReceitaDaoRoom extends DaoBase<Receita> {

    @Query("SELECT * FROM receita")
    List<Receita> getTodos();

    @Query("SELECT * FROM receita where rid LIKE :rid")
    Receita buscarPorId(long rid);

    @Query("SELECT * FROM receita where nome_receita LIKE :nomeReceita")
    Receita getReceitaPorNome(String nomeReceita);

}
