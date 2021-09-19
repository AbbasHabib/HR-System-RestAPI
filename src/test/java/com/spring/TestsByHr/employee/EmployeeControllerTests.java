package com.spring.TestsByHr.employee;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.spring.Department.Department;
import com.spring.Employee.COMMANDS.EmployeeModifyCommand;
import com.spring.Employee.DTO.EmployeeBasicInfoDTO;
import com.spring.Employee.DTO.EmployeeInfoDTO;
import com.spring.Employee.Employee;
import com.spring.Employee.Gender;
import com.spring.ExceptionsCustom.CustomException;
import com.spring.IntegrationTest;
import com.spring.Security.UserCredentials;
import com.spring.Security.UserCredentialsRepository;
import com.spring.Team.Team;
import com.spring.TestsByHr.testShortcuts.TestShortcutMethods;
import com.spring.YearAndTimeGenerator;
import javassist.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EmployeeControllerTests extends IntegrationTest {


    @Autowired
    UserCredentialsRepository userCredentialsRepository;
    private CriteriaBuilderFactory criteriaBuilderFactory;

    @Test
    @DatabaseSetup("/hr-only.xml")
    public void add_employee_by_hr() throws Exception, CustomException {
        Employee employeeToAdd = new Employee();
        employeeToAdd.setFullName("ahmed safty");
        employeeToAdd.setGender(Gender.MALE);
        employeeToAdd.setGrossSalary(10000f);


        Float expectedNetSalary = getEmployeeService().calculateNetSalary(employeeToAdd.getGrossSalary());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse("2012-01-02");
        employeeToAdd.setGraduationDate(date);

        employeeToAdd.setNationalId("1234");
        // set Department to employee
        Long departmentId = 101L;
        Department dep = getDepartmentService().getDepartment(departmentId);
        if (dep == null) throw new NotFoundException("department is not found");
        employeeToAdd.setDepartment(dep);

        // this test is expected to: return same object it receives and add employee to database

        // set Team
        Long teamId = 101L;
        Team team = getTeamService().getTeam(teamId);
        if (team == null) throw new NotFoundException("team is not found");
        employeeToAdd.setTeam(team);

        // Is expected to return same object it receives
        ObjectMapper objectMapper = new ObjectMapper();
        String employeeJson = objectMapper.writeValueAsString(employeeToAdd); // converts employee object to JSON string

        // POST request (.post("/employee/")) takes to be a Json of employee in request Body
        MvcResult response = getMockMvc().perform(MockMvcRequestBuilders.post("/employee/")
                .with(httpBasic("abbas_habib_1", "123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeJson))
                .andExpect(status().isOk())
                .andReturn();

        // check if employee got inserted in database and got credentials and attendance table

        TestShortcutMethods<Employee> tester = new TestShortcutMethods<>();
        tester.setObjectIdFromResponseResult(response, employeeToAdd); // just to get the id from the response as its auto incremented by database

        Employee employeeFromDb = getEmployeeService().getEmployee(employeeToAdd.getId());

        int expectedInitialWorkingYears = YearAndTimeGenerator.getTestingYear() - employeeToAdd.calcGraduationYear();

        Assertions.assertNotEquals(null, employeeFromDb.getAttendanceTable());
        Assertions.assertNotEquals(null, employeeFromDb.getUserCredentials());

        assertEquals(expectedInitialWorkingYears, employeeFromDb.getAttendanceTable().getInitialWorkingYears()); // from 2012 to 2021(testing year)
        assertEquals(employeeToAdd.getId(), employeeFromDb.getId());
        assertEquals(employeeToAdd.getUserName(), employeeFromDb.getUserName());
        assertEquals(employeeToAdd.getGender(), employeeFromDb.getGender());
        assertEquals(expectedNetSalary, employeeFromDb.getNetSalary());
    }

    @Test
    @DatabaseSetup("/data.xml")
    public void get_employee_with_id_by_hr() throws Exception, CustomException {
        Long searchForId = 101L;

        Employee employee = getEmployeeService().getEmployee(searchForId);


        ObjectMapper objectMapper = new ObjectMapper();
        String employeeJSON = objectMapper.writeValueAsString(employee);

        MvcResult result = getMockMvc().perform(MockMvcRequestBuilders.get("/employee/" + searchForId)
                .with(httpBasic("abbas_habib_10", "123")))
                .andExpect(status().isOk())
                .andReturn();

        // as departmentExpected id is currently null
        // we add the id coming from the response to it
        // then compare the expected object with the the object in DB
        TestShortcutMethods<Employee> tester = new TestShortcutMethods<Employee>();
        tester.setObjectIdFromResponseResult(result, employee);
        tester.compareIdOwnerWithDataBase(employee, getEmployeeRepository());

    }

    @Test
    @DatabaseSetup("/data.xml")
    public void delete_employee_with_id_by_hr() throws Exception {
        long deleteUserWithID = 102L;


        getMockMvc().perform(MockMvcRequestBuilders.delete("/employee/" + deleteUserWithID)
                .with(httpBasic("abbas_habib_10", "123")))
                .andExpect(content().string("true"))
                .andExpect(status().isOk()).andReturn();

        Assertions.assertNull(getEmployeeService().getEmployee(deleteUserWithID));
        assertEquals(getEmployeeService().getEmployee(103L).getManager().getId(), 101L);
        assertEquals(getEmployeeService().getEmployee(104L).getManager().getId(), 101L);
    }


    @Test
    @DatabaseSetup("/EmployeeWithCredentialsDeletionTest.xml")
    public void delete_employee_with_id_with_credentials_deletion_by_hr() throws Exception {
        long deleteUserWithID = 20L;
        String userName = "abbas_habib_20";
        UserCredentials userCredential = userCredentialsRepository.findById(userName).orElse(null);
        Assertions.assertNotEquals(userCredential, null); // making sure that this user credential exists at first

        getMockMvc().perform(MockMvcRequestBuilders.delete("/employee/" + deleteUserWithID)
                .with(httpBasic("abbas_habib_10", "123")))
                .andExpect(content().string("true"))
                .andExpect(status().isOk()).andReturn();


        Assertions.assertNull(getEmployeeService().getEmployee(deleteUserWithID));
        Assertions.assertNull(userCredentialsRepository.findById(userName).orElse(null)); // making sure that this user credential doesn't exist
        
    }
    @Test
    @Transactional
    @DatabaseSetup("/data.xml")
    public void modify_employee_by_hr() throws Exception, CustomException {
        // Initial values of the employee
        Long employeeId = 103L; // employee id to modify
        Employee employeeToModify = getEmployeeService().getEmployee(employeeId);

        // Expected modification
        EmployeeModifyCommand employeeModificationDto = new EmployeeModifyCommand();

        // (1) Edit basic employee info
        employeeModificationDto.setFirstName("reem");
        employeeModificationDto.setLastName("naser");
        employeeModificationDto.setGender(Gender.FEMALE);
        employeeModificationDto.setGrossSalary(7000.0f);
        // (2) set Department
        Long departmentId = 102L;
        Department dep = getDepartmentService().getDepartment(departmentId);
        if (dep == null) throw new NotFoundException("department is not found");
        employeeModificationDto.setDepartment(dep);

        // (3) set Team
        Long teamId = 102L;
        Team team = getTeamService().getTeam(teamId);
        if (team == null) throw new NotFoundException("team is not found");
        employeeModificationDto.setTeam(team);

        // (4) Edit employee manager
        Long managerId = 102L;
        Employee manager = getEmployeeService().getEmployee(managerId);
        if (manager == null) throw new NotFoundException("manager not found");
        employeeModificationDto.setManager(manager);

        employeeModificationDto.commandToEmployee(employeeToModify); // copy modified data to employee

        ObjectMapper objectMapper = new ObjectMapper();
        String employeeDtoJson = objectMapper.writeValueAsString(employeeModificationDto);

        getMockMvc().perform(MockMvcRequestBuilders.put("/employee/" + employeeId)
                .with(httpBasic("abbas_habib_10", "123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeDtoJson))
                .andExpect(status().isOk());
    }


    @Test
    @DatabaseSetup("/data.xml")
    public void get_employees_under_manager_by_hr() throws Exception, CustomException {
        Long managerId = 101L;
        List<Employee> employeesUnderManager = getEmployeeRepository().findEmployeesByManager_Id(managerId);
        EmployeeBasicInfoDTO employeeBasicInfoDTO = new EmployeeBasicInfoDTO();
        List<EmployeeBasicInfoDTO> EmployeeInfoDTO = employeeBasicInfoDTO.generateDTOListFromEmployeeList(employeesUnderManager);

        ObjectMapper objectMapper = new ObjectMapper();
        String employeeDtoJson = objectMapper.writeValueAsString(EmployeeInfoDTO);

        getMockMvc().perform(MockMvcRequestBuilders.get("/employee/manager/" + managerId)
                .with(httpBasic("abbas_habib_10", "123")))
                .andExpect(content().json(employeeDtoJson))
                .andExpect(status().isOk());
    }


    @Test
    @DatabaseSetup("/data.xml")
    public void get_employees_recursively_by_hr() throws Exception, CustomException {
        long managerId = 101L;
        List<EmployeeBasicInfoDTO> employeesUnderManager = getEmployeeService().getManagerEmployeesRecursively(managerId);
        if (employeesUnderManager == null) throw new NotFoundException("cant find manager");

        ObjectMapper objectMapper = new ObjectMapper();
        String employeesUnderManagerJSON = objectMapper.writeValueAsString(employeesUnderManager);

        getMockMvc().perform(MockMvcRequestBuilders.get("/employee/manager/recursive/" + managerId)
                .with(httpBasic("abbas_habib_10", "123")))
                .andExpect(content().json(employeesUnderManagerJSON))
                .andExpect(status().isOk());

    }
}



