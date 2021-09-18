package com.spring.Employee.EmployeeLog.dayDetails;

import com.spring.interfaces.IdOwner;
import com.spring.modelMapperGen.ModelMapperGen;


public class DayDetailsDTO implements IdOwner {
    private Long id = 0L;
    private String date;
    private boolean absent = false;
    private Float bonusInSalary = 0.0F;

    public static void setDayDetailsToDTO(DayDetails dayDetails, DayDetailsDTO DTO) {
        ModelMapperGen.getModelMapperSingleton().map(dayDetails, DTO);
        DTO.date = dayDetails.getDate().toString();
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
