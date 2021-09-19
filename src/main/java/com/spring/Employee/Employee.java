package com.spring.Employee;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spring.Department.Department;
import com.spring.Employee.EmployeeLog.AttendanceTable;
import com.spring.Security.EmployeeRole;
import com.spring.Security.UserCredentials;
import com.spring.Team.Team;
import com.spring.interfaces.IdOwner;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "employee")
public class Employee implements IdOwner {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = 0L;
    @Column(name = "national_id", nullable = false, unique = true)
    private String nationalId;
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(name = "degree")
    @Enumerated(EnumType.STRING)
    private Degree degree;
    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;
    @Column(name = "birth_date")
    private LocalDate birthDate;
    @Column(name = "graduation_date", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date graduationDate;
    @Column(name = "gender", nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @ManyToOne
    @JoinColumn(name = "department_id", nullable = true)
    private Department department;
    @ManyToOne
    @JoinColumn(name = "team_id", nullable = true)
    private Team team;

    // recursive relationship where a manager is an employee
    // many employees share the same manager id
    @ManyToOne
    @JoinColumn(name = "manager_id", nullable = true)// in case the manager_id is null that means that
    // manager is a a super manager doesn't have a manager above him
    private Employee manager;

    @JsonIgnore
    @OneToMany(mappedBy = "manager") // one manager to many employees
    private List<Employee> subEmployees;

    @Column(name = "gross_salary", nullable = false)
    private Float grossSalary;
    @Column(name = "net_salary")
    private Float netSalary;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "attendance_table_id")
    private AttendanceTable attendanceTable;

    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL)
    private UserCredentials userCredentials;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private EmployeeRole role;

    private Float SalaryRaise = 0f;

    public Employee() {
    }

    public Employee(Long id, String nationalId, String firstName, String lastName, Date graduationDate, Gender gender, Employee manager, Float grossSalary, Float netSalary, EmployeeRole role) {
        this.id = id;
        this.nationalId = nationalId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.graduationDate = graduationDate;
        this.gender = gender;
        this.manager = manager;
        this.grossSalary = grossSalary;
        this.netSalary = netSalary;
        this.role = role;
    }

    public boolean shiftSubordinates() {
        Employee managerToShiftTo = this.getManager();
        if (managerToShiftTo == null) {
            return false;
        }
        for (Employee emp : this.getSubEmployees()) {
            emp.setManager(managerToShiftTo);
        }
        return true;
    }


    public String getUserName() {
        return this.firstName + "_" + this.lastName + "_" + this.id;
    }


    public void setFullName(String name) {
        String[] fullName = name.split("\\s+");
        if (fullName.length >= 1) {
            this.firstName = fullName[0];
            if (fullName.length >= 2)
                this.lastName = fullName[1];
        }
    }

    public Float getSalaryRaise() {
        return SalaryRaise;
    }

    public void setSalaryRaise(Float salaryRaise) {
        SalaryRaise = salaryRaise;
    }

    public EmployeeRole getRole() {
        return role;
    }

    public void setRole(EmployeeRole role) {
        this.role = role;
    }

    public UserCredentials getUserCredentials() {
        return userCredentials;
    }

    public void setUserCredentials(UserCredentials userCredentials) {
        this.userCredentials = userCredentials;
    }

    public AttendanceTable getAttendanceTable() {
        return attendanceTable;
    }

    public void setAttendanceTable(AttendanceTable attendanceTable) {
        this.attendanceTable = attendanceTable;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Degree getDegree() {
        return degree;
    }

    public void setDegree(Degree degree) {
        this.degree = degree;
    }

    public Integer getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(Integer yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Date getGraduationDate() {
        return graduationDate;
    }

    public void setGraduationDate(Date graduationDate) {
        this.graduationDate = graduationDate;
    }

    public int calcGraduationYear() {
        SimpleDateFormat getYearFormat = new SimpleDateFormat("yyyy");
        String year = getYearFormat.format(this.graduationDate);
        return Integer.parseInt(year);
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

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Employee getManager() {
        return manager;
    }

    public void setManager(Employee manager) {
        this.manager = manager;
    }

    public List<Employee> getSubEmployees() {
        return subEmployees;
    }

    public void setSubEmployees(List<Employee> subEmployees) {
        this.subEmployees = subEmployees;
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
