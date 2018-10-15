package com.atox.utils.database;

//bussiness model for the user example

import com.atox.infra.BancoDeDados;
import com.atox.usuario.dominio.Usuario;

public class TestDB {

    private static Usuario addUser(final BancoDeDados db, Usuario user) {
        db.userModel().inserirTudo(user);
        return user;
    }

    public static Usuario getUser(final BancoDeDados db, String firstName, String lastName)
    {
        return db.userModel().findByName(firstName, lastName);
    }

    public static void populateWithTestData(BancoDeDados db) {
        Usuario user = new Usuario();
        user.setPrimeiroNome("Rodrigo");
        user.setUltimoNome("Xavier");
        user.setIdade(27);
        addUser(db, user);
    }

}
