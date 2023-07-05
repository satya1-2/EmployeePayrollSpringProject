package com.example.employeepayroll.controller;


import com.example.employeepayroll.dto.EmployeeDTO;
import com.example.employeepayroll.model.Employee;
import com.example.employeepayroll.model.EmployeeModel;
import com.example.employeepayroll.repo.EmployeeRepository;
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
    private EmployeeRepository employeeRepository;

    // GET all employees
    @GetMapping
    public List<EmployeeModel> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    // GET employee by ID
    @GetMapping("/{employeeId}")
    public ResponseEntity<EmployeeModel> getEmployeeById(@PathVariable Long employeeId) {
        Optional<Employee> employee = employeeRepository.findById(employeeId);
        if (employee.isPresent()) {
            return ResponseEntity.ok(convertToModel(employee.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // POST create new employee
    @PostMapping
    public ResponseEntity<EmployeeModel> createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee newEmployee = convertToEntity(employeeDTO);
        Employee createdEmployee = employeeRepository.save(newEmployee);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToModel(createdEmployee));
    }

    // PUT update employee
    @PutMapping("/{employeeId}")
    public ResponseEntity<EmployeeModel> updateEmployee(@PathVariable Long employeeId, @RequestBody EmployeeDTO employeeDTO) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        if (optionalEmployee.isPresent()) {
            Employee existingEmployee = optionalEmployee.get();
            existingEmployee.setName(employeeDTO.getName());
            existingEmployee.setSalary(employeeDTO.getSalary());
            Employee updatedEmployee = employeeRepository.save(existingEmployee);
            return ResponseEntity.ok(convertToModel(updatedEmployee));
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

    // Helper methods to convert between DTO and Model
    private EmployeeModel convertToModel(Employee employee) {
        EmployeeModel model = new EmployeeModel();
        model.setId(employee.getId());
        model.setName(employee.getName());
        model.setSalary(employee.getSalary());
        return model;
    }

    private Employee convertToEntity(EmployeeDTO dto) {
        Employee entity = new Employee();
        entity.setName(dto.getName());
        entity.setSalary(dto.getSalary());
        return entity;
    }
}
