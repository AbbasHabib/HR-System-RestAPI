package com.spring.Employee;

import com.spring.Department.Department;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "employee")
public class Employee
{
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "birth_date")
    @Temporal(TemporalType.DATE)
    private Date birthDate;
    @Column(name = "graduation_date")
    @Temporal(TemporalType.DATE)
    private Date graduationDate;
    @Column(name = "gender")
    private char gender;

    @ManyToOne
    @JoinColumn(name="department_id", nullable=false)
    private Department department;

    @Column(name="manager_id", nullable = true)
    private Long managerId;
    @Column(name="gross_salary")
    private Float grossSalary;
    @Column(name="net_salary")
    private Float netSalary;

    // list of expertise

    public Employee(){}

    public Long getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public Date getBirthDate()
    {
        return birthDate;
    }

    public Date getGraduationDate()
    {
        return graduationDate;
    }

    public char getGender()
    {
        return gender;
    }

    public Long getManagerId()
    {
        return managerId;
    }

    public Float getGrossSalary()
    {
        return grossSalary;
    }

    public Float getNetSalary()
    {
        return netSalary;
    }
}
