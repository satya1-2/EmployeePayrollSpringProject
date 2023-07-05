package com.example.employeepayroll.controller;


import com.example.employeepayroll.model.Employee;
import com.example.employeepayroll.repo.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    @Autowired
    private EmployeeRepository employeeRepository;

    // GET all employees
    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    // GET employee by ID
    @GetMapping("/{employeeId}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long employeeId) {
        Optional<Employee> employee = employeeRepository.findById(employeeId);
        return employee.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // POST create new employee
    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        Employee createdEmployee = employeeRepository.save(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployee);
    }

    // PUT update employee
    @PutMapping("/{employeeId}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long employeeId, @RequestBody Employee employeeDetails) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            employee.setName(employeeDetails.getName());
            employee.setSalary(employeeDetails.getSalary());
            Employee updatedEmployee = employeeRepository.save(employee);
            return ResponseEntity.ok(updatedEmployee);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE employee
    @DeleteMapping("/{employeeId}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long employeeId) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        if (optionalEmployee.isPresent()) {
            employeeRepository.deleteById(employeeId);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}