package com.atox.utils;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Update;

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
     *             insert() Insert something (a Java Object-type) in the database;
     *             insertAll() Insert multiples objects/data to the database at once;
     *             update() Changes an object/data inside the database;
     *             delete() Remove an object/data from the database;
     */
    @Insert
    void insert(T data);

    @Insert
    void insertAll(T... data);

    @Update
    void update(T data);

    @Delete
    void delete(T data);

}
