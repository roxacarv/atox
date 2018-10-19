package com.atox.usuario.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.atox.infra.BaseDao;
import com.atox.usuario.dominio.Endereco;

import java.util.List;

@Dao
public interface EnderecoDao extends BaseDao<Endereco> {

    @Query("SELECT * FROM endereco where usuario_id LIKE :uid")
    LiveData<Endereco> buscarEnderecoPorIdDeUsuario(long uid);

    @Query("SELECT * FROM endereco where bairro LIKE :bairro")
    LiveData<List<Endereco>> buscarEnderecoPorBairro(String bairro);
}
