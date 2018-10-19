package com.atox.usuario.dao;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.atox.infra.BancoDeDados;
import com.atox.usuario.dominio.Endereco;
import com.atox.usuario.dominio.Usuario;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;


public class UsuarioListViewModel extends AndroidViewModel {

    private BancoDeDados bancoDeDados;

    public UsuarioListViewModel(Application application)
    {
        super(application);
        bancoDeDados = BancoDeDados.getBancoDeDados(this.getApplication());
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
        Long uid = future.get();
        return uid;
    }

    public void inserirEndereco(final Endereco endereco) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                bancoDeDados.enderecoDao().inserir(endereco);
            }
        }).start();
    }

    public void atualizar(final Usuario usuario) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                bancoDeDados.usuarioDao().atualizar(usuario);
            }
        }).start();
    }

    public LiveData<List<Usuario>> getUsuarios() {
        LiveData<List<Usuario>> usuarios = bancoDeDados.usuarioDao().getTodos();
        return usuarios;
    }

    public LiveData<Usuario> buscarUsuarioPorId(long id) {
        LiveData<Usuario> usuario = bancoDeDados.usuarioDao().buscarPorId(id);
        LiveData<Endereco> endereco = bancoDeDados.usuarioDao().buscarEnderecoPorIdDeUsuario(id);
        usuario.getValue().setEndereco(endereco.getValue());
        return usuario;
    }

    public LiveData<Endereco> buscarEnderecoPorId(long eid) {
        LiveData<Endereco> endereco = bancoDeDados.usuarioDao().buscarEnderecoPorId(eid);
        return endereco;
    }

    public LiveData<List<Usuario>> buscarUsuarioPorNome(String nome) {
        LiveData<List<Usuario>> usuarios = bancoDeDados.usuarioDao().buscarPorNome(nome);
        return usuarios;
    }

    public LiveData<Usuario> buscarUsuarioPorCpf(String cpf) {
        LiveData<Usuario> usuario = bancoDeDados.usuarioDao().buscarPorCpf(cpf);
        return usuario;
    }

    public void deletarItem(Usuario usuario)
    {
        new deleteAsyncTask(bancoDeDados).execute(usuario);
    }

    private static class deleteAsyncTask extends AsyncTask<Usuario, Void, Void> {
        private BancoDeDados bd;
        deleteAsyncTask(BancoDeDados bancoDeDados)
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