package com.spring.Department;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spring.Employee.Employee;
import com.spring.interfaces.IdOwner;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "department")
@Getter
@Setter
public class Department implements IdOwner {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @JsonIgnore
    @OneToMany(mappedBy = "department")
    private Set<Employee> employees;

    public Department() {
    }

    public Department(String name) {
        this.name = name;
    }
}
