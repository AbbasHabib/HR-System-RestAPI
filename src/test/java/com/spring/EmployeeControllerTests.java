package com.spring;

import com.spring.Employee.DTO.EmployeeSalaryDTO;
import com.spring.Employee.Employee;
import com.spring.Employee.DTO.EmployeeModifyDTO;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class EmployeeControllerTests
{
    @Autowired
    MockMvc mockMvc;

    @Test
    public void add_employee() throws Exception
    {
        // this test is expected to: return same object it receives and add employee to database

        Employee employeeToAdd = new Employee();
        employeeToAdd.setId(100L);
        employeeToAdd.setName("7amada");
        employeeToAdd.setGender('R');

        ObjectMapper objectMapper = new ObjectMapper();
        String employeeJson = objectMapper.writeValueAsString(employeeToAdd); // converts employee object to JSON string

        // The from this POST request (.post("/employee/")) takes to be a Json of employee object on request Body
        mockMvc.perform(MockMvcRequestBuilders.post("/employee/")
                .contentType(MediaType.APPLICATION_JSON).content(employeeJson))
                .andExpect(content().json(employeeJson))
                .andExpect(status().isOk());
    }

    @Test
    public void get_employee_with_id() throws Exception
    {
        String searchForId = "100";
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/employee/"+searchForId))
                .andExpect(status().isOk())
                .andReturn();

        JSONObject employeeJson = new JSONObject(result.getResponse().getContentAsString());

        assertEquals(Long.toString(employeeJson.getLong("id")), searchForId);
    }

    @Test
    public void delete_employee_with_id() throws Exception
    {
        String deleteUserWithID = "100";
        mockMvc.perform(MockMvcRequestBuilders.delete("/employee/"+deleteUserWithID))
                .andExpect(content().string("true"))
                .andExpect(status().isOk())
                .andReturn();
    }
    @Test
    public void modify_employee() throws Exception
    {
        // Initial values of the employee
        Employee employeeToModify = new Employee();
        employeeToModify.setId(100L);
        employeeToModify.setName("7amada");
        employeeToModify.setGender('R');

        String EmployeeId = employeeToModify.getId().toString();

        // Expected modification
        EmployeeModifyDTO employeeModificationDto = new EmployeeModifyDTO();
        employeeModificationDto.setName("btengana");
        employeeModificationDto.setGender('F');
        employeeModificationDto.setGrossSalary(7000.0f);


        EmployeeModifyDTO.dtoToEmployee(employeeModificationDto, employeeToModify);

        ObjectMapper objectMapper = new ObjectMapper();
        String employeeDtoJson = objectMapper.writeValueAsString(employeeModificationDto);
        String employeeModifiedJson = objectMapper.writeValueAsString(employeeToModify);

        mockMvc.perform(MockMvcRequestBuilders.put("/employee/"+EmployeeId)
                .contentType(MediaType.APPLICATION_JSON).content(employeeDtoJson))
                .andExpect(content().json(employeeModifiedJson))
                .andExpect(status().isOk());
    }
    @Test
    public void get_employee_salary() throws Exception
    {
        Employee employeeRequired = new Employee();
        employeeRequired.setId(100L);
        employeeRequired.setName("7amada");
        employeeRequired.setGender('R');
        employeeRequired.setGrossSalary(7000.0f);

        String EmployeeId = employeeRequired.getId().toString();

        EmployeeSalaryDTO employeeSalaryDTO = new EmployeeSalaryDTO(employeeRequired);

        ObjectMapper objectMapper = new ObjectMapper();
        String employeeSalaryDTOJson = objectMapper.writeValueAsString(employeeSalaryDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/employee/salary/"+EmployeeId))
                .andExpect(content().json(employeeSalaryDTOJson))
                .andExpect(status().isOk());
    }
}
