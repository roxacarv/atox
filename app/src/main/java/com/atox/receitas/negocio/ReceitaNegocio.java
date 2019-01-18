package com.atox.receitas.negocio;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.FragmentActivity;

import com.atox.atoxlogs.AtoxLog;
import com.atox.atoxlogs.AtoxMensagem;
import com.atox.receitas.dominio.Receita;
import com.atox.receitas.dominio.SecaoReceita;
import com.atox.receitas.dominio.UsuarioReceita;
import com.atox.receitas.persistencia.ReceitaDao;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ReceitaNegocio {

    private ReceitaDao receitaDao;
    private Receita receita;

    public ReceitaNegocio(FragmentActivity activity){
        receitaDao = ViewModelProviders.of(activity).get(ReceitaDao.class);
    }

    public List<Long> cadastrar(Long usuarioId, Receita receita) {
        AtoxLog log = new AtoxLog();
        Long resultadoInsercaoReceita = null;
        List<Long> resultadoInsercaoSecao = null;
        List<Long> resultadoIdsDeInsercao = new ArrayList<>();
        Long resultadoInsercaoUsuarioReceita = null;
        SecaoReceita arrayDeSecoesReceita[] = new SecaoReceita[receita.getSecoes().size()];
        arrayDeSecoesReceita = receita.getSecoes().toArray(arrayDeSecoesReceita);
        try {
            resultadoInsercaoReceita = receitaDao.inserirReceita(receita);
            if(resultadoInsercaoReceita != null) {
                resultadoInsercaoSecao = receitaDao.inserirSecaoReceita(arrayDeSecoesReceita);
            }
            if(resultadoInsercaoReceita != null) {
                UsuarioReceita usuarioReceita = new UsuarioReceita();
                usuarioReceita.setUsuarioId(usuarioId);
                usuarioReceita.setReceitaId(resultadoInsercaoReceita);
                resultadoInsercaoUsuarioReceita = receitaDao.inserirUsuarioReceita(usuarioReceita);
            }
            resultadoIdsDeInsercao.add(resultadoInsercaoReceita);
            resultadoIdsDeInsercao.add(resultadoInsercaoUsuarioReceita);
        } catch (ExecutionException e) {
            log = new AtoxLog();
            log.novoRegistro(AtoxMensagem.ACAO_EFETUAR_LOGIN,
                    AtoxMensagem.ERRO_BUSCAR_REGISTRO_NO_BANCO,
                    "Um erro de execução ocorreu ao tentar inserir uma receita no banco: " + e.getMessage());
            log.empurraRegistrosPraFila();
        } catch (InterruptedException e) {
            log = new AtoxLog();
            log.novoRegistro(AtoxMensagem.ACAO_EFETUAR_LOGIN,
                    AtoxMensagem.ERRO_BUSCAR_REGISTRO_NO_BANCO,
                    "Um erro de execução ocorreu ao tentar inserir uma receita no banco: " + e.getMessage());
            log.empurraRegistrosPraFila();
        }
        return resultadoIdsDeInsercao;
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
