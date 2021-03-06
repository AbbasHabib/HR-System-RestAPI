package com.hrsystem.employee;

import com.hrsystem.utilities.interfaces.constants.Queries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByManager_Id(Long managerId);

    Optional<Employee> findEmployeeByNationalId(String nationalId);

    List<Employee> findEmployeesByManager_Id(Long manager_id);

    @Query(Queries.DELETE_FROM_EMPLOYEE_BY_ID)
    @Modifying()
    void deleteById(Long id);

    @Query(value = Queries.EMPLOYEES_UNDER_MANAGER_RECURSIVE, nativeQuery = true)
    List<Employee> findManagerEmployeesRecursivelyQueried(@Param("managerId") Long managerId);
}
