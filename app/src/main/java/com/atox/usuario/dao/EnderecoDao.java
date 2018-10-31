package com.atox.usuario.dao;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.atox.infra.BaseDao;
import com.atox.usuario.dominio.Endereco;

import java.util.List;

@Dao
public interface EnderecoDao extends BaseDao<Endereco> {
    @Query("SELECT * FROM endereco")
    LiveData<List<Endereco>> buscarTudo();

    @Query("SELECT * FROM endereco WHERE usuario_id LIKE :uid")
    LiveData<Endereco> buscarPorIdDeUsuario(long uid);

    @Query("SELECT * FROM endereco WHERE eid LIKE :eid")
    LiveData<Endereco> buscarPorId(long eid);

    @Query("SELECT * FROM endereco WHERE cidade LIKE :cidade")
    LiveData<List<Endereco>> buscarPorCidade(String cidade);

    @Query("SELECT * FROM  endereco WHERE bairro LIKE :bairro")
    LiveData<List<Endereco>> buscarPorBairro(String bairro);

    @Query("SELECT * FROM endereco WHERE logradouro LIKE :logradouro")
    LiveData<List<Endereco>> buscarPorLogradouro(String logradouro);

    @Query("SELECT * FROM endereco WHERE cep LIKE :cep")
    LiveData<List<Endereco>> buscarPorCep(String cep);

    @Query("SELECT * FROM endereco WHERE estado LIKE :estado")
    LiveData<List<Endereco>> buscarPorEstado(String estado);

    @Query("SELECT * FROM endereco WHERE pais LIKE :pais")
    LiveData<List<Endereco>> buscarPorPaís(String pais);
}

