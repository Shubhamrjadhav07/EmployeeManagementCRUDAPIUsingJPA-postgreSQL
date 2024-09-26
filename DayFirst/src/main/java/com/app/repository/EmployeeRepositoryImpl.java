package com.app.repository;

import com.app.entities.Employee;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class EmployeeRepositoryImpl implements EmployeeRepositoryCustome {

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<Employee> findAllEmployees() {
        String jpql = "select e from Employee e";
        TypedQuery<Employee> query = entityManager.createQuery(jpql, Employee.class);
        return query.getResultList();
    }
}