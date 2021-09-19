package com.spring.TestsByHr.department;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.spring.Department.Department;
import com.spring.ExceptionsCustom.CustomException;
import com.spring.IntegrationTest;
import com.spring.Team.Team;
import com.spring.TestsByHr.testShortcuts.TestShortcutMethods;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DepartmentIntegrationTest extends IntegrationTest {


    @Test
    @DatabaseSetup("/data.xml")
    public void add_department_by_hr() throws Exception, CustomException {
        Department departmentExpected = new Department();
        departmentExpected.setName("el a7bab");
        ObjectMapper objectMapper = new ObjectMapper();
        String departmentJSON = objectMapper.writeValueAsString(departmentExpected);

        MvcResult result = getMockMvc().perform(MockMvcRequestBuilders.post("/department/")
                .with(httpBasic("abbas_habib_10", "123"))
                .contentType(MediaType.APPLICATION_JSON).content(departmentJSON))
                .andExpect(status().isOk())
                .andReturn();

        TestShortcutMethods<Department> tester = new TestShortcutMethods<Department>();
        // as departmentExpected id is currently null
        // we add the id coming from the response to it
        // then compare the expected object with the the object in DB
        tester.setObjectIdFromResponseResult(result, departmentExpected);
        tester.compareIdOwnerWithDataBase(departmentExpected, getDepartmentRepository());

    }

    @Test
    @DatabaseSetup("/data.xml")
    public void get_department_by_hr() throws Exception, CustomException {
        Long searchForId = 101L;

        Department employee = getDepartmentService().getDepartment(searchForId);
        ObjectMapper objectMapper = new ObjectMapper();
        String employeeJSON = objectMapper.writeValueAsString(employee);

        getMockMvc().perform(MockMvcRequestBuilders.get("/department/" + searchForId)
                .with(httpBasic("abbas_habib_10", "123")))
                .andExpect(content().json(employeeJSON))
                .andExpect(status().isOk());
    }


    // testing exceptions ---------------------
    @Test
    @DatabaseSetup("/data.xml")
    public void add_department_with_name_is_null_exception_thrown() throws Exception {
        Department department = new Department();
        ObjectMapper objectMapper = new ObjectMapper();
        String departmentJSON = objectMapper.writeValueAsString(department);

        getMockMvc().perform(MockMvcRequestBuilders.post("/department/")
                .with(httpBasic("abbas_habib_10", "123"))
                .contentType(MediaType.APPLICATION_JSON).content(departmentJSON))
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof CustomException))
                .andExpect(result -> Assertions.assertEquals("departmentName cannot be null!", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }



}
