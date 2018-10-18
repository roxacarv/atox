package com.atox.estabelecimentos.dominio;

public enum MetodosPagamento {

    A_VISTA("À vista"),
    CREDITO("Crédito"),
    DEBITO("Débito");


    private String descricao;

    private MetodosPagamento(String descricao){
        this.descricao = descricao;
    }
}