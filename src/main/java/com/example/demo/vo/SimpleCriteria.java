package com.example.demo.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Selection;


public class SimpleCriteria<T> {

    private final EntityManager entityManager;

    private final CriteriaBuilder cb;

    private final Root<T> root;

    private final CriteriaQuery<?> cq;

    private final List<Predicate> predicates = new ArrayList<>();

    private Selection<?>[] selections = null;

    private Class<?> projectionClass = null;

    // Store joins if needed
    private final List<Join<?, ?>> joins = new ArrayList<>();

    public SimpleCriteria(EntityManager entityManager, Class<T> entityClass) {

        this.entityManager = entityManager;
        this.cb = entityManager.getCriteriaBuilder();
        this.cq = cb.createQuery(); // generic result support
        this.root = cq.from(entityClass);
    }

    public static <T> SimpleCriteria<T> forClass(EntityManager em, Class<T> clazz) {

        return new SimpleCriteria<>(em, clazz);
    }

    // ========== Filters ==========
    public SimpleCriteria<T> eq(String field, Object value) {

        if (value != null) {
            predicates.add(cb.equal(resolvePath(field), value));
        }
        return this;
    }

    public SimpleCriteria<T> like(String field, String pattern) {

        if (pattern != null) {
            predicates.add(cb.like(resolvePath(field), pattern));
        }
        return this;
    }

    public SimpleCriteria<T> in(String field, List<?> values) {

        if (values != null && !values.isEmpty()) {
            predicates.add(resolvePath(field).in(values));
        }
        return this;
    }

    public SimpleCriteria<T> gt(String field, Number value) {

        if (value != null) {
            predicates.add(cb.gt(resolvePath(field), value));
        }
        return this;
    }

    public SimpleCriteria<T> lt(String field, Number value) {

        if (value != null) {
            predicates.add(cb.lt(resolvePath(field), value));
        }
        return this;
    }

    public SimpleCriteria<T> addCustomPredicate(Consumer<PredicateBuilder<T>> consumer) {

        PredicateBuilder<T> builder = new PredicateBuilder<>(cb, root);
        consumer.accept(builder);
        predicates.addAll(builder.getPredicates());
        return this;
    }

    // ========== Projection ==========
    public <R> SimpleCriteria<T> select(Class<R> projectionClass, String... fields) {

        this.projectionClass = projectionClass;
        this.selections = new Selection<?>[fields.length];
        for (int i = 0; i < fields.length; i++) {
            this.selections[i] = resolvePath(fields[i]);
        }
        return this;
    }

    // ========== Joins ==========
    public <X, Y> Join<X, Y> join(String field) {

        Join<X, Y> join = root.join(field, JoinType.LEFT);
        joins.add(join);
        return join;
    }

    @SuppressWarnings("unchecked")
    //    public <R> List<R> getResultList() {
    //
    //        cq.where(cb.and(predicates.toArray(new Predicate[0])));
    //        if (projectionClass != null && selections != null) {
    //            cq.select(cb.construct((Class<R>) projectionClass, selections));
    //        } else {
    //            cq.select(root);
    //        }
    //        return (List<R>) entityManager.createQuery((CriteriaQuery<?>) cq).getResultList();
    //    }
    //
    //    @SuppressWarnings("unchecked")
    //    public <R> R getSingleResult() {
    //
    //        cq.where(cb.and(predicates.toArray(new Predicate[0])));
    //        if (projectionClass != null && selections != null) {
    //            cq.select(cb.construct((Class<R>) projectionClass, selections));
    //        } else {
    //            cq.select(root);
    //        }
    //        return (R) entityManager.createQuery((CriteriaQuery<?>) cq).getSingleResult();
    //    }

    // ========== Utility ==========
    private <Y> Path<Y> resolvePath(String field) {

        if (field.contains(".")) {
            String[] parts = field.split("\\.");
            Path<?> path = root.get(parts[0]);
            for (int i = 1; i < parts.length; i++) {
                path = path.get(parts[i]);
            }
            return (Path<Y>) path;
        } else {
            return root.get(field);
        }
    }

}
