package com.spring.Employee;

import com.spring.Employee.DTO.EmployeeInfoOnlyDTO;
import com.spring.Employee.DTO.EmployeeModifyDTO;
import com.spring.Employee.DTO.EmployeeSalaryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService
{
    @Autowired
    private EmployeeRepository employeeRepository;

    public Employee addEmployee(Employee employee) throws Exception
    {
        if (this.getEmployee(employee.getId()) == null)
        {
            return saveEmployee(employee);
        }
        throw new Exception("user ID already exists");
    }

    public Employee saveEmployee(Employee e)
    {
        calculateNestSalary(e); // This function calculates employee new salary and save it in employee
        return employeeRepository.save(e);
    }

    public Boolean deleteEmployee(Long employeeId)
    {
        Employee employee = this.getEmployee(employeeId);
        if (employee != null)
        {
            if (employee.shiftSubordinates())
            {
                employeeRepository.deleteById(employeeId); // if shifting shiftSubordinates process is complete then return true
                return true;
            }
        }
        return false; // means that employee is null or its a super manager {has no manager}
    }


    public List<Employee> getEmployees()
    {
        return employeeRepository.findAll();
    }

    public Employee getEmployee(Long employeeId) // send path parameter
    {
        if(employeeId == null)
            return null;
        return employeeRepository.findById(employeeId).isPresent()
                ? employeeRepository.findById(employeeId).get()
                : null;
    }

    public void calculateNestSalary(Employee employee)
    {
        if (employee.getGrossSalary() != null
                && employee.getGrossSalary() != 0
                && employee.getGrossSalary() * 0.85 - 500 > 500)
            employee.setNetSalary(employee.getGrossSalary() * 0.85f - 500);
    }

    public Employee modifyEmployee(long employeeId, EmployeeModifyDTO employeeDto)
    {
        Employee employeeToModify = this.getEmployee(employeeId);
        EmployeeModifyDTO.dtoToEmployee(employeeDto, employeeToModify);
        return saveEmployee(employeeToModify);
    }

    public EmployeeSalaryDTO employeeSalary(long employeeId)
    {
        Employee employeeRequired = this.getEmployee(employeeId);
        return new EmployeeSalaryDTO(employeeRequired);
    }

    public List<Employee> getEmployeesByName(String name)
    {
        return employeeRepository.findByName(name);
    }


    public List<EmployeeInfoOnlyDTO> getManagerEmployees(long managerId)
    {
        Employee manager = this.getEmployee(managerId);
        if (manager == null)
            return null;
        List<Employee> employeesUnderManager = employeeRepository.findByManager(manager);
        return EmployeeInfoOnlyDTO.setEmployeeToDTOList(employeesUnderManager);
    }

    public List<EmployeeInfoOnlyDTO> getManagerEmployeesRecursively(long managerId)
    {
        if (this.getEmployee(managerId) == null)
            return null;
        List<Employee> employeesUnderManagersRecursive = employeeRepository.findManagerEmployeesRecursivelyQueried(managerId);

        return EmployeeInfoOnlyDTO.setEmployeeToDTOList(employeesUnderManagersRecursive);
    }
}
