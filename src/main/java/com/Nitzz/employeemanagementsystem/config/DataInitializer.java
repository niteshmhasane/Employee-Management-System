package com.Nitzz.employeemanagementsystem.config;

import com.Nitzz.employeemanagementsystem.entity.Employee;
import com.Nitzz.employeemanagementsystem.repository.EmployeeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(EmployeeRepository repository) {
        return args -> {
            long currentCount = repository.count();

            if (currentCount < 1000) {
                String[] departments = {"IT", "HR", "Finance", "Marketing", "Sales"};

                for (long i = currentCount + 1; i <= 1000; i++) {
                    Employee employee = new Employee();
                    employee.setName("Employee " + i);
                    employee.setEmail("employee" + i + "@example.com");
                    employee.setDepartment(departments[(int)(i % departments.length)]);
                    employee.setSalary(35000.0 + (i * 15.0));

                    repository.save(employee);
                }

                System.out.println(">> Database populated! Total employee count: " + repository.count());
            } else {
                System.out.println(">> Database already has " + currentCount + " records. Skipping initialization.");
            }
        };
    }
}