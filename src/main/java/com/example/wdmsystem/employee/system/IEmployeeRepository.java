package com.example.wdmsystem.employee.system;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IEmployeeRepository extends JpaRepository<Employee, Integer> {
    List<Employee> getEmployeesByEmployeeType(EmployeeType employeeType, Limit limit);
}
