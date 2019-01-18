package com.atox.lugar.dominio;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.atox.usuario.dominio.Usuario;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "receita", indices = {@Index("usuario_id")},
        foreignKeys = @ForeignKey(entity = Usuario.class,
                                  parentColumns = "uid",
                                  childColumns = "usuario_id",
                                  onDelete = CASCADE))

public class Receita {

    @PrimaryKey(autoGenerate = true)
    private Long rid;

    @ColumnInfo(name = "usuario_id")
    private Long usuarioId;

    @ColumnInfo(name = "nome_receita")
    private String nome;

    @ColumnInfo(name = "ingredientes")
    private String ingredientes;

    @ColumnInfo(name = "modo_de_preparo")
    private String modoDePreparo;

    @ColumnInfo(name = "outras_informacoes")
    private String outrasInformacoes;

    public Long getRid() {
        return rid;
    }

    public void setRid(Long rid) {
        this.rid = rid;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(String ingredientes) {
        this.ingredientes = ingredientes;
    }

    public String getModoDePreparo() {
        return modoDePreparo;
    }

    public void setModoDePreparo(String modoDePreparo) {
        this.modoDePreparo = modoDePreparo;
    }

    public String getOutrasInformacoes() {
        return outrasInformacoes;
    }

    public void setOutrasInformacoes(String outrasInformacoes) {
        this.outrasInformacoes = outrasInformacoes;
    }
}
