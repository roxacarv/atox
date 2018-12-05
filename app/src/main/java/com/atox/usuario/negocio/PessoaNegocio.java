package com.atox.usuario.negocio;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.atox.infra.negocio.ValidaNegocio;
import com.atox.usuario.dominio.Endereco;
import com.atox.usuario.dominio.Pessoa;
import com.atox.usuario.dominio.Usuario;
import com.atox.usuario.persistencia.dao.PessoaDao;

import java.util.concurrent.ExecutionException;

public class PessoaNegocio{

    private PessoaDao pessoaDao;
    private Pessoa pessoa;
    private ValidaNegocio validarNegocio;

    public PessoaNegocio(Context context){
        pessoaDao = ViewModelProviders.of((FragmentActivity) context).get(PessoaDao.class);
        validarNegocio = new ValidaNegocio(context);
        validarNegocio.setPessoa(pessoa);
    }

    public Boolean cadastrar() throws ExecutionException, InterruptedException {

        String email = pessoa.getUsuario().getEmail();
        Boolean emailValido = validarNegocio.validarEmailExiste(email);
        if(emailValido && validarNegocio.ValidarPessoa()){
            pessoa.setPid(pessoaDao.inserirPessoa(pessoa));
            return true;
        }
        return false;
    }

    public Pessoa recuperarPessoa(String idString){
        Long idPessoa = Long.parseLong(idString);
        Pessoa pessoa = pessoaDao.buscarPessoaPorIdDePessoa(idPessoa);
        return pessoa;
    }
    public Long registrarEndereco(Endereco endereco) throws ExecutionException, InterruptedException {
        Long retorno = pessoaDao.inserirEndereco(endereco);
        return retorno;
    }

    public void setPessoa(Pessoa novaPessoa){

        this.pessoa = novaPessoa;
    }

    public Pessoa getPessoa(){
        return pessoa;
    }

}
