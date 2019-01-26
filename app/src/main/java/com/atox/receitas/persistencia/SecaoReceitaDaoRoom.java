package com.atox.receitas.persistencia;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.atox.infra.persistencia.DaoBase;
import com.atox.receitas.dominio.SecaoReceita;

import java.util.List;

@Dao
public interface SecaoReceitaDaoRoom extends DaoBase<SecaoReceita> {

    @Query("SELECT * FROM secao_receita")
    List<SecaoReceita> getTodos();

    @Query("SELECT * FROM secao_receita where srid LIKE :srid")
    SecaoReceita buscarPorId(long srid);

    @Query("SELECT * FROM secao_receita where receita_id LIKE :receitaId")
    List<SecaoReceita> getSecaoPorIdDeReceita(long receitaId);
}
