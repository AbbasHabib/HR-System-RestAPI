package com.spring.Employee;

import com.spring.Employee.Attendance.AttendanceRepository;
import com.spring.Employee.Attendance.AttendanceService;
import com.spring.Employee.Attendance.AttendanceTable;
import com.spring.Employee.COMMANDS.EmployeeModificationByLoggedUserCommand;
import com.spring.Employee.DTO.EmployeeInfoDTO;
import com.spring.Employee.COMMANDS.EmployeeModifyCommand;
import com.spring.ExceptionsCustom.CustomException;
import com.spring.Security.UserCredentials;
import com.spring.Security.UserCredentialsRepository;
import com.spring.Security.UserPrincipalDetailsService;
import com.spring.YearAndTimeGenerator;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserPrincipalDetailsService userPrincipalDetailsService;

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
            UserCredentials userCredentialForNewEmployee = new UserCredentials(employee.getUserName()
                    , passwordEncoder.encode(employee.getNationalId())
                    , employee.getRole()
                    , employee);

            UserCredentials userCredentialResponseFromServer = userCredentialsRepository.save(userCredentialForNewEmployee);
            employee.setUserCredentials(userCredentialResponseFromServer);
        }
    }


    public void AddAttendanceTable(Employee employee) throws CustomException {
        if (employee.getAttendanceTable() == null) {
            AttendanceTable attendanceTableForNewEmployee = new AttendanceTable(employee);
            attendanceTableForNewEmployee.setInitialWorkingYears(YearAndTimeGenerator.getTestingYear() - employee.calcGraduationYear());
            AttendanceTable attendanceTableForNewEmployeeFromServer = attendanceRepository.save(attendanceTableForNewEmployee);
            employee.setAttendanceTable(attendanceTableForNewEmployeeFromServer);
        }
    }

    public Employee saveEmployee(Employee employeeToAdd) throws CustomException {
        try {
            employeeToAdd.setNetSalary(calculateNetSalary(employeeToAdd.getGrossSalary())); // This function calculates employee new salary and return it
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

    public Employee getEmployeeByUserFromAuthentication() throws CustomException // send path parameter
    {
        Long employeeId = getEmployeeIdFromAuthentication();
        if (employeeId != null)
            return employeeRepository.findById(employeeId).orElse(null);
        return null;
    }


    public Float calculateNetSalary(Float employeeSalary) {
        if (employeeSalary != null && employeeSalary != 0) {
            float empSalary = employeeSalary * (1 - SalariesYearsConstants.TAXES) - SalariesYearsConstants.DEDUCTED_INSURANCE;
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
        employeeDto.commandToEmployee(employeeToModify); //  copying new data to employee
        return saveEmployee(employeeToModify);
    }


    public EmployeeInfoDTO modifyEmployeeByLoggedUser(EmployeeModificationByLoggedUserCommand employeeModificationCommand) throws NotFoundException, CustomException {
        Long employeeId = getEmployeeIdFromAuthentication();
        Employee employeeToModify = this.getEmployee(employeeId);
        employeeModificationCommand.commandToEmployee(employeeToModify);
        saveEmployee(employeeToModify);
        EmployeeInfoDTO employeeInfoDTO = new EmployeeInfoDTO();
        employeeInfoDTO.setEmployeeToDTO(employeeToModify);
        return employeeInfoDTO;
    }


    public List<EmployeeInfoDTO> getManagerEmployees(long managerId) throws CustomException {
        Employee manager = this.getEmployee(managerId);
        if (manager == null)
            return null;
        List<Employee> employeesUnderManager = employeeRepository.findByManager(manager);
        return EmployeeInfoDTO.setEmployeeToDTOList(employeesUnderManager);
    }


    public List<EmployeeInfoDTO> getManagerEmployeesRecursively(long managerId) throws CustomException {
        if (this.getEmployee(managerId) == null)
            return null;
        List<Employee> employeesUnderManagersRecursive = employeeRepository.findManagerEmployeesRecursivelyQueried(managerId);

        return EmployeeInfoDTO.setEmployeeToDTOList(employeesUnderManagersRecursive);
    }

    public Long getEmployeeIdFromAuthentication() throws CustomException {
        String userName = userPrincipalDetailsService.getLoggedUserName();
        UserCredentials userCredentials = userCredentialsRepository.findById(userName).orElse(null);
        if(userCredentials == null)
            throw new CustomException("this userName doesn't exist");
        return userCredentials.getEmployee().getId();
    }
}
