package com.atox.usuario.negocio;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.atox.infra.AtoxException;
import com.atox.usuario.dominio.Pessoa;
import com.atox.usuario.dominio.SessaoUsuario;
import com.atox.usuario.persistencia.dao.PessoaDao;
import com.atox.usuario.persistencia.dao.SessaoUsuarioDao;

import java.util.concurrent.ExecutionException;

public class SessaoNegocio {
    SessaoUsuario sessaoUsuario = SessaoUsuario.getSessao();
    SessaoUsuarioDao sessaoUsuarioDao;

    public SessaoNegocio(FragmentActivity activity){
        sessaoUsuarioDao = ViewModelProviders.of(activity).get(SessaoUsuarioDao.class);
    }

    public Pessoa obterPessoaLogada(){
        return sessaoUsuario.getPessoaLogada();
    }

    // metodo usado para iniciar uma nova sessao
    // Este metodo deve ser chamado apos já ter sido feita a validacao do login no BD
    public void iniciarNovaSessao(Pessoa pessoa) throws AtoxException, ExecutionException, InterruptedException {
        Long idSessao = sessaoUsuarioDao.salvarSessao(sessaoUsuario);
        if (idSessao != null){
            sessaoUsuario.setPessoaLogada(pessoa);
        } else {
            throw new AtoxException("Sessão não pode ser iniciada.");
        }

    }

    public void encerrarSessao(){
        sessaoUsuario.setPessoaLogada(null);
        sessaoUsuarioDao.deletarItem(sessaoUsuario);
    }


}
