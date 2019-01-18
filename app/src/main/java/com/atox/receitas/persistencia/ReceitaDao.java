package com.atox.receitas.persistencia;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.os.AsyncTask;

import com.atox.infra.persistencia.BDHelper;
import com.atox.receitas.dominio.Receita;
import com.atox.receitas.dominio.SecaoReceita;
import com.atox.receitas.dominio.UsuarioReceita;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class ReceitaDao extends AndroidViewModel {

    private BDHelper bancoDeDados;

    public ReceitaDao(Application application)
    {
        super(application);
        bancoDeDados = BDHelper.getBancoDeDados(this.getApplication());

    }

    public Long inserirReceita(final Receita receita) throws ExecutionException, InterruptedException {
        Callable<Long> call = new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                Long idDePessoa = bancoDeDados.receitaDaoRoom().inserir(receita);
                return idDePessoa;
            }
        };

        ExecutorService executor = new ScheduledThreadPoolExecutor(1);
        Future<Long> future = executor.submit(call);
        return future.get();
    }

    public List<Long> inserirSecaoReceita(final SecaoReceita... secoesReceita) throws ExecutionException, InterruptedException {
        Callable<List<Long>> call = new Callable<List<Long>>() {
            @Override
            public List<Long> call() throws Exception {
                List<Long> idDeSecaoReceita = bancoDeDados.secaoReceitaDaoRoom().inserirTudo(secoesReceita);
                return idDeSecaoReceita;
            }
        };
        ExecutorService executor = new ScheduledThreadPoolExecutor(1);
        Future<List<Long>> future = executor.submit(call);
        return future.get();
    }

    public Long inserirUsuarioReceita(final UsuarioReceita usuarioReceita) throws ExecutionException, InterruptedException {
        Callable<Long> call = new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                Long idDeUsuarioReceita = bancoDeDados.usuarioReceitaDaoRoom().inserir(usuarioReceita);
                return idDeUsuarioReceita;
            }
        };
        ExecutorService executor = new ScheduledThreadPoolExecutor(1);
        Future<Long> future = executor.submit(call);
        return future.get();
    }

    public Long atualizarReceita(final Receita receita) throws ExecutionException, InterruptedException {
        Callable<Long> call = new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                int resultado = bancoDeDados.receitaDaoRoom().atualizar(receita);
                return Long.valueOf(resultado);
            }
        };

        ExecutorService executor = new ScheduledThreadPoolExecutor(1);
        Future<Long> future = executor.submit(call);
        return future.get();
    }

    public List<Receita> buscarPorIdDeUsuario(final long usuarioId) throws ExecutionException, InterruptedException {
        Callable<List<Receita>> call = new Callable<List<Receita>>() {
            @Override
            public List<Receita> call() throws Exception {
                List<UsuarioReceita> receitasDoUsuario = bancoDeDados.usuarioReceitaDaoRoom().getPorIdDeUsuario(usuarioId);
                List<Receita> receitas = null;
                if(receitasDoUsuario != null) {
                    receitas = montarReceitas(receitasDoUsuario);
                }
                return receitas;
            }
        };
        ExecutorService executor = new ScheduledThreadPoolExecutor(1);
        Future<List<Receita>> future = executor.submit(call);
        return future.get();
    }

    public List<Receita> montarReceitas(List<UsuarioReceita> usuarioReceitas) {
        List<Receita> receitas = null;
        for(UsuarioReceita usuarioReceita : usuarioReceitas) {
            Receita receita = null;
            List<SecaoReceita> secoesReceita = null;
            receita = bancoDeDados.receitaDaoRoom().buscarPorId(usuarioReceita.getReceitaId());
            if(receita != null) {
                secoesReceita = bancoDeDados.secaoReceitaDaoRoom().getSecaoPorIdDeReceita(receita.getRid());
            }
            if(secoesReceita != null) {
                receita.setSecoes(secoesReceita);
            }
            receitas.add(receita);
        }
        return receitas;
    }

    public void deletarItem(Receita receita) {

        new ReceitaDao.deleteAsyncTask(bancoDeDados).execute(receita);
    }

    private static class deleteAsyncTask extends AsyncTask<Receita, Void, Void> {
        private BDHelper bd;
        deleteAsyncTask(BDHelper bancoDeDados) {
            bd = bancoDeDados;
        }
        @Override
        protected Void doInBackground(final Receita... params){
            bd.receitaDaoRoom().deletar(params[0]);
            return null;
        }
    }
}
