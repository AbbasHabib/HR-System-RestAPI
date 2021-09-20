package com.hrsystem.attendancelogs;


import com.hrsystem.attendancelogs.daydetails.DayDetails;
import com.hrsystem.attendancelogs.daydetails.DayDetailsCommand;
import com.hrsystem.attendancelogs.daydetails.DayDetailsDTO;
import com.hrsystem.attendancelogs.daydetails.DayDetailsRepository;
import com.hrsystem.attendancelogs.monthdetails.MonthDTO;
import com.hrsystem.attendancelogs.monthdetails.MonthDetails;
import com.hrsystem.attendancelogs.monthdetails.MonthDetailsRepository;
import com.hrsystem.employee.EmployeeService;
import com.hrsystem.employee.dtos.EmployeeSalaryDTO;
import com.hrsystem.employee.dtos.EmployeeSalaryDTOBuilder;
import com.hrsystem.utilities.CustomException;
import com.hrsystem.utilities.interfaces.constants.SalariesYearsConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AttendanceService {
    @Autowired
    EmployeeService employeeService;
    @Autowired
    private AttendanceRepository attendanceRepository;
    @Autowired
    private DayDetailsRepository dailyAttendanceRepository;
    @Autowired
    private MonthDetailsRepository monthDetailsRepository;


    public AttendanceTable getAttendanceTableByEmployeeId(long employeeId) throws CustomException {
        AttendanceTable attendanceTable = attendanceRepository.findByEmployeeId(employeeId).orElse(null);
        if (attendanceTable != null)
            return attendanceTable;
        throw new CustomException("""
                *this employee id does not exist
                *or employee doesn't have an attendance table""");
    }


    public DayDetailsDTO addNewDayDataOrModifyAndSave(Long employeeId, DayDetailsCommand dayDetailsCommand, boolean isModification) throws CustomException {
        DayDetails dayDetails = new DayDetails();

        AttendanceTable attendanceTable = getAttendanceTableByEmployeeId(employeeId);

        // map command to day object
        DayDetailsCommand.mapDayCommandToDayDetails(dayDetailsCommand, dayDetails);


        if (isModification) {
            DayDetails dayBeforeModification = dailyAttendanceRepository.findByAttendanceTable_IdAndDate(attendanceTable.getId(), dayDetails.getDate());
            removeDayDataFromMonth(dayBeforeModification, attendanceTable.getId());
        }
        else if (dailyAttendanceRepository.countAllByAttendanceTable_IdAndDate(attendanceTable.getId(), dayDetails.getDate()) > 0) {
            throw new CustomException("this day already exists!\ncheck day modification api!");
        }


        // get month this function will create it if not found
        MonthDetails monthOfThatDay = getMonthOfDayAndCreateIfNotFound(dayDetails.getDate(), attendanceTable);

        // inject month and day inside the attendance table
        injectDayAndMonthToAttendanceTable(dayDetails, monthOfThatDay, attendanceTable);

        // calculate and insert absences and bounces at this month
        insertDayDataToMonth(dayDetails, monthOfThatDay);


        // save month, day and attendance table into database
        DayDetails savedDayDetails = dailyAttendanceRepository.save(dayDetails);
        monthDetailsRepository.save(monthOfThatDay);
        attendanceRepository.save(attendanceTable);

        // map day to dto
        DayDetailsDTO responseDayDetailsDTO = new DayDetailsDTO();
        DayDetailsDTO.setDayDetailsToDTO(savedDayDetails, responseDayDetailsDTO);

        // return the dto
        return responseDayDetailsDTO;
    }


    public void injectDayAndMonthToAttendanceTable(DayDetails dayDetails, MonthDetails monthOfThatDay, AttendanceTable attendanceTable) {
        // inject day to attendance table
        dayDetails.setAttendanceTable(attendanceTable);
        if (monthOfThatDay.getAttendanceTable() == null) {
            // if month didn't exist before add new Day and Month to attendance Table lists
            monthOfThatDay.setAttendanceTable(attendanceTable);
            attendanceTable.addMonthAndDayDetails(dayDetails, monthOfThatDay);
        } else {
            // if month existed just add the day to attendanceTable
            attendanceTable.addDay(dayDetails);
        }
    }

    private void insertDayDataToMonth(DayDetails dayDetails, MonthDetails monthOfThatDay) {
        if (dayDetails.isAbsent())
            monthOfThatDay.addAbsence(1);
        if (dayDetails.getBonusInSalary() > 0)
            monthOfThatDay.addBonus(dayDetails.getBonusInSalary());
    }

    // this method is called if there was any modification in a day
    // to remove all previous data of that day from this day month
    private void removeDayDataFromMonth(DayDetails dayBeforeModification, Long attendanceTableId) throws CustomException {
        MonthDetails monthOfDayToModify = monthDetailsRepository.findByDateAndAttendanceTable_Id(dayBeforeModification.getDate(), attendanceTableId).orElse(null);
        if (monthOfDayToModify == null)
            throw new CustomException("this day did not exist before!");
        monthOfDayToModify.addAbsence((dayBeforeModification.isAbsent() ? -1 : 0));// if this day had absence remove it from the month

        float bonusStoredInDay = dayBeforeModification.getBonusInSalary();
        monthOfDayToModify.addBonus((bonusStoredInDay > 0f) ? (bonusStoredInDay * -1) : 0);// if this day had bonus in salary remove it from the month
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

    public MonthDetails getMonthDetails(long employeeId, LocalDate date) throws CustomException {
        MonthDetails monthDetails = monthDetailsRepository
                .findByDateAndAttendanceTable_Id(
                        LocalDate.of(date.getYear(), date.getMonth(), 1)
                        , getAttendanceTableIdByEmployeeId(employeeId))
                .orElse(null);

        if (monthDetails == null)
            throw new CustomException("this month is not found");

        return monthDetails;
    }

    public MonthDTO getMonthDetailsDTO(long employeeId, LocalDate date) throws CustomException {
        MonthDetails monthData = monthDetailsRepository
                .findByDateAndAttendanceTable_Id(
                        LocalDate.of(date.getYear(), date.getMonth(), 1)
                        , getAttendanceTableIdByEmployeeId(employeeId))
                .orElse(null);

        if (monthData == null)
            throw new CustomException("this month is not found");
        MonthDTO monthDTO = new MonthDTO();
        MonthDTO.setMonthDetailsToDTO(monthData, monthDTO);
        return monthDTO;
    }

    public MonthDTO getMonthDetailsByLoggedUser(LocalDate date) throws CustomException {
        Long employeeId = employeeService.getEmployeeIdFromAuthentication();
        return getMonthDetailsDTO(employeeId, date);
    }

    public EmployeeSalaryDTO employeeSalaryAtMonthByLoggedUser(LocalDate date) throws CustomException {
        Long employeeId = employeeService.getEmployeeIdFromAuthentication();
        return employeeSalaryAtMonth(employeeId, date);
    }

    public List<MonthDetails> getAllSalaryHistoryByLoggedUser() throws CustomException {
        Long employeeId = employeeService.getEmployeeIdFromAuthentication();
        return getAllSalaryHistory(employeeId);
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
        MonthDetails monthInquiring = getMonthDetails(employeeId, date);

        Integer absenceDaysTillMonth = calcAbsenceDaysInYearTillMonth(employeeId, date);
        Float monthBonuses = monthInquiring.getBonuses();
        Integer monthAbsences = monthInquiring.getAbsences();
        Integer workingYearsTillMonth = calculateWorkingYearsTillMonth(date, attendanceTable.getInitialWorkingYears(), attendanceTableId);
        Integer permittedAbsenceDays = attendanceTable.getPermittedAbsenceDays(workingYearsTillMonth);

        Float netSalary = calculateNetSalary(
                monthInquiring.getGrossSalaryOfMonth()
                , absenceDaysTillMonth
                , monthBonuses
                , attendanceTable.getEmployee().getSalaryRaise()
                , permittedAbsenceDays
                , date.getMonth().length(true)
        );

        EmployeeSalaryDTOBuilder employeeSalaryDTOBuilder = new EmployeeSalaryDTOBuilder();
        employeeSalaryDTOBuilder.setInfoDate(date)
                .setGrossSalary(monthInquiring.getGrossSalaryOfMonth())
                .setNetSalary(netSalary)
                .setNumberOfAbsencesThroughYear(absenceDaysTillMonth)
                .setNumberOfAbsencesInMonth(monthAbsences)
                .setAllowedAbsencesThroughYear(permittedAbsenceDays)
                .setExceededBy(Math.max(absenceDaysTillMonth - permittedAbsenceDays, 0));

        return employeeSalaryDTOBuilder.Build();
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


//    public void GenerateEmployeeMonthlySalary() {
//        List<Employee> employeeList = employeeService.getAllEmployees();
//
//        for(Employee emp : employeeList){
//            DayDetails dayDetails = new DayDetails()
//            this.addNewDayDataAndSave(emp, )
//        }
//    }


}
