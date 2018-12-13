package com.atox.atoxlogs;

public class AtoxMensagem {

    private static AtoxMensagem INSTANCE;

    //possíveis erros relacionados ao banco de dados
    public static final String ERRO_INSERIR_REGISTRO_NO_BANCO = "e0";
    public static final String ERRO_ATUALIZAR_REGISTRO_NO_BANCO = "e1";
    public static final String ERRO_BUSCAR_REGISTRO_NO_BANCO = "e2";
    public static final String ERRO_NO_USO_DE_API = "e3";

    //possíveis ações que estejam sendo feitas
    public static final String ACAO_INSERIR_REGISTRO_NO_BANCO = "a0";
    public static final String ACAO_ATUALIZAR_REGISTRO_NO_BANCO = "a1";
    public static final String ACAO_SALVAR_LOGS_NO_BANCO = "a2";
    public static final String ACAO_RETORNAR_LOGS_DO_BANCO = "a3";
    public static final String ACAO_REGISTRAR_USUARIO = "a4";
    public static final String ACAO_REGISTRAR_ENDERECO = "a5";
    public static final String ACAO_EFETUAR_LOGIN = "a6";
    public static final String ACAO_ATUALIZAR_PERFIL = "a7";
    public static final String ACAO_BUSCAR_LOGS_NO_BANCO = "a8";
    public static final String ACAO_RECUPERAR_PESSOA_NO_BANCO = "a9";
    public static final String ACAO_RESTAURAR_SESSAO = "a10";
    public static final String ACAO_INICIAR_NOVA_SESSAO = "a11";
    public static final String ACAO_REQUISITAR_ENDERECO_API_GOOGLE = "a12";

    public static AtoxMensagem getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new AtoxMensagem();
        }
        return INSTANCE;
    }
}
