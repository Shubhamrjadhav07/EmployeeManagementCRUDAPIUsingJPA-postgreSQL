package com.app.repository;

import com.app.entities.Employee;
import java.util.List;

public interface EmployeeRepositoryCustome {
    List<Employee> findAllEmployees();
}