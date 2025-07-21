/**
 *
 */

package com.example.demo.dao;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import org.hibernate.Hibernate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;

import com.example.demo.model.Authorities;
import com.example.demo.model.Role;
import com.example.demo.model.User;


/**
 * @author ppkumar
 */

@Repository
public class RoleDao {

    @PersistenceContext
    private EntityManager entityManager;

    public Long saveRole(Role role) throws Exception {

        try {
            if (role.getRoleId() == null) {
                entityManager.persist(role);
            } else {
                role = entityManager.merge(role);
            }
            return role.getRoleId();
        } catch (PersistenceException e) {
            throw new Exception("error.admin.role.name.exists");
        }
    }

    public Role getRoleByID(Long roleId) {

        Role role = entityManager.find(Role.class, roleId);
        if (role != null) {
            Hibernate.initialize(role.getAuthorities());
            Hibernate.initialize(role.getUsers());
            for (User user : role.getUsers()) {
                Hibernate.initialize(user.getRoles());
            }
        }
        return role;
    }

    public Role getOrgDefaultRole(Long orgId) {

        TypedQuery<Role> query = entityManager.createQuery(
            "SELECT r FROM Role r WHERE r.organization.organizationId = :orgId AND r.defaultRole = true", Role.class);
        query.setParameter("orgId", orgId);
        List<Role> roles = query.getResultList();
        return roles.isEmpty() ? null : roles.get(0);
    }

    public Role getRoleByRoleNameAndOrg(String roleName, String orgName) {

        TypedQuery<Role> query =
            entityManager.createQuery("SELECT r FROM Role r WHERE r.roleName = :roleName AND r.organization.orgName = :orgName",
                Role.class);
        query.setParameter("roleName", roleName);
        query.setParameter("orgName", orgName);
        List<Role> roles = query.getResultList();
        return roles.isEmpty() ? null : roles.get(0);
    }

    public List<Role> getRolesByOrganization(Long orgId, boolean onlyActive) {

        String hql = "SELECT r FROM Role r WHERE r.organization.organizationId = :orgId";
        if (onlyActive) {
            hql += " AND r.enable = true";
        }
        hql += " ORDER BY r.roleName";
        TypedQuery<Role> query = entityManager.createQuery(hql, Role.class);
        query.setParameter("orgId", orgId);
        List<Role> roles = query.getResultList();
        for (Role role : roles) {
            Hibernate.initialize(role.getAuthorities());
        }
        return roles;
    }

    public List<Role> getSearchRolesList(Map<String, String> filterParams) {

        StringBuilder jpql = new StringBuilder("SELECT DISTINCT r FROM Role r WHERE r.enable = true");
        Map<String, Object> params = new HashMap<>();

        if (filterParams.containsKey("roleName")) {
            jpql.append(" AND r.roleName LIKE :roleName");
            params.put("roleName", "%" + filterParams.get("roleName") + "%");
        }

        if (filterParams.containsKey("organizationId")) {
            jpql.append(" AND r.organization.organizationId = :orgId");
            params.put("orgId", Long.valueOf(filterParams.get("organizationId")));
        }

        TypedQuery<Role> query = entityManager.createQuery(jpql.toString(), Role.class);
        params.forEach(query::setParameter);

        List<Role> roles = query.getResultList();
        for (Role role : roles) {
            Hibernate.initialize(role.getAuthorities());
        }
        return roles;
    }

    public void getPrivGroupVOForRoles(String[] roleNames, Long orgId, boolean onlyAssignable) {

        String jpql = """
            SELECT DISTINCT a FROM Authorities a
            JOIN a.roles r
            JOIN a.mstrAuthority m
            WHERE a.enable = true AND r.roleName IN :roleNames
            AND a.organization.organizationId = :orgId AND m.enable = true
            """ + (onlyAssignable ? " AND m.isAssignable = true" : "") + """
            ORDER BY m.authPrivilegeGroup, m.authValue
            """;

        TypedQuery<Authorities> query = entityManager.createQuery(jpql, Authorities.class);
        query.setParameter("roleNames", Arrays.asList(roleNames));
        query.setParameter("orgId", orgId);
        List<Authorities> authorities = query.getResultList();

    }

}

