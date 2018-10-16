package com.atox.usuario.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.atox.infra.BaseDao;
import com.atox.usuario.dominio.Usuario;

import java.util.List;

@Dao
public interface UsuarioDao extends BaseDao<Usuario> {


}