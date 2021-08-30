package com.spring.Employee;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spring.Department.Department;
import com.spring.Team.Team;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;


@Entity
@Table(name = "employee")
public class Employee
{
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = 0L;
    @Column(name = "national_id", nullable = false, unique = true)
    private String nationalId;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "first_name", nullable = false)
    private String firstname;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(name = "degree")
    @Enumerated(EnumType.STRING)
    private Degree degree;
    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;
    @Column(name = "birth_date")
    @Temporal(TemporalType.DATE)
    private Date birthDate;
    @Column(name = "graduation_date")
    @Temporal(TemporalType.DATE)
    private Date graduationDate;
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
    private Set<Employee> subEmployees;

    @Column(name = "gross_salary")
    private Float grossSalary;

    @Column(name = "net_salary")
    private Float netSalary;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private AttendanceTable attendanceTable;


    public Employee(Long id, String nationalId, String firstname, String lastName, Degree degree, Integer yearsOfExperience, Date birthDate, Date graduationDate, Gender gender, Department department, Team team, Employee manager, Set<Employee> subEmployees, Float grossSalary, Float netSalary)
    {
        this.id = id;
        this.nationalId = nationalId;
        this.firstname = firstname;
        this.lastName = lastName;
        this.degree = degree;
        this.yearsOfExperience = yearsOfExperience;
        this.birthDate = birthDate;
        this.graduationDate = graduationDate;
        this.gender = gender;
        this.department = department;
        this.team = team;
        this.manager = manager;
        this.subEmployees = subEmployees;
        this.grossSalary = grossSalary;
        this.netSalary = netSalary;
    }

    public Employee()
    {

    }

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

    public Date getBirthDate()
    {
        return birthDate;
    }

    public void setBirthDate(Date birthDate)
    {
        this.birthDate = birthDate;
    }

    public Date getGraduationDate()
    {
        return graduationDate;
    }

    public void setGraduationDate(Date graduationDate)
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

    public Set<Employee> getSubEmployees()
    {
        return subEmployees;
    }

    public void setSubEmployees(Set<Employee> subEmployees)
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
