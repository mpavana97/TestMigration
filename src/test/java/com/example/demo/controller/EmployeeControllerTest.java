package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.demo.model.Employee;
import com.example.demo.model.Organization;
import com.example.demo.service.EmployeeService;


public class EmployeeControllerTest {

    private EmployeeController employeeController;

    private EmployeeService employeeServiceMock;

    @BeforeEach
    public void setUp() {

        employeeServiceMock = EasyMock.createMock(EmployeeService.class);
        employeeController = new EmployeeController();
        employeeController.setEmployeeService(employeeServiceMock);
    }

    @Test
    public void testSaveEmployee() {

        String name = "John";
        String dept = "IT";
        employeeServiceMock.addEmployee(name, dept);
        EasyMock.expectLastCall();
        EasyMock.replay(employeeServiceMock);

        String result = employeeController.saveEmployee(name, dept);
        assertEquals("Employee saved!", result);

        EasyMock.verify(employeeServiceMock);
        EasyMock.reset(employeeServiceMock);
    }

    @Test
    public void testListByDepartment() {

        String dept = "HR";
        List<Employee> expectedList = Arrays.asList(new Employee(), new Employee());
        EasyMock.expect(employeeServiceMock.getEmployeesByDepartment(dept)).andReturn(expectedList);
        EasyMock.replay(employeeServiceMock);

        List<Employee> result = employeeController.listByDepartment(dept);
        assertEquals(expectedList, result);

        EasyMock.verify(employeeServiceMock);
        EasyMock.reset(employeeServiceMock);
    }

    @Test
    public void testListOrg() {

        List<Organization> expectedOrgs = Arrays.asList(new Organization(), new Organization());
        EasyMock.expect(employeeServiceMock.getAllOrg()).andReturn(expectedOrgs);
        EasyMock.replay(employeeServiceMock);

        List<Organization> result = employeeController.listOrg();
        assertEquals(expectedOrgs, result);

        EasyMock.verify(employeeServiceMock);
        EasyMock.reset(employeeServiceMock);
    }

}
