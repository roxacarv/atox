package com.atox.receitas.dominio;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.atox.usuario.dominio.Usuario;

import java.util.List;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "receita")
public class Receita {

    @PrimaryKey(autoGenerate = true)
    private Long rid;

    @ColumnInfo(name = "nome_receita")
    private String nome;

    @Ignore
    private List<SecaoReceita> secoes;

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

    public List<SecaoReceita> getSecoes() {
        return secoes;
    }

    public void setSecoes(List<SecaoReceita> secoes) {
        this.secoes = secoes;
    }
}
