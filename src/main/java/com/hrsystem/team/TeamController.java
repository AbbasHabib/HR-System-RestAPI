package com.hrsystem.team;

import com.hrsystem.employee.dtos.EmployeeInfoDTO;
import com.hrsystem.utilities.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TeamController {
    @Autowired
    private TeamService teamService;

    @GetMapping(value = "/team/employees/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<EmployeeInfoDTO> getTeamEmployees(@PathVariable String id) throws CustomException {
        return teamService.getTeamEmployees(Long.parseLong(id));
    }

    @GetMapping(value = "/team/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Team getTeam(@PathVariable String id) {
        return teamService.getTeam(Long.parseLong(id));
    }

    @PostMapping(value = "/team/", produces = MediaType.APPLICATION_JSON_VALUE)
    public Team addTeam(@RequestBody Team team) throws Exception {
        return teamService.addTeam(team);
    }


    @GetMapping(value = "/profile/team", produces = MediaType.APPLICATION_JSON_VALUE)
    public Team getTeamByLoggedUser() throws CustomException {
        return teamService.getTeamByLoggedUser();
    }
}
