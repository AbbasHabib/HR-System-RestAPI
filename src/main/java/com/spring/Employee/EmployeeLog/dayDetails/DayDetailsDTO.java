package com.spring.Employee.EmployeeLog.dayDetails;

import com.spring.interfaces.IdOwner;
import com.spring.modelMapperGen.ModelMapperGen;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DayDetailsDTO implements IdOwner {
    private Long id = 0L;
    private String date;
    private boolean absent = false;
    private Float bonusInSalary = 0.0F;

    public static void setDayDetailsToDTO(DayDetails dayDetails, DayDetailsDTO DTO) {
        ModelMapperGen.getModelMapperSingleton().map(dayDetails, DTO);
        DTO.date = dayDetails.getDate().toString();
    }
}
