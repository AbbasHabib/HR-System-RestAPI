package com.spring.Employee.DTO;

import com.spring.Department.Department;
import com.spring.Employee.Employee;
import com.spring.modelMapperGen.ModelMapperGen;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class EmployeeInfoOnlyDTO
{
    private Long id;
    private String name;
    private Date birthDate;
    private Date graduationDate;
    private char gender = '\0';
    private Department department;
    private Float grossSalary;
    private Float netSalary;

    public void setEmployeeToDTO(Employee e)
    {
        ModelMapperGen.getModelMapperSingleton().map(e, this);
    }

    public static List<EmployeeInfoOnlyDTO> setEmployeeToDTOList(List<Employee> employees)
    {
        List<EmployeeInfoOnlyDTO> employeesDTO = new ArrayList<>();
        for (Employee emp : employees)
        {
            EmployeeInfoOnlyDTO empDTO = new EmployeeInfoOnlyDTO();
            empDTO.setEmployeeToDTO(emp);
            employeesDTO.add(empDTO);
        }
        return employeesDTO;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
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