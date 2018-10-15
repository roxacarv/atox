package com.atox.usuario.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.atox.infra.BaseDao;
import com.atox.usuario.dominio.Usuario;

import java.util.List;

@Dao
public interface UserDao extends BaseDao<Usuario> {

    @Query("SELECT * FROM Usuario")
    LiveData<List<Usuario>> getAll();

    @Query("SELECT * FROM Usuario where primeiroNome LIKE  :firstName AND ultimoNome LIKE :lastName")
    Usuario findByName(String firstName, String lastName);

    @Query("SELECT COUNT(*) from Usuario")
    int countUsers();
}