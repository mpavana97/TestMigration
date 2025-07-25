package com.example.demo.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;


@Getter
@Component
public class CriteriaUtil {

    @PersistenceContext
    private EntityManager entityManager;

    public <T> void persist(T entity) {

        entityManager.persist(entity);
    }

    // Save or update a list of entities
    public <T> void saveAll(List<T> entities) {

        for (T entity : entities) {
            entityManager.persist(entity); // or merge(entity) if updates are expected
        }
    }

    // Delete a single entity
    public <T> void delete(T entity) {

        T managedEntity = entityManager.contains(entity) ? entity : entityManager.merge(entity);
        entityManager.remove(managedEntity);
    }

    // Delete a list of entities
    public <T> void deleteAll(List<T> entities) {

        for (T entity : entities) {
            delete(entity);
        }
    }

    public <T> List<T> findByField(Class<T> entityClass, String field, Object value) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> root = cq.from(entityClass);
        cq.where(cb.equal(root.get(field), value));
        return entityManager.createQuery(cq).getResultList();

    }

    //Multiple Criteria or where conditions
    public <T> List<T> findByFields(Class<T> entityClass, Map<String, Object> conditions) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> root = cq.from(entityClass);

        List<Predicate> predicates = new ArrayList<>();
        for (Map.Entry<String, Object> entry : conditions.entrySet()) {
            predicates.add(cb.equal(root.get(entry.getKey()), entry.getValue()));
        }

        cq.where(cb.and(predicates.toArray(new Predicate[0])));
        return entityManager.createQuery(cq).getResultList();

    }

    public <T> List<T> getResultList(CriteriaQuery<T> criteriaQuery) {

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

}
