package com.atox.receitas.dominio;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "secao_receita", indices = {@Index("receita_id")},
        foreignKeys = @ForeignKey(entity = Receita.class,
                                  parentColumns = "rid",
                                  childColumns = "receita_id",
                                  onDelete = CASCADE))
public class SecaoReceita {

    @PrimaryKey(autoGenerate = true)
    private Long srid;

    @ColumnInfo(name = "receita_id")
    private Long receitaId;

    @ColumnInfo(name = "nome_secao")
    private String nome;

    @ColumnInfo(name = "conteudo_secao")
    private String conteudo;

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getReceitaId() {
        return receitaId;
    }

    public void setReceitaId(Long receitaId) {
        this.receitaId = receitaId;
    }

    public Long getSrid() {
        return srid;
    }

    public void setSrid(Long srid) {
        this.srid = srid;
    }
}
