package com.atox.navegacao.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.atox.R;
import com.atox.atoxlogs.AtoxLog;
import com.atox.atoxlogs.AtoxMensagem;
import com.atox.usuario.dominio.Endereco;
import com.atox.usuario.dominio.Pessoa;
import com.atox.usuario.dominio.SessaoUsuario;
import com.atox.usuario.dominio.Usuario;
import com.atox.usuario.gui.EditarPerfilActivity;
import com.atox.usuario.gui.LoginActivity;
import com.atox.usuario.negocio.SessaoNegocio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;

public class PerfilFragment extends Fragment {

    private static final String TAG = InicioFragment.class.getName();
    private SessaoUsuario sessaoUsuario;
    private TextView textViewPerfilNomeUsuario;
    private TextView textViewPerfilDataNascimento;
    private TextView textViewPerfilEndereco;
    private TextView textViewPerfilEmail;
    private TextView textViewPerfilTelefone;
    private String dataFinal;
    private SessaoNegocio sessaoNegocio;
    private Usuario usuarioPerfil;
    private Pessoa pessoaPerfil;
    private Endereco enderecoPerfil;
    private Button btnSair;
    private Intent homeScreen;
    private Button btnEditar;
    private Intent editActivity;
    private AtoxLog log;
    private ImageView image;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        sessaoNegocio = new SessaoNegocio(this.getActivity());
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        sessaoUsuario = SessaoUsuario.getInstance();
        pessoaPerfil = sessaoUsuario.getPessoaLogada();
        usuarioPerfil = pessoaPerfil.getUsuario();
        enderecoPerfil = pessoaPerfil.getEndereco();

        textViewPerfilNomeUsuario = (TextView) view.findViewById(R.id.textViewPerfilNomeUsuario);
        textViewPerfilDataNascimento = (TextView) view.findViewById(R.id.textViewPerfilDataNascimento);
        textViewPerfilEndereco = (TextView) view.findViewById(R.id.textViewPerfilEndereco);
        textViewPerfilEmail = (TextView) view.findViewById(R.id.textViewPerfilEmail);
        textViewPerfilTelefone = (TextView) view.findViewById(R.id.textViewPerfilTelefone);

        image = (ImageView)view.findViewById(R.id.imageView2);

        String caminho = pessoaPerfil.getCaminhoDoAvatar();

        log = new AtoxLog();

        if(caminho != null) {
            carregarImagem(caminho);
        }

        editActivity = new Intent(this.getActivity(), EditarPerfilActivity.class);
        btnEditar = (Button)view.findViewById(R.id.btnEditarDados);
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(editActivity);
            }
        });


        String nome = pessoaPerfil.getNome();
        String telefone = pessoaPerfil.getTelefone();
        String email = usuarioPerfil.getEmail();

        textViewPerfilNomeUsuario.setText(nome);
        textViewPerfilEmail.setText(email);
        textViewPerfilTelefone.setText(telefone);

        homeScreen = new Intent(this.getActivity(), LoginActivity.class);
        btnSair = (Button)view.findViewById(R.id.btnSairDoApp);
        btnSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessaoNegocio.encerrarSessao();
                startActivity(homeScreen);
            }
        });


        if(sessaoUsuario.getPessoaLogada().getEndereco() != null) {
            String bairro = enderecoPerfil.getBairro();
            String cidade = enderecoPerfil.getCidade();
            String estado = enderecoPerfil.getEstado();
            String endereco = bairro + ", " + cidade + " - " + estado;
            textViewPerfilEndereco.setText(endereco);
        }

        dataFinal = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy").format(pessoaPerfil.getDataNascimento());
        textViewPerfilDataNascimento.setText(dataFinal);
        // Inflate the layout for this fragment
        return view;

    }

    private void carregarImagem(String path){
        try {
            File arquivoDaImagem = new File(path, "profile.jpg");
            Bitmap arquivoEmBitmap = BitmapFactory.decodeStream(new FileInputStream(arquivoDaImagem));
            image.setImageBitmap(arquivoEmBitmap);
        } catch (FileNotFoundException e) {
            log.novoRegistro(usuarioPerfil.getUid(),
                    AtoxMensagem.ACAO_SALVAR_IMAGEM_NA_MEMORIA_INTERNA,
                    AtoxMensagem.ERRO_AO_ACESSAR_A_MEMORIA_INTERNA,
                    "Não foi possível salvar a imagem na memória interna: " + e.getMessage());
            log.empurraRegistrosPraFila();
        }
    }

    public void voltarParaTelaDeLogin(View view) {
        //mudar de tela
    }


}
