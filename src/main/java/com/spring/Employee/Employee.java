package com.spring.Employee;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spring.Department.Department;
import com.spring.Employee.Attendance.AttendanceTable;
import com.spring.Security.UserCredentials;
import com.spring.Team.Team;
import com.spring.interfaces.IdOwner;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "employee")
public class Employee implements IdOwner
{
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = 0L;
    @Column(name = "national_id", nullable = false, unique = true)
    private String nationalId;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "first_name")
    private String firstname;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "degree")
    @Enumerated(EnumType.STRING)
    private Degree degree;
    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;
    @Column(name = "birth_date")
    private LocalDate birthDate;
    @Column(name = "graduation_date")
    private LocalDate graduationDate;
    @Column(name = "gender")

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

    @Column(name = "gross_salary")
    private Float grossSalary;

    @Column(name = "net_salary")
    private Float netSalary;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "attendance_table_id")
    private AttendanceTable attendanceTable;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_name_credential")
    private UserCredentials userCredentials;

    public Employee() { }

    public boolean shiftSubordinates()
    {
        Employee managerToShiftTo = this.getManager();
        if (managerToShiftTo == null)
        {
            return false;
        }
        for (Employee emp : this.getSubEmployees())
        {
            emp.setManager(managerToShiftTo);
        }
        return true;
    }


    public AttendanceTable getAttendanceTable()
    {
        return attendanceTable;
    }

    public void setAttendanceTable(AttendanceTable attendanceTable)
    {
        this.attendanceTable = attendanceTable;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getNationalId()
    {
        return nationalId;
    }

    public void setNationalId(String nationalId)
    {
        this.nationalId = nationalId;
    }

    public String getFirstname()
    {
        return firstname;
    }

    public String getLastName()
    {
        return lastName;
    }

    public Degree getDegree()
    {
        return degree;
    }

    public void setDegree(Degree degree)
    {
        this.degree = degree;
    }

    public Integer getYearsOfExperience()
    {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(Integer yearsOfExperience)
    {
        this.yearsOfExperience = yearsOfExperience;
    }

    public LocalDate getBirthDate()
    {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate)
    {
        this.birthDate = birthDate;
    }

    public LocalDate getGraduationDate()
    {
        return graduationDate;
    }

    public void setGraduationDate(LocalDate graduationDate)
    {
        this.graduationDate = graduationDate;
    }

    public Gender getGender()
    {
        return gender;
    }

    public void setGender(Gender gender)
    {
        this.gender = gender;
    }

    public Department getDepartment()
    {
        return department;
    }

    public void setDepartment(Department department)
    {
        this.department = department;
    }

    public Team getTeam()
    {
        return team;
    }

    public void setTeam(Team team)
    {
        this.team = team;
    }

    public Employee getManager()
    {
        return manager;
    }

    public void setManager(Employee manager)
    {
        this.manager = manager;
    }

    public List<Employee> getSubEmployees()
    {
        return subEmployees;
    }

    public void setSubEmployees(List<Employee> subEmployees)
    {
        this.subEmployees = subEmployees;
    }

    public Float getGrossSalary()
    {
        return grossSalary;
    }

    public void setGrossSalary(Float grossSalary)
    {
        this.grossSalary = grossSalary;
    }

    public Float getNetSalary()
    {
        return netSalary;
    }

    public void setNetSalary(Float netSalary)
    {
        this.netSalary = netSalary;
    }

    public void setName(String name)
    {
        this.name = name;
        String[] fullName = name.split("\\s+");
        if (fullName.length >= 1)
        {
            this.firstname = fullName[0];
            if (fullName.length >= 2)
                this.lastName = fullName[1];
        }

    }

    public String getName()
    {
        return this.name;
    }
}
