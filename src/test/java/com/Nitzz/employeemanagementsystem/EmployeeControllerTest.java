package com.Nitzz.employeemanagementsystem;

import com.Nitzz.employeemanagementsystem.controller.EmployeeController;
import com.Nitzz.employeemanagementsystem.entity.Employee;
import com.Nitzz.employeemanagementsystem.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee createSampleEmployee() {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setName("John Doe");
        employee.setEmail("john@example.com");
        employee.setDepartment("IT");
        employee.setSalary(50000.0);
        return employee;
    }

    @Test
    public void getAllEmployees_ShouldReturnEmployeeList_WhenDataExists() throws Exception {
        // Arrange
        Employee employee = createSampleEmployee();
        when(employeeService.getAllEmployees()).thenReturn(Collections.singletonList(employee));

        // Act & Assert
        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John Doe"));
    }
}