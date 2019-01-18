package com.atox.lugar.persistencia;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Query;

import com.atox.infra.persistencia.DaoBase;
import com.atox.lugar.dominio.Receita;
import com.atox.usuario.dominio.Usuario;

import java.util.List;

public interface ReceitaDaoRoom extends DaoBase<Receita> {

    @Query("SELECT * FROM receita")
    List<Receita> getTodos();

    @Query("SELECT * FROM receita where rid LIKE :rid")
    Receita buscarPorId(long rid);

    @Query("SELECT * FROM receita where usuario_id LIKE :usuarioId")
    List<Receita> getReceitasDoUsuario(long usuarioId);

    @Query("SELECT * FROM receita where nome_receita LIKE :nomeReceita")
    Receita getReceitaPorNome(String nomeReceita);

    @Query("SELECT * FROM receita where usuario_id LIKE :usuarioId AND nome_receita LIKE :nomeReceita")
    Receita getReceitaPorIdDeUsuario(Long usuarioId, String nomeReceita);

}
