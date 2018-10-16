package com.atox.usuario.dominio;


//example in how tag attributes inside an object to be read by the Room framework
//THIS IS NOT THE FINAL USER OF THE APPLICATION JUST AN EXAMPLE
//SHOULD BE REMOVED IN PRODUCTION

import java.util.List;

public class Usuario extends Pessoa {

    private String email;

    private String senha;

    private String login;

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

}