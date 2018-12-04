package com.atox.usuario.persistencia.daoroom;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.atox.infra.persistencia.DaoBase;
import com.atox.usuario.dominio.SessaoUsuario;

@Dao
public interface SessaoUsuarioDaoRoom extends DaoBase<SessaoUsuario> {

    @Query("SELECT * FROM sessao_usuario ORDER BY sid ASC LIMIT 1")
    Long ultimoIdLogado();

    @Query("DELETE FROM sessao_usuario")
    void deletaSessao();

}
