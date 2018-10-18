package com.atox.estabelecimentos.dominio;

import com.atox.usuario.dominio.Endereco;

import java.util.List;

public class Estabelecimento {

    private String nome;

    private String resumo;

    private Endereco endereco;

    private List<MetodosPagamento> metodosPagamento;

    private Horarios horarioAbertura;

    private Horarios horarioFechamento;


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getResumo() {
        return resumo;
    }

    public void setResumo(String resumo) {
        this.resumo = resumo;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public List<MetodosPagamento> getMetodosPagamento() {
        return metodosPagamento;
    }

    public void setMetodosPagamento(List<MetodosPagamento> metodosPagamento) {
        this.metodosPagamento = metodosPagamento;
    }

    public Horarios getHorarioAbertura() {
        return horarioAbertura;
    }

    public void setHorarioAbertura(Horarios horarioAbertura) {
        this.horarioAbertura = horarioAbertura;
    }

    public Horarios getHorarioFechamento() {
        return horarioFechamento;
    }

    public void setHorarioFechamento(Horarios horarioFechamento) {
        this.horarioFechamento = horarioFechamento;
    }

}
