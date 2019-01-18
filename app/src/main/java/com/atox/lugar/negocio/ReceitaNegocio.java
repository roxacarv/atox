package com.atox.lugar.negocio;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.FragmentActivity;

import com.atox.atoxlogs.AtoxLog;
import com.atox.atoxlogs.AtoxMensagem;
import com.atox.lugar.dominio.Receita;
import com.atox.lugar.persistencia.ReceitaDao;
import com.atox.usuario.dominio.Pessoa;
import com.atox.usuario.persistencia.dao.PessoaDao;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ReceitaNegocio {

    private ReceitaDao receitaDao;
    private Receita receita;

    public ReceitaNegocio(FragmentActivity activity){
        receitaDao = ViewModelProviders.of(activity).get(ReceitaDao.class);
    }

    public Long cadastrar(Receita receita) {
        AtoxLog log = new AtoxLog();
        Long resultado = null;
        try {
            resultado = receitaDao.inserirReceita(receita);
        } catch (ExecutionException e) {
            log = new AtoxLog();
            log.novoRegistro(AtoxMensagem.ACAO_EFETUAR_LOGIN,
                    AtoxMensagem.ERRO_BUSCAR_REGISTRO_NO_BANCO,
                    "Um erro de execução ocorreu ao tentar buscar um usuário no banco: " + e.getMessage());
            log.empurraRegistrosPraFila();
        } catch (InterruptedException e) {
            log = new AtoxLog();
            log.novoRegistro(AtoxMensagem.ACAO_EFETUAR_LOGIN,
                    AtoxMensagem.ERRO_BUSCAR_REGISTRO_NO_BANCO,
                    "Um erro de execução ocorreu ao tentar buscar um usuário no banco: " + e.getMessage());
            log.empurraRegistrosPraFila();
        }
        return resultado;
    }

    public List<Receita> buscarReceitasDoUsuario(Long idDeUsuario) {
        AtoxLog log = new AtoxLog();
        List<Receita> receitas = null;
        try {
            receitas = receitaDao.buscarPorIdDeUsuario(idDeUsuario);
        } catch (ExecutionException e) {
            log = new AtoxLog();
            log.novoRegistro(AtoxMensagem.ACAO_EFETUAR_LOGIN,
                    AtoxMensagem.ERRO_BUSCAR_REGISTRO_NO_BANCO,
                    "Um erro de execução ocorreu ao tentar buscar um usuário no banco: " + e.getMessage());
            log.empurraRegistrosPraFila();
        } catch (InterruptedException e) {
            log = new AtoxLog();
            log.novoRegistro(AtoxMensagem.ACAO_EFETUAR_LOGIN,
                    AtoxMensagem.ERRO_BUSCAR_REGISTRO_NO_BANCO,
                    "Um erro de execução ocorreu ao tentar buscar um usuário no banco: " + e.getMessage());
            log.empurraRegistrosPraFila();
        }
        return receitas;
    }

    public void removerReceita(Receita receita) {
        AtoxLog log = new AtoxLog();
        Long resultado = null;
        receitaDao.deletarItem(receita);
    }

}
