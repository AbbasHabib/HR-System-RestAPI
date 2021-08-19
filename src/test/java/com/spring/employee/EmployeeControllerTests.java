package com.spring.employee;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.spring.Department.Department;
import com.spring.Department.DepartmentService;
import com.spring.Employee.DTO.EmployeeInfoOnlyDTO;
import com.spring.Employee.DTO.EmployeeSalaryDTO;
import com.spring.Employee.Employee;
import com.spring.Employee.DTO.EmployeeModifyCommandDTO;
import com.spring.Employee.EmployeeService;
import com.spring.Team.Team;
import com.spring.Team.TeamService;
import javassist.NotFoundException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class EmployeeControllerTests
{

    @Autowired
    EmployeeService employeeService;
    @Autowired
    DepartmentService departmentService;

    @Autowired
    TeamService teamService;

    @Autowired
    MockMvc mockMvc;


    private CriteriaBuilderFactory criteriaBuilderFactory;


    @Test
    public void add_employee() throws Exception
    {
        Employee employeeToAdd = new Employee();
        employeeToAdd.setName("saad");
        employeeToAdd.setGender((char) 'M');
        employeeToAdd.setGrossSalary(10025f);
        employeeToAdd.setId(22L);

        // set Department to employee
        Long departmentId = 1L;
        Department dep = departmentService.getDepartment(departmentId);
        if(dep == null)
            throw new NotFoundException("department is not found");
        employeeToAdd.setDepartment(dep);

        // this test is expected to: return same object it receives and add employee to database

        // set Team
        Long teamId = 1L;
        Team team = teamService.getTeam(teamId);
        if(team == null)
            throw new NotFoundException("team is not found");
        employeeToAdd.setTeam(team);

        Long managerId = 2L;
        Employee manager = employeeService.getEmployee(managerId);
        if(manager == null)
            throw new NotFoundException("manager not found");

        employeeToAdd.setManager(manager);

        // EmployeeService is tested and (employeeService.addEmployee)
        // Is expected to return same object it receives
        ObjectMapper objectMapper = new ObjectMapper();
        String employeeJson = objectMapper.writeValueAsString(employeeToAdd); // converts employee object to JSON string

        // POST request (.post("/employee/")) takes to be a Json of employee in request Body
        mockMvc.perform(MockMvcRequestBuilders.post("/employee/")
                .contentType(MediaType.APPLICATION_JSON).content(employeeJson))
                .andExpect(content().json(employeeJson))
                .andExpect(status().isOk());
    }

    @Test
    public void get_employee_with_id() throws Exception
    {
        Long searchForId = 1L;

        Employee employee = employeeService.getEmployee(searchForId);
        ObjectMapper objectMapper = new ObjectMapper();
        String employeeJSON = objectMapper.writeValueAsString(employee);

        mockMvc.perform(MockMvcRequestBuilders.get("/employee/" + searchForId))
                .andExpect(content().json(employeeJSON))
                .andExpect(status().isOk());
    }

    @Test
    public void delete_employee_with_id() throws Exception
    {
        long deleteUserWithID = 2L;


        mockMvc.perform(MockMvcRequestBuilders.delete("/employee/" + deleteUserWithID))
                .andExpect(content().string("true"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void modify_employee() throws Exception
    {
        // Initial values of the employee
        Long employeeId = 6L; // employee id to modify
        Employee employeeToModify = employeeService.getEmployee(employeeId);

        // Expected modification
        EmployeeModifyCommandDTO employeeModificationDto = new EmployeeModifyCommandDTO();

        //(1) Edit basic employee info
        employeeModificationDto.setName("reem");
        employeeModificationDto.setGender('F');
        employeeModificationDto.setGrossSalary(7000.0f);

        //(2) set Department
        Long departmentId = 1L;
        Department dep = departmentService.getDepartment(departmentId);
        if(dep == null)
            throw new NotFoundException("department is not found");
        employeeModificationDto.setDepartment(dep);

        //(3) set Team
        Long teamId = 1L;
        Team team = teamService.getTeam(teamId);
        if(team == null)
            throw new NotFoundException("team is not found");
        employeeModificationDto.setTeam(team);

        //(4) Edit employee manager
        Long managerId = 3L;
        Employee manager = employeeService.getEmployee(managerId);
        if (manager == null)
            throw new NotFoundException("manager not found");
        employeeModificationDto.setManager(manager);

        EmployeeModifyCommandDTO.dtoToEmployee(employeeModificationDto, employeeToModify); // copy modified data to employee

        ObjectMapper objectMapper = new ObjectMapper();
        String employeeDtoJson = objectMapper.writeValueAsString(employeeModificationDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/employee/" + employeeId)
                .contentType(MediaType.APPLICATION_JSON).content(employeeDtoJson))
                .andExpect(status().isOk());
    }

    @Test
    public void get_employee_salary() throws Exception
    {
        Employee employeeRequired = employeeService.getEmployee(1L);

        if (employeeRequired == null)
            throw new NotFoundException("cant find employee");
        String EmployeeId = employeeRequired.getId().toString();

        EmployeeSalaryDTO employeeSalaryDTO = new EmployeeSalaryDTO(employeeRequired);

        ObjectMapper objectMapper = new ObjectMapper();
        String employeeSalaryDTOJson = objectMapper.writeValueAsString(employeeSalaryDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/employee/salary/" + EmployeeId))
                .andExpect(content().json(employeeSalaryDTOJson))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void get_employees_under_manager() throws Exception
    {
        Employee manager = employeeService.getEmployee(1L);

        if (manager == null)
            throw new NotFoundException("cant find manager");

        List<Employee> employeesUnderManager = new ArrayList<>(manager.getSubEmployees());
        List<EmployeeInfoOnlyDTO> employeesDTO = EmployeeInfoOnlyDTO.setEmployeeToDTOList(employeesUnderManager);

        ObjectMapper objectMapper = new ObjectMapper();
        String employeesUnderManagerJson = objectMapper.writeValueAsString(employeesDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/employee/manager/" + manager.getId()))
                .andExpect(content().json(employeesUnderManagerJson))
                .andExpect(status().isOk());
    }


    @Test
    @Transactional
    public void getEmployeesRecursively() throws Exception
    {
        long managerId = 1L;
        List<EmployeeInfoOnlyDTO> employeesUnderManager = employeeService.getManagerEmployeesRecursively(managerId);
        if (employeesUnderManager == null)
            throw new NotFoundException("cant find manager");

        ObjectMapper objectMapper = new ObjectMapper();
        String employeesUnderManagerJSON = objectMapper.writeValueAsString(employeesUnderManager);

        mockMvc.perform(MockMvcRequestBuilders.get("/employee/manager/recursive/" + managerId))
                .andExpect(content().json(employeesUnderManagerJSON))
                .andExpect(status().isOk());

    }

}



