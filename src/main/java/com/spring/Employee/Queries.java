package com.spring.Employee;

public interface Queries {
    String EMPLOYEES_UNDER_MANAGER_RECURSIVE = "with recursive cte(id, birth_date,  degree,  first_name, gender,  graduation_date,  gross_salary,  last_name,  national_id,  net_salary,  years_of_experience, attendance_table_id, department_id, manager_id, team_id, role, salary_raise) as (\n" +
            "  select   id, birth_date,  degree,  first_name, gender,  graduation_date,  gross_salary,  last_name,  national_id,  net_salary,  years_of_experience, attendance_table_id, department_id, manager_id, team_id, role, salary_raise " +
            "\n" +
            "  from       employee\n" +
            "  where      manager_id = :managerId \n" +
            "  union all\n" +
            "  select     p.id, p.birth_date,  p.degree,  p.first_name, p.gender,  p.graduation_date,  p.gross_salary,  p.last_name,  p.national_id,  p.net_salary,  p.years_of_experience, p.attendance_table_id, p.department_id, p.manager_id, p.team_id, p.role, p.salary_raise \n" +
            "  from       employee p\n" +
            "  inner join cte\n" +
            "          on p.manager_id = cte.id\n" +
            ")\n" +
            "select * from cte;";

    String DELETE_FROM_EMPLOYEE_BY_ID = "DELETE from Employee e where e.id = ?1";

}