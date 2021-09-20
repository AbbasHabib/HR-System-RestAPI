package com.hrsystem.attendancelogs.daydetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hrsystem.attendancelogs.AttendanceTable;
import com.hrsystem.utilities.interfaces.IdOwner;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "day_details")
@Getter
@Setter
public class DayDetails implements IdOwner {
    @ManyToOne
    @JoinColumn(name = "attendance_table_id")
    @JsonIgnore
    AttendanceTable attendanceTable;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = 0L;
    private LocalDate date;
    private boolean absent = false;
    private Float bonusInSalary = 0.0F;


    public DayDetails(Long id, AttendanceTable attendanceTable, LocalDate date, boolean absent, Float bonusInSalary) {
        this.id = id;
        this.attendanceTable = attendanceTable;
        this.date = date;
        this.absent = absent;
        this.bonusInSalary = bonusInSalary;
    }


    public DayDetails(AttendanceTable attendanceTable, LocalDate date) {
        this.attendanceTable = attendanceTable;
        this.date = date;
    }

    public DayDetails(LocalDate date) {
        this.date = date;
    }

    public DayDetails() {
    }

}
