package com.example.demo.controller;

import com.example.demo.model.Employee;
import com.example.demo.service.EmployeeService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

import lombok.Setter;


@Setter
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private EmployeeService employeeService;

    @GetMapping("/save")
    public String saveEmployee(
        @RequestParam
        String name,
        @RequestParam
        String dept) {

        employeeService.addEmployee(name, dept);
        return "Employee saved!";
    }

    @GetMapping(value = "/list", produces = "application/json")
    public List<Employee> listByDepartment(
        @RequestParam
        String dept) {

        return employeeService.getEmployeesByDepartment(dept);
    }

}