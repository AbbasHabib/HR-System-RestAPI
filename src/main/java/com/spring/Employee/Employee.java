package com.spring.Employee;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spring.Department.Department;
import com.spring.Team.Team;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

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
    @JoinColumn(name="department_id", nullable=true)
    private Department department;

    // recursive relationship where a manager is an employee
    // many employees share the same manager id
    @ManyToOne
    @JoinColumn(name="manager_id", nullable=true)// in case the manager_id is null that means that
    // manager is a a super manager doesn't have a manager above him
    private Employee manager;

    @OneToMany(mappedBy="manager") // one manager to many employees
    private Set<Employee> employees;

    @Column(name="gross_salary")
    private Float grossSalary;
    @Column(name="net_salary")
    private Float netSalary;

    // list of expertise

    public Employee(){  };

    public Employee(Long id, String name, Date birthDate, Date graduationDate, char gender, Department department, Employee manager, Set<Employee> employees, Float grossSalary, Float netSalary)
    {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.graduationDate = graduationDate;
        this.gender = gender;
        this.department = department;
        this.manager = manager;
        this.employees = employees;
        this.grossSalary = grossSalary;
        this.netSalary = grossSalary * 0.85f - 500;
    }

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

    public Department getDepartment()
    {
        return department;
    }

    public Employee getManager()
    {
        return manager;
    }

    public Set<Employee> getEmployees()
    {
        return employees;
    }

    public Float getGrossSalary()
    {
        return grossSalary;
    }

    public Float getNetSalary()
    {
        return netSalary;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setBirthDate(Date birthDate)
    {
        this.birthDate = birthDate;
    }

    public void setGraduationDate(Date graduationDate)
    {
        this.graduationDate = graduationDate;
    }

    public void setGender(char gender)
    {
        this.gender = gender;
    }

    public void setDepartment(Department department)
    {
        this.department = department;
    }

    public void setManager(Employee manager)
    {
        this.manager = manager;
    }

    public void setEmployees(Set<Employee> employees)
    {
        this.employees = employees;
    }

    public void setGrossSalary(Float grossSalary)
    {
        this.grossSalary = grossSalary;
    }

    public void setNetSalary(Float netSalary)
    {
        this.netSalary = netSalary;
    }
}
