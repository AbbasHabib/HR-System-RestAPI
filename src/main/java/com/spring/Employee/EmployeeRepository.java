package com.spring.Employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>
{
    List<Employee> findByName(String name);
    List<Employee> findByManager(Employee manager);

    @Query(value = "with recursive cte as (\n" +
            "  select     id, birth_date, gender, graduation_date, gross_salary, name, net_salary, department_id, manager_id, team_id\n" +
            "\n" +
            "  from       employee\n" +
            "  where      manager_id = :managerId \n" +
            "  union all\n" +
            "  select     p.id, p.birth_date, p.gender, p.graduation_date, p.gross_salary, p.name, p.net_salary, p.department_id, p.manager_id, p.team_id\n" +
            "  from       employee p\n" +
            "  inner join cte\n" +
            "          on p.manager_id = cte.id\n" +
            ")\n" +
            "select * from cte;\n", nativeQuery = true)
    List<Employee> findManagerEmployeesRecursivelyQueried(@Param("managerId") Long managerId);
}
