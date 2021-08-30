package com.spring.employee;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.spring.Department.Department;
import com.spring.Department.DepartmentService;
import com.spring.Employee.DTO.EmployeeInfoOnlyDTO;
import com.spring.Employee.DTO.EmployeeSalaryDTO;
import com.spring.Employee.Employee;
import com.spring.Employee.DTO.EmployeeModifyCommand;
import com.spring.Employee.EmployeeService;
import com.spring.Employee.Gender;
import com.spring.ExceptionsCustom.CustomException;
import com.spring.Team.Team;
import com.spring.Team.TeamService;
import javassist.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class
})
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
    @DatabaseSetup("/data.xml")
    public void add_employee() throws Exception, CustomException
    {
        Employee employeeToAdd = new Employee();
        employeeToAdd.setName("saad");
        employeeToAdd.setGender(Gender.MALE);
        employeeToAdd.setGrossSalary(10025f);
        employeeToAdd.setId(1L);

        // set Department to employee
        Long departmentId = 101L;
        Department dep = departmentService.getDepartment(departmentId);
        if(dep == null)
            throw new NotFoundException("department is not found");
        employeeToAdd.setDepartment(dep);

        // this test is expected to: return same object it receives and add employee to database

        // set Team
        Long teamId = 101L;
        Team team = teamService.getTeam(teamId);
        if(team == null)
            throw new NotFoundException("team is not found");
        employeeToAdd.setTeam(team);

        Long managerId = 101L;
        Employee manager = employeeService.getEmployee(managerId);
        if(manager == null)
            throw new NotFoundException("manager not found");
        employeeToAdd.setManager(manager);

        // Is expected to return same object it receives
        ObjectMapper objectMapper = new ObjectMapper();
        String employeeJson = objectMapper.writeValueAsString(employeeToAdd); // converts employee object to JSON string

        // POST request (.post("/employee/")) takes to be a Json of employee in request Body
        mockMvc.perform(MockMvcRequestBuilders.post("/employee/")
                .contentType(MediaType.APPLICATION_JSON).content(employeeJson))
                .andExpect(status().isOk())
                .andReturn(); // request is 200 (OK)

        employeeToAdd.setNetSalary(employeeService.calculateNestSalary(employeeToAdd.getGrossSalary()));
        employeeJson = objectMapper.writeValueAsString(employeeToAdd); // converts employee object to JSON string

        Employee employeeFromDB = employeeService.getEmployee(employeeToAdd.getId());

        String employeeFromDBJSON = objectMapper.writeValueAsString(employeeFromDB);

        assertEquals(employeeJson, employeeFromDBJSON); // this makes sure that the JSON request = stored data in DB
    }

    @Test
    @DatabaseSetup("/data.xml")
    public void get_employee_with_id() throws Exception, CustomException
    {
        Long searchForId = 101L;

        Employee employee = employeeService.getEmployee(searchForId);
        ObjectMapper objectMapper = new ObjectMapper();
        String employeeJSON = objectMapper.writeValueAsString(employee);

        mockMvc.perform(MockMvcRequestBuilders.get("/employee/" + searchForId))
                .andExpect(content().json(employeeJSON))
                .andExpect(status().isOk());
    }

    @Test
    @DatabaseSetup("/data.xml")
    public void delete_employee_with_id() throws Exception
    {
        long deleteUserWithID = 102L;


        mockMvc.perform(MockMvcRequestBuilders.delete("/employee/" + deleteUserWithID))
                .andExpect(content().string("true"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @Transactional
    @DatabaseSetup("/data.xml")
    public void modify_employee() throws Exception, CustomException
    {
        // Initial values of the employee
        Long employeeId = 103L; // employee id to modify
        Employee employeeToModify = employeeService.getEmployee(employeeId);

        // Expected modification
        EmployeeModifyCommand employeeModificationDto = new EmployeeModifyCommand();

        //(1) Edit basic employee info
        employeeModificationDto.setName("reem");
        employeeModificationDto.setGender(Gender.FEMALE);
        employeeModificationDto.setGrossSalary(7000.0f);

        //(2) set Department
        Long departmentId = 102L;
        Department dep = departmentService.getDepartment(departmentId);
        if(dep == null)
            throw new NotFoundException("department is not found");
        employeeModificationDto.setDepartment(dep);

        //(3) set Team
        Long teamId = 102L;
        Team team = teamService.getTeam(teamId);
        if(team == null)
            throw new NotFoundException("team is not found");
        employeeModificationDto.setTeam(team);

        //(4) Edit employee manager
        Long managerId = 102L;
        Employee manager = employeeService.getEmployee(managerId);
        if (manager == null)
            throw new NotFoundException("manager not found");
        employeeModificationDto.setManager(manager);

        employeeModificationDto.dtoToEmployee(employeeModificationDto, employeeToModify); // copy modified data to employee

        ObjectMapper objectMapper = new ObjectMapper();
        String employeeDtoJson = objectMapper.writeValueAsString(employeeModificationDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/employee/" + employeeId)
                .contentType(MediaType.APPLICATION_JSON).content(employeeDtoJson))
                .andExpect(status().isOk());
    }

    @Test
    @DatabaseSetup("/data.xml")
    public void get_employee_salary() throws Exception, CustomException
    {
        Employee employeeRequired = employeeService.getEmployee(101L);

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
    @DatabaseSetup("/data.xml")
    public void get_employees_under_manager() throws Exception, CustomException
    {
        Employee manager = employeeService.getEmployee(101L);

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
    @DatabaseSetup("/data.xml")
    public void getEmployeesRecursively() throws Exception, CustomException
    {
        long managerId = 101L;
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



