package com.atox.infra;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.atox.usuario.dao.EnderecoDao;
import com.atox.usuario.dao.SessaoDao;
import com.atox.usuario.dao.UsuarioDao;
import com.atox.usuario.dominio.Endereco;
import com.atox.usuario.dominio.Sessao;
import com.atox.usuario.dominio.Usuario;
import com.atox.utils.DateTypeConverter;

@Database(entities = { Usuario.class,
        Sessao.class,
        Endereco.class
},
        version = 1)
@TypeConverters({DateTypeConverter.class})
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

    public abstract UsuarioDao usuarioDao();
    public abstract SessaoDao sessaoDao();
    public abstract EnderecoDao enderecoDao();

}