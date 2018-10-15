package com.atox.utils.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.atox.utils.BaseDao;

import java.util.List;

@Dao
public interface UserDao extends BaseDao<UserModel> {

    @Query("SELECT * FROM user")
    LiveData<List<UserModel>> getAll();

    @Query("SELECT * FROM user where primeiroNome LIKE  :firstName AND ultimoNome LIKE :lastName")
    UserModel findByName(String firstName, String lastName);

    @Query("SELECT COUNT(*) from user")
    int countUsers();
}