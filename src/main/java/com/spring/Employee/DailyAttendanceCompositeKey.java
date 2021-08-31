package com.spring.Employee;

import java.io.Serializable;
import java.time.LocalDate;

public class DailyAttendanceCompositeKey implements Serializable
{
        private Long id;
        private LocalDate date;
}
