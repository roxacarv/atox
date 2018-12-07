package com.atox.network.dominio;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Produtor {

    @SerializedName("nome")
    private String nome;

    @SerializedName("cidade")
    private String cidade;

    @SerializedName("entidade")
    private String cooperativa;

    @SerializedName("atividades")
    private String atividades;

    @SerializedName("contato")
    private String email;

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

}
