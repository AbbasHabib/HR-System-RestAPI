package com.hrsystem.attendancelogs.daydetails;

import com.hrsystem.utilities.interfaces.IdOwner;
import com.hrsystem.utilities.ModelMapperGen;
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
