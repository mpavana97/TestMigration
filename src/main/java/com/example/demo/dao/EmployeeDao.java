package com.example.demo.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.example.demo.model.Employee;


@Repository("employeeDao")
@Transactional
public class EmployeeDao {

    private final SessionFactory sessionFactory;

    public EmployeeDao(SessionFactory sessionFactory) {

        this.sessionFactory = sessionFactory;
    }

    public void save(Employee emp) {

        getCurrentSession().persist(emp);
    }

    public List<Employee> findByDepartment(String dept) {

        Session session = getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
        Root<Employee> root = query.from(Employee.class);
        query.select(root).where(cb.equal(root.get("department"), dept));
        return session.createQuery(query).getResultList();
    }

    private Session getCurrentSession() {

        return sessionFactory.getCurrentSession();
    }

}
