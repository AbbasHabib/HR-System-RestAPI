package com.spring.Team;

import com.spring.Employee.DTO.EmployeeInfoDTO;
import com.spring.Employee.Employee;
import com.spring.Employee.EmployeeService;
import com.spring.ExceptionsCustom.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeamService {
    @Autowired
    EmployeeService employeeService;
    @Autowired
    private TeamRepository teamRepository;

    public Team getTeam(Long id) {
        return teamRepository.findById(id).isPresent() ? teamRepository.findById(id).get() : null;
    }

    public List<EmployeeInfoDTO> getTeamEmployees(Long id) throws CustomException {
        if (id == null)
            throw new CustomException("this team id does not exist");
        Team team = this.getTeam(id);
        if (team == null)
            return null;

        List<Employee> employeesInTeam = new ArrayList<Employee>(team.getEmployees());

        return EmployeeInfoDTO.setEmployeeToDTOList(employeesInTeam);
    }

    public Team addTeam(Team team) throws Exception {
        if (team.getTeamName() == null)
            throw new CustomException("teamName cannot be null!");
        if (getTeam(team.getId()) == null)
            return teamRepository.save(team);
        return null;
    }

    public Team getTeamByLoggedUser() throws CustomException {
        Team team = employeeService.getEmployeeByLoggedUser().getTeam();
        if (team == null)
            throw new CustomException("this employee does not have a team!");
        return getTeam(team.getId());
    }

}
