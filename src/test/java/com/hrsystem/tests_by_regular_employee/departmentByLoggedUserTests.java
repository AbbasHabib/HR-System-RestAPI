package com.hrsystem.tests_by_regular_employee;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.hrsystem.department.Department;
import com.hrsystem.employee.Employee;
import com.hrsystem.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class departmentByLoggedUserTests extends IntegrationTest {

    @Test
    @DatabaseSetup("/data.xml")
    public void get_department_by_logged_employee() throws Exception {
        Long departmentId = 102L;
        Employee employeeInDepartment = getEmployeeService().getEmployee(105L);

        Department department = new Department();
        department.setId(departmentId);
        department.setName("apples");
        Set<Employee> employeeList = new LinkedHashSet<>();
        employeeList.add(employeeInDepartment);
        department.setEmployees(employeeList);

        ObjectMapper objectMapper = new ObjectMapper();
        String expectedDepartmentJSON = objectMapper.writeValueAsString(department);

        getMockMvc().perform(MockMvcRequestBuilders.get("/profile/department")
                .with(httpBasic("lamona_habib_105", "1234")))
                .andExpect(content().json(expectedDepartmentJSON))
                .andExpect(status().isOk());
    }


}
