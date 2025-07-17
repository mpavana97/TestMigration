package com.example.demo.service;

import com.example.demo.dao.EmployeeDao;
import com.example.demo.model.Employee;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.Setter;


@Setter
@Service("employeeService")
public class EmployeeService {

    private EmployeeDao employeeDao;

    public void addEmployee(String name, String dept) {

        Employee emp = new Employee();
        emp.setName(name);
        emp.setDepartment(dept);
        employeeDao.save(emp);
    }

    public List<Employee> getEmployeesByDepartment(String dept) {

        return employeeDao.findByDepartment(dept);
    }

}
