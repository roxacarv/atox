package com.atox.usuario.persistencia.dao;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.os.AsyncTask;
import android.util.Log;

import com.atox.infra.persistencia.BDHelper;
import com.atox.usuario.dominio.Endereco;
import com.atox.usuario.dominio.Pessoa;
import com.atox.usuario.dominio.Usuario;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;


public class PessoaDao extends AndroidViewModel {

    private final BDHelper bancoDeDados;

    public PessoaDao(Application application)
    {
        super(application);
        bancoDeDados = BDHelper.getBancoDeDados(this.getApplication());

    }

    public Long inserirPessoa(final Pessoa pessoa) throws ExecutionException, InterruptedException {
        Callable<Long> call = new Callable<Long>() {
            @Override
            public Long call() {
                return bancoDeDados.pessoaDaoRoom().inserir(pessoa);
            }
        };

        ExecutorService executor = new ScheduledThreadPoolExecutor(1);
        Future<Long> future = executor.submit(call);
        return future.get();
    }

    public Long inserirEndereco(final Endereco endereco) throws ExecutionException, InterruptedException {
        Callable<Long> call = new Callable<Long>() {
            @Override
            public Long call() {
                return bancoDeDados.enderecoDaoRoom().inserir(endereco);
            }
        };

        ExecutorService executor = new ScheduledThreadPoolExecutor(1);
        Future<Long> future = executor.submit(call);
        return future.get();
    }

    public Long inserirUsuario(final Usuario usuario) throws ExecutionException, InterruptedException {
        Callable<Long> call = new Callable<Long>() {
            @Override
            public Long call() {
                return bancoDeDados.usuarioDaoRoom().inserir(usuario);
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
                bancoDeDados.pessoaDaoRoom().atualizar(pessoa);
            }
        }).start();
    }

    public Pessoa buscarPorIdDeUsuario(final long usuarioId) throws ExecutionException, InterruptedException {
        Callable<Pessoa> call = new Callable<Pessoa>() {
            @Override
            public Pessoa call() {
                Pessoa pessoa = bancoDeDados.pessoaDaoRoom().buscarPorIdDeusuario(usuarioId);
                return montaPessoa(pessoa);
            }
        };
        ExecutorService executor = new ScheduledThreadPoolExecutor(1);
        Future<Pessoa> future = executor.submit(call);
        return future.get();
    }

    private Pessoa montaPessoa(Pessoa pessoa) {
        Log.i("PessoaDaoActivity", "essa é a pessoa do banco: " + pessoa);
        if(pessoa == null) {
            return null;
        }
        Usuario usuario = this.buscarUsuarioPorId(pessoa.getUsuarioId());
        if(usuario == null) {
            return null;
        }
        Endereco endereco = this.buscarEnderecoPorIdDePessoa(pessoa.getPid());
        pessoa.setUsuarioId(usuario.getUid());
        pessoa.setUsuario(usuario);
        pessoa.setEndereco(endereco);
        return pessoa;
    }

    private Usuario buscarUsuarioPorId(Long id) {
        return bancoDeDados.usuarioDaoRoom().buscarPorId(id);
    }

    private Endereco buscarEnderecoPorIdDePessoa(Long id) {
        return bancoDeDados.enderecoDaoRoom().buscarPorIdDePessoa(id);
    }

    public Pessoa buscarPessoaPorId(final Long id) throws ExecutionException, InterruptedException {
        Callable<Pessoa> call = new Callable<Pessoa>() {
            @Override
            public Pessoa call() {
                Pessoa pessoa = bancoDeDados.pessoaDaoRoom().buscarPorId(id);
                return montaPessoa(pessoa);
            }
        };
        ExecutorService executor = new ScheduledThreadPoolExecutor(1);
        Future<Pessoa> future = executor.submit(call);
        return future.get();
    }
    public Usuario buscarUsuarioPorEmailESenha(String email, String senha){
        return bancoDeDados.usuarioDaoRoom().buscarPorEmailESenha(email, senha);
    }
    public Usuario buscarPorEmaildeUsuario(final String usuarioEmail) throws ExecutionException, InterruptedException {
        Callable<Usuario> call = new Callable<Usuario>() {
            @Override
            public Usuario call() {
                Usuario usuario = bancoDeDados.usuarioDaoRoom().buscarPorEmail(usuarioEmail);
                if(usuario == null){
                    return null;
                }
                return usuario;
            }
        };
        ExecutorService executor = new ScheduledThreadPoolExecutor(1);
        Future<Usuario> future = executor.submit(call);
        return future.get();
    }
    public void deletarItem(Pessoa pessoa) {

        new deleteAsyncTask(bancoDeDados).execute(pessoa);
    }

    private static class deleteAsyncTask extends AsyncTask<Pessoa, Void, Void> {
        private final BDHelper bd;
        deleteAsyncTask(BDHelper bancoDeDados) {
            bd = bancoDeDados;
        }
        @Override
        protected Void doInBackground(final Pessoa... params){
            bd.pessoaDaoRoom().deletar(params[0]);
            return null;
        }
    }

}