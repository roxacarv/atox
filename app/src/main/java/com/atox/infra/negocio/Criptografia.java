package com.atox.infra.negocio;

import com.atox.atoxlogs.AtoxLog;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Criptografia {
    public static String encryptPassword(String inputPassword) {


        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            AtoxLog log = new AtoxLog();
        }
        byte[] messageDigested = new byte[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && md!= null) {
            messageDigested = md.digest(inputPassword.getBytes(StandardCharsets.UTF_8));
        }


        StringBuilder hexString = new StringBuilder();
        for (byte b : messageDigested) {
            hexString.append(String.format("%02X", b));
        }

        String outputPassword = hexString.toString();

        return outputPassword;
    }



    public static boolean validatePassword(String password1, String password2) {

        return password1.equals(password2);

    }
}
