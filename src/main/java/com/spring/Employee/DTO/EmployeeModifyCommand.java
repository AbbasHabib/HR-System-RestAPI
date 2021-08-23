package com.spring.Employee.DTO;

import com.spring.Department.Department;
import com.spring.Employee.Employee;
import com.spring.Team.Team;
import com.spring.modelMapperGen.ModelMapperGen;
import org.hibernate.mapping.Map;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

public class EmployeeModifyCommand
{
    private String name;
    private Team team;
    private Date birthDate;
    private Date graduationDate;
    private char gender = '\0';
    private Department department;
    private Employee manager;
    private Set<Employee> employees;
    private Float grossSalary;
    private Float netSalary;

    public void dtoToEmployee(EmployeeModifyCommand dto, Employee employee)
    {
        ModelMapperGen.getModelMapperSingleton().map(dto, employee);
    }

    public Team getTeam()
    {
        return team;
    }

    public void setTeam(Team team)
    {
        this.team = team;
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