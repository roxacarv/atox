package com.atox.infra.persistencia;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.atox.usuario.dominio.SessaoUsuario;
import com.atox.usuario.persistencia.daoroom.EnderecoDaoRoom;
import com.atox.usuario.persistencia.daoroom.PessoaDaoRoom;
import com.atox.usuario.persistencia.daoroom.SessaoUsuarioDaoRoom;
import com.atox.usuario.persistencia.daoroom.UsuarioDaoRoom;
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
public abstract class BDHelper extends RoomDatabase {

    private static final String NOME_BANCO_DE_DADOS = "banco-de-dados-atox";

    private static BDHelper INSTANCE;

    public static BDHelper getBancoDeDados(Context context) {
        if (INSTANCE == null) {
            Context appContext = context.getApplicationContext();
            INSTANCE = Room.databaseBuilder(appContext, BDHelper.class, NOME_BANCO_DE_DADOS).build();
        }
        return INSTANCE;
    }

    public static void deletarInstancias() {
        INSTANCE = null;
    }

    public abstract UsuarioDaoRoom usuarioDao();
    public abstract SessaoUsuarioDaoRoom sessaoDao();
    public abstract EnderecoDaoRoom enderecoDao();
    public abstract PessoaDaoRoom pessoaDao();

}