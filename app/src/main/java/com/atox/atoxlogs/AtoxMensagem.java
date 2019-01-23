package com.atox.atoxlogs;

public class AtoxMensagem {

    private static AtoxMensagem INSTANCE;

    //possíveis erros relacionados ao banco de dados
    public static final int ERRO_INSERIR_REGISTRO_NO_BANCO = 0;
    public static final int ERRO_ATUALIZAR_REGISTRO_NO_BANCO = 1;
    public static final int ERRO_BUSCAR_REGISTRO_NO_BANCO = 2;
    public static final int ERRO_NO_USO_DE_API = 3;
    public static final int ERRO_AO_ACESSAR_A_MEMORIA_INTERNA = 4;

    //possíveis ações que estejam sendo feitas
    public static final int ACAO_INSERIR_REGISTRO_NO_BANCO = 0;
    public static final int ACAO_ATUALIZAR_REGISTRO_NO_BANCO = 1;
    public static final int ACAO_SALVAR_LOGS_NO_BANCO = 2;
    public static final int ACAO_RETORNAR_LOGS_DO_BANCO = 3;
    public static final int ACAO_REGISTRAR_USUARIO = 4;
    public static final int ACAO_REGISTRAR_ENDERECO = 5;
    public static final int ACAO_EFETUAR_LOGIN = 6;
    public static final int ACAO_ATUALIZAR_PERFIL = 7;
    public static final int ACAO_BUSCAR_LOGS_NO_BANCO = 8;
    public static final int ACAO_RECUPERAR_PESSOA_NO_BANCO = 9;
    public static final int ACAO_RESTAURAR_SESSAO = 10;
    public static final int ACAO_INICIAR_NOVA_SESSAO = 11;
    public static final int ACAO_REQUISITAR_ENDERECO_API_GOOGLE = 12;
    public static final int ACAO_BUSCAR_IMAGEM_NA_MEMORIA_INTERNA = 13;
    public static final int ACAO_SALVAR_IMAGEM_NA_MEMORIA_INTERNA = 14;
    public static final int ACAO_RECUPERAR_RECEITAS_POR_TIPO = 15;
    public static final int ACAO_RECUPERAR_RECEITAS_DO_USUARIO = 16;
    public static final int ACAO_CADASTRAR_NOVA_RECEITA = 17;
    public static final int ACAO_CADASTRAR_RECEITA_FAVORITA = 18;
    public static final int ACAO_RECUPERAR_RECEITAS = 19;

    public static AtoxMensagem getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new AtoxMensagem();
        }
        return INSTANCE;
    }
}
