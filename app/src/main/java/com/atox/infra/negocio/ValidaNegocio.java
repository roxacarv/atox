package com.atox.infra.negocio;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.atox.usuario.dominio.Pessoa;
import com.atox.usuario.dominio.Usuario;
import com.atox.usuario.persistencia.dao.PessoaDao;

import java.util.concurrent.ExecutionException;

public class ValidaNegocio {

    private PessoaDao pessoaDao;
    private ValidaCadastro validaCadastro;

    public ValidaNegocio(Context context) {
        pessoaDao = ViewModelProviders.of((FragmentActivity) context).get(PessoaDao.class);
        validaCadastro = new ValidaCadastro();

    }


}