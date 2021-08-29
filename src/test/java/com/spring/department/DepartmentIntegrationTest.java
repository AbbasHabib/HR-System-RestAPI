package com.spring.department;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.spring.Department.Department;
import com.spring.Department.DepartmentService;
import com.spring.Employee.Employee;
import com.spring.ExceptionsCustom.CustomException;
import org.dbunit.DBTestCase;
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
public class DepartmentIntegrationTest
{
    @Autowired
    DepartmentService departmentService;

    @Autowired
    MockMvc mockMvc;


    @Test
    @DatabaseSetup("/data.xml")
    public void add_department() throws Exception, CustomException
    {
        Department department = new Department();
        department.setName("el a7bab");
        department.setId(1L); // this id has to be added just to compare it with the received JSON response
        ObjectMapper objectMapper = new ObjectMapper();
        String departmentJSON = objectMapper.writeValueAsString(department);

        mockMvc.perform(MockMvcRequestBuilders.post("/department/")
                .contentType(MediaType.APPLICATION_JSON).content(departmentJSON))
                .andExpect(content().json(departmentJSON))
                .andExpect(status().isOk());
    }

    @Test
    @DatabaseSetup("/data.xml")
    public void get_department() throws Exception, CustomException
    {
        Long searchForId = 101L;

        Department employee = departmentService.getDepartment(searchForId);
        ObjectMapper objectMapper = new ObjectMapper();
        String employeeJSON = objectMapper.writeValueAsString(employee);

        mockMvc.perform(MockMvcRequestBuilders.get("/department/" + searchForId))
                .andExpect(content().json(employeeJSON))
                .andExpect(status().isOk());
    }
}
