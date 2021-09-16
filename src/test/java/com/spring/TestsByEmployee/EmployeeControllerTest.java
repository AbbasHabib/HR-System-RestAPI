package com.spring.TestsByEmployee;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.spring.Department.Department;
import com.spring.Employee.DTO.EmployeeInfoOnlyDTO;
import com.spring.Employee.DTO.EmployeeModifyCommand;
import com.spring.Employee.Employee;
import com.spring.Employee.Gender;
import com.spring.ExceptionsCustom.CustomException;
import com.spring.IntegrationTest;
import com.spring.Team.Team;
import com.spring.TestsByHr.testShortcuts.TestShortcutMethods;
import com.spring.YearAndTimeGenerator;
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

import static com.spring.Employee.DTO.EmployeeInfoOnlyDTO.setEmployeeToDTOList;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EmployeeControllerTest extends IntegrationTest {

    @Test
    @DatabaseSetup("/EmployeeWithCredentials.xml")
    public void get_employee_with_id_by_logged_user() throws Exception, CustomException {
        Long searchForId = 1L;
        Employee employee = getEmployeeService().getEmployee(searchForId);

        MvcResult result = getMockMvc().perform(MockMvcRequestBuilders.get("/profile/employee/")
                .with(httpBasic("ahmed_habib_1", "123")))
                .andExpect(status().isOk())
                .andReturn();

        // as departmentExpected id is currently null
        // we add the id coming from the response to it
        // then compare the expected object with the the object in DB
        TestShortcutMethods<Employee> tester = new TestShortcutMethods<Employee>();
        tester.setObjectIdFromResponseResult(result, employee);
        tester.compareIdOwnerWithDataBase(employee, getEmployeeRepository());

    }
//
//    public void modify_employee_by_hr() throws Exception, CustomException {
//    }
}



