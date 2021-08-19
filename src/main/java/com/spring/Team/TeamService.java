package com.spring.Team;

import com.spring.Department.Department;
import com.spring.Employee.DTO.EmployeeInfoOnlyDTO;
import com.spring.Employee.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class TeamService
{
    @Autowired
    private TeamRepository teamRepository;

    public Team getTeam(Long id)
    {
        if(id == null)
            return null;
        return teamRepository.findById(id).isPresent() ? teamRepository.findById(id).get() : null;
    }

    public List<EmployeeInfoOnlyDTO> getTeamEmployees(Long id)
    {
        if(id == null)
            return null;
        Team team = this.getTeam(id);
        if (team == null)
            return null;

        List<Employee> employeesInTeam = new ArrayList<Employee>(team.getEmployees());

        return EmployeeInfoOnlyDTO.setEmployeeToDTOList(employeesInTeam);
    }

    public Team addTeam(Team team)
    {
        if (getTeam(team.getId()) == null || team.getId() == null)
            return teamRepository.save(team);
        return null;
    }
}
