package com.spring.Security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spring.Employee.Employee;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class UserCredentials {
    @Id
    private String userName;
    private String password;
    @Enumerated(EnumType.STRING)
    private EmployeeRole userRole; // HR , EMPLOYEE

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "employee_id")
    private Employee employee;


    public UserCredentials(String userName, String password, EmployeeRole userRole, Employee employee) {
        this.userName = userName;
        this.password = password;
        this.userRole = userRole;
        this.employee = employee;
    }

    public UserCredentials() {
    }

}
