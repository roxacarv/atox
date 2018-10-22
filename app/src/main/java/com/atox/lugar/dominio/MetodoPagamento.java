package com.atox.lugar.dominio;

public enum MetodoPagamento {

    A_VISTA("À vista"),
    CREDITO("Crédito"),
    DEBITO("Débito");


    private String descricao;

    private MetodoPagamento(String descricao){
        this.descricao = descricao;
    }
}