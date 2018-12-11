package com.atox.usuario.persistencia.dao;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.os.AsyncTask;

import com.atox.infra.persistencia.BDHelper;
import com.atox.usuario.dominio.Endereco;
import com.atox.usuario.dominio.Pessoa;
import com.atox.usuario.dominio.SessaoUsuario;
import com.atox.usuario.dominio.Usuario;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class SessaoUsuarioDao extends AndroidViewModel {

    private final BDHelper bancoDeDados;

    public SessaoUsuarioDao(Application application)
    {
        super(application);
        bancoDeDados = BDHelper.getBancoDeDados(this.getApplication());
    }

    public void atualizar(final SessaoUsuario sessaoUsuario) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                bancoDeDados.sessaoDaoRoom().atualizar(sessaoUsuario);
            }
        }).start();
    }

    public Long salvarSessao(final SessaoUsuario sessaoUsuario) throws ExecutionException, InterruptedException {
        Callable<Long> call = new Callable<Long>() {
            @Override
            public Long call() {
                return bancoDeDados.sessaoDaoRoom().inserir(sessaoUsuario);
            }
        };
        ExecutorService executor = new ScheduledThreadPoolExecutor(1);
        Future<Long> future = executor.submit(call);
        return future.get();
    }

    public Pessoa restaurarSessao() throws ExecutionException, InterruptedException {
        Callable<Pessoa> call = new Callable<Pessoa>() {
            @Override
            public Pessoa call() {
                Long idDeRetorno = bancoDeDados.sessaoDaoRoom().ultimoIdLogado();
                if (idDeRetorno == null) {
                    return null;
                }
                return iniciarSessao(idDeRetorno);
            }
        };
        ExecutorService executor = new ScheduledThreadPoolExecutor(1);
        Future<Pessoa> future = executor.submit(call);
        return future.get();
    }

    private Pessoa iniciarSessao(Long id) {
        Pessoa pessoa = bancoDeDados.pessoaDaoRoom().buscarPorIdDeusuario(id);
        if(pessoa == null) {
            return null;
        }
        Usuario usuario = buscarUsuarioPorId(id);
        if(usuario == null) {
            return null;
        }
        Endereco endereco = buscarEnderecoPorIdDePessoa(pessoa.getPid());
        pessoa.setUsuarioId(usuario.getUid());
        pessoa.setUsuario(usuario);
        pessoa.setEndereco(endereco);
        SessaoUsuario sessaoUsuario = SessaoUsuario.getSessao();
        sessaoUsuario.setPessoaLogada(pessoa);
        sessaoUsuario.setUsuarioLogado(pessoa.getUsuario());
        return pessoa;
    }



    private Usuario buscarUsuarioPorId(Long id) {
        return bancoDeDados.usuarioDaoRoom().buscarPorId(id);
    }

    private Endereco buscarEnderecoPorIdDePessoa(Long id) {
        return bancoDeDados.enderecoDaoRoom().buscarPorIdDePessoa(id);
    }


    public void deletarItem(SessaoUsuario sessao)
    {
        new SessaoUsuarioDao.deleteAsyncTaskSessao(bancoDeDados).execute(sessao);
    }

    private static class deleteAsyncTaskSessao extends AsyncTask<SessaoUsuario, Void, Void> {
        private final BDHelper bd;
        deleteAsyncTaskSessao(BDHelper bancoDeDados)
        {
            bd = bancoDeDados;
        }
        @Override
        protected Void doInBackground(final SessaoUsuario... params)
        {
            bd.sessaoDaoRoom().deletar(params[0]);
            return null;
        }
    }

}
