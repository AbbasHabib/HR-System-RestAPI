package com.spring.Employee;

import com.spring.Employee.Attendance.AttendanceTable;
import com.spring.Employee.DTO.EmployeeInfoOnlyDTO;
import com.spring.Employee.DTO.EmployeeModifyCommand;
import com.spring.ExceptionsCustom.CustomException;
import com.spring.Security.UserCredentials;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    public Employee addEmployee(Employee employee) throws Exception, CustomException {
        if (employeeRepository.findEmployeeByNationalId(employee.getNationalId()).isPresent())
            throw new CustomException(">>national id already exists?");


        if (employee.getId() == null)
            return saveEmployee(employee);
        if (this.getEmployee(employee.getId()) == null)
            return saveEmployee(employee);

        throw new CustomException(">>User ID already exists");
    }

    private void AddCredentials(Employee employee) {
        if (employee.getUserCredentials() == null) {
            employee.setUserCredentials(new UserCredentials(employee.getName(), employee.getNationalId(), employee.getRole(), employee));
        }
    }

    private void AddAttendanceTable(Employee employee) {
        if (employee.getAttendanceTable() == null) {
            employee.setAttendanceTable(new AttendanceTable(employee));
        }
    }

    public Employee saveEmployee(Employee employeeToAdd) throws CustomException {
        try {
            employeeToAdd.setNetSalary(calculateNetSalary(employeeToAdd.getGrossSalary(), employeeToAdd.getAttendanceTable())); // This function calculates employee new salary and return it
            AddAttendanceTable(employeeToAdd);
            AddCredentials(employeeToAdd);
            return employeeRepository.save(employeeToAdd);
        } catch (Exception ex) {
            throw new CustomException("saving to database failed ??");
        }
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


    public Employee getEmployeeByName(String name) throws CustomException {
        Employee employeeFound = employeeRepository.findByName(name).orElse(null);
        if (employeeFound == null)
            throw new CustomException("this username doesn't exist");
        return employeeFound;
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
