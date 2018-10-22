package com.atox.usuario.dominio;


//example in how tag attributes inside an object to be read by the Room framework
//THIS IS NOT THE FINAL USER OF THE APPLICATION JUST AN EXAMPLE
//SHOULD BE REMOVED IN PRODUCTION

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.atox.lugar.dominio.Lugar;

import java.util.List;

@Entity(tableName = "usuario")
public class Usuario extends Pessoa {

    @PrimaryKey(autoGenerate = true)
    private long uid;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "senha")
    private String senha;

    @ColumnInfo(name = "login")
    private String login;

    @Ignore
    private List<Lugar> lugares;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public List<Lugar> getLugares() {
        return lugares;
    }

    public void setLugares(List<Lugar> lugares) {
        this.lugares = lugares;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }
}