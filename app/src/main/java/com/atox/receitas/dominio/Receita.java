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

    //cluster (0 - frango; 1 - Carne; 2 - Tortas; 3 - chocolate; 4 - Mousses; 5 - bolos)
    private int tipo;

    @ColumnInfo(name = "nome_receita")
    private String nome;

    @ColumnInfo(name = "tipo_receita")
    private Long tipoReceita;

    @Ignore
    private List<SecaoReceita> secoes;

    public Long getRid() {
        return rid;
    }

    public void setRid(Long rid) {
        this.rid = rid;
    }

    public int getTipo(){
        return this.tipo;
    }

    public void setTipo(int tipo){
        this.tipo = tipo;
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

    public Long getTipoReceita() {
        return tipoReceita;
    }

    public void setTipoReceita(Long tipoReceita) {
        this.tipoReceita = tipoReceita;
    }
}
