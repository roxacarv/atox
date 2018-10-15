package com.atox.utils.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {UserModel.class}, version = 1)
public abstract class BancoDeDados extends RoomDatabase {

    private static final String NOME_BANCO_DE_DADOS = "banco-de-dados-atox";

    private static BancoDeDados INSTANCE;

    public static BancoDeDados getBancoDeDados(Context context) {
        if (INSTANCE == null) {
            Context appContext = context.getApplicationContext();
            INSTANCE = Room.databaseBuilder(appContext, BancoDeDados.class, NOME_BANCO_DE_DADOS).build();
        }
        return INSTANCE;
    }

    public static void deletarInstancias() {
        INSTANCE = null;
    }

    public abstract UserDao userModel();

}