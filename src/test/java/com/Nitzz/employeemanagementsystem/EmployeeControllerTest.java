package com.Nitzz.employeemanagementsystem;

import com.Nitzz.employeemanagementsystem.controller.EmployeeController;
import com.Nitzz.employeemanagementsystem.entity.Employee;
import com.Nitzz.employeemanagementsystem.exception.ResourceNotFoundException;
import com.Nitzz.employeemanagementsystem.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
        when(employeeService.getAllEmployees()).thenReturn(Collections.singletonList(createSampleEmployee()));

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John Doe"));
    }

    @Test
    public void getEmployeeById_ShouldReturnEmployee_WhenIdExists() throws Exception {
        when(employeeService.getEmployeeById(1L)).thenReturn(createSampleEmployee());

        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    public void getEmployeeById_ShouldReturn404_WhenIdDoesNotExist() throws Exception {
        when(employeeService.getEmployeeById(99L))
                .thenThrow(new ResourceNotFoundException("Employee not found with id: 99"));

        mockMvc.perform(get("/api/employees/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Employee not found with id: 99"));
    }

    @Test
    public void createEmployee_ShouldReturnEmployee_WhenPayloadIsValid() throws Exception {
        Employee employee = createSampleEmployee();
        when(employeeService.saveEmployee(any(Employee.class))).thenReturn(employee);

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    public void createEmployee_ShouldReturn400_WhenNameIsBlank() throws Exception {
        Employee employee = createSampleEmployee();
        employee.setName("");

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("Name is required"));
    }

    @Test
    public void createEmployee_ShouldReturn400_WhenEmailIsInvalid() throws Exception {
        Employee employee = createSampleEmployee();
        employee.setEmail("not-an-email");

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").value("Invalid email format"));
    }

    @Test
    public void createEmployee_ShouldReturn400_WhenSalaryIsNegative() throws Exception {
        Employee employee = createSampleEmployee();
        employee.setSalary(-100.0);

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.salary").value("Salary must be positive"));
    }

    @Test
    public void updateEmployee_ShouldReturnUpdatedEmployee_WhenPayloadIsValid() throws Exception {
        Employee updated = createSampleEmployee();
        updated.setName("Jane Doe");
        when(employeeService.updateEmployee(eq(1L), any(Employee.class))).thenReturn(updated);

        mockMvc.perform(put("/api/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Jane Doe"));
    }

    @Test
    public void deleteEmployee_ShouldReturnOk_WhenIdExists() throws Exception {
        mockMvc.perform(delete("/api/employees/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteEmployee_ShouldReturn404_WhenIdDoesNotExist() throws Exception {
        doThrow(new ResourceNotFoundException("Employee not found with id: 99"))
                .when(employeeService).deleteEmployee(99L);

        mockMvc.perform(delete("/api/employees/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Employee not found with id: 99"));
    }
}