package com.atox.usuario.dominio;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

//example in how tag attributes inside an object to be read by the Room framework
//THIS IS NOT THE FINAL USER OF THE APPLICATION JUST AN EXAMPLE
//SHOULD BE REMOVED IN PRODUCTION

@Entity(tableName = "usuario")
public class Usuario {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "primeiro_nome")
    private String primeiroNome;

    @ColumnInfo(name = "ultimo_nome")
    private String ultimoNome;

    @ColumnInfo(name = "idade")
    private int idade;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getPrimeiroNome() {
        return primeiroNome;
    }

    public void setPrimeiroNome(String primeiroNome) {
        this.primeiroNome = primeiroNome;
    }

    public String getUltimoNome() {
        return ultimoNome;
    }

    public void setUltimoNome(String ultimoNome) {
        this.ultimoNome = ultimoNome;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

}