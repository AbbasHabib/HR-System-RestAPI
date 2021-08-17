package com.spring.Employee.DTO;

import com.spring.Department.Department;
import com.spring.Employee.Employee;

import java.util.Date;
import java.util.Set;

public class EmployeeModifyDTO
{
    private String name;
    private Date birthDate;
    private Date graduationDate;
    private char gender = '\0';
    private Department department;
    private Employee manager;
    private Set<Employee> employees;
    private Float grossSalary;
    private Float netSalary;

    public static void dtoToEmployee(EmployeeModifyDTO dto, Employee employee)
    {
        if (dto.name != null && !dto.name.equals(""))
            employee.setName(dto.name);
        if (dto.graduationDate != null)
            employee.setGraduationDate(dto.graduationDate);
        if (dto.gender != '\0')
            employee.setGender(dto.gender);
        if (dto.department != null)
            employee.setDepartment(dto.department);
        if (dto.manager != null)
            employee.setManager(dto.manager);
        if (dto.birthDate != null)
            employee.setBirthDate(dto.birthDate);
        if (dto.employees != null)
            employee.setEmployees(dto.employees);
        if (dto.grossSalary != null && dto.grossSalary != 0)
        {
            employee.setGrossSalary(dto.grossSalary);
            employee.setNetSalary(dto.netSalary = dto.grossSalary * 0.85f - 500);
        }
    }

    public void setDTOtoEmployee(Employee e)
    {
        this.setName(e.getName());

        this.setGraduationDate(e.getGraduationDate());

        this.setGender(e.getGender());

        this.setDepartment(e.getDepartment());

        this.setManager(e.getManager());

        this.setEmployees(e.getEmployees());

        this.setGrossSalary(e.getGrossSalary());

        this.setNetSalary(e.getGrossSalary() * 0.85f - 500);

        this.setBirthDate(e.getBirthDate());
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

    public Float getNetSalary()
    {
        return netSalary;
    }

    public void setNetSalary(Float netSalary)
    {
        this.netSalary = netSalary;
    }
}
