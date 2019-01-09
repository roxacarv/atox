package com.atox.network.persistencia.daoroom;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.atox.infra.persistencia.DaoBase;
import com.atox.network.dominio.Produtor;

import java.util.List;

@Dao
public interface ProdutorDaoRoom extends DaoBase<Produtor> {
    @Query("SELECT * FROM produtor")
    List<Produtor> getProdutores();
}
