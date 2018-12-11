package com.atox.network.persistencia.dao;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import com.atox.infra.persistencia.BDHelper;
import com.atox.network.dominio.Produtor;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class ProdutorDao extends AndroidViewModel {

    private final BDHelper bancoDeDados;

    public ProdutorDao(Application application)
    {
        super(application);
        bancoDeDados = BDHelper.getBancoDeDados(this.getApplication());

    }

    public List<Long> inserirProdutores(final Produtor... produtores) throws ExecutionException, InterruptedException {
        Callable<List<Long>> call = new Callable<List<Long>>() {
            @Override
            public List<Long> call() {
                List<Long> idDosProdutores = bancoDeDados.produtorDaoRoom().inserirTudo(produtores);
                if(idDosProdutores.isEmpty()) {
                    return null;
                }
                return idDosProdutores;
            }
        };

        ExecutorService executor = new ScheduledThreadPoolExecutor(1);
        Future<List<Long>> future = executor.submit(call);
        return future.get();
    }

    public List<Produtor> buscarTodosProdutores() throws ExecutionException, InterruptedException {
        Callable<List<Produtor>> call = new Callable<List<Produtor>>() {
            @Override
            public List<Produtor> call() {
                List<Produtor> produtores = bancoDeDados.produtorDaoRoom().getProdutores();
                if(produtores.isEmpty()) {
                    return null;
                }
                return produtores;
            }
        };

        ExecutorService executor = new ScheduledThreadPoolExecutor(1);
        Future<List<Produtor>> future = executor.submit(call);
        return future.get();
    }

}
