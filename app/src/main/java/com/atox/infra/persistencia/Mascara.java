package com.atox.infra.persistencia;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public abstract class Mascara {
    private static String unmask(String s) {
        return s.replaceAll("[.]", "").replaceAll("[-]", "")
                .replaceAll("[/]", "").replaceAll("[(]", "")
                .replaceAll("[)]", "");
    }

    /**
     * Método estático que será vinculado ao @see {@link EditText} para a formatação do texto de
     * acordo com o padrão passado.
     * @param mask - String com o padrão que a máscara utilizará.
     * @param ediTxt - @see {@link EditText} que receberá a formatação da máscara.
     * @return - Retorna o texto formatado.
     */

    public static TextWatcher insert(final String mask, final EditText ediTxt) {
        return new TextWatcher() {
            boolean isUpdating;
            String old = "";
            public void onTextChanged(CharSequence s, int start, int before,int count) {
                String str = Mascara.unmask(s.toString());
                String mascara = "";
                if (isUpdating) {
                    old = str;
                    isUpdating = false;
                    return;
                }
                int i = 0;
                for (char m : mask.toCharArray()) {
                    if (m != '#' && str.length() > old.length()) {
                        mascara += m;
                        continue;
                    }
                    try {
                        mascara += str.charAt(i);
                    } catch (Exception e) {
                        break;
                    }
                    i++;
                }
                isUpdating = true;
                ediTxt.setText(mascara);
                ediTxt.setSelection(mascara.length());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        };
    }

    public static String[] split(String text) {
        java.util.List<String> parts = new java.util.ArrayList<>();

        text += ",";

        for (int i = text.indexOf(","), j = 0; i != -1;) {
            String temp = text.substring(j,i);
            if(temp.trim().length() != 0) {
                parts.add(temp);
            }
            j = i + ",".length();
            i = text.indexOf(",",j);
        }

        return parts.toArray(new String[0]);
    }
}
