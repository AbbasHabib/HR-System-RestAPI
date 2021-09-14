package com.spring.Employee;

import com.spring.Employee.Attendance.AttendanceRepository;
import com.spring.Employee.Attendance.AttendanceService;
import com.spring.Employee.Attendance.AttendanceTable;
import com.spring.Employee.DTO.EmployeeInfoOnlyDTO;
import com.spring.Employee.DTO.EmployeeModifyCommand;
import com.spring.ExceptionsCustom.CustomException;
import com.spring.Security.UserCredentials;
import com.spring.Security.UserCredentialsRepository;
import com.spring.YearAndTimeGenerator;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private AttendanceService attendanceService;
    @Autowired
    private AttendanceRepository attendanceRepository;
    @Autowired
    private UserCredentialsRepository userCredentialsRepository;

    public Employee addEmployee(Employee employee) throws Exception, CustomException {
        if (employeeRepository.findEmployeeByNationalId(employee.getNationalId()).isPresent())
            throw new CustomException(">>national id already exists?");


        if (employee.getId() == null)
            return saveEmployee(employee);
        if (this.getEmployee(employee.getId()) == null)
            return saveEmployee(employee);

        throw new CustomException(">>User ID already exists");
    }

    public void AddCredentials(Employee employee) {
        if (employee.getUserCredentials() == null) {
            UserCredentials userCredentialForNewEmployee = new UserCredentials(employee.getUserName(), employee.getNationalId(), employee.getRole(), employee);
            UserCredentials userCredentialResponseFromServer = userCredentialsRepository.save(userCredentialForNewEmployee);
            employee.setUserCredentials(userCredentialResponseFromServer);
        }
    }


    public void AddAttendanceTable(Employee employee) throws CustomException {
        if (employee.getAttendanceTable() == null) {
            AttendanceTable attendanceTableForNewEmployee = new AttendanceTable(employee);

            attendanceTableForNewEmployee.setInitialWorkingYears(YearAndTimeGenerator.getTestingYear() - employee.getGraduationDate().getYear());
            AttendanceTable attendanceTableForNewEmployeeFromServer = attendanceRepository.save(attendanceTableForNewEmployee);
            employee.setAttendanceTable(attendanceTableForNewEmployeeFromServer);
        }
    }

    public Employee saveEmployee(Employee employeeToAdd) throws CustomException {
        try {
            employeeToAdd.setNetSalary(calculateNetSalary(employeeToAdd.getGrossSalary(), employeeToAdd.getAttendanceTable())); // This function calculates employee new salary and return it
            Employee savedEmployee = employeeRepository.save(employeeToAdd);
            AddCredentials(savedEmployee);
            AddAttendanceTable(savedEmployee);
            return employeeRepository.save(savedEmployee);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public boolean deleteEmployee(Long employeeId) throws CustomException {
        Employee employee = this.getEmployee(employeeId);
        if (employee != null) {
            if (employee.shiftSubordinates()) {
                employeeRepository.deleteById(employeeId); // if shifting shiftSubordinates process is complete
                return true;
            }
        }
        throw new CustomException(">>This employee id does not exist!!!");
    }


    public List<Employee> getEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployee(Long employeeId) // send path parameter
    {
        if (employeeId != null)
            return employeeRepository.findById(employeeId).orElse(null);
        return null;
    }

    public Float calculateNetSalary(Float employeeSalary, AttendanceTable employeeAttendanceTable) {
        if (employeeSalary != null && employeeSalary != 0) {
            float empSalary = employeeSalary * (1 - SalariesYearsConstants.TAXES) - SalariesYearsConstants.DEDUCTED_INSURANCE;
//            float salaryPerDay = empSalary / employeeAttendanceTable.getCurrentMonthDays();
            // if absence in this month is zero then there wont be any deduction
//            empSalary -= salaryPerDay * employeeAttendanceTable.getAbsenceDaysInCurrentMonth();

            if (empSalary > 0)
                return empSalary;
        }
        return 0.0f;
    }


    public boolean checkManagerChange(Employee employeeToModify, Employee goToManager) {
        if (employeeToModify.getId().equals(goToManager.getId()))
            return false;
        List<Employee> employeesUnderCurrentEmployee = employeeRepository.findManagerEmployeesRecursivelyQueried(employeeToModify.getId());

        return employeesUnderCurrentEmployee.stream().noneMatch(o -> o.getId().equals(goToManager.getId())); // if it contains this manager then he cant be my manager
    }

    public Employee modifyEmployee(long employeeId, EmployeeModifyCommand employeeDto) throws NotFoundException, CustomException {
        Employee employeeToModify = this.getEmployee(employeeId);
        if (employeeDto.getManager() != null) // if employee manager is modified check problem could occur
        {
            if (!checkManagerChange(employeeToModify, employeeDto.getManager())) // if the manager is working underMe he cant be my manager
            {
                throw new CustomException("Infinite recursive relation between employee and manager");
            }
        }
        employeeDto.dtoToEmployee(employeeDto, employeeToModify); //  copying new data to employee
        return saveEmployee(employeeToModify);
    }


    public List<EmployeeInfoOnlyDTO> getManagerEmployees(long managerId) throws CustomException {
        Employee manager = this.getEmployee(managerId);
        if (manager == null)
            return null;
        List<Employee> employeesUnderManager = employeeRepository.findByManager(manager);
        return EmployeeInfoOnlyDTO.setEmployeeToDTOList(employeesUnderManager);
    }


    public List<EmployeeInfoOnlyDTO> getManagerEmployeesRecursively(long managerId) throws CustomException {
        if (this.getEmployee(managerId) == null)
            return null;
        List<Employee> employeesUnderManagersRecursive = employeeRepository.findManagerEmployeesRecursivelyQueried(managerId);

        return EmployeeInfoOnlyDTO.setEmployeeToDTOList(employeesUnderManagersRecursive);
    }
}
