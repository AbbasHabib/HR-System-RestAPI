package com.hrsystem.attendancelogs;

import com.hrsystem.attendancelogs.daydetails.DayDetailsCommand;
import com.hrsystem.employee.Employee;
import com.hrsystem.employee.EmployeeService;
import com.hrsystem.utilities.CustomException;
import com.hrsystem.utilities.TimeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class AutomatedService {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private AttendanceService attendanceService;


    @Scheduled(cron = "0 0 0 25 * *")
    public void GenerateEmployeeMonthlySalary() throws CustomException {
        List<Employee> employeeList = employeeService.getAllEmployees();
        TimeGenerator timeGenerator = new TimeGenerator();

        Date date = timeGenerator.getCurrentCalender().getTime(); // get current calender
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateStringFormatted = formatter.format(date);

        System.out.println(dateStringFormatted);

        for (Employee emp : employeeList) {
            DayDetailsCommand dayDetailsCommand = new DayDetailsCommand();
            dayDetailsCommand.setAbsent(false);
            dayDetailsCommand.setBonusInSalary(0.0f);
            dayDetailsCommand.setDate(dateStringFormatted);
            try {
                attendanceService.addNewDayDataOrModifyAndSave(emp.getId(), dayDetailsCommand, false);// this day will be inserted if it exists nothing will happen
            } catch (Exception e) {
                //nothing
            }
        }
    }
}
