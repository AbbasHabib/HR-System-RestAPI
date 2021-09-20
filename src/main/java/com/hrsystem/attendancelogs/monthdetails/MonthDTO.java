package com.hrsystem.attendancelogs.monthdetails;

import com.hrsystem.utilities.ModelMapperGen;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
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
}
