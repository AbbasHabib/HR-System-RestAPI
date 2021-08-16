package com.spring.Employee.dto;

import com.spring.Department.Department;
import com.spring.Employee.Employee;

import java.util.Date;
import java.util.Set;

public class EmployeeModifyDto
{
    private String name;
    private Date birthDate;
    private Date graduationDate;
    private char gender = '\0';
    private Department department;
    private Employee manager;
    private Set<Employee> employees;
    private Float grossSalary;

    public static void dtoToEmployee(EmployeeModifyDto dto, Employee employee)
    {
        if(dto.name != null && !dto.name.equals(""))
            employee.setName(dto.name);
        if(dto.graduationDate != null)
            employee.setGraduationDate(dto.graduationDate);
        if(dto.gender != '\0')
            employee.setGender(dto.gender);
        if(dto.department != null)
            employee.setDepartment(dto.department);
        if(dto.manager != null)
            employee.setManager(dto.manager);
        if(dto.employees != null)
            employee.setEmployees(dto.employees);
        if(dto.grossSalary != null)
            employee.setGrossSalary(dto.grossSalary);
    }


    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Date getBirthDate()
    {
        return birthDate;
    }

    public void setBirthDate(Date birthDate)
    {
        this.birthDate = birthDate;
    }

    public Date getGraduationDate()
    {
        return graduationDate;
    }

    public void setGraduationDate(Date graduationDate)
    {
        this.graduationDate = graduationDate;
    }

    public char getGender()
    {
        return gender;
    }

    public void setGender(char gender)
    {
        this.gender = gender;
    }

    public Department getDepartment()
    {
        return department;
    }

    public void setDepartment(Department department)
    {
        this.department = department;
    }

    public Employee getManager()
    {
        return manager;
    }

    public void setManager(Employee manager)
    {
        this.manager = manager;
    }

    public Set<Employee> getEmployees()
    {
        return employees;
    }

    public void setEmployees(Set<Employee> employees)
    {
        this.employees = employees;
    }

    public Float getGrossSalary()
    {
        return grossSalary;
    }

    public void setGrossSalary(Float grossSalary)
    {
        this.grossSalary = grossSalary;
    }
}
