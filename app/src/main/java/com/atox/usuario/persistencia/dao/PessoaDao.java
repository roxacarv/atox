package com.atox.usuario.persistencia.dao;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.atox.infra.persistencia.BDHelper;
import com.atox.usuario.dominio.Endereco;
import com.atox.usuario.dominio.Pessoa;
import com.atox.usuario.dominio.SessaoUsuario;
import com.atox.usuario.dominio.Usuario;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;


public class PessoaDao extends AndroidViewModel {

    private BDHelper bancoDeDados;

    public PessoaDao(Application application)
    {
        super(application);
        bancoDeDados = BDHelper.getBancoDeDados(this.getApplication());

    }

    public Long inserirPessoa(final Pessoa pessoa) throws ExecutionException, InterruptedException {
        Callable<Long> call = new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                return bancoDeDados.pessoaDao().inserir(pessoa);
            }
        };

        ExecutorService executor = new ScheduledThreadPoolExecutor(1);
        Future<Long> future = executor.submit(call);
        return future.get();
    }

    public void atualizar(final Pessoa pessoa) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                bancoDeDados.pessoaDao().atualizar(pessoa);
            }
        }).start();
    }

    public Pessoa buscarPorIdDeUsuario(final long usuarioId) throws ExecutionException, InterruptedException {
        Callable<Pessoa> call = new Callable<Pessoa>() {
            @Override
            public Pessoa call() throws Exception {
                Pessoa pessoa = montaPessoa(usuarioId);
                return pessoa;
            }
        };
        ExecutorService executor = new ScheduledThreadPoolExecutor(1);
        Future<Pessoa> future = executor.submit(call);
        return future.get();
    }

    public Pessoa montaPessoa(Long usuarioId) {
        Pessoa pessoa = bancoDeDados.pessoaDao().buscarPorIdDeusuario(usuarioId);
        if(pessoa == null) {
            return null;
        }
        Usuario usuario = buscarUsuarioPorId(usuarioId);
        if(usuario == null) {
            return null;
        }
        Endereco endereco = buscarEnderecoPorIdDePessoa(pessoa.getPid());
        if(endereco == null) {
            return null;
        }
        pessoa.setUsuarioId(usuario.getUid());
        pessoa.setUsuario(usuario);
        pessoa.setEndereco(endereco);
        return pessoa;
    }

    public Usuario buscarUsuarioPorId(Long id) {
        Usuario usuario = bancoDeDados.usuarioDao().buscarPorId(id);
        return usuario;
    }

    public Endereco buscarEnderecoPorIdDePessoa(Long id) {
        Endereco endereco = bancoDeDados.enderecoDao().buscarPorIdDePessoa(id);
        return endereco;
    }

    public void deletarItem(Pessoa pessoa)
    {
        new deleteAsyncTask(bancoDeDados).execute(pessoa);
    }

    private static class deleteAsyncTask extends AsyncTask<Pessoa, Void, Void> {
        private BDHelper bd;
        deleteAsyncTask(BDHelper bancoDeDados)
        {
            bd = bancoDeDados;
        }
        @Override
        protected Void doInBackground(final Pessoa... params)
        {
            bd.pessoaDao().deletar(params[0]);
            return null;
        }
    }

}