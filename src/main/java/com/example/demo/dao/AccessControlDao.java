package com.example.demo.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import org.hibernate.Hibernate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

import com.example.demo.model.Organization;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.util.CriteriaUtil;


@Repository("accessControlDao")
@Transactional
public class AccessControlDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private CriteriaUtil criteriaUtil;

    public List<User> searchUsers(Map<String, String> filterMap) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);
        root.fetch("userAuthorityMaps", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();

        //Tuple

        //Check if there s something like Select abc from table //TO-DO

        //        cq.multiselect(root.get("userLoginId"), root.get("email")); // Example projection
        //
        //        List<Object[]> results = entityManager.createQuery(cq).getResultList();
        //        for (Object[] row : results) {
        //            String userLoginId = (String) row[0];
        //            String email = (String) row[1];
        //            // Process values manually
        //        }

        //Multipselect

        //        CriteriaQuery<Tuple> tupleQuery = cb.createTupleQuery();
        //        Root<User> root = tupleQuery.from(User.class);
        //
        //        tupleQuery.multiselect(
        //            root.get("userLoginId").alias("login"),
        //            root.get("email").alias("mail")
        //        );
        //
        //        List<Tuple> tuples = entityManager.createQuery(tupleQuery).getResultList();
        //        for (Tuple tuple : tuples) {
        //            String login = tuple.get("login", String.class);
        //            String email = tuple.get("mail", String.class);
        //        }

        if (filterMap.containsKey("roleName")) {
            String roleName = filterMap.get("roleName");
            if (roleName != null && !roleName.isEmpty()) {
                Join<User, Role> roleJoin = root.join("roles", JoinType.LEFT);
                predicates.add(cb.like(roleJoin.get("roleName"), "%" + roleName + "%"));
            }
        }

        //Joins
        if (filterMap.containsKey("orgName")) {
            String orgName = filterMap.get("orgName");
            if (orgName != null && !orgName.isEmpty()) {
                Join<User, Organization> orgJoin = root.join("organization", JoinType.LEFT);
                predicates.add(cb.equal(orgJoin.get("orgName"), orgName));
            }
        }

        cq.select(root).where(cb.and(predicates.toArray(new Predicate[0])));

        //Sorting by userLoginId
        cq.orderBy(cb.asc(root.get("userLoginId")));

        return criteriaUtil.getResultList(cq);
    }

    public User getUserByUserId(Long userId) {

        List<User> userList = criteriaUtil.findByField(User.class, "userId", userId);

        if (!userList.isEmpty()) {
            User user = userList.get(0);
            Hibernate.initialize(user.getRoles());
            for (Role role : user.getRoles()) {
                Hibernate.initialize(role.getAuthorities());
            }

            return user;
        }
        return null;
    }

}
