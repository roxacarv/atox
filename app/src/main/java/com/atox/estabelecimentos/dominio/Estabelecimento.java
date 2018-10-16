package com.atox.estabelecimentos.dominio;

import com.atox.usuario.dominio.Endereco;

public class Estabelecimento {

    private String nome;

    private String resumo;

    private Endereco endereco;

    //TODO: criar os atributos metodoPagamento e horarioFuncionamento que são do tipo Enum


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getResumo() {
        return resumo;
    }

    public void setResumo(String resumo) {
        this.resumo = resumo;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

}
