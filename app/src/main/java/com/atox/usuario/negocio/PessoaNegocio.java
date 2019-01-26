package com.atox.usuario.negocio;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.atox.atoxlogs.AtoxLog;
import com.atox.atoxlogs.AtoxMensagem;
import com.atox.infra.negocio.Criptografia;
import com.atox.usuario.dominio.Endereco;
import com.atox.usuario.dominio.Pessoa;
import com.atox.usuario.dominio.Usuario;
import com.atox.usuario.persistencia.dao.PessoaDao;

import java.util.concurrent.ExecutionException;

public class PessoaNegocio {

    private PessoaDao pessoaDao;
    private Pessoa pessoa;

    public PessoaNegocio(FragmentActivity activity){
        pessoaDao = ViewModelProviders.of(activity).get(PessoaDao.class);
    }

    public Long cadastrar() {
        String email = this.pessoa.getUsuario().getEmail();
        Usuario usuarioExiste = this.validarSeUsuarioExiste(email);
        if(usuarioExiste == null) {
            Long idDeUsuario = cadastraUsuario();
            Log.i("PESSOA NEGOCIO", "ID DE USUARIO É: " + idDeUsuario);
            Log.i("PESSOA NEGOCIO", "PESSOA É: " + this.pessoa);
            this.pessoa.setUsuarioId(idDeUsuario);
            Long idDePessoa = null;
            if(idDeUsuario != null) {
                idDePessoa = cadastraPessoa();
            }
            return idDeUsuario;
        }
        return Long.valueOf(-1);
    }

    public Long cadastraPessoa() {
        AtoxLog log = new AtoxLog();

        Long idDePessoa = null;
        try {
            idDePessoa = pessoaDao.inserirPessoa(this.pessoa);
        } catch (ExecutionException e) {
            log = new AtoxLog();
            log.novoRegistro(AtoxMensagem.ACAO_INSERIR_REGISTRO_NO_BANCO,
                    AtoxMensagem.ERRO_INSERIR_REGISTRO_NO_BANCO,
                    "Um erro de execução ocorreu ao tentar inserir uma nova pessoa no banco: " + e.getMessage());
            log.empurraRegistrosPraFila();
        } catch (InterruptedException e) {
            log = new AtoxLog();
            log.novoRegistro(AtoxMensagem.ACAO_INSERIR_REGISTRO_NO_BANCO,
                    AtoxMensagem.ERRO_INSERIR_REGISTRO_NO_BANCO,
                    "Uma interrupção foi registrado ao tentar inserir uma pessoa no banco: " + e.getMessage());
            log.empurraRegistrosPraFila();
            Thread.currentThread().interrupt();
        }

        return idDePessoa;
    }

    public Long cadastraUsuario() {
        AtoxLog log = new AtoxLog();

        Long idDeUsuario = null;

        try {
            Log.i("PESSOA NEGOCIO", "VAI TENTAR INSERIR USUARIO: " + this.pessoa.getUsuario());
            idDeUsuario = pessoaDao.inserirUsuario(this.pessoa.getUsuario());
            Log.i("PESSOA NEGOCIO", "O ID DE USUÁRIO DO BANCO É: " + idDeUsuario);
        } catch (ExecutionException e) {
            log = new AtoxLog();
            log.novoRegistro(AtoxMensagem.ACAO_INSERIR_REGISTRO_NO_BANCO,
                    AtoxMensagem.ERRO_INSERIR_REGISTRO_NO_BANCO,
                    "Um erro de execução ocorreu ao tentar inserir um novo usuario no banco: " + e.getMessage());
            log.empurraRegistrosPraFila();
        } catch (InterruptedException e) {
            Log.i("PESSOA NEGOCIO", "DEU EXCEPTION INTERRUPTED");
            log = new AtoxLog();
            log.novoRegistro(AtoxMensagem.ACAO_INSERIR_REGISTRO_NO_BANCO,
                    AtoxMensagem.ERRO_INSERIR_REGISTRO_NO_BANCO,
                    "Uma interrupção foi registrado ao tentar inserir um usuario no banco: " + e.getMessage());
            log.empurraRegistrosPraFila();
            Thread.currentThread().interrupt();
        }

        return idDeUsuario;
    }

    public Long atualizar() {
        AtoxLog log = new AtoxLog();

        Long resultadoDaAtualizacaoDePessoa = null;
        Long resultadoDaAtualizacaoDeUsuario = null;
        try {
            resultadoDaAtualizacaoDePessoa = pessoaDao.atualizar(this.pessoa);
            if(resultadoDaAtualizacaoDePessoa != null) {
                resultadoDaAtualizacaoDeUsuario = pessoaDao.atualizarUsuario(this.pessoa.getUsuario());
            }
        } catch (ExecutionException e) {
            log = new AtoxLog();
            log.novoRegistro(AtoxMensagem.ACAO_ATUALIZAR_REGISTRO_NO_BANCO,
                    AtoxMensagem.ERRO_ATUALIZAR_REGISTRO_NO_BANCO,
                    "Um erro de execução ocorreu ao tentar atualizar uma nova pessoa no banco: " + e.getMessage());
            log.empurraRegistrosPraFila();
        } catch (InterruptedException e) {
            log = new AtoxLog();
            log.novoRegistro(AtoxMensagem.ACAO_ATUALIZAR_REGISTRO_NO_BANCO,
                    AtoxMensagem.ERRO_ATUALIZAR_REGISTRO_NO_BANCO,
                    "Uma interrupção foi registrado ao tentar atualizar uma pessoa no banco: " + e.getMessage());
            log.empurraRegistrosPraFila();
            Thread.currentThread().interrupt();
        }

        return resultadoDaAtualizacaoDeUsuario;
    }

    public Pessoa recuperarPessoaPorId(Long idUsuario) {
        AtoxLog log = new AtoxLog();

        Pessoa pessoa = null;
        try {
            pessoa = pessoaDao.buscarPorIdDeUsuario(idUsuario);
        } catch (ExecutionException e) {
            log = new AtoxLog();
            log.novoRegistro(AtoxMensagem.ACAO_RECUPERAR_PESSOA_NO_BANCO,
                    AtoxMensagem.ERRO_BUSCAR_REGISTRO_NO_BANCO,
                    "Um erro de execução ocorreu ao tentar buscar uma nova pessoa no banco: " + e.getMessage());
            log.empurraRegistrosPraFila();
        } catch (InterruptedException e) {
            log = new AtoxLog();
            log.novoRegistro(AtoxMensagem.ACAO_ATUALIZAR_REGISTRO_NO_BANCO,
                    AtoxMensagem.ERRO_ATUALIZAR_REGISTRO_NO_BANCO,
                    "Uma interrupção foi registrado ao tentar buscar uma pessoa no banco: " + e.getMessage());
            log.empurraRegistrosPraFila();
            Thread.currentThread().interrupt();
        }

        return pessoa;
    }

    public Usuario efetuarLogin(String emailParaLogin, String senhaParaLogin) {
        AtoxLog log = new AtoxLog();

        Usuario usuarioCadastradoNoBanco = null;
        try {
            usuarioCadastradoNoBanco = pessoaDao.buscarPorEmaildeUsuario(emailParaLogin);
        } catch (ExecutionException e) {
            log = new AtoxLog();
            log.novoRegistro(AtoxMensagem.ACAO_EFETUAR_LOGIN,
                    AtoxMensagem.ERRO_BUSCAR_REGISTRO_NO_BANCO,
                    "Um erro de execução ocorreu ao tentar buscar um usuário no banco: " + e.getMessage());
            log.empurraRegistrosPraFila();
        } catch (InterruptedException e) {
            log = new AtoxLog();
            log.novoRegistro(AtoxMensagem.ACAO_EFETUAR_LOGIN,
                    AtoxMensagem.ERRO_ATUALIZAR_REGISTRO_NO_BANCO,
                    "Uma interrupção foi registrado ao tentar buscar um usuário no banco: " + e.getMessage());
            log.empurraRegistrosPraFila();
            Thread.currentThread().interrupt();
        }

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

    public Long registrarEndereco(Endereco endereco) {
        Long retorno = null;
        try {
            retorno = pessoaDao.inserirEndereco(endereco);
        } catch (ExecutionException e) {
            AtoxLog log = new AtoxLog();
        } catch (InterruptedException e) {
            AtoxLog log = new AtoxLog();
            Thread.currentThread().interrupt();
        }
        return retorno;
    }

    public Pessoa getPessoa(){
        return pessoa;
    }

    public void setPessoa(Pessoa novaPessoa){
        this.pessoa = novaPessoa;
    }

    public Usuario validarSeUsuarioExiste(String email) {
        Usuario usuario = null;
        try {
            usuario = pessoaDao.buscarPorEmaildeUsuario(email);
        } catch (ExecutionException e) {
            AtoxLog log = new AtoxLog();
        } catch (InterruptedException e) {
            AtoxLog log = new AtoxLog();
            Thread.currentThread().interrupt();
        }
        if (usuario == null) {
            return null;
        }
        return usuario;
    }
}
