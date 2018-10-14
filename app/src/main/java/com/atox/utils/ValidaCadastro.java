package com.atox.utils;

import android.text.TextUtils;
import android.util.Patterns;

public class ValidaCadastro {
    private final int TAMANHO_SENHA = 6;
    private final int TAMANHO_CPF = 14;
    private final int TAMANHO_DATA = 10;

    public boolean isCampoVazio(String texto){
        return (texto.trim().isEmpty() || TextUtils.isEmpty(texto));
    }

    public boolean isEmail(String texto){
        return (Patterns.EMAIL_ADDRESS.matcher(texto).matches());
    }

    public boolean isSenhaValida(String texto) {
        return !isCampoVazio(texto) && texto.length() >= TAMANHO_SENHA;
    }

    public boolean isCpfValida(String texto) {
        return !isCampoVazio(texto) && texto.length() == TAMANHO_CPF;
    }

}
