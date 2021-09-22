package com.hrsystem.employee.commands;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hrsystem.attendancelogs.AttendanceTable;
import com.hrsystem.department.Department;
import com.hrsystem.employee.Degree;
import com.hrsystem.employee.Employee;
import com.hrsystem.employee.Gender;
import com.hrsystem.security.EmployeeRole;
import com.hrsystem.security.UserCredentials;
import com.hrsystem.team.Team;
import com.hrsystem.utilities.ModelMapperGen;
import com.hrsystem.utilities.interfaces.IEmployeeInfoCommand;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.convention.MatchingStrategies;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class AddEmployeeCommand implements IEmployeeInfoCommand {
    private String nationalId;
    private String firstName;
    private String lastName;
    private Degree degree;
    private Integer yearsOfExperience;
    private LocalDate birthDate;
    private Date graduationDate;
    private Gender gender;
    private Department department;
    private Float grossSalary;
    private Float netSalary;
    private AttendanceTable attendanceTable;
    private UserCredentials userCredentials;
    private EmployeeRole role;
    private List<Employee> subEmployees;
    private Employee manager;
    private Float salaryRaise = 0f;

    public void commandToEmployee(Employee employee) {
        ModelMapperGen.getModelMapperSingleton().getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        ModelMapperGen.getModelMapperSingleton().map(this, employee);
        ModelMapperGen.getModelMapperSingleton().getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);

    }
}
