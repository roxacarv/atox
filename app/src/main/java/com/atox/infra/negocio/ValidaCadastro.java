package com.atox.infra.negocio;

import android.text.TextUtils;
import android.util.Patterns;

import com.atox.infra.persistencia.FormataData;

public class ValidaCadastro {

    private static final int tamanhoCPF = 14;
    private static final int tamanhoData = 10;

    public boolean isCampoVazio(String texto){
        return (TextUtils.isEmpty(texto.trim()));
    }

    public boolean isEmail(String texto){
        return (Patterns.EMAIL_ADDRESS.matcher(texto).matches());
    }

    public boolean isSenhaValida(String texto) {
        int tamanhoSenha = 6;
        return !isCampoVazio(texto) && texto.length() >= tamanhoSenha;
    }

    public boolean isCpfValida(String texto) {
        return !isCampoVazio(texto) && texto.length() == tamanhoCPF;
    }

    public boolean isDataNascimento (String data){
        return (FormataData.dataExiste(data) && FormataData.dataMenorOuIgualQueAtual(data)
                && data.length() == tamanhoData);
    }
}
