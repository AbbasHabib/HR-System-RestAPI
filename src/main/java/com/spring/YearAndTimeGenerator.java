package com.spring;

import java.util.Calendar;

public class YearAndTimeGenerator
{
    public static int getCurrentYear()
    {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public static int getTestingYear()
    {
        return 2021;
    }
}
