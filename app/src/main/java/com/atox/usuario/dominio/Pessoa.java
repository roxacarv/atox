package com.atox.usuario.dominio;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Ignore;

import java.util.Date;

public class Pessoa {

    @ColumnInfo(name = "nome")
    private String nome;

    @ColumnInfo(name = "data_nascimento")
    private Date dataNascimento;

    @ColumnInfo(name = "cpf")
    private String cpf;

    @Ignore
    private Endereco endereco;

    @ColumnInfo(name = "telefone")
    private String telefone;


    public String getNome() {
        return nome;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento){
        this.dataNascimento = dataNascimento;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

}
