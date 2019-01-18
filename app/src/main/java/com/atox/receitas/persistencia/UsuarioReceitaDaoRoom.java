package com.atox.receitas.persistencia;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.atox.infra.persistencia.DaoBase;
import com.atox.receitas.dominio.Receita;
import com.atox.receitas.dominio.UsuarioReceita;

import java.util.List;

@Dao
public interface UsuarioReceitaDaoRoom extends DaoBase<UsuarioReceita> {

    @Query("SELECT * FROM usuario_receita")
    List<UsuarioReceita> getTodos();

    @Query("SELECT * FROM usuario_receita where urid LIKE :urid")
    UsuarioReceita buscarPorId(long urid);

    @Query("SELECT * FROM usuario_receita where usuario_id LIKE :usuarioId")
    List<UsuarioReceita> getPorIdDeUsuario(long usuarioId);

    @Query("SELECT * FROM usuario_receita where receita_id LIKE :receitaId")
    UsuarioReceita getPorIdDeReceita(long receitaId);

}
