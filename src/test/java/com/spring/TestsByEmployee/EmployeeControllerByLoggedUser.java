package com.spring.TestsByEmployee;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.spring.Employee.COMMANDS.EmployeeModificationByLoggedUserCommand;
import com.spring.Employee.DTO.EmployeeBasicInfoDTO;
import com.spring.Employee.DTO.EmployeeInfoDTO;
import com.spring.Employee.Employee;
import com.spring.Employee.Gender;
import com.spring.ExceptionsCustom.CustomException;
import com.spring.IntegrationTest;
import com.spring.Security.EmployeeRole;
import com.spring.TestsByHr.testShortcuts.TestShortcutMethods;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EmployeeControllerByLoggedUser extends IntegrationTest {

    @Test
    @DatabaseSetup("/EmployeeWithCredentials.xml")
    public void get_employee_with_id_by_logged_user() throws Exception, CustomException {
        Long searchForId = 1L;
        Employee employeeExpected = getEmployeeService().getEmployee(searchForId);

        EmployeeInfoDTO expectedEmployeeInfoDTO = new EmployeeInfoDTO();
        expectedEmployeeInfoDTO.setEmployeeToDTO(employeeExpected);

        MvcResult result = getMockMvc().perform(MockMvcRequestBuilders.get("/profile/employee/")
                .with(httpBasic("ahmed_habib_1", "123")))
                .andExpect(status().isOk())
                .andReturn();

        // as departmentExpected id is currently null
        // we add the id coming from the response to it
        // then compare the expected object with the the object in DB
        TestShortcutMethods<EmployeeInfoDTO> tester = new TestShortcutMethods<EmployeeInfoDTO>();
        tester.setObjectIdFromResponseResult(result, expectedEmployeeInfoDTO);

        ObjectMapper objectMapper = new ObjectMapper();
        String expectedEmployeeInfoDTOJSON = objectMapper.writeValueAsString(expectedEmployeeInfoDTO);

        Assertions.assertEquals(expectedEmployeeInfoDTOJSON, result.getResponse().getContentAsString());
    }

    @Test
    @DatabaseSetup("/EmployeeWithCredentials.xml")
    public void modify_employee_by_logged_user() throws Exception, CustomException {
        Long searchForId = 1L;
        Employee employeeExpected = getEmployeeService().getEmployee(searchForId);
        EmployeeModificationByLoggedUserCommand modificationCommand = new EmployeeModificationByLoggedUserCommand();
        modificationCommand.setFirstName("7omos");
        modificationCommand.setLastName("elsham");
        modificationCommand.setGender(Gender.FEMALE);
        modificationCommand.commandToEmployee(employeeExpected);

        EmployeeInfoDTO expectedEmployeeInfoDTO = new EmployeeInfoDTO();
        expectedEmployeeInfoDTO.setEmployeeToDTO(employeeExpected);

        ObjectMapper objectMapper = new ObjectMapper();
        String modificationCommandJSON = objectMapper.writeValueAsString(modificationCommand);

        String expectedEmployeeInfoDTOJSON = objectMapper.writeValueAsString(expectedEmployeeInfoDTO);

        MvcResult result = getMockMvc().perform(MockMvcRequestBuilders.put("/profile/employee/")
                .with(httpBasic("ahmed_habib_1", "123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(modificationCommandJSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        Assertions.assertEquals(expectedEmployeeInfoDTOJSON, responseJson);
    }


    @Test
    @DatabaseSetup("/ManagerWithSubEmployees.xml")
    public void get_employee_employees_by_logged_user_with_employee_role() throws Exception {
        Long managerId = 1L;
        Employee manager = getEmployeeService().getEmployee(managerId);
        List<Employee> employeesUnderThisManager = new ArrayList<Employee>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = "2012-01-01";
        Date gradDate = sdf.parse(strDate);

        employeesUnderThisManager.add(new Employee(2L, "12345", "a", "habib"
                , gradDate, Gender.MALE, manager, 100000f, 84500.0f, EmployeeRole.EMPLOYEE));
        employeesUnderThisManager.add(new Employee(3L, "15695", "b", "habib"
                , gradDate, Gender.MALE, manager, 100000f, 84500.0f, EmployeeRole.EMPLOYEE));
        employeesUnderThisManager.add(new Employee(4L, "48514", "c", "habib"
                , gradDate, Gender.MALE, manager, 100000f, 84500.0f, EmployeeRole.EMPLOYEE));

        EmployeeBasicInfoDTO employeeBasicInfoDTO = new EmployeeBasicInfoDTO();
        List<EmployeeBasicInfoDTO> employeeBasicInfoDTOList = employeeBasicInfoDTO.generateDTOListFromEmployeeList(employeesUnderThisManager);

        ObjectMapper objectMapper = new ObjectMapper();
        String expectedDTOLListJson = objectMapper.writeValueAsString(employeeBasicInfoDTOList);

        MvcResult result = getMockMvc().perform(MockMvcRequestBuilders.get("/profile/employee/all-sub-employees")
                .with(httpBasic("ahmed_habib_1", "123")))
                .andReturn();


        String responseJson = result.getResponse().getContentAsString();
        Assertions.assertEquals(expectedDTOLListJson, responseJson);
    }


}



