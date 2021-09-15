package com.spring.TestsByHr.Security;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.spring.Department.Department;
import com.spring.Employee.DTO.EmployeeInfoOnlyDTO;
import com.spring.Employee.DTO.EmployeeModifyCommand;
import com.spring.Employee.Employee;
import com.spring.Employee.Gender;
import com.spring.ExceptionsCustom.CustomException;
import com.spring.IntegrationTest;
import com.spring.Security.UserCredentialsRepository;
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

public class SecurityTests extends IntegrationTest {





}



