package com.atox.usuario.negocio;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.FragmentActivity;

import com.atox.atoxlogs.AtoxLog;
import com.atox.atoxlogs.AtoxMensagem;
import com.atox.usuario.dominio.Pessoa;
import com.atox.usuario.dominio.SessaoUsuario;
import com.atox.usuario.persistencia.dao.SessaoUsuarioDao;

import java.util.concurrent.ExecutionException;

public class SessaoNegocio {
    SessaoUsuario sessaoUsuario;
    SessaoUsuarioDao sessaoUsuarioDao;

    public SessaoNegocio(FragmentActivity activity) {
        this.sessaoUsuarioDao = ViewModelProviders.of(activity).get(SessaoUsuarioDao.class);
        sessaoUsuario = SessaoUsuario.getInstance();
    }

    // metodo usado para iniciar uma nova sessao
    // Este metodo deve ser chamado apos já ter sido feita a validacao do login no BD
    public Long iniciarNovaSessao(Pessoa pessoa) {
        AtoxLog log = new AtoxLog();

        this.sessaoUsuario.setPessoaLogada(pessoa);
        this.sessaoUsuario.setUsuarioLogado(pessoa.getUsuario());
        Long idSessao = null;
        try {
            idSessao = sessaoUsuarioDao.salvarSessao(this.sessaoUsuario);
        } catch (ExecutionException e) {
            log = new AtoxLog();
            log.novoRegistro(AtoxMensagem.ACAO_INICIAR_NOVA_SESSAO,
                    AtoxMensagem.ERRO_INSERIR_REGISTRO_NO_BANCO,
                    "Um erro de execução ocorreu na hora de iniciar uma nova sessão: " + e.getMessage());
            log.empurraRegistrosPraFila();
        } catch (InterruptedException e) {
            log = new AtoxLog();
            log.novoRegistro(AtoxMensagem.ACAO_INICIAR_NOVA_SESSAO,
                    AtoxMensagem.ERRO_INSERIR_REGISTRO_NO_BANCO,
                    "Uma interrupção foi registrado na hora de iniciar uma nova sessão: " + e.getMessage());
            log.empurraRegistrosPraFila();
        }

        if (idSessao != null){
            this.sessaoUsuario.setSid(idSessao);
            this.sessaoUsuario.setPessoaLogada(pessoa);
        }

        return idSessao;
    }

    public Pessoa restaurarSessao() {
        AtoxLog log = new AtoxLog();

        Pessoa pessoaLogada = null;
        try {
            pessoaLogada = this.sessaoUsuarioDao.restaurarSessao();
        } catch (ExecutionException e) {
            log = new AtoxLog();
            log.novoRegistro(AtoxMensagem.ACAO_RESTAURAR_SESSAO,
                    AtoxMensagem.ERRO_BUSCAR_REGISTRO_NO_BANCO,
                    "Um erro de execução ocorreu na hora de restaurar a sessão: " + e.getMessage());
            log.empurraRegistrosPraFila();
        } catch (InterruptedException e) {
            log = new AtoxLog();
            log.novoRegistro(AtoxMensagem.ACAO_RESTAURAR_SESSAO,
                    AtoxMensagem.ERRO_BUSCAR_REGISTRO_NO_BANCO,
                    "Uma interrupção foi registrado na hora de restaurar a sessão: " + e.getMessage());
            log.empurraRegistrosPraFila();
        }

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
