package com.example.employeepayroll.services;

import com.example.employeepayroll.dto.EmployeeDTO;
import com.example.employeepayroll.model.Employee;
import com.example.employeepayroll.model.EmployeeModel;
import com.example.employeepayroll.repo.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    public List<EmployeeModel> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    public EmployeeModel getEmployeeById(Long employeeId) throws EmployeeNotFoundException {
        Optional<Employee> employee = employeeRepository.findById(employeeId);
        if (employee.isPresent()) {
            return convertToModel(employee.get());
        } else {
            throw new  EmployeeNotFoundException("Employee not found with ID: " + employeeId);
        }
    }

    public EmployeeModel createEmployee(EmployeeDTO employeeDTO) {
        Employee newEmployee = convertToEntity(employeeDTO);
        Employee createdEmployee = employeeRepository.save(newEmployee);
        return convertToModel(createdEmployee);
    }

    public EmployeeModel updateEmployee(Long employeeId, EmployeeDTO employeeDTO) throws EmployeeNotFoundException {
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        if (optionalEmployee.isPresent()) {
            Employee existingEmployee = optionalEmployee.get();
            existingEmployee.setName(employeeDTO.getName());
            existingEmployee.setSalary(employeeDTO.getSalary());
            Employee updatedEmployee = employeeRepository.save(existingEmployee);
            return convertToModel(updatedEmployee);
        } else {
            throw new EmployeeNotFoundException("Employee not found with ID: " + employeeId);
        }
    }

    public void deleteEmployee(Long employeeId) throws EmployeeNotFoundException {
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        if (optionalEmployee.isPresent()) {
            employeeRepository.deleteById(employeeId);
        } else {
            throw new EmployeeNotFoundException("Employee not found with ID: " + employeeId);
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