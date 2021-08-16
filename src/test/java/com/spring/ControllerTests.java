package com.spring;

import com.spring.Employee.Employee;
import com.spring.Employee.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class ControllerTests
{
    @MockBean
    EmployeeService employeeService;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void add_employee() throws Exception
    {
        Employee employeeToAdd = new Employee();
        employeeToAdd.setId(11L);
        employeeToAdd.setName("Safty");

        // EmployeeService is tested and (employeeService.addEmployee)
        // Is expected to return same object it receives
        given(employeeService.addEmployee(employeeToAdd)).willReturn(employeeToAdd);

        ObjectMapper objectMapper = new ObjectMapper();
        String employeeJson = objectMapper.writeValueAsString(employeeToAdd); // converts employee object to JSON string

        // The from this POST request (.post("/employee/add")) takes to be a Json of employee object on request Body
        mockMvc.perform(MockMvcRequestBuilders.post("/employee/add")
                .contentType(MediaType.APPLICATION_JSON).content(employeeJson))
                .andExpect(status().isOk());
    }
}
