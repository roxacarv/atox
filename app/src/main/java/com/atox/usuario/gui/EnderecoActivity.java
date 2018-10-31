package com.atox.usuario.gui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.atox.R;
import com.atox.usuario.dominio.Endereco;
import com.atox.usuario.dominio.Sessao;
import com.atox.usuario.dominio.Usuario;
import com.atox.usuario.negocio.UsuarioNegocio;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class EnderecoActivity extends AppCompatActivity {

    private UsuarioNegocio usuarioNegocio;
    private Sessao sessao;
    private Bundle dadosPessoais;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endereco);
        dadosPessoais = getIntent().getExtras();
    }

    public void backToRegistroScreen(View view){

        Intent registerScreen = new Intent(EnderecoActivity.this, RegistroActivity.class);
        startActivity(registerScreen);

    }

    private void registraNoBancoDeDados() throws ExecutionException, InterruptedException {
        usuarioNegocio = ViewModelProviders.of(this).get(UsuarioNegocio.class);
        Usuario usuario = criarUsuario();
        Long idDeUsuario = usuarioNegocio.inserirUsuario(usuario);
        Endereco endereco = criarEndereco(idDeUsuario);
        Long idDeEndereco = usuarioNegocio.inserirEndereco(endereco);
    }

    private Usuario criarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setTelefone(dadosPessoais.getString("TELEFONE"));
        usuario.setSenha(dadosPessoais.getString("SENHA"));
        usuario.setNome(dadosPessoais.getString("NOME"));
        DateFormat dataFormatada = new SimpleDateFormat("dd/MM/yyyy");
        Date data;
        try {
            data = dataFormatada.parse(dadosPessoais.getString("DATA_NASCIMENTO"));
            usuario.setDataNascimento(data);
        } catch (ParseException pe) {
            System.out.println("ERRO_NA_DATA_NASCIMENTO: " + pe);
        }
        usuario.setEmail(dadosPessoais.getString("EMAIL"));
        return usuario;
    }

    private Endereco criarEndereco(Long idDeUsuario) {
        Endereco endereco = new Endereco();
        endereco.setUsuarioId(idDeUsuario);
        return endereco;
    }

}
