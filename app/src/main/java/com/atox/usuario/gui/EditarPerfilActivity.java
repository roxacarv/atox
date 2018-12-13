package com.atox.usuario.gui;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.atox.R;
import com.atox.atoxlogs.AtoxLog;
import com.atox.atoxlogs.AtoxMensagem;
import com.atox.infra.negocio.Criptografia;
import com.atox.infra.negocio.ValidaCadastro;
import com.atox.navegacao.activities.MenuActivity;
import com.atox.usuario.dominio.Endereco;
import com.atox.usuario.dominio.Pessoa;
import com.atox.usuario.dominio.SessaoUsuario;
import com.atox.usuario.dominio.Usuario;
import com.atox.usuario.negocio.PessoaNegocio;
import com.google.android.gms.location.places.Place;
import com.shishank.autocompletelocationview.interfaces.OnQueryCompleteListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EditarPerfilActivity extends AppCompatActivity implements OnQueryCompleteListener {

    private SimpleDateFormat sdf;
    private EditText mNome;
    private EditText mTelefone;
    private EditText mData;
    private EditText mEmail;
    private EditText mSenha;
    private EditText mSenhaConfirm;
    private AutoCompleteTextView editTextAddress;
    private SessaoUsuario sessaoUsuario;
    private ImageView image;
    private PessoaNegocio pessoaNegocio;
    private Button buttonSalvar;
    private Pessoa pessoaLogada;
    private Usuario usuarioLogado;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);
        inicializarCampos();
        image = (ImageView) findViewById(R.id.imageNew);
        buttonSalvar = (Button) findViewById(R.id.salvarPerfil);

        pessoaNegocio = new PessoaNegocio(this);

        pessoaLogada = sessaoUsuario.getPessoaLogada();
        mNome.setText(pessoaLogada.getNome());
        mTelefone.setText(pessoaLogada.getTelefone());
        String dataNascimentoFormatada = sdf.format(pessoaLogada.getDataNascimento());
        mData.setText(dataNascimentoFormatada);

        usuarioLogado = pessoaLogada.getUsuario();
        mEmail.setText(usuarioLogado.getEmail());
    }

    private Endereco criarEndereco(Long idDePessoa) {
        Address testLocation = getAddress(editTextAddress.getText().toString());
        Endereco endereco = new Endereco();
        endereco.setPessoaId(idDePessoa);
        endereco.setCidade(testLocation.getSubAdminArea());
        endereco.setBairro(testLocation.getSubLocality());
        endereco.setCep(testLocation.getPostalCode());
        endereco.setEstado(testLocation.getAdminArea());
        endereco.setPais(testLocation.getCountryName());
        endereco.setLogradouro(testLocation.getThoroughfare());
        return endereco;
    }

    public Address getAddress(String completeAddress) {
        Geocoder geocoder;
        List addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocationName(completeAddress, 1);
            return (Address) addresses.get(0);
        } catch (IOException e) {
            AtoxLog log = new AtoxLog();
            log.novoRegistro(AtoxMensagem.ACAO_REQUISITAR_ENDERECO_API_GOOGLE,
                    AtoxMensagem.ERRO_NO_USO_DE_API,
                    "Um erro ocorreu na requisição da API da Google: " + e.getMessage());
            log.empurraRegistrosPraFila();
        }

        return null;
    }

    public void imagemClick(View view) {
        carregarImagem();
    }

    private void carregarImagem() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent, "selecione a aplicação"), 10);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_OK) {
            Uri path = data.getData();
            image.setImageURI(path);
        }
    }

    public void inicializarCampos() {
        sdf = new SimpleDateFormat("dd/MM/yyyy");
        mNome = (EditText) findViewById(R.id.editTextEditarNome);
        mTelefone = (EditText) findViewById(R.id.editTextEditarTelefone);
        mData = (EditText) findViewById(R.id.editTextEditarDataNascimento);
        mEmail = (EditText) findViewById(R.id.editTextEditarEmail);
        mSenha = (EditText) findViewById(R.id.editTextEditarSenha);
        mSenhaConfirm = (EditText) findViewById(R.id.editTextEditarConfirmacaoSenha);
        editTextAddress = (AutoCompleteTextView) findViewById(R.id.editTextEditarEndereco);
        sessaoUsuario = SessaoUsuario.getInstance();
    }


    @Override
    public void onTextClear() {

    }

    @Override
    public void onPlaceSelected(Place place) {

    }

    public void salvar(Pessoa pessoa, Usuario usuario) {

        Boolean valido = true;
        String nome = mNome.getText().toString();
        String telefone = mTelefone.getText().toString();
        String dataNascimento = mData.getText().toString();
        String email = mEmail.getText().toString();
        String senha = mSenha.getText().toString();
        String confirmSenha = mSenhaConfirm.getText().toString();

        ValidaCadastro validaCadastro = new ValidaCadastro();

        if (validaCadastro.isCampoVazio(nome)) {
            mNome.requestFocus();
            mNome.setError(getString(R.string.error_invalid_name));
            valido = false;
        }

        if (validaCadastro.isCampoVazio(telefone)) {
            mTelefone.requestFocus();
            mTelefone.setError(getString(R.string.error_invalid_tel));
            valido = false;
        } else if (!validaCadastro.isDataNascimento(dataNascimento)) {
            mData.requestFocus();
            mData.setError(getString(R.string.error_invalid_date));
            valido = false;
        } else if (!validaCadastro.isEmail(email)) {
            mEmail.requestFocus();
            mEmail.setError(getString(R.string.error_invalid_email));
            valido = false;
        } else if (!validaCadastro.isSenhaValida(senha)) {
            mSenha.requestFocus();
            mSenha.setError(getString(R.string.error_invalid_password));
            valido = false;
        } else if (!validaCadastro.isSenhaValida(confirmSenha)) {
            mSenhaConfirm.requestFocus();
            mSenhaConfirm.setError(getString(R.string.error_invalid_password));
            valido = false;
        } else if (!(senha.equals(confirmSenha))) {
            mSenhaConfirm.requestFocus();
            mSenhaConfirm.setError(getString(R.string.error_invalid_password));
            valido = false;
        }

        Long resultadoDaAtualizacao = null;

        if (valido) {
            String realSenha = Criptografia.encryptPassword(senha);
            pessoa.setNome(nome);
            pessoa.setTelefone(telefone);
            pessoa.setDataNascimento(new Date(dataNascimento));
            usuario.setEmail(email);
            usuario.setSenha(realSenha);
            pessoaNegocio.setPessoa(pessoa);
            resultadoDaAtualizacao = pessoaNegocio.atualizar();
        }

        if(resultadoDaAtualizacao != null) {
            sessaoUsuario.setPessoaLogada(pessoa);
            sessaoUsuario.setUsuarioLogado(usuario);
            goToHomeScreen();
        } else {
            alert("Ocorreu um erro na atualização. Verifque se os campos foram preenchidos corretamente.");
        }
    }

    private void alert(String s){
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    public void salvarPerfil(View view) {
        salvar(pessoaLogada, usuarioLogado);
    }

    public void goToHomeScreen() {
        Intent homeScrenn = new Intent(EditarPerfilActivity.this, MenuActivity.class);
        startActivity(homeScrenn);
    }

}
