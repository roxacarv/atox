package com.atox.usuario.persistencia;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.atox.infra.persistencia.BaseDao;
import com.atox.usuario.dominio.Sessao;

@Dao
public interface SessaoDao extends BaseDao<Sessao> {

    @Query("SELECT * FROM sessao ORDER BY sid ASC LIMIT 1")
    Long ultimoIdLogado();

    @Query("DELETE FROM sessao")
    void deletaSessao();

}
