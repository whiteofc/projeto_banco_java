package com.bankumj.projeto.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidaEmail {

    public static void main(String[] args) {
        System.out.println( isValidEmailAddressRegex("garbosoftware@gmail.com") );
        System.out.println( isValidEmailAddressRegex("aaabbb@gmail.com") );
        System.out.println( isValidEmailAddressRegex("invalido@com.br") );
        System.out.println( isValidEmailAddressRegex("inv@lido@com.br") );
    }

    public static boolean isValidEmailAddressRegex(String email) {
        boolean isEmailIdValid = false;
        if (email != null && email.length() > 0) {
            String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(email);
            if (matcher.matches()) {
                isEmailIdValid = true;
            }
        }
        return isEmailIdValid;
    }
    public static String imprimeEmail(String email){

        return email;
    }
}