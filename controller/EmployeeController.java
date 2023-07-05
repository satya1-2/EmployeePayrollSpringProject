package com.example.employeepayroll.controller;


import com.example.employeepayroll.dto.EmployeeDTO;
import com.example.employeepayroll.model.Employee;
import com.example.employeepayroll.model.EmployeeModel;
import com.example.employeepayroll.repo.EmployeeRepository;
import com.example.employeepayroll.services.EmployeeNotFoundException;
import com.example.employeepayroll.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    // GET all employees
    @GetMapping
    public List<EmployeeModel> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    // GET employee by ID
    @GetMapping("/{employeeId}")
    public EmployeeModel getEmployeeById(@PathVariable Long employeeId) throws EmployeeNotFoundException {
        return employeeService.getEmployeeById(employeeId);
    }

    // POST create new employee
    @PostMapping
    public ResponseEntity<EmployeeModel> createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        EmployeeModel createdEmployee = employeeService.createEmployee(employeeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployee);
    }

    // PUT update employee
    @PutMapping("/{employeeId}")
    public EmployeeModel updateEmployee(@PathVariable Long employeeId, @RequestBody EmployeeDTO employeeDTO) throws EmployeeNotFoundException {
        return employeeService.updateEmployee(employeeId, employeeDTO);
    }

    // DELETE employee
    @DeleteMapping("/{employeeId}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long employeeId) throws EmployeeNotFoundException {
        employeeService.deleteEmployee(employeeId);
        return ResponseEntity.noContent().build();
    }
}