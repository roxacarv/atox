package com.atox.usuario.persistencia;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.atox.infra.persistencia.BaseDao;
import com.atox.usuario.dominio.Pessoa;

import java.util.List;

@Dao
public interface PessoaDao extends BaseDao<Pessoa> {

    @Query("SELECT * FROM pessoa")
    LiveData<List<Pessoa>> getTodas();

    @Query("SELECT * FROM pessoa where pid LIKE :pid")
    LiveData<Pessoa> buscarPorId(long pid);

    @Query("SELECT * FROM pessoa where nome LIKE :nome")
    LiveData<List<Pessoa>> buscarPorNome(String nome);

    @Query("SELECT * FROM pessoa where cpf LIKE :cpf")
    LiveData<Pessoa> buscarPorCpf(String cpf);

    @Query("SELECT * FROM pessoa where telefone LIKE :telefone")
    LiveData<Pessoa> buscarPorTelefone(String telefone);

}
