package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Setter;

import com.example.demo.dao.AccessControlDao;
import com.example.demo.dao.EmployeeDao;
import com.example.demo.dao.OrganizationDao;
import com.example.demo.model.Employee;
import com.example.demo.model.Organization;


@Setter
@Service("employeeService")
public class EmployeeService {

    @Autowired
    private EmployeeDao employeeDao;

    @Autowired
    private AccessControlDao accessControlDao;

    @Autowired
    private OrganizationDao organizationDao;

    public void addEmployee(String name, String dept) {

        Employee emp = new Employee();
        emp.setName(name);
        emp.setDepartment(dept);
        employeeDao.save(emp);
    }

    public List<Employee> getEmployeesByDepartment(String dept) {

        return employeeDao.findByDepartment(dept);
    }

    public List<Organization> getAllOrg() {

        return organizationDao.getAllOrganization(true);
    }

}
