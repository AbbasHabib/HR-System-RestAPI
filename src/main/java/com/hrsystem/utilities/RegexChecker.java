package com.hrsystem.utilities;

public class RegexChecker {
    public static boolean isStringOnlyAlphabet(String str)
    {
        return (!str.equals("") && str.matches("^[a-zA-Z]*$"));
    }

    public static boolean isNumbersOnly(String str)
    {
        return (!str.equals("") && str.matches("^[0-9]+$"));
    }
}
