package com.example.demo.util;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.hibernate.query.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import org.hibernate.SessionFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.demo.vo.SimpleCriteria;


public abstract class GenericDAO<T, I extends Serializable> extends HibernateDaoSupport {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    private Class<T> persistentClass;

    private JdbcTemplate jdbcTemplate;

    protected GenericDAO() {

        this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * Create a new persistent or save the existing instance of the Object The mapping for the class
     * should exist
     *
     * @param t T
     */
    public void saveOrUpdate(T t) {

        currentSession().saveOrUpdate(t);
    }

    /**
     * Creates new persistent or save the existing instances of the Objects
     *
     * @param list List
     */
    public void saveOrUpdateAll(List<? extends T> list) {

        for (T t : list) {
            saveOrUpdate(t);
        }
    }

    /**
     * Create a new persistent or save the existing instance of the Object The mapping for the class
     * should exists
     *
     * @param t T
     * @return I
     */
    public I save(T t) {

        return (I) currentSession().save(t);
    }

    /**
     * save the existing instance of the Object The mapping for the class
     * should exist
     *
     * @param t T
     */
    public void update(T t) {

        currentSession().update(t);
    }

    /**
     * Delete a persistent entity
     *
     * @param t T
     */
    public void delete(T t) {

        currentSession().delete(t);
    }

    /**
     * Delete a persistent entity by id
     *
     * @param id I
     */
    public void delete(I id) {

        delete(findById(id));
    }

    /**
     * Delete all persistent entities
     *
     * @param objectList List
     */
    public <E> void deleteAll(List<E> objectList) {

        assert getHibernateTemplate() != null;
        getHibernateTemplate().deleteAll(objectList);
    }

    /**
     * Find the enity of a specified class with the specified id
     *
     * @param id I
     * @return the entity instance
     */
    public T findById(I id) {

        return currentSession().load(persistentClass, id);
    }

    /**
     * Get the enity of a specified class with the specified id (returns an actual object rather than proxy)
     *
     * @param id I
     * @return the entity instance
     */
    public T getById(I id) {

        return currentSession().get(persistentClass, id);
    }

    /**
     * Executes HQL Query
     * @param query {@link String} HQL Query
     * @return {@List} Query result
     */
    public List findByQuery(final String query) {

        return currentSession().createQuery(query).list();
    }

    /**
     * Executes Native Query
     * @param query {@link String} Native SQL Query
     * @return {@List} Query result
     */
    public List findBySQLQuery(final String query) {

        //Called by Health Check. Not need to log in Splunk
        return currentSession().createSQLQuery(query).list();
    }

    public List<T> findAll() {
        String hql = "from " + persistentClass.getSimpleName();
        Query<T> q = currentSession().createQuery(hql, persistentClass);
        return q.getResultList();
    }

    private void getLogType() {

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

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    public void initiateSessionFactory(SessionFactory sessionFactory) {

        setSessionFactory(sessionFactory);
    }

    public <X> SimpleCriteria<X> criteria(Class<X> clazz) {

        return new SimpleCriteria<>(currentSession(), clazz);
    }

}
