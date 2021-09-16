package com.spring.Employee.Attendance.monthDetails;

import com.spring.modelMapperGen.ModelMapperGen;

import java.sql.Date;

public class MonthDTO {
    private Integer id;
    private Float grossSalaryOfMonth = null;
    private Date date;
    private Integer absences = 0;
    private Float bonuses = 0F;


    public static void setMonthDetailsToDTO(MonthDetails m, MonthDTO DTO) {
        ModelMapperGen.getModelMapperSingleton().map(m, DTO);
        DTO.date = Date.valueOf(m.getDate());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Float getGrossSalaryOfMonth() {
        return grossSalaryOfMonth;
    }

    public void setGrossSalaryOfMonth(Float grossSalaryOfMonth) {
        this.grossSalaryOfMonth = grossSalaryOfMonth;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getAbsences() {
        return absences;
    }

    public void setAbsences(Integer absences) {
        this.absences = absences;
    }

    public Float getBonuses() {
        return bonuses;
    }

    public void setBonuses(Float bonuses) {
        this.bonuses = bonuses;
    }
}
