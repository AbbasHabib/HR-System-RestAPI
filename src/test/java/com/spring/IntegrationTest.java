package com.spring;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.spring.Employee.Attendance.AttendanceService;
import com.spring.Employee.Attendance.dayDetails.DayDetailsRepository;
import com.spring.Employee.EmployeeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class
        , TransactionalTestExecutionListener.class})
public class IntegrationTest {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private DayDetailsRepository dayDetailsRepository;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterEach
    public void deleteAllTables() {
        jdbcTemplate.execute("delete from month_details; ");
        jdbcTemplate.execute("delete from DAY_DETAILS; ");
        jdbcTemplate.execute("delete from user_credentials; ");
        jdbcTemplate.execute("delete from employee; ");
        jdbcTemplate.execute("delete from attendance_table; ");
        jdbcTemplate.execute("commit ");
    }

    public AttendanceService getAttendanceService() {
        return attendanceService;
    }

    public EmployeeService getEmployeeService() {
        return employeeService;
    }

    public MockMvc getMockMvc() {
        return mockMvc;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public DayDetailsRepository getDayDetailsRepository() {
        return dayDetailsRepository;
    }
}
