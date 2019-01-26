package com.atox.atoxlogs.negocio;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.FragmentActivity;

import com.atox.atoxlogs.AtoxLog;
import com.atox.atoxlogs.AtoxMensagem;
import com.atox.atoxlogs.persistencia.dao.AtoxLogDao;
import com.atox.usuario.dominio.SessaoUsuario;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class AtoxLogNegocio {

    private AtoxLogDao atoxLogDao;
    private List<AtoxLog> atoxLog;
    private SessaoUsuario sessaoUsuario;

    public AtoxLogNegocio(FragmentActivity fragmentActivity) {
        atoxLogDao = ViewModelProviders.of(fragmentActivity).get(AtoxLogDao.class);
        sessaoUsuario = SessaoUsuario.getInstance();
    }

    public Long cadastrar(AtoxLog atoxLog) {
        AtoxLog log;
        Long idDoUsuario = sessaoUsuario.getIdDeUsuario();
        Long idDoLog = null;
        try {
            idDoLog = atoxLogDao.inserirLog(atoxLog);
        } catch (ExecutionException e) {
            log = new AtoxLog();
            log.novoRegistro(idDoUsuario,
                    AtoxMensagem.ACAO_SALVAR_LOGS_NO_BANCO,
                    AtoxMensagem.ERRO_INSERIR_REGISTRO_NO_BANCO,
                    "Um erro de execução ocorreu ao tentar salvar o log: " + e.getMessage());
            log.empurraRegistrosPraFila();
        } catch (InterruptedException e) {
            log = new AtoxLog();
            log.novoRegistro(idDoUsuario,
                    AtoxMensagem.ACAO_SALVAR_LOGS_NO_BANCO,
                    AtoxMensagem.ERRO_INSERIR_REGISTRO_NO_BANCO,
                    "Uma interrupção foi registrado ao tentar salvar o log: " + e.getMessage());
            log.empurraRegistrosPraFila();
            Thread.currentThread().interrupt();
        }

        return idDoLog;
    }

    public List<Long> cadastrarMultiplosLogs(List<AtoxLog> logsParaInserir) {
        AtoxLog log;

        Long idDoUsuario = sessaoUsuario.getIdDeUsuario();
        AtoxLog arrayDeLogs[] = new AtoxLog[logsParaInserir.size()];
        arrayDeLogs = logsParaInserir.toArray(arrayDeLogs);
        //tentando inserir logs
        List<Long> listaDeIds = null;
        try {
            listaDeIds = atoxLogDao.inserirTodosLogs(arrayDeLogs);
        } catch (ExecutionException e) {
            log = new AtoxLog();
            log.novoRegistro(idDoUsuario,
                    AtoxMensagem.ACAO_SALVAR_LOGS_NO_BANCO,
                    AtoxMensagem.ERRO_INSERIR_REGISTRO_NO_BANCO,
                    "Um erro de execução ocorreu ao tentar salvar o log: " + e.getMessage());
            log.empurraRegistrosPraFila();
        } catch (InterruptedException e) {
            log = new AtoxLog();
            log.novoRegistro(idDoUsuario,
                    AtoxMensagem.ACAO_SALVAR_LOGS_NO_BANCO,
                    AtoxMensagem.ERRO_INSERIR_REGISTRO_NO_BANCO,
                    "Uma interrupção foi registrado ao tentar salvar o log: " + e.getMessage());
            log.empurraRegistrosPraFila();
            Thread.currentThread().interrupt();
        }

        return listaDeIds;
    }

    public List<AtoxLog> buscarLogsDoUsuario(long usuarioId) {
        AtoxLog log = new AtoxLog();

        Long idDoUsuario = sessaoUsuario.getIdDeUsuario();
        List<AtoxLog> logsDeRetorno = null;
        try {
            logsDeRetorno = atoxLogDao.buscarLogsDoUsuario(usuarioId);
        } catch (ExecutionException e) {
            log = new AtoxLog();
            log.novoRegistro(idDoUsuario,
                    AtoxMensagem.ACAO_BUSCAR_LOGS_NO_BANCO,
                    AtoxMensagem.ERRO_BUSCAR_REGISTRO_NO_BANCO,
                    "Um erro de execução ocorreu durante a busca: " + e.getMessage());
        } catch (InterruptedException e) {
            log = new AtoxLog();
            log.novoRegistro(idDoUsuario,
                    AtoxMensagem.ACAO_BUSCAR_LOGS_NO_BANCO,
                    AtoxMensagem.ERRO_BUSCAR_REGISTRO_NO_BANCO,
                    "Uma interrupção foi registrado durante a busca: " + e.getMessage());
            Thread.currentThread().interrupt();
        }

        if(!log.getLogs().isEmpty()) {
            //salvar logs em um XML/TXT
        }

        return logsDeRetorno;
    }

    public List<AtoxLog> getAtoxLog() {
        return atoxLog;
    }

    public void setAtoxLog(List<AtoxLog> atoxLog) {
        this.atoxLog = atoxLog;
    }
}
