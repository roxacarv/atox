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
    private AppDatabase appDatabase;

    public UserListViewModel(Application application)
    {
        super(application);
        appDatabase = AppDatabase.getAppDatabase(this.getApplication());

        userModelList = appDatabase.userModel().getAll();
    }

    public LiveData<List<UserModel>> getUserList() {
        return userModelList;
    }

    public void deleteItem(UserModel userModel)
    {
        new deleteAsyncTask(appDatabase).execute(userModel);
    }

    private static class deleteAsyncTask extends AsyncTask<UserModel, Void, Void> {
        private AppDatabase db;
        deleteAsyncTask(AppDatabase appDatabase)
        {
            db = appDatabase;
        }
        @Override
        protected Void doInBackground(final UserModel... params)
        {
            db.userModel().delete(params[0]);
            return null;
        }
    }
}