package com.atox.usuario.negocio;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.atox.infra.persistencia.DBHelper;
import com.atox.usuario.dominio.Endereco;
import com.atox.usuario.dominio.Sessao;
import com.atox.usuario.dominio.Usuario;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;


public class UsuarioNegocio extends AndroidViewModel {

    private DBHelper bancoDeDados;

    public UsuarioNegocio(Application application)
    {
        super(application);
        bancoDeDados = DBHelper.getBancoDeDados(this.getApplication());
    }

    public Long inserirUsuario(final Usuario usuario) throws ExecutionException, InterruptedException {
        Callable<Long> call = new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                return bancoDeDados.usuarioDao().inserir(usuario);
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
                return bancoDeDados.enderecoDao().inserir(endereco);
            }
        };

        ExecutorService executor = new ScheduledThreadPoolExecutor(1);
        Future<Long> future = executor.submit(call);
        return future.get();
    }

    public void atualizar(final Usuario usuario) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                bancoDeDados.usuarioDao().atualizar(usuario);
            }
        }).start();
    }

    public Long salvarSessao(final Sessao sessao) throws ExecutionException, InterruptedException {
        Callable<Long> call = new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                return bancoDeDados.sessaoDao().inserir(sessao);
            }
        };
        ExecutorService executor = new ScheduledThreadPoolExecutor(1);
        Future<Long> future = executor.submit(call);
        return future.get();
    }

    public void restaurarSessao() {
        Sessao sessao = Sessao.getSessao();
        Long idDeRetorno = bancoDeDados.sessaoDao().ultimoIdLogado();
        Usuario usuario = null;

        if(idDeRetorno != null)
            usuario = bancoDeDados.usuarioDao().buscarPorIdDeSessao(idDeRetorno);

        sessao.setUsuarioId(usuario.getUid());
        sessao.setUsuario(usuario);
    }

    public LiveData<List<Usuario>> getUsuarios() {
        LiveData<List<Usuario>> usuarios = bancoDeDados.usuarioDao().getTodos();
        return usuarios;
    }

    public LiveData<Usuario> buscarUsuarioPorId(long id) {
        LiveData<Usuario> usuario = bancoDeDados.usuarioDao().buscarPorId(id);
        return usuario;
    }

    public Usuario buscarUsuarioPorEmail(String email) {
        Usuario usuario = bancoDeDados.usuarioDao().buscarPorEmail(email);
        return usuario;
    }


    public void deletarItem(Usuario usuario)
    {
        new deleteAsyncTask(bancoDeDados).execute(usuario);
    }

    private static class deleteAsyncTask extends AsyncTask<Usuario, Void, Void> {
        private DBHelper bd;
        deleteAsyncTask(DBHelper bancoDeDados)
        {
            bd = bancoDeDados;
        }
        @Override
        protected Void doInBackground(final Usuario... params)
        {
            bd.usuarioDao().deletar(params[0]);
            return null;
        }
    }

}