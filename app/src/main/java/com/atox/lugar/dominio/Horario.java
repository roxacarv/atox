package com.atox.lugar.dominio;

public enum Horario {

    ZERO_HORA("00:00"),
    UMA_HORA("01:00"),
    DUAS_HORAS("02:00"),
    TRES_HORAS("03:00"),
    QUATRO_HORAS("04:00"),
    CINCO_HORAS("05:00"),
    SEIS_HORAS("06:00"),
    SETE_HORAS("07:00"),
    OITO_HORAS("08:00"),
    NOVE_HORAS("09:00"),
    DEZ_HORAS("10:00"),
    ONZE_HORAS("11:00"),
    DOZE_HORAS("12:00"),
    TREZE_HORAS("13:00"),
    QUATORZE_HORAS("14:00"),
    QUINZE_HORAS("15:00"),
    DEZESSEIS_HORAS("16:00"),
    DEZESSETE_HORAS("17:00"),
    DEZOITO_HORAS("18:00"),
    DEZENOVE_HORAS("19:00"),
    VINTE_HORAS("20:00"),
    VINTE_UMA_HORAS("21:00"),
    VINTE_DUAS_HORAS("22:00"),
    VINTE_TRES_HORAS("23:00");


    private Horario(String descricao) {
        this.descricao = descricao;
    }

    private String descricao;

    public String getDescricao() {
        return descricao;
    }

}
