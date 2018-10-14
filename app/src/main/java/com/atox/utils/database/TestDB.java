package com.atox.utils;

//bussiness model for the user example

public class TestDB {

    private static UserModel addUser(final AppDatabase db, UserModel user) {
        db.userModel().insertAll(user);
        return user;
    }

    public static UserModel getUser(final AppDatabase db, String firstName, String lastName)
    {
        return db.userModel().findByName(firstName, lastName);
    }

    public static void populateWithTestData(AppDatabase db) {
        UserModel user = new UserModel();
        user.setFirstName("Rodrigo");
        user.setLastName("Xavier");
        user.setAge(27);
        addUser(db, user);
    }

}
