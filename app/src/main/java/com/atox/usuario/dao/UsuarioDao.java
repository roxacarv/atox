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

    @Query("SELECT * FROM endereco where usuario_id LIKE :uid")
    LiveData<List<Endereco>> buscarTodosEnderecosPorIdDeUsuario(long uid);

    @Query("SELECT * FROM endereco where usuario_id LIKE :uid")
    LiveData<Endereco> buscarEnderecoPorIdDeUsuario(long uid);

    @Query("SELECT * FROM endereco where eid LIKE :eid")
    LiveData<Endereco> buscarEnderecoPorId(long eid);

    @Query("SELECT * FROM usuario where nome LIKE :nome")
    LiveData<List<Usuario>> buscarPorNome(String nome);

    @Query("SELECT * FROM usuario where cpf LIKE :cpf")
    LiveData<Usuario> buscarPorCpf(String cpf);

    @Query("SELECT * FROM usuario where telefone LIKE :telefone")
    LiveData<Usuario> buscarPorTelefone(String telefone);


}