package com.atox.infra.persistencia;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.atox.usuario.dominio.SessaoUsuario;
import com.atox.usuario.persistencia.EnderecoDaoBase;
import com.atox.usuario.persistencia.PessoaDaoBase;
import com.atox.usuario.persistencia.SessaoUsuarioDaoBase;
import com.atox.usuario.persistencia.UsuarioDaoBase;
import com.atox.usuario.dominio.Endereco;
import com.atox.usuario.dominio.Pessoa;
import com.atox.usuario.dominio.Usuario;

@Database(entities = { Usuario.class,
                       SessaoUsuario.class,
                       Endereco.class,
                       Pessoa.class
                     },
          version = 1)
@TypeConverters({ConversorDeDate.class})
public abstract class DBHelper extends RoomDatabase {

    private static final String NOME_BANCO_DE_DADOS = "banco-de-dados-atox";

    private static DBHelper INSTANCE;

    public static DBHelper getBancoDeDados(Context context) {
        if (INSTANCE == null) {
            Context appContext = context.getApplicationContext();
            INSTANCE = Room.databaseBuilder(appContext, DBHelper.class, NOME_BANCO_DE_DADOS).build();
        }
        return INSTANCE;
    }

    public static void deletarInstancias() {
        INSTANCE = null;
    }

    public abstract UsuarioDaoBase usuarioDao();
    public abstract SessaoUsuarioDaoBase sessaoDao();
    public abstract EnderecoDaoBase enderecoDao();
    public abstract PessoaDaoBase pessoaDao();

}