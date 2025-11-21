//package com.example.demo.util;
//
//import java.util.List;
//
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.criteria.CriteriaBuilder;
//import jakarta.persistence.criteria.CriteriaQuery;
//import jakarta.persistence.criteria.Predicate;
//import jakarta.persistence.criteria.Root;
//
//
//public class JPACriteriaUtils {
//
//    public static <T> List<T> findByProperty(EntityManager em, Class<T> entityClass, String propertyName, Object value) {
//
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<T> cq = cb.createQuery(entityClass);
//        Root<T> root = cq.from(entityClass);
//
//        cq.select(root).where(cb.equal(root.get(propertyName), value));
//
//        return em.createQuery(cq).getResultList();
//    }
//
//    public static <T> T findSingleByProperty(EntityManager em, Class<T> entityClass, String propertyName, Object value) {
//
//        List<T> results = findByProperty(em, entityClass, propertyName, value);
//        return results.isEmpty() ? null : results.get(0);
//    }
//
//    public static <T> T findById(EntityManager em, Class<T> entityClass, String idField, Object idValue) {
//
//        return findSingleByProperty(em, entityClass, idField, idValue);
//    }
//
//    public static <T> List<T> findAll(EntityManager em, Class<T> entityClass) {
//
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<T> cq = cb.createQuery(entityClass);
//        cq.from(entityClass);
//        return em.createQuery(cq).getResultList();
//    }
//
//    public static <T> List<T> findByPredicate(EntityManager em, Class<T> entityClass, Predicate predicate) {
//
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<T> cq = cb.createQuery(entityClass);
//        Root<T> root = cq.from(entityClass);
//        cq.select(root).where(predicate);
//        return em.createQuery(cq).getResultList();
//    }
//
//}
