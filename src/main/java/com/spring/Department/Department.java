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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @JsonIgnore
    @OneToMany(mappedBy="department")
    private Set<Employee> employees;

    public Department()
    { }

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
}
