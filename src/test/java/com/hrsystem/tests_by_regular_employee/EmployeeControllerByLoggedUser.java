package com.hrsystem.tests_by_regular_employee;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.hrsystem.IntegrationTest;
import com.hrsystem.employee.Employee;
import com.hrsystem.employee.Gender;
import com.hrsystem.employee.commands.EmployeeModificationByLoggedUserCommand;
import com.hrsystem.employee.dtos.EmployeeBasicInfoDTO;
import com.hrsystem.employee.dtos.EmployeeInfoDTO;
import com.hrsystem.tests_by_hr.testShortcuts.TestShortcutMethods;
import com.hrsystem.utilities.CustomException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EmployeeControllerByLoggedUser extends IntegrationTest {

    @Test
    @DatabaseSetup("/EmployeeWithCredentials.xml")
    public void get_employee_with_id_by_logged_user() throws Exception {
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
    public void modify_employee_by_logged_user() throws Exception {
        Long searchForId = 1L;
        Employee employeeExpected = getEmployeeService().getEmployee(searchForId);
        EmployeeModificationByLoggedUserCommand modificationCommand = new EmployeeModificationByLoggedUserCommand();
        modificationCommand.setFirstName("homos");
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
    @DatabaseSetup("/data.xml")
    public void get_sub_employees_by_logged_user() throws Exception {
        Long managerId = 102L;
        List<Employee> employeesUnderManager = getEmployeeRepository().findEmployeesByManager_Id(managerId);
        EmployeeBasicInfoDTO employeeBasicInfoDTO = new EmployeeBasicInfoDTO();
        List<EmployeeBasicInfoDTO> EmployeesInfoDTO = employeeBasicInfoDTO.generateDTOListFromEmployeeList(employeesUnderManager);

        ObjectMapper objectMapper = new ObjectMapper();
        String employeesDtoJson = objectMapper.writeValueAsString(EmployeesInfoDTO);

        getMockMvc().perform(MockMvcRequestBuilders.get("/profile/employee/all-sub-employees")
                .with(httpBasic("hamada_elgin_102", "101087")))
                .andExpect(content().json(employeesDtoJson))
                .andExpect(status().isOk());

    }

    // exception handling
    @Test
    @DatabaseSetup("/EmployeeWithCredentials.xml")
    public void modify_employee_by_logged_user_sending_number_in_first_name() throws Exception {
        Long searchForId = 1L;
        Employee employeeExpected = getEmployeeService().getEmployee(searchForId);
        EmployeeModificationByLoggedUserCommand modificationCommand = new EmployeeModificationByLoggedUserCommand();
        modificationCommand.setFirstName("18aje932");
        modificationCommand.setLastName("elsham");
        modificationCommand.setGender(Gender.FEMALE);
        modificationCommand.commandToEmployee(employeeExpected);

        EmployeeInfoDTO expectedEmployeeInfoDTO = new EmployeeInfoDTO();
        expectedEmployeeInfoDTO.setEmployeeToDTO(employeeExpected);

        ObjectMapper objectMapper = new ObjectMapper();
        String modificationCommandJSON = objectMapper.writeValueAsString(modificationCommand);

        getMockMvc().perform(MockMvcRequestBuilders.put("/profile/employee/")
                .with(httpBasic("ahmed_habib_1", "123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(modificationCommandJSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof CustomException))
                .andExpect(result -> assertEquals("firstName has to be alphabets only!", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @DatabaseSetup("/EmployeeWithCredentials.xml")
    public void modify_employee_by_logged_user_sending_number_in_last_name() throws Exception {
        Long searchForId = 1L;
        Employee employeeExpected = getEmployeeService().getEmployee(searchForId);
        EmployeeModificationByLoggedUserCommand modificationCommand = new EmployeeModificationByLoggedUserCommand();
        modificationCommand.setFirstName("ahmed");
        modificationCommand.setLastName("564elsham");
        modificationCommand.setGender(Gender.FEMALE);
        modificationCommand.commandToEmployee(employeeExpected);

        EmployeeInfoDTO expectedEmployeeInfoDTO = new EmployeeInfoDTO();
        expectedEmployeeInfoDTO.setEmployeeToDTO(employeeExpected);

        ObjectMapper objectMapper = new ObjectMapper();
        String modificationCommandJSON = objectMapper.writeValueAsString(modificationCommand);

        getMockMvc().perform(MockMvcRequestBuilders.put("/profile/employee/")
                .with(httpBasic("ahmed_habib_1", "123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(modificationCommandJSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof CustomException))
                .andExpect(result -> assertEquals("lastName has to be alphabets only!", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }


}



