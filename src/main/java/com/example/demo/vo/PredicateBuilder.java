package com.example.demo.vo;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.CriteriaBuilder;
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
            predicates.add(cb.equal(root.get(field), value));
        }
        return this;
    }

    public PredicateBuilder<T> ne(String field, Object value) {

        if (value != null) {
            predicates.add(cb.notEqual(root.get(field), value));
        }
        return this;
    }

    public PredicateBuilder<T> like(String field, String pattern) {
        if (pattern != null && !pattern.isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get(field)), "%" + pattern.toLowerCase() + "%"));
        }
        return this;
    }

    public PredicateBuilder<T> or(Predicate... p) {

        predicates.add(cb.or(p));
        return this;
    }

    public List<Predicate> getPredicates() {
        return predicates;
    }

    public CriteriaBuilder getCriteriaBuilder() {

        return cb;
    }

    public Root<T> getRoot() {

        return root;
    }
}

