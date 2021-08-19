package com.spring.Employee;

import com.spring.Employee.DTO.EmployeeInfoOnlyDTO;
import com.spring.Employee.DTO.EmployeeModifyCommandDTO;
import com.spring.Employee.DTO.EmployeeSalaryDTO;
import javassist.NotFoundException;
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

    public boolean checkManagerChange(Employee employeeToModify, Employee goToManager)
    {
        if(employeeToModify.getId().equals(goToManager.getId()))
            return false;
        List<Employee> employeesUnderCurrentEmployee = employeeRepository.findManagerEmployeesRecursivelyQueried(employeeToModify.getId());

        return employeesUnderCurrentEmployee.stream().noneMatch(o -> o.getId().equals(goToManager.getId())); // if it contains this manager then he cant be my manager
    }

    public Employee modifyEmployee(long employeeId, EmployeeModifyCommandDTO employeeDto) throws NotFoundException
    {
        Employee employeeToModify = this.getEmployee(employeeId);
        if(employeeDto.getManager() != null) // if employee manager is modified check problem could occur
        {
            if(!checkManagerChange(employeeToModify, employeeDto.getManager())) // if the manager is working underMe he cant be my manager
            {
                throw new NotFoundException("Infinite recursive relation between employee and manager");

//                System.out.println("Infinite recursive relation between employee and manager");
//                return null;

            }
        }
        EmployeeModifyCommandDTO.dtoToEmployee(employeeDto, employeeToModify); //  copying new data to employee
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
