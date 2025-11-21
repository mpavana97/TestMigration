package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.Setter;

import com.example.demo.model.Employee;
import com.example.demo.model.Organization;
import com.example.demo.service.EmployeeService;


@Setter
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/save")
    public String saveEmployee(
        @RequestParam("name")
        String name,
        @RequestParam("dept")
        String dept) {
        employeeService.addEmployee(name, dept);
        return "Employee saved!";
    }

    @GetMapping(value = "/list", produces = "application/json")
    public List<Employee> listByDepartment(
        @RequestParam("dept")
        String dept) {

        return employeeService.getEmployeesByDepartment(dept);
    }

    @GetMapping(value = "/listOrg", produces = "application/json")
    public List<Organization> listOrg() {

        return employeeService.getAllOrg();
    }

}
