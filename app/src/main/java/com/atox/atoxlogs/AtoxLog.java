package com.atox.atoxlogs;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(tableName = "atox_log")
public class AtoxLog {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "usuario_log_id")
    private Long usuarioId;

    @ColumnInfo(name = "acao")
    private String acao;

    @ColumnInfo(name = "erro")
    private String erro;

    @ColumnInfo(name = "mensagem")
    private String mensagem;

    @ColumnInfo(name = "time_stamp")
    private String timeStamp;

    @Ignore
    private List<AtoxLog> logs;

    @ColumnInfo(name = "tipo")
    private int tipo;

    public AtoxLog() {
        this.logs = new ArrayList<>();
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

    public String getAcao() {
        return acao;
    }

    public void setAcao(String acao) {
        this.acao = acao;
    }

    public String getErro() {
        return erro;
    }

    public void setErro(String erro) {
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

    public void novoRegistro(String acao, String erro, String mensagem) {
        AtoxLog novoLog = new AtoxLog();
        novoLog.setAcao(acao);
        novoLog.setErro(erro);
        novoLog.setMensagem(mensagem);
        novoLog.setTipo(0);
        this.logs.add(novoLog);
    }

    public void novoRegistro(Long uid, String acao, String erro, String mensagem) {
        AtoxLog novoLog = new AtoxLog();
        novoLog.setUsuarioId(uid);
        novoLog.setAcao(acao);
        novoLog.setErro(erro);
        novoLog.setMensagem(mensagem);
        novoLog.setTipo(0);
        this.logs.add(novoLog);
    }

    public void novoRegistro(Long uid, String acao, String mensagem) {
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

    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
}
