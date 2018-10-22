package com.atox.usuario.dominio;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "sessao")
public class Sessao {

    @Ignore
    private static Sessao INSTANCE;

    @PrimaryKey(autoGenerate = true)
    private long sid;

    @ColumnInfo(name = "sessao_uid")
    private static Long USUARIO_ID;

    @Ignore
    private static Usuario USUARIO_ATIVO;

    public static Sessao getSessao() {
        if(INSTANCE == null)
            INSTANCE = new Sessao();
        return INSTANCE;
    }

    public static Usuario getUsuario() {
        return USUARIO_ATIVO;
    }

    public static void setUsuario(Usuario usuario) {
        USUARIO_ATIVO = usuario;
    }

    public static Long getUsuarioId() {
        return USUARIO_ID;
    }

    public static void setUsuarioId(Long usuarioId) {
        USUARIO_ID = usuarioId;
    }

    public long getSid() {
        return sid;
    }

    public void setSid(long sid) {
        this.sid = sid;
    }

    public void finalizarSessao() {
        this.setUsuario(null);
        this.setUsuarioId(null);
    }
}
