package com.spring.department;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.spring.Department.Department;
import com.spring.Department.DepartmentRepository;
import com.spring.Department.DepartmentService;
import com.spring.ExceptionsCustom.CustomException;
import com.spring.testShortcuts.TestShortcutMethods;
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
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
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
public class DepartmentIntegrationTest {
    @Autowired
    DepartmentService departmentService;

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    MockMvc mockMvc;


    @Test
    @DatabaseSetup("/data.xml")
    public void add_department_by_hr() throws Exception, CustomException {
        Department departmentExpected = new Department();
        departmentExpected.setName("el a7bab");
        ObjectMapper objectMapper = new ObjectMapper();
        String departmentJSON = objectMapper.writeValueAsString(departmentExpected);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/department/")
                .with(httpBasic("abbas_habib_10", "123"))
                .contentType(MediaType.APPLICATION_JSON).content(departmentJSON))
                .andExpect(status().isOk())
                .andReturn();

        TestShortcutMethods<Department> tester = new TestShortcutMethods<Department>();
        // as departmentExpected id is currently null
        // we add the id coming from the response to it
        // then compare the expected object with the the object in DB
        tester.setObjectIdFromResponseResult(result, departmentExpected);
        tester.compareIdOwnerWithDataBase(departmentExpected, departmentRepository);

    }

    @Test
    @DatabaseSetup("/data.xml")
    public void get_department_by_hr() throws Exception, CustomException {
        Long searchForId = 101L;

        Department employee = departmentService.getDepartment(searchForId);
        ObjectMapper objectMapper = new ObjectMapper();
        String employeeJSON = objectMapper.writeValueAsString(employee);

        mockMvc.perform(MockMvcRequestBuilders.get("/department/" + searchForId)
                .with(httpBasic("abbas_habib_10", "123")))
                .andExpect(content().json(employeeJSON))
                .andExpect(status().isOk());
    }
}
