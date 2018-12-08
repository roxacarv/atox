package com.atox.usuario.negocio;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.atox.infra.negocio.Criptografia;
import com.atox.infra.negocio.ValidaCadastro;
import com.atox.usuario.dominio.Endereco;
import com.atox.usuario.dominio.Pessoa;
import com.atox.usuario.dominio.Usuario;
import com.atox.usuario.persistencia.dao.PessoaDao;

import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutionException;

public class PessoaNegocio {

    private PessoaDao pessoaDao;
    private Pessoa pessoa;

    public PessoaNegocio(FragmentActivity activity){
        pessoaDao = ViewModelProviders.of(activity).get(PessoaDao.class);
    }

    public Long cadastrar() throws ExecutionException, InterruptedException {
        String email = this.pessoa.getUsuario().getEmail();
        Usuario usuarioExiste = this.validarSeUsuarioExiste(email);
        if(usuarioExiste == null) {
            Long idDeUsuario = pessoaDao.inserirUsuario(this.pessoa.getUsuario());
            this.pessoa.setUsuarioId(idDeUsuario);
            Long idDePessoa = pessoaDao.inserirPessoa(this.pessoa);
            return idDeUsuario;
        }
        return Long.valueOf(-1);
    }

    public Pessoa recuperarPessoaPorId(Long idUsuario) throws ExecutionException, InterruptedException {
        Pessoa pessoa = pessoaDao.buscarPorIdDeUsuario(idUsuario);
        return pessoa;
    }

    public Usuario efetuarLogin(String emailParaLogin, String senhaParaLogin) throws ExecutionException, InterruptedException, NoSuchAlgorithmException {
        Usuario usuarioCadastradoNoBanco = pessoaDao.buscarPorEmaildeUsuario(emailParaLogin);
        if (usuarioCadastradoNoBanco == null) {
            return null;
        }
        int resultadoEmail = usuarioCadastradoNoBanco.compararEmail(emailParaLogin);
        String senhaCriptografada = Criptografia.encryptPassword(senhaParaLogin);
        int resultadoSenha = usuarioCadastradoNoBanco.compararSenha(senhaCriptografada);
        Usuario resultado = null;
        if (resultadoEmail == 1 && resultadoSenha == 1){
            resultado = usuarioCadastradoNoBanco;
        }
        return resultado;
    }

    public Long registrarEndereco(Endereco endereco) throws ExecutionException, InterruptedException {
        Long retorno = pessoaDao.inserirEndereco(endereco);
        return retorno;
    }

    public Pessoa getPessoa(){
        return pessoa;
    }

    public void setPessoa(Pessoa novaPessoa){
        this.pessoa = novaPessoa;
    }

    public Usuario validarSeUsuarioExiste(String email) throws ExecutionException, InterruptedException {
        Usuario usuario = pessoaDao.buscarPorEmaildeUsuario(email);
        if (usuario == null) {
            return null;
        }
        return usuario;
    }
}
