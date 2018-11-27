package com.atox.infra.persistencia;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Creates a base interface to be used by DAO operations on the Database using the Room Framework.
 * This method should be inherited by another class to be used properly. The Query operation should
 * be created individually by each DAO class.
 * @param <T> receives a generic Java Object-type value. (eg. User object, Person object...)
 * @version 1.0
 */

@Dao
public interface BaseDao<T> {

    /**
     * Basic operations for inserting, updating and deleting data from the SQLite database
     * @param data receives a generic Java Object-type value to be used in one of the four operations
     *             inserir() Insert something (a Java Object-type) in the database;
     *             inserirTudo() Insert multiples objects/data to the database at once;
     *             atualizar() Changes an object/data inside the database;
     *             deletar() Remove an object/data from the database;
     */
    @Insert
    long inserir(T data);

    @Insert
    List<Long> inserirTudo(T... data);

    @Update
    void atualizar(T data);

    @Delete
    void deletar(T data);

}
