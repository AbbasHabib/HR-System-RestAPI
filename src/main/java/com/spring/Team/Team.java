package com.spring.Team;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spring.Employee.Employee;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Team
{
    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(name = "team_id_seq", sequenceName = "team_id_seq", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "team_id_seq")
    private Long id;
    @Column(name = "team_name")
    private String teamName;
    @JsonIgnore
    @OneToMany(mappedBy = "team")
    private Set<Employee> employees;

    public Team() { }

    public Team(Long id, String teamName)
    {
        this.id = id;
        this.teamName = teamName;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getTeamName()
    {
        return teamName;
    }

    public void setTeamName(String teamName)
    {
        this.teamName = teamName;
    }

    public Set<Employee> getEmployees()
    {
        return employees;
    }

    public void setEmployees(Set<Employee> employees)
    {
        this.employees = employees;
    }

    public void addEmployee(Employee e)
    {
        if(this.employees == null)
            this.employees = new HashSet<>();
        this.employees.add(e);
    }

}
