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

import com.example.demo.dto.UserDTO;
import com.example.demo.model.Employee;
import com.example.demo.model.Organization;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.util.CriteriaUtil;
import com.example.demo.vo.SimpleCriteria;


@Repository("accessControlDao")
@Transactional
public class AccessControlDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private CriteriaUtil criteriaUtil;

    public List<UserDTO> searchUsers(Map<String, String> filterMap) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<UserDTO> cq = cb.createQuery(UserDTO.class);
        Root<User> root = cq.from(User.class);

        List<Predicate> predicates = new ArrayList<>();

        if (filterMap.containsKey("roleName")) {
            String roleName = filterMap.get("roleName");
            if (roleName != null && !roleName.isEmpty()) {
                Join<User, Role> roleJoin = root.join("roles", JoinType.LEFT);
                predicates.add(cb.like(roleJoin.get("roleName"), "%" + roleName + "%"));
            }
        }

        if (filterMap.containsKey("orgName")) {
            String orgName = filterMap.get("orgName");
            if (orgName != null && !orgName.isEmpty()) {
                Join<User, Organization> orgJoin = root.join("organization", JoinType.LEFT);
                predicates.add(cb.equal(orgJoin.get("orgName"), orgName));
            }
        }

        cq.select(cb.construct(UserDTO.class, root.get("userId"), root.get("userLoginId")));

        cq.where(cb.and(predicates.toArray(new Predicate[0])));
        cq.orderBy(cb.asc(root.get("userLoginId")));

        return criteriaUtil.getResultList(cq);
    }

    public void search(Employee emp) {

        SimpleCriteria<User> result = SimpleCriteria.forClass(entityManager, User.class).addCustomPredicate(
                p -> p.eq("status", "ACTIVE").like("name", "Harish%").in("role.name", List.of("ADMIN", "USER")).notNull("email"))
            .select(UserDTO.class, "name", "email");
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
