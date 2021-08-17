package com.spring.employee;

import com.spring.Employee.DTO.EmployeeInfoOnlyDTO;
import com.spring.Employee.DTO.EmployeeSalaryDTO;
import com.spring.Employee.Employee;
import com.spring.Employee.DTO.EmployeeModifyDTO;
import com.spring.Employee.EmployeeService;
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
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class EmployeeControllerTests
{

    @Autowired
    EmployeeService employeeService;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void add_employee() throws Exception
    {
        Employee emp = new Employee();
        emp.setId(22L);
        emp.setName("nau");
        emp.setGender((char) 's');
        emp.setGrossSalary(520f);
        // this test is expected to: return same object it receives and add employee to database


        // EmployeeService is tested and (employeeService.addEmployee)
        // Is expected to return same object it receives
        ObjectMapper objectMapper = new ObjectMapper();
        String employeeJson = objectMapper.writeValueAsString(emp); // converts employee object to JSON string

        // The from this POST request (.post("/employee/")) takes to be a Json of employee object on request Body
        mockMvc.perform(MockMvcRequestBuilders.post("/employee/")
                .contentType(MediaType.APPLICATION_JSON).content(employeeJson))
                .andExpect(status().isOk());
    }

    @Test
    public void get_employee_with_id() throws Exception
    {
        String searchForId = "2";
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/employee/" + searchForId))
                .andExpect(status().isOk())
                .andReturn();

        JSONObject employeeJson = new JSONObject(result.getResponse().getContentAsString());

        assertEquals(Long.toString(employeeJson.getLong("id")), searchForId);
    }

    @Test
    public void delete_employee_with_id() throws Exception
    {
        String deleteUserWithID = "100";
        mockMvc.perform(MockMvcRequestBuilders.delete("/employee/" + deleteUserWithID))
                .andExpect(content().string("true"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void modify_employee() throws Exception
    {
        // Initial values of the employee
        Employee employeeToModify = new Employee();
        employeeToModify.setId(1L);

        String EmployeeId = employeeToModify.getId().toString();

        // Expected modification
        EmployeeModifyDTO employeeModificationDto = new EmployeeModifyDTO();
        employeeModificationDto.setName("btengana");
        employeeModificationDto.setGender('F');
        employeeModificationDto.setGrossSalary(7000.0f);


        EmployeeModifyDTO.dtoToEmployee(employeeModificationDto, employeeToModify);

        ObjectMapper objectMapper = new ObjectMapper();
        String employeeDtoJson = objectMapper.writeValueAsString(employeeModificationDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/employee/" + EmployeeId)
                .contentType(MediaType.APPLICATION_JSON).content(employeeDtoJson))
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

        mockMvc.perform(MockMvcRequestBuilders.get("/employee/salary/" + EmployeeId))
                .andExpect(content().json(employeeSalaryDTOJson))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void get_employees_under_manager() throws Exception
    {
        Employee manager = employeeService.getEmployee(2L);

        List<Employee> employeesUnderManager = new ArrayList<>(manager.getEmployees());
        List<EmployeeInfoOnlyDTO> employeesDTO = EmployeeInfoOnlyDTO.setEmployeeToDTOList(employeesUnderManager);

        ObjectMapper objectMapper = new ObjectMapper();
        String employeesUnderManagerJson = objectMapper.writeValueAsString(employeesDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/employee/manager/" + manager.getId()))
                .andExpect(content().json(employeesUnderManagerJson))
                .andExpect(status().isOk());
    }

}












