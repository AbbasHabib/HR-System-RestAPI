package com.hrsystem.team;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hrsystem.employee.Employee;
import com.hrsystem.utilities.interfaces.IdOwner;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
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

    public void addEmployee(Employee e) {
        if (this.employees == null)
            this.employees = new HashSet<>();
        this.employees.add(e);
    }

}
