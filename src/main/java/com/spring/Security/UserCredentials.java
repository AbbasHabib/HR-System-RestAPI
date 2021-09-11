package com.spring.Security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spring.Employee.Employee;

import javax.persistence.*;

@Entity
public class UserCredentials
{
    @Id
    private String userName;
    private String password;
    private Role userRole; // HR , EMPLOYEE

    @OneToOne(mappedBy = "userCredentials", cascade = CascadeType.ALL)
    @JsonIgnore
    @JoinColumn(name="employee_id")
    private Employee employee;


    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public Role getUserRole()
    {
        return userRole;
    }

    public void setUserRole(Role userRole)
    {
        this.userRole = userRole;
    }

    public Employee getEmployee()
    {
        return employee;
    }

    public void setEmployee(Employee employee)
    {
        this.employee = employee;
    }

    public UserCredentials()
    {
    }
}
