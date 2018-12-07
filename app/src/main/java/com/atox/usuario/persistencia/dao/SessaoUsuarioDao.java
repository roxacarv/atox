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

    private BDHelper bancoDeDados;
    private SessaoUsuario sessaoUsuario;

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
            public Long call() throws Exception {
                Long idDeRetorno = bancoDeDados.sessaoDaoRoom().inserir(sessaoUsuario);
                return idDeRetorno;
            }
        };
        ExecutorService executor = new ScheduledThreadPoolExecutor(1);
        Future<Long> future = executor.submit(call);
        return future.get();
    }

    public Pessoa restaurarSessao() throws ExecutionException, InterruptedException {
        Callable<Pessoa> call = new Callable<Pessoa>() {
            @Override
            public Pessoa call() throws Exception {
                Long idDeRetorno = bancoDeDados.sessaoDaoRoom().ultimoIdLogado();
                if (idDeRetorno == null) {
                    return null;
                }
                Pessoa pessoa = iniciarSessao(idDeRetorno);
                return pessoa;
            }
        };
        ExecutorService executor = new ScheduledThreadPoolExecutor(1);
        Future<Pessoa> future = executor.submit(call);
        return future.get();
    }

    public Pessoa iniciarSessao(Long id) {
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
        sessaoUsuario = SessaoUsuario.getSessao();
        sessaoUsuario.setPessoaLogada(pessoa);
        sessaoUsuario.setUsuarioLogado(pessoa.getUsuario());
        return pessoa;
    }



    public Usuario buscarUsuarioPorId(Long id) {
        Usuario usuario = bancoDeDados.usuarioDaoRoom().buscarPorId(id);
        return usuario;
    }

    public Endereco buscarEnderecoPorIdDePessoa(Long id) {
        Endereco endereco = bancoDeDados.enderecoDaoRoom().buscarPorIdDePessoa(id);
        return endereco;
    }


    public void deletarItem(SessaoUsuario sessao)
    {
        new SessaoUsuarioDao.deleteAsyncTaskSessao(bancoDeDados).execute(sessao);
    }

    private static class deleteAsyncTaskSessao extends AsyncTask<SessaoUsuario, Void, Void> {
        private BDHelper bd;
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
