package com.atox.usuario.persistencia.daoroom;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.atox.infra.persistencia.DaoBase;
import com.atox.usuario.dominio.Pessoa;

import java.util.List;

@Dao
public interface PessoaDaoRoom extends DaoBase<Pessoa> {

    @Query("SELECT * FROM pessoa")
    List<Pessoa> getTodas();

    @Query("SELECT * FROM pessoa where pid LIKE :pid")
    Pessoa buscarPorId(long pid);

    @Query("SELECT * FROM pessoa where nome LIKE :nome")
    List<Pessoa> buscarPorNome(String nome);

    @Query("SELECT * FROM pessoa where cpf LIKE :cpf")
    Pessoa buscarPorCpf(String cpf);

    @Query("SELECT * FROM pessoa where telefone LIKE :telefone")
    Pessoa buscarPorTelefone(String telefone);

    @Query("SELECT * FROM pessoa where usuario_id LIKE :usuarioId")
    Pessoa buscarPorIdDeusuario(long usuarioId);
}
