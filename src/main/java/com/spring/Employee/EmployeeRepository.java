package com.spring.Employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>
{
    public List<Employee> findByName(String name);
    public List<Employee> findByManager(Employee manager);
}
