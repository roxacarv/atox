package com.atox.usuario.negocio;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.telecom.Call;
import android.util.Log;

import com.atox.infra.AtoxException;
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
import org.apache.commons.validator.routines.EmailValidator;


public class UsuarioNegocio extends AndroidViewModel {

    private DBHelper bancoDeDados;
    private String TAG = UsuarioNegocio.class.getName();

    public UsuarioNegocio(Application application)
    {
        super(application);
        bancoDeDados = DBHelper.getBancoDeDados(this.getApplication());
    }

    public Long inserirUsuario(final Usuario usuario) throws ExecutionException, InterruptedException {
        boolean isEmailValid = EmailValidator.getInstance().isValid(usuario.getEmail());
        if(!isEmailValid) {
            return Long.valueOf(-1);
        }
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

    public Usuario restaurarSessao() throws ExecutionException, InterruptedException {
        final Sessao sessao = Sessao.getSessao();
        Callable<Usuario> call = new Callable<Usuario>() {
            @Override
            public Usuario call() throws Exception {
                Usuario usuario = null;
                Long idDeRetorno = null;
                idDeRetorno = bancoDeDados.sessaoDao().ultimoIdLogado();
                if (idDeRetorno == null) {
                    return null;
                }
                usuario = bancoDeDados.usuarioDao().buscarPorIdDeSessao(idDeRetorno);
                if (usuario == null) {
                    return null;
                }
                sessao.setUsuarioId(usuario.getUid());
                sessao.setUsuario(usuario);
                return usuario;
            }
        };
        ExecutorService executor = new ScheduledThreadPoolExecutor(1);
        Future<Usuario> future = executor.submit(call);
        return future.get();
    }

    public LiveData<List<Usuario>> getUsuarios() {
        LiveData<List<Usuario>> usuarios = bancoDeDados.usuarioDao().getTodos();
        return usuarios;
    }

    public LiveData<Usuario> buscarUsuarioPorId(long id) {
        LiveData<Usuario> usuario = bancoDeDados.usuarioDao().buscarPorId(id);
        if(!usuario.hasObservers()) {
            return null;
        }
        return usuario;
    }

    public void verificaSeUsuarioCadastrado(Usuario usuario) throws AtoxException, ExecutionException, InterruptedException {
        Usuario busca = buscarUsuarioPorEmail(usuario.getEmail(), usuario.getSenha());

        if (busca == null){
            throw new AtoxException("O usuário não existe ou a senha está incorreta");
        }
        else{
            return;
        }
    }

    public Usuario buscarUsuarioPorEmail(final String email, final String senha) throws ExecutionException, InterruptedException {
        Callable<Usuario> call = new Callable<Usuario>() {
            @Override
            public Usuario call() throws Exception {
                Usuario usuario = null;
                usuario = bancoDeDados.usuarioDao().buscarPorEmail(email);
                if(usuario == null) {
                    return null;
                }
                if(!usuario.getSenha().equals(senha)) {
                    return null;
                }
                return usuario;
            }
        };
        ExecutorService executor = new ScheduledThreadPoolExecutor(1);
        Future<Usuario> future = executor.submit(call);
        return future.get();
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