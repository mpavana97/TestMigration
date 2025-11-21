package com.example.demo.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

import com.example.demo.model.Employee;
import com.example.demo.util.CriteriaUtil;


@Repository("employeeDao")
@Transactional
public class EmployeeDao {

    @Autowired
    private CriteriaUtil jpaCriteriaUtil;

    public void save(Employee emp) {

        jpaCriteriaUtil.persist(emp);
    }

    //    public List<Employee> findByDepartment(String dept) {
    //
    //        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    //        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
    //        Root<Employee> root = query.from(Employee.class);
    //        query.select(root).where(cb.equal(root.get("department"), dept));
    //        return entityManager.createQuery(query).getResultList();
    //    }

    public List<Employee> findByDepartment(String dept) {

        return jpaCriteriaUtil.findByField(Employee.class, "department", dept);
    }

}
