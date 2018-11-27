package com.atox.usuario.negocio;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.atox.infra.persistencia.BancoDeDados;
import com.atox.usuario.dominio.Endereco;
import com.atox.usuario.dominio.Pessoa;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;


public class PessoaNegocio extends AndroidViewModel {

    private BancoDeDados bancoDeDados;

    public PessoaNegocio(Application application)
    {
        super(application);
        bancoDeDados = BancoDeDados.getBancoDeDados(this.getApplication());
    }

    public Long inserirPessoa(final Pessoa pessoa) throws ExecutionException, InterruptedException {
        Callable<Long> call = new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                return bancoDeDados.pessoaDao().inserir(pessoa);
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

    public void atualizar(final Pessoa pessoa) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                bancoDeDados.pessoaDao().atualizar(pessoa);
            }
        }).start();
    }

    public LiveData<Endereco> buscarPorIdDeEndereco(long eid) {
        LiveData<Endereco> endereco = bancoDeDados.enderecoDao().buscarPorId(eid);
        return endereco;
    }

    public LiveData<Endereco> buscarPorEnderecoPorIdDeUsuario(long uid) {
        LiveData<Endereco> endereco = bancoDeDados.enderecoDao().buscarPorIdDeUsuario(uid);
        return endereco;
    }

    public LiveData<List<Endereco>> buscarEnderecoPorCidade(String cidade) {
        LiveData<List<Endereco>> endereco = bancoDeDados.enderecoDao().buscarPorCidade(cidade);
        return endereco;
    }

    public LiveData<List<Endereco>> buscarEnderecoPorBairro(String bairro) {
        LiveData<List<Endereco>> endereco = bancoDeDados.enderecoDao().buscarPorBairro(bairro);
        return endereco;
    }

    public void deletarItem(Pessoa pessoa)
    {
        new deleteAsyncTask(bancoDeDados).execute(pessoa);
    }

    private static class deleteAsyncTask extends AsyncTask<Pessoa, Void, Void> {
        private BancoDeDados bd;
        deleteAsyncTask(BancoDeDados bancoDeDados)
        {
            bd = bancoDeDados;
        }
        @Override
        protected Void doInBackground(final Pessoa... params)
        {
            bd.pessoaDao().deletar(params[0]);
            return null;
        }
    }

}