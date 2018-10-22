package com.atox.lugar.dominio;

import com.atox.usuario.dominio.Endereco;

public class Lugar {

    private String nome;

    private String resumo;

    private Endereco endereco;

    private MetodoPagamento metodoPagamento;

    private Horario horarioAbertura;

    private Horario horarioFechamento;

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

    public Horario getHorarioAbertura() {
        return horarioAbertura;
    }

    public void setHorarioAbertura(Horario horarioAbertura) {
        this.horarioAbertura = horarioAbertura;
    }

    public Horario getHorarioFechamento() {
        return horarioFechamento;
    }

    public void setHorarioFechamento(Horario horarioFechamento) {
        this.horarioFechamento = horarioFechamento;
    }

    public MetodoPagamento getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(MetodoPagamento metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }
}
