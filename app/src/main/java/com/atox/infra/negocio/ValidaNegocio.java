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
    private Pessoa pessoa;

    public ValidaNegocio(Context context) {
        pessoaDao = ViewModelProviders.of((FragmentActivity) context).get(PessoaDao.class);
        validaCadastro = new ValidaCadastro();
        pessoa = new Pessoa();

    }

    public boolean validarEmailExiste(String email) throws ExecutionException, InterruptedException {
        Usuario retorno = pessoaDao.buscarPorEmaildeUsuario(email);
        if (retorno == null) {
            return true;
        }
        return false;
    }

    public boolean validarEmailESenha(String email, String senha) {
        Usuario retorno = pessoaDao.buscarUsuarioPorEmailESenha(email, senha);
        if (retorno == null) {
            return false;
        }
        return true;
    }
    public boolean ValidarPessoa(){
        Usuario usuario = pessoa.getUsuario();
        boolean emailValido = validaCadastro.isEmail(usuario.getEmail());
        boolean senhaValida = validaCadastro.isSenhaValida(usuario.getSenha());
        boolean dataValida = validaCadastro.isDataNascimento(pessoa.getDataNascimento().toString());
        if(emailValido && senhaValida && dataValida){
            return true;
        }
        return false;
    }
    public Pessoa getPessoa(){
        return pessoa;
    }
    public  void setPessoa(Pessoa novaPessoa){
        pessoa = novaPessoa;
    }
}