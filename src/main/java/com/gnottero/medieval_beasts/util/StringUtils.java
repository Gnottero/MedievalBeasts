package com.gnottero.medieval_beasts.util;


public class StringUtils {

    public static String toTitleCase(String s) {

        final String ACTIONABLE_DELIMITERS = " '-/_"; // these cause the character following

        StringBuilder sb = new StringBuilder();
        boolean capNext = true;

        for (char c : s.toCharArray()) {
            c = (capNext)
                    ? Character.toUpperCase(c)
                    : Character.toLowerCase(c);
            sb.append(c);
            capNext = (ACTIONABLE_DELIMITERS.indexOf(c) >= 0); // explicit cast not needed
        }
        return sb.toString().replace("_", " ");
    }

}
