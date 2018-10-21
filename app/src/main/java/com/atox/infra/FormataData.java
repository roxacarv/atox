package com.atox.infra;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormataData {
    private static final String DATA_POST_BANCO  = "yyyyMMddHHmmss";
    private static final String DATA_POST_GUI    = "dd/MM/yyyy HH:mm:ss";
    private static final String DATA_COMUM_GUI   = "dd/MM/yyyy";
    private static final String DATA_COMUM_BANCO = "yyyyMMdd";
    private static final String DATA_NASC_BANCO  = "yyyyMMdd";
    private static final String DATA_HOJE = "yyyyMMdd000000";

    private static final int SEGUNDO = 1000;
    private static final int MINUTO = 60000;
    private static final int HORA = 3600000;
    private static final int DIA = 86400000;
    private static final int SEMANA = 604800000;
    private static final long MES = 2592000000L;
    private static final long ANO = 31536000000L;

    private static final int NUMDATA1 = 6;
    private static final int NUMDATA2 = 3;
    private static final int NUMDATA3 = 5;
    private static final int NUMDATA4 = 0;
    private static final int NUMDATA5 = 2;

    /**
     * Recebe data no formato dd/MM/yyyy
     * @param data Recebe data
     * @return Retorna data no formato yyMMdd
     */

    public static String americano(String data){
        StringBuilder novaData = new StringBuilder();
        novaData.append(data.substring(NUMDATA1));
        novaData.append(data.substring(NUMDATA2, NUMDATA3));
        novaData.append(data.substring(NUMDATA4, NUMDATA5));

        return novaData.toString();
    }

    /**
     * Método que formata data no formato que será exibido para o Usuario
     * @param stringData
     * @return Retorna data no forato dd/MM/yyyy
     */

    public static String formatarDataHoraDataBaseParaExibicao (String stringData){
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat(DATA_NASC_BANCO);
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(DATA_COMUM_GUI);
        simpleDateFormat1.setLenient(false);

        try{
            Date date = simpleDateFormat1.parse(stringData);
            return simpleDateFormat2.format(date);
        }catch (Exception e){
            return e.getMessage();
        }
    }

    /**
     * Método utilizado para fornecer a data no formato que será inserido no banco
     * @return Retorna data no formato yyyyMMddHHmmss
     */

    public static String formatarDataHoraAtualParaPostDataBase(){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATA_POST_BANCO);
        return simpleDateFormat.format(date);
    }

    /**
     * Método utilizado para formatar data atual
     * @return retorna data no formato yyyyMMddHHmmss
     */

    public static String formataDataHoraHoje(){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATA_HOJE);
        return simpleDateFormat.format(date);
    }

    /**
     * Método para verificar se a data está no formato adequado
     * @param data Data
     * @return Retorna se a data é válida ou não
     */

    public static boolean dataExiste(String data){
        SimpleDateFormat dataFormatada = new SimpleDateFormat (DATA_COMUM_GUI);
        dataFormatada.setLenient (false);

        //Testa no formato dd/MM/yyyy
        try {
            dataFormatada.parse(data);
            return true;
        } catch (Exception e) {
            Log.i("FormataData", e.getMessage());
        }

        dataFormatada = new SimpleDateFormat (DATA_COMUM_BANCO);
        dataFormatada.setLenient (false);

        //Testa no formato yyyyMMdd
        try {
            dataFormatada.parse(data);
            return true;
        } catch (Exception e) {
            Log.i("FormataData", e.getMessage());
        }

        return false;
    }

    /**
     * Método para verificar se a data informada é menor ou igual a de hoje
     * @param data Data
     * @return Retorna se a data é menor ou igal que a atual
     */

    public static boolean dataMenorOuIgualQueAtual(String data){
        SimpleDateFormat dataFormatada = new SimpleDateFormat (DATA_COMUM_GUI);
        dataFormatada.setLenient (false);
        //Testa no formato dd/MM/yyyy
        try {
            Date dataAtual = new Date();
            Date dataCliente = dataFormatada.parse(data);

            if(dataAtual.compareTo(dataCliente) >= 0){
                return true;
            }
        } catch (Exception e) {
            Log.i("FormataData", e.getMessage());
        }

        dataFormatada = new SimpleDateFormat (DATA_COMUM_BANCO);
        dataFormatada.setLenient (false);
        //Testa no formato yyyyMMdd
        try {
            Date dataAtual = new Date();
            Date dataCliente = dataFormatada.parse(data);

            if(dataAtual.compareTo(dataCliente) >= 0){
                return true;
            }
        } catch (Exception e) {
            Log.i("FormataData", e.getMessage());
        }

        return false;
    }

    /**
     * Método para exibir horário/data da criação do post
     * @param data Data de criação do post
     * @return Retorna uma String com a descrição do horário/data da criação do post
     */

    public static String tempoParaMostrarEmPost(String data){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat (DATA_POST_BANCO);
        Date datainicio = new Date();
        Date datafim = new Date();
        try {
            datainicio = simpleDateFormat.parse(data);
        } catch (ParseException e) {
            Log.d("tempoParaMostrarEmPost", e.getMessage());
        }

        long diferenca = datafim.getTime() - datainicio.getTime();
        String tempo;

        if(diferenca < MINUTO){
            tempo = Long.toString(diferenca/ SEGUNDO);
            tempo = "Há " + tempo + (tempo.equals("1") ? " segundo":" segundos");
        } else if(diferenca < HORA){
            tempo = Long.toString(diferenca/ MINUTO);
            tempo = "Há " + tempo + (tempo.equals("1") ? " minuto":" minutos");
        } else if(diferenca < DIA){
            tempo = Long.toString(diferenca/ HORA);
            tempo = "Há " + tempo + (tempo.equals("1") ? " hora":" horas");
        } else if(diferenca < SEMANA){
            tempo = Long.toString(diferenca/ DIA);
            tempo = "Há " + tempo + (tempo.equals("1") ? " dia":" dias");
        } else if(diferenca < MES){
            tempo = Long.toString(diferenca/ SEMANA);
            tempo = "Há " + tempo + (tempo.equals("1") ? " semana":" semanas");
        } else if(diferenca < ANO){
            tempo = Long.toString(diferenca/ MES);
            tempo = "Há " + tempo + (tempo.equals("1") ? " mês":" meses");
        } else {
            simpleDateFormat = new SimpleDateFormat(DATA_POST_GUI);
            tempo = "em " + simpleDateFormat.format(datainicio);

        }
        return tempo;
    }
}
