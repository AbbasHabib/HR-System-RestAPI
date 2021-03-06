package com.hrsystem.tests_by_hr.employee;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.hrsystem.department.Department;
import com.hrsystem.employee.commands.AddEmployeeCommand;
import com.hrsystem.employee.commands.EmployeeModifyCommand;
import com.hrsystem.employee.commands.EmployeeSalaryModifyCommand;
import com.hrsystem.employee.dtos.EmployeeBasicInfoDTO;
import com.hrsystem.employee.dtos.EmployeeInfoDTO;
import com.hrsystem.employee.Employee;
import com.hrsystem.employee.Gender;
import com.hrsystem.utilities.CustomException;
import com.hrsystem.IntegrationTest;
import com.hrsystem.security.EmployeeRole;
import com.hrsystem.security.UserCredentials;
import com.hrsystem.security.UserCredentialsRepository;
import com.hrsystem.team.Team;
import com.hrsystem.tests_by_hr.testShortcuts.TestShortcutMethods;
import com.hrsystem.utilities.TimeGenerator;
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
import java.util.Objects;

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
    public void add_employee_by_hr() throws Exception {
        Employee addEmployeeCommand = new Employee();
        addEmployeeCommand.setFullName("ahmed safty");
        addEmployeeCommand.setGender(Gender.MALE);
        addEmployeeCommand.setGrossSalary(10000f);
        addEmployeeCommand.setRole(EmployeeRole.EMPLOYEE);

        Float expectedNetSalary = getEmployeeService().calculateNetSalary(addEmployeeCommand.getGrossSalary(), addEmployeeCommand.getSalaryRaise());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse("2012-01-02");
        addEmployeeCommand.setGraduationDate(date);

        addEmployeeCommand.setNationalId("1234");
        // set Department to employee
        Long departmentId = 101L;
        Department dep = getDepartmentService().getDepartment(departmentId);
        if (dep == null) throw new NotFoundException("department is not found");
        addEmployeeCommand.setDepartment(dep);

        // this test is expected to: return same object it receives and add employee to database

        // set Team
        Long teamId = 101L;
        Team team = getTeamService().getTeam(teamId);
        if (team == null) throw new NotFoundException("team is not found");
        addEmployeeCommand.setTeam(team);

        // Is expected to return same object it receives
        ObjectMapper objectMapper = new ObjectMapper();
        String employeeJson = objectMapper.writeValueAsString(addEmployeeCommand); // converts employee object to JSON string

        // POST request (.post("/employee/")) takes to be a Json of employee in request Body
        MvcResult response = getMockMvc().perform(MockMvcRequestBuilders.post("/employee/")
                .with(httpBasic("abbas_habib_1", "123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeJson))
                .andExpect(status().isOk())
                .andReturn();

        // check if employee got inserted in database and got credentials and attendance table

        TestShortcutMethods<Employee> tester = new TestShortcutMethods<>();
        tester.setObjectIdFromResponseResult(response, addEmployeeCommand); // just to get the id from the response as its auto incremented by database

        Employee employeeFromDb = getEmployeeService().getEmployee(addEmployeeCommand.getId());

        int expectedInitialWorkingYears = (new TimeGenerator()).getCurrentYear() - addEmployeeCommand.calcGraduationYear();

        Assertions.assertNotEquals(null, employeeFromDb.getAttendanceTable());
        Assertions.assertNotEquals(null, employeeFromDb.getUserCredentials());

        assertEquals(expectedInitialWorkingYears, employeeFromDb.getAttendanceTable().getInitialWorkingYears()); // from 2012 to 2021(testing year)
        assertEquals(addEmployeeCommand.getId(), employeeFromDb.getId());
        assertEquals(addEmployeeCommand.createUserName(), employeeFromDb.createUserName());
        assertEquals(addEmployeeCommand.getGender(), employeeFromDb.getGender());
        assertEquals(expectedNetSalary, employeeFromDb.getNetSalary());
    }


    @Test
    @DatabaseSetup("/data.xml")
    public void modify_manager_to_be_manager_of_himself() throws Exception {
        // Initial values of the employee
        Long employeeId = 101L; // employee id to modify
        Employee employeeToModify = getEmployeeService().getEmployee(employeeId);

        // Expected modification
        EmployeeModifyCommand employeeModificationDto = new EmployeeModifyCommand();
        employeeModificationDto.setManager(employeeToModify);

        employeeModificationDto.commandToEmployee(employeeToModify); // copy modified data to employee

        ObjectMapper objectMapper = new ObjectMapper();
        String employeeDtoJson = objectMapper.writeValueAsString(employeeModificationDto);

        getMockMvc().perform(MockMvcRequestBuilders.put("/employee/" + employeeId)
                .with(httpBasic("abbas_habib_10", "123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeDtoJson))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof CustomException))
                .andExpect(result -> assertEquals("manager cannot be a manager of himself!", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @DatabaseSetup("/data.xml")
    public void modify_a_manager_to_be_a_sub_employee_of_his_sub_employees() throws Exception {
        // Initial values of the employee
        Long managerId = 101L; // employee id to modify
        Long aSubEmployee = 102L;
        Employee employeeToModify = getEmployeeService().getEmployee(managerId);
        Employee subEmployee = getEmployeeService().getEmployee(aSubEmployee);

        // Expected modification
        EmployeeModifyCommand employeeModificationDto = new EmployeeModifyCommand();
        employeeModificationDto.setManager(subEmployee);

        employeeModificationDto.commandToEmployee(employeeToModify); // copy modified data to employee

        ObjectMapper objectMapper = new ObjectMapper();
        String employeeDtoJson = objectMapper.writeValueAsString(employeeModificationDto);

        getMockMvc().perform(MockMvcRequestBuilders.put("/employee/" + managerId)
                .with(httpBasic("abbas_habib_10", "123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeDtoJson))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof CustomException))
                .andExpect(result -> assertEquals("(a sub employee cannot be a manager of his manager) infinite recursive relation between employee and manager!",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @DatabaseSetup("/data.xml")
    public void get_employee_with_id_by_hr() throws Exception {
        Long searchForId = 101L;


        Team team = getTeamService().getTeam(101L);
        Department department = getDepartmentService().getDepartment(101L);

        Employee employeeExpected = new Employee();
        employeeExpected.setId(searchForId);
        employeeExpected.setFullName("big rami");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse("2012-01-01");
        employeeExpected.setGraduationDate(date);
        employeeExpected.setNationalId("10101001");
        employeeExpected.setSalaryRaise(0f);
        employeeExpected.setGrossSalary(100000f);
        employeeExpected.setGender(Gender.MALE);
        employeeExpected.setRole(EmployeeRole.HR);
        employeeExpected.setTeam(team);
        employeeExpected.setDepartment(department);


        getMockMvc().perform(MockMvcRequestBuilders.get("/employee/" + searchForId)
                .with(httpBasic("abbas_habib_10", "123")))
                .andExpect(status().isOk());

        // as departmentExpected id is currently null
        // we add the id coming from the response to it
        // then compare the expected object with the the object in DB
        TestShortcutMethods<Employee> tester = new TestShortcutMethods<Employee>();
        tester.compareIdOwnerWithDataBase(employeeExpected, getEmployeeRepository());

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
    public void modify_employee_by_hr() throws Exception {
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
    public void modify_employee_salary_with_salary_command() throws Exception {
        Long employeeIdToModify = 101L;
        Employee employee = getEmployeeService().getEmployee(employeeIdToModify);
        employee.setSalaryRaise(5000f);
        employee.setGrossSalary(20000f);
        Float netSalary = getEmployeeService().calculateNetSalary(employee.getGrossSalary(), employee.getSalaryRaise());
        employee.setNetSalary(netSalary);


        EmployeeSalaryModifyCommand employeeSalaryModifyCommand = new EmployeeSalaryModifyCommand();
        employeeSalaryModifyCommand.setGrossSalary(20000f);
        employeeSalaryModifyCommand.setSalaryRaise(5000f);


        ObjectMapper objectMapper = new ObjectMapper();
        String employeeSalaryModifyCommandJSON = objectMapper.writeValueAsString(employeeSalaryModifyCommand);


        EmployeeInfoDTO employeeInfoDTO = new EmployeeInfoDTO();
        employeeInfoDTO.setEmployeeToDTO(employee);
        String expectedResponseDTOJSON = objectMapper.writeValueAsString(employeeInfoDTO);


        getMockMvc().perform(MockMvcRequestBuilders.put("/employee/" + employeeIdToModify + "/salary")
                .with(httpBasic("abbas_habib_10", "123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeSalaryModifyCommandJSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponseDTOJSON));


    }

    @Test
    @DatabaseSetup("/data.xml")
    public void get_employees_under_manager_by_hr() throws Exception {
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
    public void get_employees_recursively_by_hr() throws Exception {
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


    // duplicates and exception handling tests *********************************

    @Test
    @DatabaseSetup("/hr-only.xml")
    public void add_employee_by_hr_duplicate_national_id() throws Exception {
        Employee addEmployeeCommand = new Employee();
        addEmployeeCommand.setFullName("ahmed safty");
        addEmployeeCommand.setGender(Gender.MALE);
        addEmployeeCommand.setGrossSalary(10000f);
        addEmployeeCommand.setRole(EmployeeRole.EMPLOYEE);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse("2012-01-02");
        addEmployeeCommand.setGraduationDate(date);


        addEmployeeCommand.setNationalId("123"); // duplicate nationalId
        // Is expected to return same object it receives
        ObjectMapper objectMapper = new ObjectMapper();
        String employeeJson = objectMapper.writeValueAsString(addEmployeeCommand); // converts employee object to JSON string

        // POST request (.post("/employee/")) takes to be a Json of employee in request Body
        getMockMvc().perform(MockMvcRequestBuilders.post("/employee/")
                .with(httpBasic("abbas_habib_1", "123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeJson))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof CustomException))
                .andExpect(result -> assertEquals("nationalId already exists!", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @DatabaseSetup("/hr-only.xml")
    public void add_employee_by_hr_first_name_null() throws Exception {
        AddEmployeeCommand addEmployeeCommand = new AddEmployeeCommand();
        addEmployeeCommand.setLastName("ahmed");
        addEmployeeCommand.setGender(Gender.MALE);
        addEmployeeCommand.setGrossSalary(10000f);
        addEmployeeCommand.setRole(EmployeeRole.EMPLOYEE);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse("2012-01-02");
        addEmployeeCommand.setGraduationDate(date);

        addEmployeeCommand.setNationalId("1234");
        // Is expected to return same object it receives
        ObjectMapper objectMapper = new ObjectMapper();
        String employeeJson = objectMapper.writeValueAsString(addEmployeeCommand);

        // POST request (.post("/employee/")) takes to be a Json of employee in request Body
        getMockMvc().perform(MockMvcRequestBuilders.post("/employee/")
                .with(httpBasic("abbas_habib_1", "123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeJson))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof CustomException))
                .andExpect(result -> assertEquals("firstName cannot be null!", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @DatabaseSetup("/hr-only.xml")
    public void add_employee_by_hr_last_name_null() throws Exception {
        AddEmployeeCommand addEmployeeCommand = new AddEmployeeCommand();
        addEmployeeCommand.setFirstName("ahmed");
        addEmployeeCommand.setGender(Gender.MALE);
        addEmployeeCommand.setGrossSalary(10000f);
        addEmployeeCommand.setRole(EmployeeRole.EMPLOYEE);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse("2012-01-02");
        addEmployeeCommand.setGraduationDate(date);

        addEmployeeCommand.setNationalId("1234");
        // Is expected to return same object it receives
        ObjectMapper objectMapper = new ObjectMapper();
        String employeeJson = objectMapper.writeValueAsString(addEmployeeCommand); // converts employee object to JSON string

        // POST request (.post("/employee/")) takes to be a Json of employee in request Body
        getMockMvc().perform(MockMvcRequestBuilders.post("/employee/")
                .with(httpBasic("abbas_habib_1", "123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeJson))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof CustomException))
                .andExpect(result -> assertEquals("lastName cannot be null!", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }


    @Test
    @DatabaseSetup("/hr-only.xml")
    public void add_employee_by_hr_national_id_null() throws Exception {
        AddEmployeeCommand addEmployeeCommand = new AddEmployeeCommand();
        addEmployeeCommand.setFirstName("ahmed");
        addEmployeeCommand.setLastName("abbas");
        addEmployeeCommand.setGender(Gender.MALE);
        addEmployeeCommand.setGrossSalary(10000f);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse("2012-01-02");
        addEmployeeCommand.setGraduationDate(date);


        // Is expected to return same object it receives
        ObjectMapper objectMapper = new ObjectMapper();
        String employeeJson = objectMapper.writeValueAsString(addEmployeeCommand); // converts employee object to JSON string

        // POST request (.post("/employee/")) takes to be a Json of employee in request Body
        getMockMvc().perform(MockMvcRequestBuilders.post("/employee/")
                .with(httpBasic("abbas_habib_1", "123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeJson))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof CustomException))
                .andExpect(result -> assertEquals("nationalId cannot be null!", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @DatabaseSetup("/hr-only.xml")
    public void add_employee_by_hr_gender_null() throws Exception {
        AddEmployeeCommand addEmployeeCommand = new AddEmployeeCommand();
        addEmployeeCommand.setFirstName("ahmed");
        addEmployeeCommand.setLastName("abbas");
        addEmployeeCommand.setGrossSalary(10000f);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse("2012-01-02");
        addEmployeeCommand.setGraduationDate(date);
        addEmployeeCommand.setNationalId("1234");


        // Is expected to return same object it receives
        ObjectMapper objectMapper = new ObjectMapper();
        String employeeJson = objectMapper.writeValueAsString(addEmployeeCommand); // converts employee object to JSON string

        // POST request (.post("/employee/")) takes to be a Json of employee in request Body
        getMockMvc().perform(MockMvcRequestBuilders.post("/employee/")
                .with(httpBasic("abbas_habib_1", "123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeJson))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof CustomException))
                .andExpect(result -> assertEquals("gender cannot be null!", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }


    @Test
    @DatabaseSetup("/hr-only.xml")
    public void add_employee_by_hr_graduation_date_null() throws Exception {
        AddEmployeeCommand addEmployeeCommand = new AddEmployeeCommand();
        addEmployeeCommand.setFirstName("ahmed");
        addEmployeeCommand.setLastName("abbas");
        addEmployeeCommand.setGrossSalary(10000f);
        addEmployeeCommand.setNationalId("1234");
        addEmployeeCommand.setGender(Gender.MALE);


        // Is expected to return same object it receives
        ObjectMapper objectMapper = new ObjectMapper();
        String employeeJson = objectMapper.writeValueAsString(addEmployeeCommand); // converts employee object to JSON string

        // POST request (.post("/employee/")) takes to be a Json of employee in request Body
        getMockMvc().perform(MockMvcRequestBuilders.post("/employee/")
                .with(httpBasic("abbas_habib_1", "123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeJson))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof CustomException))
                .andExpect(result -> assertEquals("graduationDate cannot be null!", Objects.requireNonNull(result.getResolvedException()).getMessage()));

    }

    @Test
    @DatabaseSetup("/hr-only.xml")
    public void add_employee_by_hr_gross_salary_null() throws Exception {
        AddEmployeeCommand addEmployeeCommand = new AddEmployeeCommand();
        addEmployeeCommand.setFirstName("ahmed");
        addEmployeeCommand.setLastName("abbas");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse("2012-01-02");
        addEmployeeCommand.setGraduationDate(date);
        addEmployeeCommand.setNationalId("1234");
        addEmployeeCommand.setGender(Gender.MALE);
        // Is expected to return same object it receives
        ObjectMapper objectMapper = new ObjectMapper();
        String employeeJson = objectMapper.writeValueAsString(addEmployeeCommand); // converts employee object to JSON string

        // POST request (.post("/employee/")) takes to be a Json of employee in request Body
        getMockMvc().perform(MockMvcRequestBuilders.post("/employee/")
                .with(httpBasic("abbas_habib_1", "123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeJson))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof CustomException))
                .andExpect(result -> assertEquals("grossSalary cannot be null!", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @DatabaseSetup("/hr-only.xml")
    public void add_employee_by_hr_user_role_null() throws Exception {
        AddEmployeeCommand addEmployeeCommand = new AddEmployeeCommand();
        addEmployeeCommand.setFirstName("ahmed");
        addEmployeeCommand.setGrossSalary(10000f);

        addEmployeeCommand.setLastName("abbas");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse("2012-01-02");
        addEmployeeCommand.setGraduationDate(date);
        addEmployeeCommand.setNationalId("1234");
        addEmployeeCommand.setGender(Gender.MALE);
        // Is expected to return same object it receives
        ObjectMapper objectMapper = new ObjectMapper();
        String employeeJson = objectMapper.writeValueAsString(addEmployeeCommand); // converts employee object to JSON string

        // POST request (.post("/employee/")) takes to be a Json of employee in request Body
        getMockMvc().perform(MockMvcRequestBuilders.post("/employee/")
                .with(httpBasic("abbas_habib_1", "123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeJson))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof CustomException))
                .andExpect(result -> assertEquals("role cannot be null!", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }


    @Test
    @DatabaseSetup("/hr-only.xml")
    public void add_employee_by_hr_user_gross_salary_negative() throws Exception {
        AddEmployeeCommand addEmployeeCommand = new AddEmployeeCommand();
        addEmployeeCommand.setFirstName("ahmed");
        addEmployeeCommand.setLastName("ahmed");
        addEmployeeCommand.setGender(Gender.MALE);
        addEmployeeCommand.setRole(EmployeeRole.EMPLOYEE);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse("2012-01-02");
        addEmployeeCommand.setGraduationDate(date);

        addEmployeeCommand.setNationalId("1234");
        // Is expected to return same object it receives
        addEmployeeCommand.setGrossSalary(-500f);
        ObjectMapper objectMapper = new ObjectMapper();
        String employeeJson = objectMapper.writeValueAsString(addEmployeeCommand); // converts employee object to JSON string

        // POST request (.post("/employee/")) takes to be a Json of employee in request Body
        getMockMvc().perform(MockMvcRequestBuilders.post("/employee/")
                .with(httpBasic("abbas_habib_1", "123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeJson))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof CustomException))
                .andExpect(result -> assertEquals("grossSalary cannot be less than 0!", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }


    @Test
    @Transactional
    @DatabaseSetup("/data.xml")
    public void modify_employee_gross_salary_negative_by_hr() throws Exception {
        // Initial values of the employee
        Long employeeId = 103L; // employee id to modify
        Employee employeeToModify = getEmployeeService().getEmployee(employeeId);

        // Expected modification
        EmployeeModifyCommand employeeModificationDto = new EmployeeModifyCommand();

        // (1) Edit basic employee info
        employeeModificationDto.setFirstName("reem");
        employeeModificationDto.setLastName("naser");
        employeeModificationDto.setGender(Gender.FEMALE);
        employeeModificationDto.setGrossSalary(-500f);
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
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof CustomException))
                .andExpect(result -> assertEquals("grossSalary cannot be less than 0!", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }


    @Test
    @DatabaseSetup("/hr-only.xml")
    public void add_employee_with_hr_first_name_contains_numbers() throws Exception {
        AddEmployeeCommand addEmployeeCommand = new AddEmployeeCommand();
        addEmployeeCommand.setFirstName("ahme2d");
        addEmployeeCommand.setLastName("ahmed");
        addEmployeeCommand.setGender(Gender.MALE);
        addEmployeeCommand.setRole(EmployeeRole.EMPLOYEE);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse("2012-01-02");
        addEmployeeCommand.setGraduationDate(date);

        addEmployeeCommand.setNationalId("1234");
        // Is expected to return same object it receives
        addEmployeeCommand.setGrossSalary(5000f);
        ObjectMapper objectMapper = new ObjectMapper();
        String employeeJson = objectMapper.writeValueAsString(addEmployeeCommand); // converts employee object to JSON string

        // POST request (.post("/employee/")) takes to be a Json of employee in request Body
        getMockMvc().perform(MockMvcRequestBuilders.post("/employee/")
                .with(httpBasic("abbas_habib_1", "123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeJson))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof CustomException))
                .andExpect(result -> assertEquals("firstName has to be alphabets only!", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @DatabaseSetup("/hr-only.xml")
    public void add_employee_with_hr_last_name_contains_numbers() throws Exception {
        AddEmployeeCommand addEmployeeCommand = new AddEmployeeCommand();
        addEmployeeCommand.setLastName("sa51fty");
        addEmployeeCommand.setFirstName("saty");
        addEmployeeCommand.setGender(Gender.MALE);
        addEmployeeCommand.setRole(EmployeeRole.EMPLOYEE);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse("2012-01-02");
        addEmployeeCommand.setGraduationDate(date);

        addEmployeeCommand.setNationalId("1234");
        // Is expected to return same object it receives
        addEmployeeCommand.setGrossSalary(5000f);
        ObjectMapper objectMapper = new ObjectMapper();
        String employeeJson = objectMapper.writeValueAsString(addEmployeeCommand); // converts employee object to JSON string

        // POST request (.post("/employee/")) takes to be a Json of employee in request Body
        getMockMvc().perform(MockMvcRequestBuilders.post("/employee/")
                .with(httpBasic("abbas_habib_1", "123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeJson))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof CustomException))
                .andExpect(result -> assertEquals("lastName has to be alphabets only!", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @DatabaseSetup("/hr-only.xml")
    public void add_employee_with_hr_national_id_contains_letters() throws Exception {
        AddEmployeeCommand addEmployeeCommand = new AddEmployeeCommand();
        addEmployeeCommand.setLastName("ahmed");
        addEmployeeCommand.setFirstName("ahaha");
        addEmployeeCommand.setGender(Gender.MALE);
        addEmployeeCommand.setRole(EmployeeRole.EMPLOYEE);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse("2012-01-02");
        addEmployeeCommand.setGraduationDate(date);

        addEmployeeCommand.setNationalId("123a4");
        // Is expected to return same object it receives
        addEmployeeCommand.setGrossSalary(5000f);
        ObjectMapper objectMapper = new ObjectMapper();
        String employeeJson = objectMapper.writeValueAsString(addEmployeeCommand); // converts employee object to JSON string

        // POST request (.post("/employee/")) takes to be a Json of employee in request Body
        getMockMvc().perform(MockMvcRequestBuilders.post("/employee/")
                .with(httpBasic("abbas_habib_1", "123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeJson))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof CustomException))
                .andExpect(result -> assertEquals("nationalId has to be numbers only!", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }
}


