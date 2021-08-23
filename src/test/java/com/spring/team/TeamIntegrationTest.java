package com.spring.team;


import com.spring.Employee.DTO.EmployeeInfoOnlyDTO;

import com.spring.Team.Team;
import com.spring.Team.TeamService;
import javassist.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import javax.transaction.Transactional;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class TeamIntegrationTest
{
    @Autowired
    TeamService teamService;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void add_team() throws Exception
    {
        Team teamToAdd = new Team();
        teamToAdd.setId(1L);
        teamToAdd.setTeamName("el 7bayb");
        ObjectMapper objectMapper = new ObjectMapper();
        String teamJson = objectMapper.writeValueAsString(teamToAdd);

        mockMvc.perform(MockMvcRequestBuilders.post("/team/")
                .contentType(MediaType.APPLICATION_JSON).content(teamJson))
                .andExpect(content().json(teamJson))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void getTeamEmployees() throws Exception
    {
        Long teamId = 1L;
        List<EmployeeInfoOnlyDTO> teamEmployees = teamService.getTeamEmployees(teamId);
        if(teamEmployees == null)
            throw new NotFoundException("no employees in this team");
        ObjectMapper objectMapper = new ObjectMapper();
        String teamEmployeesJson = objectMapper.writeValueAsString(teamEmployees);



        mockMvc.perform(MockMvcRequestBuilders.get("/team/employees/" + teamId))
                .andExpect(content().json(teamEmployeesJson))
                .andExpect(status().isOk());
    }
}