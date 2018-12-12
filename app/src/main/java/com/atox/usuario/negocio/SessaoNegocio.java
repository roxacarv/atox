package com.atox.usuario.negocio;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.atox.infra.AtoxException;
import com.atox.usuario.dominio.Pessoa;
import com.atox.usuario.dominio.SessaoUsuario;
import com.atox.usuario.persistencia.dao.PessoaDao;
import com.atox.usuario.persistencia.dao.SessaoUsuarioDao;

import java.util.concurrent.ExecutionException;

public class SessaoNegocio {
    SessaoUsuario sessaoUsuario;
    SessaoUsuarioDao sessaoUsuarioDao;

    public SessaoNegocio(FragmentActivity activity) {
        this.sessaoUsuarioDao = ViewModelProviders.of(activity).get(SessaoUsuarioDao.class);
        sessaoUsuario = SessaoUsuario.getSessao();
    }

    // metodo usado para iniciar uma nova sessao
    // Este metodo deve ser chamado apos já ter sido feita a validacao do login no BD
    public void iniciarNovaSessao(Pessoa pessoa) throws AtoxException, ExecutionException, InterruptedException {
        this.sessaoUsuario.setPessoaLogada(pessoa);
        this.sessaoUsuario.setUsuarioLogado(pessoa.getUsuario());
        Long idSessao = sessaoUsuarioDao.salvarSessao(this.sessaoUsuario);
        if (idSessao != null){
            this.sessaoUsuario.setSid(idSessao);
            this.sessaoUsuario.setPessoaLogada(pessoa);
        } else {
            throw new AtoxException("Sessão não pode ser iniciada.");
        }

    }

    public Pessoa restaurarSessao() throws ExecutionException, InterruptedException {
        Pessoa pessoaLogada = this.sessaoUsuarioDao.restaurarSessao();
        if(pessoaLogada == null) {
            return null;
        }
        return pessoaLogada;
    }

    public void encerrarSessao() {
        sessaoUsuarioDao.deletarItem(sessaoUsuario);
        sessaoUsuario.setPessoaLogada(null);
    }


}
