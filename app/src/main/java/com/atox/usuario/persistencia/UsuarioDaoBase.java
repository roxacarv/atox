package com.atox.usuario.persistencia;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.atox.infra.persistencia.DaoBase;
import com.atox.usuario.dominio.Usuario;

import java.util.List;

@Dao
public interface UsuarioDaoBase extends DaoBase<Usuario> {

    @Query("SELECT * FROM usuario")
    LiveData<List<Usuario>> getTodos();

    @Query("SELECT * FROM usuario where uid LIKE :uid")
    LiveData<Usuario> buscarPorId(long uid);

    @Query("SELECT * FROM usuario where uid LIKE :uid")
    Usuario buscarPorIdDeSessao(long uid);

    @Query("SELECT * FROM usuario where email LIKE :email")
    Usuario buscarPorEmail(String email);



}