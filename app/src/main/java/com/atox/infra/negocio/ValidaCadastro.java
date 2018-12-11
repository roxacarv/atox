package com.atox.infra.negocio;

import android.text.TextUtils;
import android.util.Patterns;

import com.atox.infra.persistencia.FormataData;

public class ValidaCadastro {

    public boolean isCampoVazio(String texto){
        return (TextUtils.isEmpty(texto.trim()));
    }

    public boolean isEmail(String texto){
        return (!Patterns.EMAIL_ADDRESS.matcher(texto).matches());
    }

    public boolean isSenhaValida(String texto) {
        int TAMANHO_SENHA = 6;
        return isCampoVazio(texto) || texto.length() < TAMANHO_SENHA;
    }

    public boolean isCpfValida(String texto) {
        int TAMANHO_CPF = 14;
        return !isCampoVazio(texto) && texto.length() == TAMANHO_CPF;
    }

    public boolean isDataNascimento (String data){

        int TAMANHO_DATA = 10;
        return (FormataData.dataExiste(data) && FormataData.dataMenorOuIgualQueAtual(data)
                && data.length() == TAMANHO_DATA);
    }
}
