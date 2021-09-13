package com.spring.Team;

import com.spring.Employee.DTO.EmployeeInfoOnlyDTO;
import com.spring.Employee.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeamService {
    @Autowired
    private TeamRepository teamRepository;

    public Team getTeam(Long id) {
        if (id == null)
            return null;
        return teamRepository.findById(id).isPresent() ? teamRepository.findById(id).get() : null;
    }

    public List<EmployeeInfoOnlyDTO> getTeamEmployees(Long id) {
        assert id != null;
        Team team = this.getTeam(id);
        if (team == null)
            return null;

        List<Employee> employeesInTeam = new ArrayList<Employee>(team.getEmployees());

        return EmployeeInfoOnlyDTO.setEmployeeToDTOList(employeesInTeam);
    }

    public Team addTeam(Team team) {
        assert team.getId() != null;
        if (getTeam(team.getId()) == null)
            return teamRepository.save(team);
        return null;
    }
}
