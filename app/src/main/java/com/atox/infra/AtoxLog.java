package com.atox.infra;

import java.text.SimpleDateFormat;
import java.util.Date;


public class AtoxLog {


    private long id;

    private long usuarioId;

    private long acao;

    private String erro;

    private String mensagem;

    private String timeStamp;



    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public long getAcao() {
        return acao;
    }

    public void setAcao(int acao) {
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

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date data) {
        SimpleDateFormat formato = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss");
        String dateTimeString = formato.format(data);

        this.timeStamp = dateTimeString;
    }
}
