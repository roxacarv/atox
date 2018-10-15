package com.atox.usuario.dao;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.atox.infra.BancoDeDados;
import com.atox.usuario.dominio.Usuario;

import java.util.List;

//ListViewModel example
//AsyncTask example using LiveData package
//should be removed before commiting final versions
//VIEWMODEL SHOULD BE CREATED FOR EACH TABLE TO MAINTAIN THE CODE CLEAN

public class UserListViewModel extends AndroidViewModel {

    private final LiveData<List<Usuario>> userModelList;
    private BancoDeDados bancoDeDados;

    public UserListViewModel(Application application)
    {
        super(application);
        bancoDeDados = BancoDeDados.getBancoDeDados(this.getApplication());

        userModelList = bancoDeDados.userModel().getAll();
    }

    public LiveData<List<Usuario>> getUserList() {
        return userModelList;
    }

    public void deleteItem(Usuario usuario)
    {
        new deleteAsyncTask(bancoDeDados).execute(usuario);
    }

    private static class deleteAsyncTask extends AsyncTask<Usuario, Void, Void> {
        private BancoDeDados db;
        deleteAsyncTask(BancoDeDados bancoDeDados)
        {
            db = bancoDeDados;
        }
        @Override
        protected Void doInBackground(final Usuario... params)
        {
            db.userModel().deletar(params[0]);
            return null;
        }
    }
}