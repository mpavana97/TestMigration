package com.example.demo.dao;

import java.util.List;

import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import lombok.Setter;

import com.example.demo.model.Employee;


@Setter
@Repository("employeeDao")
public class EmployeeDao {

    private HibernateTemplate hibernateTemplate;

    public void save(Employee emp) {
        hibernateTemplate.save(emp);
    }

    public List<Employee> findByDepartment(String dept) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Employee.class);
        criteria.add(Restrictions.eq("department", dept));
        return (List<Employee>) hibernateTemplate.findByCriteria(criteria);
    }
}