package com.atox.usuario.negocio;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.atox.infra.negocio.ValidaCadastro;
import com.atox.usuario.dominio.Endereco;
import com.atox.usuario.dominio.Pessoa;
import com.atox.usuario.dominio.Usuario;
import com.atox.usuario.persistencia.dao.PessoaDao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutionException;

public class PessoaNegocio{

    private PessoaDao pessoaDao;
    private Pessoa pessoa;
    private ValidaCadastro validaCadastro;

    public PessoaNegocio(FragmentActivity activity){
        pessoaDao = ViewModelProviders.of(activity).get(PessoaDao.class);
        validaCadastro = new ValidaCadastro();
    }

    public Long cadastrar() throws ExecutionException, InterruptedException {

        String email = this.pessoa.getUsuario().getEmail();
        Usuario usuarioExiste = this.validarSeUsuarioExiste(email);
        if(usuarioExiste == null) {
            Long idDeUsuario = pessoaDao.inserirUsuario(this.pessoa.getUsuario());
            this.pessoa.setUsuarioId(idDeUsuario);
            Log.i("PessoaNegocio", "IdDeUsuarioÉ: " + this.pessoa.getUsuarioId());
            Long idDePessoa = pessoaDao.inserirPessoa(this.pessoa);
            Log.i("PessoaNegocio", "IdDePessoaÉ: " + idDePessoa);
            return idDeUsuario;
        }
        return Long.valueOf(-1);
    }

    public Pessoa recuperarPessoaPorId(Long idUsuario) throws ExecutionException, InterruptedException {
        Pessoa pessoa = pessoaDao.buscarPorIdDeUsuario(idUsuario);
        return pessoa;
    }

    public Usuario isUsuarioCadastrado(Usuario usuarioInformadoNoLogin) throws ExecutionException, InterruptedException {
        Usuario usuarioCadastradoNoBanco = pessoaDao.buscarPorEmaildeUsuario(usuarioInformadoNoLogin.getEmail());
        if (usuarioCadastradoNoBanco == null){
            //usuario nao cadastrado
            return null;
        } else{
            //compara se a senha do usuario informado no login é a mesma do usuário cadastrado
            String senhaInformada = usuarioInformadoNoLogin.getSenha();
            String senhaCadastrada = usuarioCadastradoNoBanco.getSenha();
            if (senhaInformada.equals(senhaCadastrada)){
                //usuario ja esta cadastrado
                return usuarioCadastradoNoBanco;
            } else{
                //senha incorreta para o usuario informado.
                return null;
            }
        }
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

    public boolean validarEmailESenha(String email, String senha) {
        Usuario retorno = pessoaDao.buscarUsuarioPorEmailESenha(email, senha);
        if (retorno == null) {
            return false;
        }
        return true;
    }

    //ver se é realmente necessário (perguntar a Gabriel)
    public boolean ValidarPessoa(Pessoa pessoa) {
        DateFormat dataFormatada = new SimpleDateFormat("dd/MM/yyyy");
        String dataParaValidar;
        dataParaValidar = dataFormatada.format(pessoa.getDataNascimento());

        Usuario usuario = pessoa.getUsuario();

        boolean emailValido = validaCadastro.isEmail(usuario.getEmail());
        boolean senhaValida = validaCadastro.isSenhaValida(usuario.getSenha());
        boolean dataValida = validaCadastro.isDataNascimento(dataParaValidar);

        if(emailValido && senhaValida && dataValida){
            return true;
        }
        return false;
    }
}
