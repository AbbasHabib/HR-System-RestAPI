package com.spring.Team;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spring.Employee.Employee;
import com.spring.interfaces.IdOwner;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Team implements IdOwner {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = 0L;
    @Column(name = "team_name", nullable = false)
    private String teamName;
    @JsonIgnore
    @OneToMany(mappedBy = "team", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Employee> employees;

    public Team() {
    }

    public Team(Long id, String teamName) {
        this.id = id;
        this.teamName = teamName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Set<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }

    public void addEmployee(Employee e) {
        if (this.employees == null)
            this.employees = new HashSet<>();
        this.employees.add(e);
    }

}
