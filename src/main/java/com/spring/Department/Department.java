package com.spring.Department;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spring.Employee.Employee;
import org.aspectj.lang.annotation.Before;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "department")
public class Department
{
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = 0L;
    @Column(name = "name", nullable = false)
    private String name;
    @JsonIgnore
    @OneToMany(mappedBy = "department")
    private Set<Employee> employees;

    public Department()
    {
    }

    public Department(String name)
    {
        this.name = name;
    }

    public Long getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public Set<Employee> getEmployees()
    {
        return employees;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setEmployees(Set<Employee> employees)
    {
        this.employees = employees;
    }
}
