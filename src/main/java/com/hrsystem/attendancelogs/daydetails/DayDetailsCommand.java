package com.hrsystem.attendancelogs.daydetails;

import com.hrsystem.utilities.interfaces.IdOwner;
import com.hrsystem.utilities.ModelMapperGen;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DayDetailsCommand {

    private String date;
    private boolean absent = false;
    private Float bonusInSalary = 0.0F;

    public DayDetailsCommand() {
    }


    public DayDetailsCommand(Long id, String date, boolean absent, Float bonusInSalary) {
        this.date = date;
        this.absent = absent;
        this.bonusInSalary = bonusInSalary;
    }

    public static void mapDayCommandToDayDetails(DayDetailsCommand dayDetailsCommand, DayDetails dayDetails) {
        ModelMapperGen.getModelMapperSingleton().map(dayDetailsCommand, dayDetails);
        dayDetails.setDate(LocalDate.parse(dayDetailsCommand.date));
    }
}
