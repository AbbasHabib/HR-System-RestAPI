package com.spring.Employee.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.spring.Department.Department;
import com.spring.Employee.Employee;
import com.spring.Employee.Gender;
import com.spring.Team.Team;
import com.spring.modelMapperGen.ModelMapperGen;

import java.util.Date;
import java.util.Set;

public class EmployeeModifyCommand {
    private String lastName;
    private String firstName;
    private Team team;
    private Date birthDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date graduationDate;
    private Gender gender;
    private Department department;
    private Employee manager;
    private Set<Employee> employees;
    private Float grossSalary;
    private Float netSalary;

    public void dtoToEmployee(EmployeeModifyCommand dto, Employee employee) {
        ModelMapperGen.getModelMapperSingleton().map(dto, employee);
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Date getGraduationDate() {
        return graduationDate;
    }

    public void setGraduationDate(Date graduationDate) {
        this.graduationDate = graduationDate;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Employee getManager() {
        return manager;
    }

    public void setManager(Employee manager) {
        this.manager = manager;
    }

    public Set<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }

    public Float getGrossSalary() {
        return grossSalary;
    }

    public void setGrossSalary(Float grossSalary) {
        this.grossSalary = grossSalary;
    }

    public Float getNetSalary() {
        return netSalary;
    }

    public void setNetSalary(Float netSalary) {
        this.netSalary = netSalary;
    }
}
