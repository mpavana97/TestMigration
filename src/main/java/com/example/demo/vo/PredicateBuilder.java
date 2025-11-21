package com.example.demo.vo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;


public class PredicateBuilder<T> {

    private final CriteriaBuilder cb;

    private final Root<T> root;

    private final List<Predicate> predicates = new ArrayList<>();

    public PredicateBuilder(CriteriaBuilder cb, Root<T> root) {

        this.cb = cb;
        this.root = root;
    }

    public PredicateBuilder<T> eq(String field, Object value) {

        if (value != null) {
            predicates.add(cb.equal(resolvePath(field), value));
        }
        return this;
    }

    public PredicateBuilder<T> like(String field, String pattern) {

        if (pattern != null) {
            predicates.add(cb.like(resolvePath(field), pattern));
        }
        return this;
    }

    public PredicateBuilder<T> in(String field, Collection<?> values) {

        if (values != null && !values.isEmpty()) {
            predicates.add(resolvePath(field).in(values));
        }
        return this;
    }

    public PredicateBuilder<T> gt(String field, Number value) {

        if (value != null) {
            predicates.add(cb.gt(resolvePath(field), value));
        }
        return this;
    }

    public PredicateBuilder<T> lt(String field, Number value) {

        if (value != null) {
            predicates.add(cb.lt(resolvePath(field), value));
        }
        return this;
    }

    public PredicateBuilder<T> notNull(String field) {

        predicates.add(cb.isNotNull(resolvePath(field)));
        return this;
    }

    //    public PredicateBuilder<T> between(String field, Comparable<?> start, Comparable<?> end) {
    //
    //        if (start != null && end != null) {
    //            predicates.add(cb.between(resolvePath(field), start, end));
    //        }
    //        return this;
    //    }

    public List<Predicate> getPredicates() {

        return predicates;
    }

    @SuppressWarnings("unchecked")
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