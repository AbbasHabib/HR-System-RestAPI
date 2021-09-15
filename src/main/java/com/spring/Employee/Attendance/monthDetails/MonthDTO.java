package com.spring.Employee.Attendance.monthDetails;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.spring.Employee.Attendance.AttendanceTable;
import com.spring.Employee.DTO.EmployeeInfoOnlyDTO;
import com.spring.Employee.Employee;
import com.spring.modelMapperGen.ModelMapperGen;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Date;
import java.time.LocalDate;

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
