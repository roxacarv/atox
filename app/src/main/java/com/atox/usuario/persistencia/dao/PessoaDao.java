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
                Long idDePessoa = bancoDeDados.pessoaDaoRoom().inserir(pessoa);
                return idDePessoa;
            }
        };

        ExecutorService executor = new ScheduledThreadPoolExecutor(1);
        Future<Long> future = executor.submit(call);
        return future.get();
    }

    public Long inserirEndereco(final Endereco endereco) throws ExecutionException, InterruptedException {
        Callable<Long> call = new Callable<Long>() {
            @Override
            public Long call() throws Exception {
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
            public Long call() throws Exception {
                Long idDeUsuario = bancoDeDados.usuarioDaoRoom().inserir(usuario);
                return idDeUsuario;
            }
        };

        ExecutorService executor = new ScheduledThreadPoolExecutor(1);
        Future<Long> future = executor.submit(call);
        return future.get();
    }

    public Long atualizar(final Pessoa pessoa) throws ExecutionException, InterruptedException {
        Callable<Long> call = new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                int resultado = bancoDeDados.pessoaDaoRoom().atualizar(pessoa);
                return Long.valueOf(resultado);
            }
        };

        ExecutorService executor = new ScheduledThreadPoolExecutor(1);
        Future<Long> future = executor.submit(call);
        return future.get();
    }

    public Long atualizarUsuario(final Usuario usuario) throws ExecutionException, InterruptedException {
        Callable<Long> call = new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                int resultado = bancoDeDados.usuarioDaoRoom().atualizar(usuario);
                return Long.valueOf(resultado);
            }
        };

        ExecutorService executor = new ScheduledThreadPoolExecutor(1);
        Future<Long> future = executor.submit(call);
        return future.get();
    }

    public Pessoa buscarPorIdDeUsuario(final long usuarioId) throws ExecutionException, InterruptedException {
        Callable<Pessoa> call = new Callable<Pessoa>() {
            @Override
            public Pessoa call() throws Exception {
                Pessoa pessoa = bancoDeDados.pessoaDaoRoom().buscarPorIdDeusuario(usuarioId);
                Pessoa pessoaRetorno = montaPessoa(pessoa);
                return pessoaRetorno;
            }
        };
        ExecutorService executor = new ScheduledThreadPoolExecutor(1);
        Future<Pessoa> future = executor.submit(call);
        return future.get();
    }

    public Pessoa montaPessoa(Pessoa pessoa) {
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
        Usuario usuario = bancoDeDados.usuarioDaoRoom().buscarPorId(id);
        return usuario;
    }

    private Endereco buscarEnderecoPorIdDePessoa(Long id) {
        Endereco endereco = bancoDeDados.enderecoDaoRoom().buscarPorIdDePessoa(id);
        return endereco;
    }

    public Pessoa buscarPessoaPorId(final Long id) throws ExecutionException, InterruptedException {
        Callable<Pessoa> call = new Callable<Pessoa>() {
            @Override
            public Pessoa call() throws Exception {
                Pessoa pessoa = bancoDeDados.pessoaDaoRoom().buscarPorId(id);
                Pessoa pessoaRetorno = montaPessoa(pessoa);
                return pessoaRetorno;
            }
        };
        ExecutorService executor = new ScheduledThreadPoolExecutor(1);
        Future<Pessoa> future = executor.submit(call);
        return future.get();
    }
    public Usuario buscarUsuarioPorEmailESenha(String email, String senha){
        Usuario usuario = bancoDeDados.usuarioDaoRoom().buscarPorEmailESenha(email, senha);
        return usuario;
    }
    public Usuario buscarPorEmaildeUsuario(final String usuarioEmail) throws ExecutionException, InterruptedException {
        Callable<Usuario> call = new Callable<Usuario>() {
            @Override
            public Usuario call() throws Exception {
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
        private BDHelper bd;
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