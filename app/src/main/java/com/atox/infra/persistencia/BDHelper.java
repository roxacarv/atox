package com.atox.infra.persistencia;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.atox.atoxlogs.AtoxLog;
import com.atox.atoxlogs.persistencia.daoroom.AtoxLogDaoRoom;
import com.atox.receitas.dominio.Receita;
import com.atox.receitas.dominio.SecaoReceita;
import com.atox.receitas.dominio.UsuarioReceita;
import com.atox.receitas.persistencia.ReceitaDaoRoom;
import com.atox.network.dominio.Produtor;
import com.atox.network.persistencia.daoroom.ProdutorDaoRoom;
import com.atox.receitas.persistencia.SecaoReceitaDaoRoom;
import com.atox.receitas.persistencia.UsuarioReceitaDaoRoom;
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
                       Pessoa.class,
                       Produtor.class,
                       AtoxLog.class,
                       Receita.class,
                       UsuarioReceita.class,
                       SecaoReceita.class
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

    public abstract UsuarioDaoRoom usuarioDaoRoom();
    public abstract SessaoUsuarioDaoRoom sessaoDaoRoom();
    public abstract EnderecoDaoRoom enderecoDaoRoom();
    public abstract PessoaDaoRoom pessoaDaoRoom();
    public abstract ProdutorDaoRoom produtorDaoRoom();
    public abstract AtoxLogDaoRoom atoxLogDaoRoom();
    public abstract ReceitaDaoRoom receitaDaoRoom();
    public abstract UsuarioReceitaDaoRoom usuarioReceitaDaoRoom();
    public abstract SecaoReceitaDaoRoom secaoReceitaDaoRoom();

}