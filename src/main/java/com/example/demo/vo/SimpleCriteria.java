package com.example.demo.vo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import jakarta.persistence.Parameter;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;

import lombok.Getter;


@Getter
public class SimpleCriteria<T> {

    private final Session session;

    private final CriteriaBuilder cb;

    private final CriteriaQuery<T> cq;

    private final Root<T> root;

    private final Map<String, Join<?, ?>> joinCache = new HashMap<>();

    private final List<Predicate> predicates = new ArrayList<>();

    private final Map<ParameterExpression<?>, Object> parameters = new LinkedHashMap<>();

    private final Class<T> entityClass;

    public SimpleCriteria(Session session, Class<T> entityClass) {

        this.session = session;
        this.entityClass = entityClass;
        this.cb = session.getCriteriaBuilder();
        this.cq = cb.createQuery(entityClass);
        this.root = cq.from(entityClass);
    }

    public <Y> SimpleCriteria<T> eq(String field, Y value) {

        if (value != null) {
            ParameterExpression<Y> param = cb.parameter((Class<Y>) value.getClass(), field + "_eq");
            predicates.add(cb.equal(getPath(field), param));
            parameters.put(param, value);
        }
        return this;
    }

    public <Y> SimpleCriteria<T> ne(String field, Y value) {

        if (value != null) {
            ParameterExpression<Y> param = cb.parameter((Class<Y>) value.getClass(), field + "_ne");
            predicates.add(cb.notEqual(getPath(field), param));
            parameters.put(param, value);
        }
        return this;
    }

    public SimpleCriteria<T> like(String field, String pattern) {

        if (pattern != null && !pattern.isEmpty()) {
            ParameterExpression<String> param = cb.parameter(String.class, field + "_like");
            predicates.add(cb.like(cb.lower(root.get(field)), param));
            parameters.put(param, "%" + pattern.toLowerCase() + "%");
        }
        return this;
    }

    public <Y extends Comparable<? super Y>> SimpleCriteria<T> gt(String field, Y value) {

        if (value != null) {
            ParameterExpression<Y> param = cb.parameter((Class<Y>) value.getClass(), field + "_gt");
            predicates.add(cb.greaterThan(root.get(field), param)); //Chaining might not work her
            parameters.put(param, value);
        }
        return this;
    }

    public <Y extends Comparable<? super Y>> SimpleCriteria<T> ge(String field, Y value) {

        if (value != null) {
            ParameterExpression<Y> param = cb.parameter((Class<Y>) value.getClass(), field + "_ge");
            predicates.add(cb.greaterThanOrEqualTo(root.get(field), param));
            parameters.put(param, value);
        }
        return this;
    }

    public <Y extends Comparable<? super Y>> SimpleCriteria<T> lt(String field, Y value) {

        if (value != null) {
            ParameterExpression<Y> param = cb.parameter((Class<Y>) value.getClass(), field + "_lt");
            predicates.add(cb.lessThan(root.get(field), param));
            parameters.put(param, value);
        }
        return this;
    }

    public <Y extends Comparable<? super Y>> SimpleCriteria<T> le(String field, Y value) {

        if (value != null) {
            ParameterExpression<Y> param = cb.parameter((Class<Y>) value.getClass(), field + "_le");
            predicates.add(cb.lessThanOrEqualTo(root.get(field), param));
            parameters.put(param, value);
        }
        return this;
    }

    public SimpleCriteria<T> isNotNull(String field) {

        predicates.add(cb.isNotNull(getPath(field)));
        return this;
    }

    public SimpleCriteria<T> isNull(String field) {

        predicates.add(cb.isNull(getPath(field)));
        return this;
    }

    public SimpleCriteria<T> in(String field, Collection<?> values) {

        if (values != null && !values.isEmpty()) {

            Path<?> path = getPath(field);
            CriteriaBuilder.In<Object> inClause = cb.in(path);

            int idx = 0;
            for (Object v : values) {
                ParameterExpression<Object> param = cb.parameter((Class<Object>) v.getClass(), field + "_in_" + idx++);
                inClause.value(param);
                parameters.put(param, v);
            }

            predicates.add(inClause);
        }
        return this;
    }

    public SimpleCriteria<T> custom(Consumer<PredicateBuilder<T>> consumer) {

        PredicateBuilder<T> builder = new PredicateBuilder<>(cb, root);
        consumer.accept(builder);
        predicates.addAll(builder.getPredicates());
        return this;
    }

    public SimpleCriteria<T> orderAsc(String field) {

        cq.orderBy(cb.asc(getPath(field)));
        return this;
    }

    public SimpleCriteria<T> orderDesc(String field) {

        cq.orderBy(cb.desc(getPath(field)));
        return this;
    }

    public SimpleCriteria<T> distinctRoot() {

        cq.distinct(true);
        return this;
    }

    public SimpleCriteria<T> distinct(String field) {

        cq.select(root.get(field)).distinct(true);
        return this;
    }

    public List<T> list() {

        cq.where(predicates.toArray(new Predicate[0]));
        Query<T> query = session.createQuery(cq);
        bindParams(query);
        return query.getResultList();
    }

    public T uniqueResult() {

        cq.where(predicates.toArray(new Predicate[0]));
        Query<T> query = session.createQuery(cq);
        bindParams(query);
        return query.uniqueResult();
    }

    @SuppressWarnings("unchecked")
    public <R> List<R> projectList(Class<R> resultType) {

        cq.where(predicates.toArray(new Predicate[0]));
        Query<R> query = (Query<R>) session.createQuery((CriteriaQuery<?>) cq);
        bindParams(query);
        return query.getResultList();
    }

    public long count() {

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<T> countRoot = countQuery.from(entityClass);
        countQuery.select(cb.count(countRoot));
        countQuery.where(predicates.toArray(new Predicate[0]));

        Query<Long> query = session.createQuery(countQuery);
        bindParams(query);

        return query.getSingleResult();
    }

    //    private void bindParams(Query<?> query) {
    //
    //        parameters.forEach(query::setParameter);
    //    }
    // To make hibernate 5.x compatible else use above commented code for hibernate 6.x
    private void bindParams(Query<?> query) {

        for (Map.Entry<ParameterExpression<?>, Object> entry : parameters.entrySet()) {
            ParameterExpression<?> param = entry.getKey();
            Object value = entry.getValue();

            // If value is a temporal type, bind explicitly using TemporalType
            if (value instanceof java.util.Date) {
                query.setParameter((Parameter) param, (java.util.Date) value, jakarta.persistence.TemporalType.TIMESTAMP);
            } else if (value instanceof java.time.LocalDateTime) {
                query.setParameter((Parameter) param, (java.time.LocalDateTime) value);
            } else if (value instanceof java.time.LocalDate) {
                query.setParameter((Parameter) param, (java.time.LocalDate) value);
            } else if (value instanceof java.time.Instant) {
                query.setParameter((Parameter) param, (java.time.Instant) value);
            } else {
                // Safe for String, Long, Integer, Enum, Boolean, etc.
                query.setParameter((Parameter) param, value);
            }
        }

    }

    // To handle chaining(originAd.id)
    private Path<?> getPath(String field) {

        if (!field.contains(".")) {
            return root.get(field);
        }

        String[] parts = field.split("\\.");
        Path<?> path = root;

        for (int i = 0; i < parts.length; i++) {

            String part = parts[i];

            // Last piece → actual field
            if (i == parts.length - 1) {
                return path.get(part);
            }

            // Intermediate piece → JOIN needed
            Join<?, ?> join = joinCache.get(part);

            if (join == null) {
                // Hibernate 5.x and 6.x both support root.join / join.join
                if (path instanceof Root) {
                    join = ((Root<?>) path).join(part);
                } else if (path instanceof Join) {
                    join = ((Join<?, ?>) path).join(part);
                } else {
                    throw new IllegalArgumentException("Cannot join on path type: " + path.getClass() + " for field " + part);
                }

                joinCache.put(part, join);
            }

            path = join; // move deeper
        }

        throw new IllegalArgumentException("Invalid path: " + field);
    }

}
