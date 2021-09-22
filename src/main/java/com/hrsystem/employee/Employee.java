package com.hrsystem.employee;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hrsystem.department.Department;
import com.hrsystem.attendancelogs.AttendanceTable;
import com.hrsystem.security.EmployeeRole;
import com.hrsystem.security.UserCredentials;
import com.hrsystem.team.Team;
import com.hrsystem.utilities.interfaces.IdOwner;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "employee")
@Getter
@Setter
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
    @JoinColumn(name = "team_id")
    private Team team;

    private String expertise;

    // recursive relationship where a manager is an employee
    // many employees share the same manager id
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "manager_id")// in case the manager_id is null that means that
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

    private Float salaryRaise = 0f;

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


    public String createUserName() {
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

    public int calcGraduationYear() {
        SimpleDateFormat getYearFormat = new SimpleDateFormat("yyyy");
        String year = getYearFormat.format(this.graduationDate);
        return Integer.parseInt(year);
    }
}
