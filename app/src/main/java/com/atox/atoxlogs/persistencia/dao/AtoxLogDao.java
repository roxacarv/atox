package com.atox.atoxlogs.persistencia.dao;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import com.atox.atoxlogs.AtoxLog;
import com.atox.infra.persistencia.BDHelper;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class AtoxLogDao extends AndroidViewModel {

    private BDHelper bancoDeDados;

    public AtoxLogDao(Application application) {
        super(application);
        bancoDeDados = BDHelper.getBancoDeDados(this.getApplication());

    }

    public Long inserirLog(final AtoxLog atoxLog) throws ExecutionException, InterruptedException {
        Callable<Long> call = new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                Long idDoLog = bancoDeDados.atoxLogDaoRoom().inserir(atoxLog);
                return idDoLog;
            }
        };

        ExecutorService executor = new ScheduledThreadPoolExecutor(1);
        Future<Long> future = executor.submit(call);
        return future.get();
    }

    public List<Long> inserirTodosLogs(final AtoxLog atoxLogs[]) throws ExecutionException, InterruptedException {
        Callable<List<Long>> call = new Callable<List<Long>>() {
            @Override
            public List<Long> call() throws Exception {
                List<Long> idDoLog = bancoDeDados.atoxLogDaoRoom().inserirTudo(atoxLogs);
                return idDoLog;
            }
        };

        ExecutorService executor = new ScheduledThreadPoolExecutor(1);
        Future<List<Long>> future = executor.submit(call);
        return future.get();
    }

    public List<AtoxLog> buscarLogs() throws ExecutionException, InterruptedException {
        Callable<List<AtoxLog>> call = new Callable<List<AtoxLog>>() {
            @Override
            public List<AtoxLog> call() throws Exception {
                List<AtoxLog> logs = bancoDeDados.atoxLogDaoRoom().buscarTodosLogs();
                return logs;
            }
        };

        ExecutorService executor = new ScheduledThreadPoolExecutor(1);
        Future<List<AtoxLog>> future = executor.submit(call);
        return future.get();
    }

    public List<AtoxLog> buscarLogsDoUsuario(final long idDeUsuario) throws ExecutionException, InterruptedException {
        Callable<List<AtoxLog>> call = new Callable<List<AtoxLog>>() {
            @Override
            public List<AtoxLog> call() throws Exception {
                List<AtoxLog> logs = bancoDeDados.atoxLogDaoRoom().buscarLogsDoUsuario(idDeUsuario);
                return logs;
            }
        };

        ExecutorService executor = new ScheduledThreadPoolExecutor(1);
        Future<List<AtoxLog>> future = executor.submit(call);
        return future.get();
    }

    public List<AtoxLog> buscarLogsPorAcao(final long acao) throws ExecutionException, InterruptedException {
        Callable<List<AtoxLog>> call = new Callable<List<AtoxLog>>() {
            @Override
            public List<AtoxLog> call() throws Exception {
                List<AtoxLog> logs = bancoDeDados.atoxLogDaoRoom().buscarLogsPorAcao(acao);
                return logs;
            }
        };

        ExecutorService executor = new ScheduledThreadPoolExecutor(1);
        Future<List<AtoxLog>> future = executor.submit(call);
        return future.get();
    }

    public List<AtoxLog> buscarLogsDoUsuarioPorTipo(final long idDeUsuario, final long tipo) throws ExecutionException, InterruptedException {
        Callable<List<AtoxLog>> call = new Callable<List<AtoxLog>>() {
            @Override
            public List<AtoxLog> call() throws Exception {
                List<AtoxLog> logs = bancoDeDados.atoxLogDaoRoom().buscarLogsDoUsuarioPorTipo(idDeUsuario ,tipo);
                return logs;
            }
        };

        ExecutorService executor = new ScheduledThreadPoolExecutor(1);
        Future<List<AtoxLog>> future = executor.submit(call);
        return future.get();
    }
}
