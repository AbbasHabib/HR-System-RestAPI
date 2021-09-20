package com.hrsystem.employee;

import com.hrsystem.security.EmployeeRole;
import com.hrsystem.utilities.ModelMapperGen;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;
import java.util.Date;

@Getter
@Setter
public class EmployeeNotNullableFields {
    private String nationalId;
    private String firstName;
    private String lastName;
    private Gender gender;
    private Date graduationDate;
    private Float grossSalary;
    private EmployeeRole role;


    public EmployeeNotNullableFields(Employee employee) {
        ModelMapperGen.getModelMapperSingleton().map(employee, this);

    }

    public EmployeeNotNullableFields() {
    }

    public void mapFromEmployee(Employee employee) {
        ModelMapperGen.getModelMapperSingleton().map(employee, this);
    }

    public String checkNull() throws IllegalAccessException {
        for (Field f : getClass().getDeclaredFields())
            if (f.get(this) == null)
                return f.getName();
        return "";
    }
}
