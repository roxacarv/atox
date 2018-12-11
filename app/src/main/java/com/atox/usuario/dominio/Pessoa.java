package com.atox.usuario.dominio;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "pessoa", indices = {@Index("usuario_id")},
        foreignKeys = @ForeignKey(entity = Usuario.class,
                                  parentColumns = "uid",
                                  childColumns = "usuario_id",
                                  onDelete = CASCADE))
public class Pessoa {

    @PrimaryKey(autoGenerate = true)
    private long pid;

    @ColumnInfo(name = "usuario_id")
    private long usuarioId;

    @ColumnInfo(name = "nome")
    private String nome;

    @ColumnInfo(name = "data_nascimento")
    private Date dataNascimento;

    @ColumnInfo(name = "cpf")
    private String cpf;

    @Ignore
    private Endereco endereco;

    @Ignore
    private Usuario usuario;

    @ColumnInfo(name = "telefone")
    private String telefone;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento){
        this.dataNascimento = dataNascimento;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }
}
