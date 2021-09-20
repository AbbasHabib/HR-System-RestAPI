package com.hrsystem.tests_by_hr.team;


import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.hrsystem.employee.dtos.EmployeeInfoDTO;
import com.hrsystem.utilities.CustomException;
import com.hrsystem.IntegrationTest;
import com.hrsystem.team.Team;
import com.hrsystem.tests_by_hr.testShortcuts.TestShortcutMethods;
import javassist.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class TeamIntegrationTest extends IntegrationTest {


    @Test
    @DatabaseSetup("/data.xml")
    public void add_team_by_hr() throws Exception {
        Team teamToAdd = new Team();
        teamToAdd.setTeamName("el 7bayb");
        ObjectMapper objectMapper = new ObjectMapper();
        String teamJson = objectMapper.writeValueAsString(teamToAdd);

        MvcResult result = getMockMvc().perform(MockMvcRequestBuilders.post("/team/")
                .with(httpBasic("abbas_habib_10", "123"))
                .contentType(MediaType.APPLICATION_JSON).content(teamJson))
                .andExpect(status().isOk())
                .andReturn();

        TestShortcutMethods<Team> tester = new TestShortcutMethods<Team>();
        // as departmentExpected id is currently null
        // we add the id coming from the response to it
        // then compare the expected object with the the object in DB
        tester.setObjectIdFromResponseResult(result, teamToAdd);
        tester.compareIdOwnerWithDataBase(teamToAdd, getTeamRepository());
    }

    @Test
    @DatabaseSetup("/data.xml")
    @Transactional
    public void getTeamEmployees_by_hr() throws Exception {
        Long teamId = 101L;
        List<EmployeeInfoDTO> teamEmployees = getTeamService().getTeamEmployees(teamId);
        if (teamEmployees == null)
            throw new NotFoundException("no employees in this team");

        ObjectMapper objectMapper = new ObjectMapper();
        String teamEmployeesJson = objectMapper.writeValueAsString(teamEmployees);


        getMockMvc().perform(MockMvcRequestBuilders.get("/team/employees/" + teamId)
                .with(httpBasic("abbas_habib_10", "123")))
                .andExpect(content().json(teamEmployeesJson))
                .andExpect(status().isOk())
                .andReturn();

    }

    // testing exceptions ---------------------
    @Test
    @DatabaseSetup("/data.xml")
    public void add_team_by_hr_team_name_null_exception_thrown() throws Exception {
        Team teamToAdd = new Team();
        ObjectMapper objectMapper = new ObjectMapper();
        String teamJson = objectMapper.writeValueAsString(teamToAdd);

        getMockMvc().perform(MockMvcRequestBuilders.post("/team/")
                .with(httpBasic("abbas_habib_10", "123"))
                .contentType(MediaType.APPLICATION_JSON).content(teamJson))
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof CustomException))
                .andExpect(result -> assertEquals("teamName cannot be null!", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

}

