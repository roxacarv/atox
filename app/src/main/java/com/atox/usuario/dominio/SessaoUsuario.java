package com.atox.usuario.dominio;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "sessao_usuario")
public class SessaoUsuario {

    @Ignore
    private static SessaoUsuario INSTANCE;

    @PrimaryKey(autoGenerate = true)
    private long sid;

    @Ignore
    private Usuario usuarioLogado;

    @Ignore
    private Pessoa pessoaLogada;

    public static SessaoUsuario getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new SessaoUsuario();
        }
        return INSTANCE;
    }

    public long getSid() {
        return sid;
    }

    public void setSid(long sid) {
        this.sid = sid;
    }

    public void finalizarSessao() {
        this.setUsuarioLogado(null);
    }

    public Long getIdDeUsuario() {
        return this.usuarioLogado.getUid();
    }

    public Long getIdDePessoa() {
        return this.pessoaLogada.getPid();
    }

    public Pessoa getPessoaLogada() {
        return pessoaLogada;
    }

    public void setPessoaLogada(Pessoa pessoaLogada) {
        this.pessoaLogada = pessoaLogada;
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public void setUsuarioLogado(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
    }
}
