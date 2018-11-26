package com.atox.usuario.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.atox.infra.BaseDao;
import com.atox.usuario.dominio.Endereco;
import com.atox.usuario.dominio.Usuario;

import java.util.List;

@Dao
public interface UsuarioDao extends BaseDao<Usuario> {

    @Query("SELECT * FROM usuario")
    LiveData<List<Usuario>> getTodos();

    @Query("SELECT * FROM usuario where uid LIKE :uid")
    LiveData<Usuario> buscarPorId(long uid);

    @Query("SELECT * FROM usuario where uid LIKE :uid")
    Usuario buscarPorIdDeSessao(long uid);


    @Query("SELECT * FROM usuario where email LIKE :email")
    Usuario buscarPorEmail(String email);



}