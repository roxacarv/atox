package com.atox.usuario.dominio;


//example in how tag attributes inside an object to be read by the Room framework
//THIS IS NOT THE FINAL USER OF THE APPLICATION JUST AN EXAMPLE
//SHOULD BE REMOVED IN PRODUCTION

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "usuario")
public class Usuario {

    @PrimaryKey(autoGenerate = true)
    private long uid;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "senha")
    private String senha;



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
    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public int compararSenha(String senhaParaComparar) {
        if(this.senha.equals(senhaParaComparar)) {
            return 1;
        }
        return 0;
    }

    public int compararEmail(String emailParaComparar) {
        if(this.email.equals(emailParaComparar)) {
            return 1;
        }
        return 0;
    }
}