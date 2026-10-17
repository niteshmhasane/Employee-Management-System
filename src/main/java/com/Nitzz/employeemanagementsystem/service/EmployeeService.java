package com.Nitzz.employeemanagementsystem.service;

import com.Nitzz.employeemanagementsystem.entity.Employee;
import java.util.List;

public interface EmployeeService {
    List<Employee> getAllEmployees();
    Employee getEmployeeById(Long id);
    Employee saveEmployee(Employee employee);
    Employee updateEmployee(Long id, Employee employeeDetails);
    void deleteEmployee(Long id);
}