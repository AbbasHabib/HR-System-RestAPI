package com.hrsystem.utilities;

import java.util.Calendar;

public class YearAndTimeGenerator {
    public static Calendar getCurrentCalender() {
        return Calendar.getInstance();
    }

    public static int getCurrentYear() {
        return getCurrentCalender().get(Calendar.YEAR);
    }

    public static int getTestingYear() {
        return 2021;
    }
}
