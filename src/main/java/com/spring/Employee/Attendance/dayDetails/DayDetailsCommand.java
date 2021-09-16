package com.spring.Employee.Attendance.dayDetails;

import com.spring.interfaces.IdOwner;
import com.spring.modelMapperGen.ModelMapperGen;

import java.time.LocalDate;

public class DayDetailsCommand implements IdOwner {

    private Long id = 0L;
    private String date;
    private boolean absent = false;
    private Float bonusInSalary = 0.0F;

    public DayDetailsCommand() {
    }


    public DayDetailsCommand(Long id, String date, boolean absent, Float bonusInSalary) {
        this.id = id;
        this.date = date;
        this.absent = absent;
        this.bonusInSalary = bonusInSalary;
    }

    public static void mapDayCommandToDayDetails(DayDetailsCommand dayDetailsCommand, DayDetails dayDetails) {
        ModelMapperGen.getModelMapperSingleton().map(dayDetailsCommand, dayDetails);
        dayDetails.setDate(LocalDate.parse(dayDetailsCommand.date));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isAbsent() {
        return absent;
    }

    public void setAbsent(boolean absent) {
        this.absent = absent;
    }

    public Float getBonusInSalary() {
        return bonusInSalary;
    }

    public void setBonusInSalary(Float bonusInSalary) {
        this.bonusInSalary = bonusInSalary;
    }
}
