package com.spring.Team;

import com.spring.Employee.DTO.EmployeeInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/team")
public class TeamController {
    @Autowired
    private TeamService teamService;

    @GetMapping(value = "/employees/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<EmployeeInfoDTO> getTeamEmployees(@PathVariable String id) {
        return teamService.getTeamEmployees(Long.parseLong(id));
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Team getTeam(@PathVariable String id) {
        return teamService.getTeam(Long.parseLong(id));
    }

    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public Team addTeam(@RequestBody Team team) {
        return teamService.addTeam(team);
    }
}
