package com.spring.Employee;

public interface Queries
{
    public static final String EMPLOYEES_UNDER_MANAGER_RECURSIVE = "with recursive cte(id, birth_date, gender, graduation_date, gross_salary, name, net_salary, department_id, manager_id, team_id) as (\n" +
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
            "select * from cte;";

}