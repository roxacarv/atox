package com.atox.network.negocio;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.FragmentActivity;

import com.atox.infra.AtoxException;
import com.atox.infra.AtoxLog;
import com.atox.network.dominio.Produtor;
import com.atox.network.persistencia.dao.ProdutorDao;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ProdutorNegocio {

    private ProdutorDao produtorDao;
    private Produtor produtor;

    public ProdutorNegocio(FragmentActivity activity){
        produtorDao = ViewModelProviders.of(activity).get(ProdutorDao.class);
    }

    public List<Long> inserirProdutores(List<Produtor> produtoresParaInserir) {
        List<Long> idDeProdutores = null;
        Produtor[] produtores = produtoresParaInserir.toArray(new Produtor[produtoresParaInserir.size()]);
        try {
            idDeProdutores = produtorDao.inserirProdutores(produtores);
            if(idDeProdutores.isEmpty()) {
                idDeProdutores = null;
                throw new AtoxException("Não há produtores cadastrados");
            }
        } catch (ExecutionException e) {
            AtoxLog novoLog = new AtoxLog();
        } catch (InterruptedException e) {
            AtoxLog novoLog = new AtoxLog();
        } catch (AtoxException ae) {
            AtoxLog novoLogo = new AtoxLog();
        }

        return idDeProdutores;
    }

    public List<Produtor> buscarTodosProdutores() {
        List<Produtor> produtores = null;
        try {
            produtores = produtorDao.buscarTodosProdutores();
            if(produtores.isEmpty()) {
                produtores = null;
                throw new AtoxException("Não há produtores cadastrados no banco");
            }
        } catch (ExecutionException e) {
            AtoxLog novoLogo = new AtoxLog();
        } catch (InterruptedException e) {
            AtoxLog novoLogo = new AtoxLog();
        } catch(AtoxException ae) {
            AtoxLog novoLogo = new AtoxLog();
        }
        return produtores;
    }

}
