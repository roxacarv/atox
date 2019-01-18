package com.atox.lugar.persistencia;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.os.AsyncTask;

import com.atox.infra.persistencia.BDHelper;
import com.atox.lugar.dominio.Receita;
import com.atox.usuario.dominio.Pessoa;
import com.atox.usuario.persistencia.dao.PessoaDao;

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

    public Long atualizar(final Receita receita) throws ExecutionException, InterruptedException {
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
                List<Receita> receitasDoUsuario = bancoDeDados.receitaDaoRoom().getReceitasDoUsuario(usuarioId);
                return receitasDoUsuario;
            }
        };
        ExecutorService executor = new ScheduledThreadPoolExecutor(1);
        Future<List<Receita>> future = executor.submit(call);
        return future.get();
    }

    public Receita buscarReceitaPorIdDeUsuario(final long usuarioId, final String nome) throws ExecutionException, InterruptedException {
        Callable<Receita> call = new Callable<Receita>() {
            @Override
            public Receita call() throws Exception {
                Receita receitaDoUsuario = bancoDeDados.receitaDaoRoom().getReceitaPorIdDeUsuario(usuarioId, nome);
                return receitaDoUsuario;
            }
        };
        ExecutorService executor = new ScheduledThreadPoolExecutor(1);
        Future<Receita> future = executor.submit(call);
        return future.get();
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
