package com.spring.Employee;

import com.spring.Employee.COMMANDS.EmployeeModificationByLoggedUserCommand;
import com.spring.Employee.COMMANDS.EmployeeModifyCommand;
import com.spring.Employee.DTO.EmployeeBasicInfoDTO;
import com.spring.Employee.DTO.EmployeeInfoDTO;
import com.spring.Employee.EmployeeLog.AttendanceRepository;
import com.spring.Employee.EmployeeLog.AttendanceService;
import com.spring.Employee.EmployeeLog.AttendanceTable;
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
    private AttendanceRepository attendanceRepository;
    @Autowired
    private UserCredentialsRepository userCredentialsRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserPrincipalDetailsService userPrincipalDetailsService;

    public EmployeeInfoDTO addEmployee(Employee employee) throws Exception, CustomException {
        Employee employeeToAdd = null;
        this.handleEmployeeInsertionExceptions(employee);

        if (employeeRepository.findEmployeeByNationalId(employee.getNationalId()).isPresent())
            throw new CustomException("nationalId already exists!");

        if (employee.getId() == null)
            employeeToAdd = saveEmployee(employee);
        else if (this.getEmployee(employee.getId()) == null)
            employeeToAdd = saveEmployee(employee);
        if (employeeToAdd != null) {
            EmployeeInfoDTO employeeInfoDTO = new EmployeeInfoDTO();
            employeeInfoDTO.setEmployeeToDTO(employeeToAdd);
            return employeeInfoDTO;
        }
        throw new CustomException("employeeId already exists!");
    }

    public void handleEmployeeInsertionExceptions(Employee employee) throws CustomException, IllegalAccessException {
        EmployeeNotNullableFields employeeNotNullableFields = new EmployeeNotNullableFields(employee);
        String nullFieldString = employeeNotNullableFields.checkNull();
        if (!nullFieldString.equals("")) {
            throw new CustomException(nullFieldString + " cannot be null!");
        }

        if (employee.getGrossSalary() < 0) {
            throw new CustomException("grossSalary cannot be less than 0!");
        }

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
                if (employee.getUserCredentials() != null) {
                    userCredentialsRepository.deleteById(employee.getUserCredentials().getUserName());
                }
                employeeRepository.deleteById(employeeId); // if shifting shiftSubordinates process is complete
                return true;
            }
            throw new CustomException(">>This employee has no manager it cant be deleted");
        }
        throw new CustomException(">>This employee id does not exist!!!");
    }


    public List<EmployeeBasicInfoDTO> getEmployeesInDTO() {
        List<Employee> employees = employeeRepository.findAll();
        EmployeeBasicInfoDTO employeeBasicInfoDTO = new EmployeeBasicInfoDTO();
        return employeeBasicInfoDTO.generateDTOListFromEmployeeList(employees);
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

    public EmployeeInfoDTO getEmployeeInDTO(Long employeeId) throws CustomException {
        Employee employeeFound = null;
        if (employeeId != null)
            employeeFound = employeeRepository.findById(employeeId).orElse(null);
        if (employeeFound != null) {
            EmployeeInfoDTO employeeInfoDTO = new EmployeeInfoDTO();
            employeeInfoDTO.setEmployeeToDTO(employeeFound);
            return employeeInfoDTO;
        }
        throw new CustomException("did not find this employee id");
    }

    public EmployeeInfoDTO getEmployeeInDTOByUserFromAuthentication() throws CustomException // send path parameter
    {
        Employee employee = getEmployeeByLoggedUser();
        if (employee != null) {
            EmployeeInfoDTO employeeInfoDTO = new EmployeeInfoDTO();
            employeeInfoDTO.setEmployeeToDTO(employee);
            return employeeInfoDTO;
        }
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

    public EmployeeInfoDTO modifyEmployee(long employeeId, EmployeeModifyCommand employeeDto) throws NotFoundException, CustomException, IllegalAccessException {
        Employee employeeToModify = this.getEmployee(employeeId);

        if (employeeDto.getManager() != null) // if employee manager is modified check problem could occur
        {
            if (!checkManagerChange(employeeToModify, employeeDto.getManager())) // if the manager is working underMe he cant be my manager
            {
                throw new CustomException("Infinite recursive relation between employee and manager");
            }
        }
        employeeDto.commandToEmployee(employeeToModify); //  copying new data to employee
        this.handleEmployeeInsertionExceptions(employeeToModify);


        Employee employeeModified = null;
        employeeModified = saveEmployee(employeeToModify);
        if (employeeModified != null) {
            EmployeeInfoDTO employeeInfoDTO = new EmployeeInfoDTO();
            employeeInfoDTO.setEmployeeToDTO(employeeModified);
            return employeeInfoDTO;
        }
        throw new CustomException("could not modify employee");

    }


    public EmployeeInfoDTO modifyEmployeeByLoggedUser(EmployeeModificationByLoggedUserCommand employeeModificationCommand) throws NotFoundException, CustomException, IllegalAccessException {
        Long employeeId = getEmployeeIdFromAuthentication();
        Employee employeeToModify = this.getEmployee(employeeId);

        employeeModificationCommand.commandToEmployee(employeeToModify);
        this.handleEmployeeInsertionExceptions(employeeToModify);

        saveEmployee(employeeToModify);
        EmployeeInfoDTO employeeInfoDTO = new EmployeeInfoDTO();
        employeeInfoDTO.setEmployeeToDTO(employeeToModify);
        return employeeInfoDTO;
    }


    public List<EmployeeBasicInfoDTO> getManagerEmployees(Long managerId) throws CustomException {
        if (managerId == null)
            throw new CustomException("Id is null");

        List<Employee> employeesUnderManager = employeeRepository.findByManager_Id(managerId);
        EmployeeBasicInfoDTO employeeBasicInfoDTO = new EmployeeBasicInfoDTO();

        return employeeBasicInfoDTO.generateDTOListFromEmployeeList(employeesUnderManager);
    }


    public List<EmployeeBasicInfoDTO> getManagerEmployeesByLoggedUser() throws CustomException {
        Long managerId = getEmployeeIdFromAuthentication();
        if (managerId == null)
            throw new CustomException("this employee Id does not exist");
        List<Employee> employeesUnderManager = employeeRepository.findByManager_Id(managerId);
        EmployeeBasicInfoDTO employeeBasicInfoDTO = new EmployeeBasicInfoDTO();
        return employeeBasicInfoDTO.generateDTOListFromEmployeeList(employeesUnderManager);
    }

    public List<EmployeeBasicInfoDTO> getManagerEmployeesRecursively(long managerId) throws CustomException {
        if (this.getEmployee(managerId) == null)
            return null;
        List<Employee> employeesUnderManagersRecursive = employeeRepository.findManagerEmployeesRecursivelyQueried(managerId);
        EmployeeBasicInfoDTO employeeBasicInfoDTO = new EmployeeBasicInfoDTO();

        return employeeBasicInfoDTO.generateDTOListFromEmployeeList(employeesUnderManagersRecursive);
    }

    public Long getEmployeeIdFromAuthentication() throws CustomException {
        String userName = userPrincipalDetailsService.getLoggedUserName();
        UserCredentials userCredentials = userCredentialsRepository.findById(userName).orElse(null);
        if (userCredentials == null)
            throw new CustomException("this userName doesn't exist");
        return userCredentials.getEmployee().getId();
    }

    public Employee getEmployeeByLoggedUser() throws CustomException // send path parameter
    {
        String userName = userPrincipalDetailsService.getLoggedUserName();
        UserCredentials userCredentials = userCredentialsRepository.findById(userName).orElse(null);
        Employee employee = null;
        if (userCredentials == null)
            throw new CustomException("this userName doesn't exist");
        return userCredentials.getEmployee();
    }

}
