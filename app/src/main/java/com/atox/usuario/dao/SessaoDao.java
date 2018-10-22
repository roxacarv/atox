package com.atox.usuario.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.atox.infra.BaseDao;
import com.atox.usuario.dominio.Sessao;
import com.atox.usuario.dominio.Usuario;

@Dao
public interface SessaoDao extends BaseDao<Sessao> {

    @Query("SELECT * FROM sessao ORDER BY sid DESC LIMIT 1")
    Long ultimoIdLogado();

    @Query("DELETE FROM sessao")
    void deletaSessao();

}
