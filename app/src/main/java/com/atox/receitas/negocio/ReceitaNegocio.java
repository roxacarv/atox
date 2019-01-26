package com.atox.receitas.negocio;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.FragmentActivity;

import com.atox.atoxlogs.AtoxLog;
import com.atox.atoxlogs.AtoxMensagem;
import com.atox.receitas.dominio.Receita;
import com.atox.receitas.dominio.SecaoReceita;
import com.atox.receitas.dominio.UsuarioReceita;
import com.atox.receitas.persistencia.ReceitaDao;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ReceitaNegocio {

    private ReceitaDao receitaDao;
    private Receita receita;

    public ReceitaNegocio(FragmentActivity activity){
        receitaDao = ViewModelProviders.of(activity).get(ReceitaDao.class);
    }


    public List<Long> cadastrar(Long usuarioId, Receita receita) {
        AtoxLog log = new AtoxLog();
        Long resultadoInsercaoReceita = null;
        List<Long> resultadoInsercaoSecao = null;
        List<Long> resultadoIdsDeInsercao = new ArrayList<>();
        Long resultadoInsercaoUsuarioReceita = null;
        SecaoReceita arrayDeSecoesReceita[] = new SecaoReceita[receita.getSecoes().size()];
        arrayDeSecoesReceita = receita.getSecoes().toArray(arrayDeSecoesReceita);
        try {
            resultadoInsercaoReceita = receitaDao.inserirReceita(receita);
            if(resultadoInsercaoReceita != null) {
                inserirSecoes(resultadoInsercaoReceita, receitaDao, arrayDeSecoesReceita);
            }
            if(resultadoInsercaoReceita != null) {
                resultadoInsercaoUsuarioReceita = inserirUsuarioReceita(usuarioId, receitaDao, resultadoInsercaoReceita);
            }
            resultadoIdsDeInsercao.add(resultadoInsercaoReceita);
            resultadoIdsDeInsercao.add(resultadoInsercaoUsuarioReceita);
        } catch (ExecutionException e) {
            log = new AtoxLog();
            log.novoRegistro(AtoxMensagem.ACAO_CADASTRAR_RECEITA_FAVORITA,
                    AtoxMensagem.ERRO_BUSCAR_REGISTRO_NO_BANCO,
                    "Um erro de execução ocorreu ao tentar inserir uma receita no banco: " + e.getMessage());
            log.empurraRegistrosPraFila();
        } catch (InterruptedException e) {
            log = new AtoxLog();
            log.novoRegistro(AtoxMensagem.ACAO_CADASTRAR_RECEITA_FAVORITA,
                    AtoxMensagem.ERRO_BUSCAR_REGISTRO_NO_BANCO,
                    "Um erro de execução ocorreu ao tentar inserir uma receita no banco: " + e.getMessage());
            log.empurraRegistrosPraFila();
            Thread.currentThread().interrupt();
        }
        return resultadoIdsDeInsercao;
    }

    //retorna o id em que foi inserida a receita no bd
    public Long cadastrarSemUsuario(Receita receita) {
        AtoxLog log = new AtoxLog();
        Long idReceitaInsercao = null;
        List<Long> resultadoInsercaoSecao = null;
        SecaoReceita arrayDeSecoesReceita[] = new SecaoReceita[receita.getSecoes().size()];//define um array com tam, mas vazio
        arrayDeSecoesReceita = receita.getSecoes().toArray(arrayDeSecoesReceita);//preenche o array com a lista secoes
        try {
            //insere receita no banco e retorna o id de receita
            idReceitaInsercao = receitaDao.inserirReceita(receita);
            if(idReceitaInsercao != null) {
                //metodo retorna uma lista de objetos Secao (com estas secoes já inseridas no banco)
                inserirSecoes(idReceitaInsercao, receitaDao, arrayDeSecoesReceita);
            }

        } catch (ExecutionException e) {
            log = new AtoxLog();
            log.novoRegistro(AtoxMensagem.ACAO_CADASTRAR_NOVA_RECEITA,
                    AtoxMensagem.ERRO_BUSCAR_REGISTRO_NO_BANCO,
                    "Um erro de execução ocorreu ao tentar inserir uma receita no banco: " + e.getMessage());
            log.empurraRegistrosPraFila();
        } catch (InterruptedException e) {
            log = new AtoxLog();
            log.novoRegistro(AtoxMensagem.ACAO_CADASTRAR_NOVA_RECEITA,
                    AtoxMensagem.ERRO_BUSCAR_REGISTRO_NO_BANCO,
                    "Um erro de execução ocorreu ao tentar inserir uma receita no banco: " + e.getMessage());
            log.empurraRegistrosPraFila();
            Thread.currentThread().interrupt();
        }
        return idReceitaInsercao;
    }

    public Receita buscarReceitaPorId(Long idReceita) throws ExecutionException, InterruptedException {
        //busca o objeto Receita
        Receita receitaObj = receitaDao.buscarReceitaPorId(idReceita);

        return receitaObj;
    }

    public List<Receita> buscarReceitasDoUsuario(Long idDeUsuario) {
        AtoxLog log = new AtoxLog();
        List<Receita> receitas = null;
        try {
            receitas = receitaDao.buscarPorIdDeUsuario(idDeUsuario);
        } catch (ExecutionException e) {
            log = new AtoxLog();
            log.novoRegistro(AtoxMensagem.ACAO_RECUPERAR_RECEITAS_DO_USUARIO,
                    AtoxMensagem.ERRO_BUSCAR_REGISTRO_NO_BANCO,
                    "Um erro de execução ocorreu ao tentar buscar um usuário no banco: " + e.getMessage());
            log.empurraRegistrosPraFila();
        } catch (InterruptedException e) {
            log = new AtoxLog();
            log.novoRegistro(AtoxMensagem.ACAO_RECUPERAR_RECEITAS_DO_USUARIO,
                    AtoxMensagem.ERRO_BUSCAR_REGISTRO_NO_BANCO,
                    "Um erro de execução ocorreu ao tentar buscar um usuário no banco: " + e.getMessage());
            log.empurraRegistrosPraFila();
            Thread.currentThread().interrupt();
        }
        return receitas;
    }

    public Receita buscarReceitaPorNome(String nomeReceita){
        AtoxLog log = new AtoxLog();
        Receita resultado = null;
        try{
            List<Receita> receitaEncontrada = receitaDao.buscarReceitaPorNome(nomeReceita);
            if (receitaEncontrada != null){
                resultado = receitaEncontrada.get(0);
            }
        }
        catch(ExecutionException e){
            log.novoRegistro(AtoxMensagem.ACAO_RECUPERAR_RECEITA_POR_NOME,
                    AtoxMensagem.ERRO_BUSCAR_REGISTRO_NO_BANCO,
                    ("Ocorreu um erro ao buscar a receita \"" + nomeReceita +
            "\" no banco.\n" +
            "Mensagem: " + e.getMessage()));
            log.empurraRegistrosPraFila();
        } catch(InterruptedException e){
            log.novoRegistro(AtoxMensagem.ACAO_RECUPERAR_RECEITA_POR_NOME,
                    AtoxMensagem.ERRO_BUSCAR_REGISTRO_NO_BANCO,
                    ("Ocorreu um erro ao buscar a receita \"" + nomeReceita +
                            "\" no banco.\n" +
                            "Mensagem: " + e.getMessage()));
            Thread.currentThread().interrupt();
        }

        return resultado;
    }

    public List<Receita> buscarReceitasPorTipo(Long tipo) {
        AtoxLog log = new AtoxLog();
        List<Receita> receitas = null;
        try {
            receitas = receitaDao.buscarReceitasPorTipo(tipo);
        } catch (ExecutionException e) {
            log.novoRegistro(AtoxMensagem.ACAO_RECUPERAR_RECEITAS_POR_TIPO,
                    AtoxMensagem.ERRO_BUSCAR_REGISTRO_NO_BANCO,
                    "Um erro de execução ocorreu ao tentar buscar um usuário no banco: " + e.getMessage());
            log.empurraRegistrosPraFila();
        } catch (InterruptedException e) {
            log = new AtoxLog();
            log.novoRegistro(AtoxMensagem.ACAO_RECUPERAR_RECEITAS_POR_TIPO,
                    AtoxMensagem.ERRO_BUSCAR_REGISTRO_NO_BANCO,
                    "Um erro de execução ocorreu ao tentar buscar um usuário no banco: " + e.getMessage());
            log.empurraRegistrosPraFila();
            Thread.currentThread().interrupt();
        }
        return receitas;
    }

    public List<Receita> buscarTodasReceitas() {
        AtoxLog log = new AtoxLog();
        List<Receita> receitas = null;
        try {
            receitas = receitaDao.buscarTodasReceitas();
        } catch (ExecutionException e) {
            log = new AtoxLog();
            log.novoRegistro(AtoxMensagem.ACAO_RECUPERAR_RECEITAS,
                    AtoxMensagem.ERRO_BUSCAR_REGISTRO_NO_BANCO,
                    "Um erro de execução ocorreu ao tentar buscar um usuário no banco: " + e.getMessage());
            log.empurraRegistrosPraFila();
        } catch (InterruptedException e) {
            log = new AtoxLog();
            log.novoRegistro(AtoxMensagem.ACAO_RECUPERAR_RECEITAS,
                    AtoxMensagem.ERRO_BUSCAR_REGISTRO_NO_BANCO,
                    "Um erro de execução ocorreu ao tentar buscar um usuário no banco: " + e.getMessage());
            log.empurraRegistrosPraFila();
            Thread.currentThread().interrupt();
        }
        return receitas;
    }

    public SecaoReceita[] inserirSecoes(Long receitaId, ReceitaDao receitaDao, SecaoReceita... secaoReceitas) throws ExecutionException, InterruptedException {
        SecaoReceita[] novoArrayDeSecoes = new SecaoReceita[secaoReceitas.length];
        for(int i = 0; i < secaoReceitas.length; i++) {
            //seta o id da receita em cada secao passada
            secaoReceitas[i].setReceitaId(receitaId);
            //add essa secao com id de receita setado a lista nova (q sera retornada)
            novoArrayDeSecoes[i] = secaoReceitas[i];
        }
        //passa a lista de secao atualizada para add no banco
        receitaDao.inserirSecaoReceita(novoArrayDeSecoes);
        return novoArrayDeSecoes;
    }

    public Long inserirUsuarioReceita(Long usuarioId, ReceitaDao receitaDao, Long receitaId) throws ExecutionException, InterruptedException {
        Long resultadoInsercaoUsuarioReceita = null;
        UsuarioReceita usuarioReceita = new UsuarioReceita();
        usuarioReceita.setUsuarioId(usuarioId);
        usuarioReceita.setReceitaId(receitaId);
        resultadoInsercaoUsuarioReceita = receitaDao.inserirUsuarioReceita(usuarioReceita);
        return resultadoInsercaoUsuarioReceita;
    }


    public void removerReceita(Receita receita) {
        AtoxLog log = new AtoxLog();
        Long resultado = null;
        receitaDao.deletarItem(receita);
    }

}
