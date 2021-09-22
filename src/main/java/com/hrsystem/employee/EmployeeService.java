package com.hrsystem.employee;

import com.hrsystem.employee.commands.AddEmployeeCommand;
import com.hrsystem.employee.commands.EmployeeModificationByLoggedUserCommand;
import com.hrsystem.employee.commands.EmployeeModifyCommand;
import com.hrsystem.employee.commands.EmployeeSalaryModifyCommand;
import com.hrsystem.employee.dtos.EmployeeBasicInfoDTO;
import com.hrsystem.employee.dtos.EmployeeInfoDTO;
import com.hrsystem.attendancelogs.AttendanceRepository;
import com.hrsystem.attendancelogs.AttendanceTable;
import com.hrsystem.utilities.CustomException;
import com.hrsystem.utilities.RegexChecker;
import com.hrsystem.security.UserCredentials;
import com.hrsystem.security.UserCredentialsRepository;
import com.hrsystem.security.UserPrincipalDetailsService;
import com.hrsystem.utilities.interfaces.constants.SalariesYearsConstants;
import com.hrsystem.utilities.TimeGenerator;
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

    public EmployeeInfoDTO addEmployee(AddEmployeeCommand addEmployeeCommand) throws Exception {
        Employee employeeToAdd = new Employee();
        addEmployeeCommand.commandToEmployee(employeeToAdd);

        this.handleEmployeeInsertionExceptions(employeeToAdd);

        if (employeeRepository.findEmployeeByNationalId(employeeToAdd.getNationalId()).isPresent())
            throw new CustomException("nationalId already exists!");

        if (employeeToAdd.getId() == null)
            employeeToAdd = saveEmployee(employeeToAdd);
        else if (this.getEmployee(employeeToAdd.getId()) == null)
            employeeToAdd = saveEmployee(employeeToAdd);
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

    public void handleEmployeeFieldsNamesWithRegex(Employee employee) throws CustomException {
        if (!RegexChecker.isStringOnlyAlphabet(employee.getFirstName()))
            throw new CustomException("firstName has to be alphabets only!");
        if (!RegexChecker.isStringOnlyAlphabet(employee.getLastName()))
            throw new CustomException("lastName has to be alphabets only!");
        if (!RegexChecker.isNumbersOnly(employee.getNationalId()))
            throw new CustomException("nationalId has to be numbers only!");
    }


    public void AddCredentials(Employee employee) {
        if (employee.getUserCredentials() == null) {
            UserCredentials userCredentialForNewEmployee = new UserCredentials(employee.createUserName()
                    , passwordEncoder.encode(employee.getNationalId())
                    , employee.getRole()
                    , employee);

            UserCredentials userCredentialResponseFromServer = userCredentialsRepository.save(userCredentialForNewEmployee);
            employee.setUserCredentials(userCredentialResponseFromServer);
        }
    }


    public void AddAttendanceTable(Employee employee) {
        if (employee.getAttendanceTable() == null) {
            AttendanceTable attendanceTableForNewEmployee = new AttendanceTable(employee);
            attendanceTableForNewEmployee.setInitialWorkingYears((new TimeGenerator()).getCurrentYear() - employee.calcGraduationYear());
            AttendanceTable attendanceTableForNewEmployeeFromServer = attendanceRepository.save(attendanceTableForNewEmployee);
            employee.setAttendanceTable(attendanceTableForNewEmployeeFromServer);
        }
    }

    public Employee saveEmployee(Employee employeeToAdd) throws CustomException {

        this.handleEmployeeFieldsNamesWithRegex(employeeToAdd);
        employeeToAdd.setNetSalary(calculateNetSalary(employeeToAdd.getGrossSalary(), employeeToAdd.getSalaryRaise())); // This function calculates employee new salary and return it

        try {
            Employee savedEmployee = employeeRepository.save(employeeToAdd);
            AddCredentials(savedEmployee);
            AddAttendanceTable(savedEmployee);
            return employeeRepository.save(savedEmployee);
        } catch (Exception e) {
            e.printStackTrace();
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


    public Float calculateNetSalary(Float employeeSalary, Float salaryRaise) {
        if (salaryRaise == null)
            salaryRaise = 0f;
        if (employeeSalary != null && employeeSalary != 0) {
            float empSalary = (employeeSalary + salaryRaise) * (1 - SalariesYearsConstants.TAXES) - SalariesYearsConstants.DEDUCTED_INSURANCE;
            if (empSalary > 0)
                return empSalary;
        }
        return 0.0f;
    }


    public boolean checkManagerChange(Employee employeeToModify, Employee goToManager) throws CustomException {
        if (employeeToModify.getId().equals(goToManager.getId()))
            throw new CustomException("manager cannot be a manager of himself!");

        List<Employee> employeesUnderCurrentEmployee = employeeRepository.findManagerEmployeesRecursivelyQueried(employeeToModify.getId());

        return employeesUnderCurrentEmployee.stream().noneMatch(o -> o.getId().equals(goToManager.getId())); // if it contains this manager then he cant be my manager
    }

    public EmployeeInfoDTO modifyEmployee(long employeeId, EmployeeModifyCommand employeeModifyCommand) throws CustomException, IllegalAccessException {
        Employee employeeToModify = this.getEmployee(employeeId);

        if (employeeModifyCommand.getManager() != null) // if employee manager is modified check problem could occur
        {
            if (!checkManagerChange(employeeToModify, employeeModifyCommand.getManager())) // if the manager is working underMe he cant be my manager
            {
                throw new CustomException("(a sub employee cannot be a manager of his manager) infinite recursive relation between employee and manager!");
            }
        }
        employeeModifyCommand.commandToEmployee(employeeToModify); //  copying new data to employee
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

    public EmployeeInfoDTO modifyEmployeeSalary(long employeeId, EmployeeSalaryModifyCommand employeeSalaryModifyCommand) throws CustomException {
        Employee employeeToModify = this.getEmployee(employeeId);
        employeeSalaryModifyCommand.commandToEmployee(employeeToModify); //  copying new data to employee

        Employee employeeModified = null;
        employeeModified = saveEmployee(employeeToModify);
        if (employeeModified != null) {
            EmployeeInfoDTO employeeInfoDTO = new EmployeeInfoDTO();
            employeeInfoDTO.setEmployeeToDTO(employeeModified);
            return employeeInfoDTO;
        }
        throw new CustomException("could not modify employee!");

    }


    public EmployeeInfoDTO modifyEmployeeByLoggedUser(EmployeeModificationByLoggedUserCommand employeeModificationCommand) throws CustomException, IllegalAccessException {
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
            throw new CustomException("this employee id does not exist");
        List<Employee> employeesUnderManager = employeeRepository.findByManager_Id(managerId);
        EmployeeBasicInfoDTO employeeBasicInfoDTO = new EmployeeBasicInfoDTO();
        return employeeBasicInfoDTO.generateDTOListFromEmployeeList(employeesUnderManager);
    }

    public List<EmployeeBasicInfoDTO> getManagerEmployeesRecursively(long managerId) {
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
        if (userCredentials == null)
            throw new CustomException("this userName doesn't exist");
        return userCredentials.getEmployee();
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

}
