package com.atox.atoxlogs;

public class AtoxException extends Exception {

    public AtoxException(String mensagem){
        super(mensagem);
    }

    public AtoxException(String mensagem, Throwable causa){
        super(mensagem, causa);
    }

}
