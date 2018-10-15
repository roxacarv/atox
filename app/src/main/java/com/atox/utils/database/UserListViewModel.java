package com.atox.utils.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

//ListViewModel example
//AsyncTask example using LiveData package
//should be removed before commiting final versions
//VIEWMODEL SHOULD BE CREATED FOR EACH TABLE TO MAINTAIN THE CODE CLEAN

public class UserListViewModel extends AndroidViewModel {

    private final LiveData<List<UserModel>> userModelList;
    private BancoDeDados bancoDeDados;

    public UserListViewModel(Application application)
    {
        super(application);
        bancoDeDados = BancoDeDados.getBancoDeDados(this.getApplication());

        userModelList = bancoDeDados.userModel().getAll();
    }

    public LiveData<List<UserModel>> getUserList() {
        return userModelList;
    }

    public void deleteItem(UserModel userModel)
    {
        new deleteAsyncTask(bancoDeDados).execute(userModel);
    }

    private static class deleteAsyncTask extends AsyncTask<UserModel, Void, Void> {
        private BancoDeDados db;
        deleteAsyncTask(BancoDeDados bancoDeDados)
        {
            db = bancoDeDados;
        }
        @Override
        protected Void doInBackground(final UserModel... params)
        {
            db.userModel().deletar(params[0]);
            return null;
        }
    }
}