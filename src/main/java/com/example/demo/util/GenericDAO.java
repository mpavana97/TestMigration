package com.example.demo.util;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.vo.SimpleCriteria;

@Repository
public abstract class GenericDAO<T, I extends Serializable> {

    private Class<T> persistentClass;

    @Autowired
    private SessionFactory sessionFactory;

    private JdbcTemplate jdbcTemplate;

    protected GenericDAO() {
        this.persistentClass = (Class<T>)
            ((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
    }

    protected Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    public void saveOrUpdate(T entity) {
        currentSession().saveOrUpdate(entity);
    }

    public I save(T entity) {
        return (I) currentSession().save(entity);
    }

    public void update(T entity) {
        currentSession().merge(entity);
    }

    public void delete(T entity) {
        currentSession().remove(entity);
    }

    public void delete(I id) {
        delete(findById(id));
    }

    public T findById(I id) {
        return currentSession().find(persistentClass, id);
    }

    public T getById(I id) {
        return currentSession().find(persistentClass, id);
    }

    public List findByQuery(String hql) {
        return currentSession().createQuery(hql).list();
    }

    public List findBySQLQuery(String sql) {
        return currentSession().createNativeQuery(sql).list();
    }

    public List<T> findAll() {
        String hql = "from " + persistentClass.getSimpleName();
        Query<T> q = currentSession().createQuery(hql, persistentClass);
        return q.getResultList();
    }

    public Class<? extends T> getPersistentClass() {
        return persistentClass;
    }

    public void setPersistentClass(Class<T> clazz) {
        this.persistentClass = clazz;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Replaces HibernateDaoSupport.setSessionFactory()
    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public <X> SimpleCriteria<X> criteria(Class<X> clazz) {
        return new SimpleCriteria<>(currentSession(), clazz);
    }
}
