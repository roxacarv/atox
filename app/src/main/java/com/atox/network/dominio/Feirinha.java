package com.atox.network.dominio;

import com.google.gson.annotations.SerializedName;

public class Feirinha {

    @SerializedName("nome")
    private String nome;

    @SerializedName("endereco")
    private String endereco;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longitude")
    private String longitude;

    @SerializedName("dia")
    private String dia;

    @SerializedName("horarioInicio")
    private String horarioInicio;

    @SerializedName("horarioFim")
    private String horarioFim;


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getHorarioInicio() {
        return horarioInicio;
    }

    public void setHorarioInicio(String horarioInicio) {
        this.horarioInicio = horarioInicio;
    }

    public String getHorarioFim() {
        return horarioFim;
    }

    public void setHorarioFim(String horarioFim) {
        this.horarioFim = horarioFim;
    }
}
