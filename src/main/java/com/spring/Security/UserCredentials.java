package com.spring.Security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spring.Employee.Employee;

import javax.persistence.*;

@Entity
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public EmployeeRole getUserRole() {
        return userRole;
    }

    public void setUserRole(EmployeeRole userRole) {
        this.userRole = userRole;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
