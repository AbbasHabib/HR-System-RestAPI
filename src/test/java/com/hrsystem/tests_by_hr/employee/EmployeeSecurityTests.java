package com.hrsystem.tests_by_hr.employee;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.hrsystem.IntegrationTest;
import com.hrsystem.department.Department;
import com.hrsystem.employee.Employee;
import com.hrsystem.employee.Gender;
import com.hrsystem.employee.commands.EmployeeModifyCommand;
import com.hrsystem.security.UserCredentials;
import com.hrsystem.security.passwordChangeCommand;
import com.hrsystem.team.Team;
import com.hrsystem.utilities.CustomException;
import javassist.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EmployeeSecurityTests extends IntegrationTest {

    @Test
    @DatabaseSetup("/data.xml")
    public void modify_hr_password_done_by_hr() throws Exception {
        String userName = "abbas_habib_10";
        String newPassword = "ahmed";
        String currentPassword = "123";

        passwordChangeCommand passwordChangeCommand = new passwordChangeCommand();
        passwordChangeCommand.setNewPassword(newPassword);
        passwordChangeCommand.setCurrentPassword(currentPassword);


        ObjectMapper objectMapper = new ObjectMapper();
        String passwordChangeCommandJSON = objectMapper.writeValueAsString(passwordChangeCommand);


        getMockMvc().perform(MockMvcRequestBuilders.put("/security/password-reset")
                .with(httpBasic(userName, currentPassword))
                .contentType(MediaType.APPLICATION_JSON)
                .content(passwordChangeCommandJSON))
                .andExpect(status().isOk())
                .andExpect(content().string("password updated!"));

        UserCredentials userCredentialsAfterModification = getUserCredentialsRepository().getById(userName);

        Assertions.assertEquals(userName, userCredentialsAfterModification.getUserName());
        Assertions.assertTrue(BCrypt.checkpw(newPassword, userCredentialsAfterModification.getPassword()));
    }

    @Test
    @DatabaseSetup("/data.xml")
    public void modify_hr_password_done_by_hr_current_password_incorrect() throws Exception {
        String userName = "abbas_habib_10";
        String newPassword = "ahmed";
        String currentPassword = "123";
        String currentPasswordIncorrect = "5515";
        passwordChangeCommand passwordChangeCommand = new passwordChangeCommand();
        passwordChangeCommand.setNewPassword(newPassword);
        passwordChangeCommand.setCurrentPassword(currentPasswordIncorrect);

        ObjectMapper objectMapper = new ObjectMapper();
        String passwordChangeCommandJSON = objectMapper.writeValueAsString(passwordChangeCommand);


        getMockMvc().perform(MockMvcRequestBuilders.put("/security/password-reset")
                .with(httpBasic(userName, currentPassword))
                .contentType(MediaType.APPLICATION_JSON)
                .content(passwordChangeCommandJSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof CustomException))
                .andExpect(result -> Assertions.assertEquals("current password does not match actual current password!", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @Transactional
    @DatabaseSetup("/data.xml")
    public void modify_hr_password_done_by_employee() throws Exception {
        String userName = "lamona_habib_105";
        String newPassword = "ahmed";
        String currentPassword = "1234";

        passwordChangeCommand passwordChangeCommand = new passwordChangeCommand();
        passwordChangeCommand.setNewPassword(newPassword);
        passwordChangeCommand.setCurrentPassword(currentPassword);


        ObjectMapper objectMapper = new ObjectMapper();
        String passwordChangeCommandJSON = objectMapper.writeValueAsString(passwordChangeCommand);


        getMockMvc().perform(MockMvcRequestBuilders.put("/security/password-reset")
                .with(httpBasic(userName, currentPassword))
                .contentType(MediaType.APPLICATION_JSON)
                .content(passwordChangeCommandJSON))
                .andExpect(status().isOk())
                .andExpect(content().string("password updated!"));

        UserCredentials userCredentialsAfterModification = getUserCredentialsRepository().getById(userName);

        Assertions.assertEquals(userName, userCredentialsAfterModification.getUserName());
        Assertions.assertTrue(BCrypt.checkpw(newPassword, userCredentialsAfterModification.getPassword()));
    }

    @Test
    @Transactional
    @DatabaseSetup("/data.xml")
    public void modify_hr_password_done_by_employee_current_password_incorrect() throws Exception {
        String userName = "lamona_habib_105";
        String newPassword = "ahmed";
        String currentPassword = "1234";
        String currentPasswordIncorrect = "5515";
        passwordChangeCommand passwordChangeCommand = new passwordChangeCommand();
        passwordChangeCommand.setNewPassword(newPassword);
        passwordChangeCommand.setCurrentPassword(currentPasswordIncorrect);

        ObjectMapper objectMapper = new ObjectMapper();
        String passwordChangeCommandJSON = objectMapper.writeValueAsString(passwordChangeCommand);


        getMockMvc().perform(MockMvcRequestBuilders.put("/security/password-reset")
                .with(httpBasic(userName, currentPassword))
                .contentType(MediaType.APPLICATION_JSON)
                .content(passwordChangeCommandJSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof CustomException))
                .andExpect(result -> Assertions.assertEquals("current password does not match actual current password!", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }




    @Test
    @Transactional
    @DatabaseSetup("/data.xml")
    public void modify_employee_by_hr_non_existing_user_un_authorized() throws Exception {
        // Initial values of the employee
        Long employeeId = 103L; // employee id to modify
        Employee employeeToModify = getEmployeeService().getEmployee(employeeId);

        // Expected modification
        EmployeeModifyCommand employeeModificationDto = new EmployeeModifyCommand();

        // (1) Edit basic employee info
        employeeModificationDto.setFirstName("reem");
        employeeModificationDto.setLastName("naser");
        employeeModificationDto.setGender(Gender.FEMALE);
        employeeModificationDto.setGrossSalary(7000.0f);
        // (2) set Department
        Long departmentId = 102L;
        Department dep = getDepartmentService().getDepartment(departmentId);
        if (dep == null) throw new NotFoundException("department is not found");
        employeeModificationDto.setDepartment(dep);

        // (3) set Team
        Long teamId = 102L;
        Team team = getTeamService().getTeam(teamId);
        if (team == null) throw new NotFoundException("team is not found");
        employeeModificationDto.setTeam(team);

        // (4) Edit employee manager
        Long managerId = 102L;
        Employee manager = getEmployeeService().getEmployee(managerId);
        if (manager == null) throw new NotFoundException("manager not found");
        employeeModificationDto.setManager(manager);

        employeeModificationDto.commandToEmployee(employeeToModify); // copy modified data to employee

        ObjectMapper objectMapper = new ObjectMapper();
        String employeeDtoJson = objectMapper.writeValueAsString(employeeModificationDto);

        getMockMvc().perform(MockMvcRequestBuilders.put("/employee/" + employeeId)
                .with(httpBasic("zabdi_zbadi", "123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeDtoJson))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Transactional
    @DatabaseSetup("/data.xml")
    public void modify_employee_by_hr_existing_user_incorrect_password_un_authorized() throws Exception {
        // Initial values of the employee
        Long employeeId = 103L; // employee id to modify
        Employee employeeToModify = getEmployeeService().getEmployee(employeeId);

        // Expected modification
        EmployeeModifyCommand employeeModificationDto = new EmployeeModifyCommand();

        // (1) Edit basic employee info
        employeeModificationDto.setFirstName("reem");
        employeeModificationDto.setLastName("naser");
        employeeModificationDto.setGender(Gender.FEMALE);
        employeeModificationDto.setGrossSalary(7000.0f);
        // (2) set Department
        Long departmentId = 102L;
        Department dep = getDepartmentService().getDepartment(departmentId);
        if (dep == null) throw new NotFoundException("department is not found");
        employeeModificationDto.setDepartment(dep);

        // (3) set Team
        Long teamId = 102L;
        Team team = getTeamService().getTeam(teamId);
        if (team == null) throw new NotFoundException("team is not found");
        employeeModificationDto.setTeam(team);

        // (4) Edit employee manager
        Long managerId = 102L;
        Employee manager = getEmployeeService().getEmployee(managerId);
        if (manager == null) throw new NotFoundException("manager not found");
        employeeModificationDto.setManager(manager);

        employeeModificationDto.commandToEmployee(employeeToModify); // copy modified data to employee

        ObjectMapper objectMapper = new ObjectMapper();
        String employeeDtoJson = objectMapper.writeValueAsString(employeeModificationDto);

        getMockMvc().perform(MockMvcRequestBuilders.put("/employee/" + employeeId)
                .with(httpBasic("abbas_habib_10", "12351515"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeDtoJson))
                .andExpect(status().isUnauthorized());
    }

}
