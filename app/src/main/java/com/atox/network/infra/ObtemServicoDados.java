package com.atox.network.infra;

import com.atox.network.dominio.Produtor;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ObtemServicoDados {
    //id de um host de API temporario
    @GET("/bins/hutxi")
    Call<List<Produtor>> getAllProdutores();
}
