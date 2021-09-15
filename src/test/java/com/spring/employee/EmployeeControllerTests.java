package com.spring.employee;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.spring.Department.Department;
import com.spring.Department.DepartmentService;
import com.spring.Employee.DTO.EmployeeInfoOnlyDTO;
import com.spring.Employee.DTO.EmployeeModifyCommand;
import com.spring.Employee.Employee;
import com.spring.Employee.EmployeeRepository;
import com.spring.Employee.EmployeeService;
import com.spring.Employee.Gender;
import com.spring.ExceptionsCustom.CustomException;
import com.spring.Security.UserCredentialsRepository;
import com.spring.Team.Team;
import com.spring.Team.TeamService;
import com.spring.YearAndTimeGenerator;
import com.spring.testShortcuts.TestShortcutMethods;
import javassist.NotFoundException;
import org.junit.jupiter.api.Assertions;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.spring.Employee.DTO.EmployeeInfoOnlyDTO.setEmployeeToDTOList;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class})
public class EmployeeControllerTests {

    @Autowired
    EmployeeService employeeService;
    @Autowired
    DepartmentService departmentService;
    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    TeamService teamService;

    @Autowired
    MockMvc mockMvc;

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


        Float expectedNetSalary = employeeService.calculateNetSalary(employeeToAdd.getGrossSalary());


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse("2012-01-02");
        employeeToAdd.setGraduationDate(date);

        employeeToAdd.setNationalId("1234");
        // set Department to employee
        Long departmentId = 101L;
        Department dep = departmentService.getDepartment(departmentId);
        if (dep == null) throw new NotFoundException("department is not found");
        employeeToAdd.setDepartment(dep);

        // this test is expected to: return same object it receives and add employee to database

        // set Team
        Long teamId = 101L;
        Team team = teamService.getTeam(teamId);
        if (team == null) throw new NotFoundException("team is not found");
        employeeToAdd.setTeam(team);

        // Is expected to return same object it receives
        ObjectMapper objectMapper = new ObjectMapper();
        String employeeJson = objectMapper.writeValueAsString(employeeToAdd); // converts employee object to JSON string

        // POST request (.post("/employee/")) takes to be a Json of employee in request Body
        MvcResult response =  mockMvc.perform(MockMvcRequestBuilders.post("/employee/")
                .with(httpBasic("abbas_habib_1", "123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeJson))
                .andExpect(status().isOk())
                .andReturn();

        // check if employee got inserted in database and got credentials and attendance table

        TestShortcutMethods<Employee> tester = new TestShortcutMethods<>();
        tester.setObjectIdFromResponseResult(response, employeeToAdd); // just to get the id from the response as its auto incremented by database

        Employee employeeFromDb = employeeService.getEmployee(employeeToAdd.getId());

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

        Employee employee = employeeService.getEmployee(searchForId);


        ObjectMapper objectMapper = new ObjectMapper();
        String employeeJSON = objectMapper.writeValueAsString(employee);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/employee/" + searchForId)
                .with(httpBasic("abbas_habib_10", "123")))
                .andExpect(status().isOk())
                .andReturn();

        // as departmentExpected id is currently null
        // we add the id coming from the response to it
        // then compare the expected object with the the object in DB
        TestShortcutMethods<Employee> tester = new TestShortcutMethods<Employee>();
        tester.setObjectIdFromResponseResult(result, employee);
        tester.compareIdOwnerWithDataBase(employee, employeeRepository);

    }

    @Test
    @DatabaseSetup("/data.xml")
//    @ExpectedDatabase(value ="/expectedDatabases/employee_deletion.xml", table = "employee")
    public void delete_employee_with_id_by_hr() throws Exception {
        long deleteUserWithID = 102L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/employee/" + deleteUserWithID)
                .with(httpBasic("abbas_habib_10", "123")))
                .andExpect(content().string("true"))
                .andExpect(status().isOk()).andReturn();

        assertNull(employeeService.getEmployee(deleteUserWithID));
        assertEquals(employeeService.getEmployee(103L).getManager().getId(), 101L);
        assertEquals(employeeService.getEmployee(104L).getManager().getId(), 101L);
    }

    @Test
    @Transactional
    @DatabaseSetup("/data.xml")
    public void modify_employee_by_hr() throws Exception, CustomException {
        // Initial values of the employee
        Long employeeId = 103L; // employee id to modify
        Employee employeeToModify = employeeService.getEmployee(employeeId);

        // Expected modification
        EmployeeModifyCommand employeeModificationDto = new EmployeeModifyCommand();

        // (1) Edit basic employee info
        employeeModificationDto.setFirstName("reem");
        employeeModificationDto.setLastName("naser");
        employeeModificationDto.setGender(Gender.FEMALE);
        employeeModificationDto.setGrossSalary(7000.0f);
        // (2) set Department
        Long departmentId = 102L;
        Department dep = departmentService.getDepartment(departmentId);
        if (dep == null) throw new NotFoundException("department is not found");
        employeeModificationDto.setDepartment(dep);

        // (3) set Team
        Long teamId = 102L;
        Team team = teamService.getTeam(teamId);
        if (team == null) throw new NotFoundException("team is not found");
        employeeModificationDto.setTeam(team);

        // (4) Edit employee manager
        Long managerId = 102L;
        Employee manager = employeeService.getEmployee(managerId);
        if (manager == null) throw new NotFoundException("manager not found");
        employeeModificationDto.setManager(manager);

        employeeModificationDto.dtoToEmployee(employeeModificationDto, employeeToModify); // copy modified data to employee

        ObjectMapper objectMapper = new ObjectMapper();
        String employeeDtoJson = objectMapper.writeValueAsString(employeeModificationDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/employee/" + employeeId)
                .with(httpBasic("abbas_habib_10", "123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeDtoJson))
                .andExpect(status().isOk());
    }


    @Test
    @DatabaseSetup("/data.xml")
    public void get_employees_under_manager_by_hr() throws Exception, CustomException {
        Long managerId = 101L;
        List<Employee> employeesUnderManager = employeeRepository.findEmployeesByManager_Id(managerId);
        List<EmployeeInfoOnlyDTO> EmployeeInfoOnlyDTO = setEmployeeToDTOList(employeesUnderManager);

        ObjectMapper objectMapper = new ObjectMapper();
        String employeeDtoJson = objectMapper.writeValueAsString(EmployeeInfoOnlyDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/employee/manager/" + managerId)
                .with(httpBasic("abbas_habib_10", "123")))
                .andExpect(content().json(employeeDtoJson))
                .andExpect(status().isOk());
    }


    @Test
    @DatabaseSetup("/data.xml")
    public void get_employees_recursively_by_hr() throws Exception, CustomException {
        long managerId = 101L;
        List<EmployeeInfoOnlyDTO> employeesUnderManager = employeeService.getManagerEmployeesRecursively(managerId);
        if (employeesUnderManager == null) throw new NotFoundException("cant find manager");

        ObjectMapper objectMapper = new ObjectMapper();
        String employeesUnderManagerJSON = objectMapper.writeValueAsString(employeesUnderManager);

        mockMvc.perform(MockMvcRequestBuilders.get("/employee/manager/recursive/" + managerId)
                .with(httpBasic("abbas_habib_10", "123")))
                .andExpect(content().json(employeesUnderManagerJSON))
                .andExpect(status().isOk());

    }


}



