package com.atox.usuario.persistencia.daoroom;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.atox.infra.persistencia.DaoBase;
import com.atox.usuario.dominio.Usuario;

import java.util.List;

@Dao
public interface UsuarioDaoRoom extends DaoBase<Usuario> {

    @Query("SELECT * FROM usuario")
    LiveData<List<Usuario>> getTodos();

    @Query("SELECT * FROM usuario where uid LIKE :uid")
    Usuario buscarPorId(long uid);

    @Query("SELECT * FROM usuario where uid LIKE :uid")
    Usuario buscarPorIdDeSessao(long uid);

    @Query("SELECT * FROM usuario where email LIKE :email")
    Usuario buscarPorEmail(String email);

    @Query("SELECT * FROM usuario where email LIKE :email AND senha LIKE :senha")
    Usuario buscarPorEmailESenha(String email, String senha);

}