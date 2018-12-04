package com.atox.usuario.dominio;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "endereco", foreignKeys = @ForeignKey(entity = Pessoa.class,
                                                          parentColumns = "pid",
                                                          childColumns = "pessoa_id",
                                                          onDelete = CASCADE))

public class Endereco {

    @PrimaryKey(autoGenerate = true)
    private long eid;

    @ColumnInfo(name = "pessoa_id")
    private long pessoaId;

    @ColumnInfo(name = "cep")
    private String cep;

    @ColumnInfo(name = "logradouro")
    private String logradouro;

    @ColumnInfo(name = "numero")
    private String  numero;

    @ColumnInfo(name = "complemento")
    private String complemento;

    @ColumnInfo(name = "bairro")
    private String bairro;

    @ColumnInfo(name = "cidade")
    private String cidade;

    @ColumnInfo(name = "estado")
    private String estado;

    @ColumnInfo(name = "pais")
    private String pais;

    public Endereco() { }

    public Endereco(int pessoaId) {
        this.pessoaId = pessoaId;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public long getPessoaId() {
        return pessoaId;
    }

    public void setPessoaId(long pessoaId) {
        this.pessoaId = pessoaId;
    }

    public long getEid() {
        return eid;
    }

    public void setEid(long eid) {
        this.eid = eid;
    }
}
