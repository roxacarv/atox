package com.atox.usuario.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.atox.infra.persistencia.BaseDao;
import com.atox.usuario.dominio.Sessao;

@Dao
public interface SessaoDao extends BaseDao<Sessao> {

    @Query("SELECT * FROM sessao ORDER BY sid DESC LIMIT 1")
    Long ultimoIdLogado();

    @Query("DELETE FROM sessao")
    void deletaSessao();

}
