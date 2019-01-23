package com.atox.atoxlogs;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.atox.atoxlogs.negocio.AtoxLogNegocio;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Entity(tableName = "atox_log")
public class AtoxLog {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "usuario_log_id")
    private Long usuarioId;

    @ColumnInfo(name = "acao")
    private int acao;

    @ColumnInfo(name = "erro")
    private int erro;

    @ColumnInfo(name = "mensagem")
    private String mensagem;

    @ColumnInfo(name = "time_stamp")
    private String timeStamp;

    @Ignore
    private List<AtoxLog> logs;

    @Ignore
    private Queue<AtoxLog> filaDeLogs;

    @Ignore
    private AtoxLogNegocio atoxLogNegocio;

    @ColumnInfo(name = "tipo")
    private int tipo;

    public AtoxLog() {
        this.logs = new ArrayList<>();
        this.filaDeLogs = new LinkedList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public int getAcao() {
        return acao;
    }

    public void setAcao(int acao) {
        this.acao = acao;
    }

    public int getErro() {
        return erro;
    }

    public void setErro(int erro) {
        this.erro = erro;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public List<AtoxLog> getLogs() {
        return logs;
    }

    public void setLogs(List<AtoxLog> logs) {
        this.logs = logs;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setTimeStamp(Date data) {
        SimpleDateFormat formato = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss");
        String dateTimeString = formato.format(data);

        this.timeStamp = dateTimeString;
    }

    public void novoRegistro(int acao, int erro, String mensagem) {
        AtoxLog novoLog = new AtoxLog();
        novoLog.setAcao(acao);
        novoLog.setErro(erro);
        novoLog.setMensagem(mensagem);
        novoLog.setTipo(0);
        this.logs.add(novoLog);
    }

    public void novoRegistro(Long uid, int acao, int erro, String mensagem) {
        AtoxLog novoLog = new AtoxLog();
        novoLog.setUsuarioId(uid);
        novoLog.setAcao(acao);
        novoLog.setErro(erro);
        novoLog.setMensagem(mensagem);
        novoLog.setTipo(0);
        this.logs.add(novoLog);
    }

    public void novoRegistro(Long uid, int acao, String mensagem) {
        AtoxLog novoLog = new AtoxLog();
        novoLog.setUsuarioId(uid);
        novoLog.setAcao(acao);
        novoLog.setMensagem(mensagem);
        novoLog.setTipo(1);
        this.logs.add(novoLog);
    }

    public void mostrarLog(AtoxLog log) {
        Log.i("Atox Log: ", "[" + log.getTimeStamp() + "]" + "Usuário: " + log.getUsuarioId() + " registrou log durante a ação: " +
                log.getAcao() + " - " + log.getMensagem());
    }

    public void mostrarLogDeErro(AtoxLog log) {
        Log.i("Atox Log: ",   "[" + log.getTimeStamp() + "]" + "Usuário: " + log.getUsuarioId() + " registrou o erro " + log.getErro() +  " durante a ação: " +
                log.getAcao() + " - " + log.getMensagem());
    }

    public void mostrarLogsRegistrados() {
        for(AtoxLog log : this.logs) {
            if(log.getTipo() == 0) {
                this.mostrarLog(log);
            } else {
                this.mostrarLogDeErro(log);
            }
        }
    }

    public void empurraRegistrosPraFila() {
        if(!this.logs.isEmpty()) {
            List<AtoxLog> logsParaRemover = new ArrayList<>();
            for (AtoxLog log : this.logs) {
                logsParaRemover.add(log);
                this.filaDeLogs.add(log);
            }
            this.logs.removeAll(logsParaRemover);
        }
    }

    public void salvaRegistrosNoBancoDeDados(FragmentActivity fragmentActivity) {
        if(!this.filaDeLogs.isEmpty()) {
            atoxLogNegocio = new AtoxLogNegocio(fragmentActivity);
            for (int i = 0; i < this.filaDeLogs.size(); i++) {
                AtoxLog logParaRemover = this.filaDeLogs.peek();
                AtoxLog logRemovido = null;
                Long resultadoDaInsercao = null;
                if (logParaRemover != null) {
                    resultadoDaInsercao = atoxLogNegocio.cadastrar(logParaRemover);
                }
                if (resultadoDaInsercao != null) {
                    logRemovido = this.filaDeLogs.poll();
                }
            }
        }

    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
}
