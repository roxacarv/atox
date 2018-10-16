package com.atox.usuario.dominio;

public class Sessao {

    private final Usuario USUARIO_ATIVO;

    public Sessao(Usuario usuario_ativo) {
        USUARIO_ATIVO = usuario_ativo;
    }


    public void iniciarSessao(){

    }

    public void finalizarSessao(){

    }

    public Usuario getUSUARIO_ATIVO() {
        return USUARIO_ATIVO;
    }

}
