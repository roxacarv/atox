package com.atox.network.dominio;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "produtor")
public class Produtor {

    @PrimaryKey(autoGenerate = true)
    private long prid;

    @ColumnInfo(name = "nome")
    @SerializedName("nome")
    private String nome;

    @ColumnInfo(name = "cidade")
    @SerializedName("cidade")
    private String cidade;

    @ColumnInfo(name = "cooperativa")
    @SerializedName("entidade")
    private String cooperativa;

    @ColumnInfo(name = "atividades")
    @SerializedName("atividades")
    private String atividades;

    @ColumnInfo(name = "contato")
    @SerializedName("contato")
    private String email;

    @ColumnInfo(name = "telefone")
    @SerializedName("telefone")
    private String telefone;

    public Produtor(String nome, String cidade, String cooperativa, String atividades, String email, String telefone) {
        this.nome = nome;
        this.cidade = cidade;
        this.cooperativa = cooperativa;
        this.atividades = atividades;
        this.email = email;
        this.telefone = telefone;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getCooperativa() {
        return cooperativa;
    }

    public void setCooperativa(String cooperativa) {
        this.cooperativa = cooperativa;
    }

    public String getAtividades() {
        return atividades;
    }

    public void setAtividades(String atividades) {
        this.atividades = atividades;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public long getPrid() {
        return prid;
    }

    public void setPrid(long prid) {
        this.prid = prid;
    }
}
