package com.spring.Employee.Attendance;


import com.spring.Employee.Attendance.dayDetails.DayDetails;
import com.spring.Employee.Attendance.dayDetails.DayDetailsRepository;
import com.spring.Employee.Attendance.monthDetails.MonthDetails;
import com.spring.Employee.Attendance.monthDetails.MonthDetailsRepository;
import com.spring.Employee.DTO.EmployeeSalaryDTO;
import com.spring.Employee.Employee;
import com.spring.Employee.SalariesYearsConstants;
import com.spring.ExceptionsCustom.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AttendanceService {
    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private DayDetailsRepository dailyAttendanceRepository;

    @Autowired
    private MonthDetailsRepository monthDetailsRepository;

    public AttendanceTable findAttendanceTable(Long attendanceTableId) {
        // if no table with this id is found return null
        return attendanceRepository.findById(attendanceTableId).orElse(null);
    }

    public AttendanceTable getAttendanceTable(Long attendanceTableId) throws CustomException {
        AttendanceTable attendanceTableToFind = this.findAttendanceTable(attendanceTableId);
        if (attendanceTableToFind == null)
            throw new CustomException("this table id doesnt exist");
        else
            return attendanceTableToFind;
    }

    public AttendanceTable getAttendanceTableByEmployeeId(long employeeId) throws CustomException {
        AttendanceTable attendanceTable = attendanceRepository.findByEmployeeId(employeeId).orElse(null);
        if (attendanceTable != null)
            return attendanceTable;
        throw new CustomException("""
                *this employee id doesn't exist
                *or employee doesn't have an attendance table
                *-->check add attendance table api""");
    }


    public AttendanceTable saveToDb(AttendanceTable attendanceTable)
    {
        return attendanceRepository.save(attendanceTable);
    }

    // this api return a list of all days info where employee had stored day info
    public List<DayDetails> findAllStoredDaysInfo(Long attendanceTableId) {
        return dailyAttendanceRepository.findAllByAttendanceTable_Id(attendanceTableId);
    }

    public DayDetails addNewDayDataAndSave(Long employeeId, DayDetails dayDetails) throws CustomException {
        // get attendance table to add month and day data into
        AttendanceTable attendanceTable = getAttendanceTableByEmployeeId(employeeId);

        if (dailyAttendanceRepository.countAllByAttendanceTable_IdAndDate(attendanceTable.getId(), dayDetails.getDate()) > 0)
            throw new CustomException("Info at this date is already stored for this employee\n->use update day data api");

        // get month this function will create it not found
        MonthDetails monthOfThatDay = getMonthOfDayAndCreateIfNotFound(dayDetails.getDate(), attendanceTable);

        // inject month and day inside the attendance table
        injectDayAndMonthToAttendanceTable(dayDetails, monthOfThatDay, attendanceTable);

        // calculate and insert absences and bounces at this month
        insertDayDataToMonth(dayDetails, monthOfThatDay);


        // save month, day and attendance table into database
        DayDetails savedDate = dailyAttendanceRepository.save(dayDetails);
        monthDetailsRepository.save(monthOfThatDay);
        attendanceRepository.save(attendanceTable);
        return savedDate;
    }




    public void injectDayAndMonthToAttendanceTable(DayDetails dayDetails, MonthDetails monthOfThatDay, AttendanceTable attendanceTable) {
        // inject to attendance table dayDetails
        dayDetails.setAttendanceTable(attendanceTable);
        if (monthOfThatDay.getAttendanceTable() == null) {
            // if month didn't exist before add new Day and Month to attendance Table lists
            monthOfThatDay.setAttendanceTable(attendanceTable);
            attendanceTable.addMonthAndDayDetails(dayDetails, monthOfThatDay);
        } else
            // if month existed just add the day to attendanceTable-> Days List
            attendanceTable.addDay(dayDetails);

    }

    private void insertDayDataToMonth(DayDetails dayDetails, MonthDetails monthOfThatDay) {
        if (dayDetails.isAbsent())
            monthOfThatDay.addAbsence(1);
        if (dayDetails.getBonusInSalary() > 0)
            monthOfThatDay.addBonus(dayDetails.getBonusInSalary());

    }


    public MonthDetails getMonthOfDayAndCreateIfNotFound(LocalDate date, AttendanceTable attendanceTable) {
        // 1 doesn't mean any thing all I care about is month and year
        LocalDate monthDate = LocalDate.of(date.getYear(), date.getMonth(), 1);
        MonthDetails monthToFind = monthDetailsRepository.findByDateAndAttendanceTable_Id(monthDate, attendanceTable.getId()).orElse(null);
        if (monthToFind == null) {
            monthToFind = new MonthDetails(monthDate);
        }
        if (monthToFind.getGrossSalaryOfMonth() == null) {
            // to change the salary later , there will be a change for days and month apis
            monthToFind.setGrossSalaryOfMonth(attendanceTable.getEmployee().getGrossSalary());
        }
        return monthToFind;
    }

    public MonthDetails getMonthData(long employeeId, LocalDate date) throws CustomException {
        MonthDetails monthData = monthDetailsRepository
                .findByDateAndAttendanceTable_Id(
                        LocalDate.of(date.getYear(), date.getMonth(), 1)
                        , getAttendanceTableIdByEmployeeId(employeeId))
                .orElse(null);

        if (monthData == null)
            throw new CustomException("this month is not found");
        return monthData;
    }

    public Long getAttendanceTableIdByEmployeeId(Long employeeId) throws CustomException {
        return getAttendanceTableByEmployeeId(employeeId).getId();
    }

    public Integer calcAbsenceDaysInYearTillMonth(long employeeId, LocalDate date) throws CustomException {
        int absenceDays = 0;
        List<MonthDetails> monthDetails = monthDetailsRepository.findAllByDateBetweenAndAttendanceTable_Id(
                LocalDate.of(date.getYear(), 1, 1)
                , LocalDate.of(date.getYear(), date.getMonth(), 1)
                , getAttendanceTableIdByEmployeeId(employeeId));

        for (MonthDetails md : monthDetails)
            absenceDays += md.getAbsences();

        return absenceDays;
    }


    public EmployeeSalaryDTO employeeSalaryAtMonth(long employeeId, LocalDate date) throws CustomException {
        AttendanceTable attendanceTable = getAttendanceTableByEmployeeId(employeeId);
        Long attendanceTableId = attendanceTable.getId();
        MonthDetails monthInquiring = getMonthData(employeeId, date);

        Integer absenceDaysTillMonth = calcAbsenceDaysInYearTillMonth(employeeId, date);
        Float monthBonuses = monthInquiring.getBonuses();
        Integer monthAbsences = monthInquiring.getAbsences();
        Integer workingYearsTillMonth = calculateWorkingYearsTillMonth(date, attendanceTable.getInitialWorkingYears(), attendanceTableId);
        Integer permittedAbsenceDays = attendanceTable.getPermittedAbsenceDays(workingYearsTillMonth);

        Float netSalary = calculateNetSalary(
                monthInquiring.getGrossSalaryOfMonth()
                , absenceDaysTillMonth
                , monthBonuses
                , attendanceTable.getSalaryRaise()
                , permittedAbsenceDays
                , date.getMonth().length(true)
        );

        EmployeeSalaryDTO employeeSalaryDTO = new EmployeeSalaryDTO();
        employeeSalaryDTO.setInfoDate(date)
                .setGrossSalary(monthInquiring.getGrossSalaryOfMonth())
                .setNetSalary(netSalary)
                .setNumberOfAbsencesThroughYear(absenceDaysTillMonth)
                .setNumberOfAbsencesInMonth(monthAbsences)
                .setAllowedAbsencesThroughYear(permittedAbsenceDays)
                .setExceededBy(Math.max(absenceDaysTillMonth - permittedAbsenceDays, 0));

        return employeeSalaryDTO;
    }


    private float calculateNetSalary(float grossSalary, int absenceDaysTillMonth, float monthBonuses, float salaryRaise, int permittedAbsenceDays, int monthDays) {
        float netSalary = grossSalary + monthBonuses + salaryRaise;
        if (absenceDaysTillMonth > permittedAbsenceDays) {
            float salaryPerDay = netSalary / monthDays;
            netSalary -= salaryPerDay * absenceDaysTillMonth;
        }
        netSalary = netSalary * (1 - SalariesYearsConstants.TAXES) - SalariesYearsConstants.DEDUCTED_INSURANCE;
        return netSalary;

    }

    private Integer calculateWorkingYearsTillMonth(LocalDate toDate, int employeeInitialYearsAtWork, Long attendanceTableId) {
        MonthDetails FirstYearAtWorkMonthDetails = monthDetailsRepository.findFirstByAttendanceTable_IdOrderByDateAsc(attendanceTableId);

        int startWorkingYear = FirstYearAtWorkMonthDetails.getDate().getYear();

        // employee working years since join till this month
        // I was told to make employeeInitialYearsAtWork starting from his graduation date..
        return (startWorkingYear - toDate.getYear()) + employeeInitialYearsAtWork;
    }


    public List<MonthDetails> getAllSalaryHistory(long employeeID) throws CustomException {
        Long attendanceTableId = this.getAttendanceTableIdByEmployeeId(employeeID);
        return monthDetailsRepository.findAllByAttendanceTable_IdAndGrossSalaryOfMonthNotNullOrderByDateAsc(attendanceTableId);
    }
}
