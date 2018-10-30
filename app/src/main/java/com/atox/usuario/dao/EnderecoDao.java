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

    @Query("SELECT * FROM endereco WHERE endereco_id LIKE :endereco_id")
    LiveData<Endereco> buscarPeloId(long endereco_id);

    @Query("SELECT * FROM endereco WHERE cidade LIKE :cidade")
    LiveData<Endereco> buscarPelaCidade(String cidade);

    @Query("SELECT * FROM  endereco WHERE bairro LIKE :bairro")
    LiveData<Endereco> buscarPeloBairro(String bairro);

    @Query("SELECT * FROM endereco WHERE logradouro LIKE :logradouro")
    LiveData<Endereco> buscarPeloLogradouro(String logradouro);

    @Query("SELECT * FROM endereco WHERE cep LIKE :cep")
    LiveData<Endereco> buscarPeloCep(String cep);

    @Query("SELECT * FROM endereco WHERE estado LIKE :estado")
    LiveData<Endereco> buscarPeloEstado(String estado);

    @Query("SELECT * FROM endereco WHERE pais LIKE :pais")
    LiveData<Endereco> buscarPeloPaís(String pais);
}
