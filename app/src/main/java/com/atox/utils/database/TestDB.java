package com.atox.utils.database;

//bussiness model for the user example

public class TestDB {

    private static UserModel addUser(final BancoDeDados db, UserModel user) {
        db.userModel().inserirTudo(user);
        return user;
    }

    public static UserModel getUser(final BancoDeDados db, String firstName, String lastName)
    {
        return db.userModel().findByName(firstName, lastName);
    }

    public static void populateWithTestData(BancoDeDados db) {
        UserModel user = new UserModel();
        user.setPrimeiroNome("Rodrigo");
        user.setUltimoNome("Xavier");
        user.setIdade(27);
        addUser(db, user);
    }

}
