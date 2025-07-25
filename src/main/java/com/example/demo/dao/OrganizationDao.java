/**
 *
 */

package com.example.demo.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import org.hibernate.exception.ConstraintViolationException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

import com.example.demo.model.Organization;


/**
 * @author ppkumar
 */

@Repository("organizationDao")
@Transactional
public class OrganizationDao {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Organization> getAllOrganization(boolean onlyActive) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Organization> cq = cb.createQuery(Organization.class);
        Root<Organization> root = cq.from(Organization.class);

        Predicate predicate = cb.notEqual(root.get("orgName"), "System");
        if (onlyActive) {
            predicate = cb.and(predicate, cb.equal(root.get("enable"), true));
        }

        cq.where(predicate);
        cq.orderBy(cb.asc(root.get("orgName")));

        return entityManager.createQuery(cq).getResultList();

    }

    public Organization fetchOrgById(Long orgId) {

        return entityManager.find(Organization.class, orgId);
    }

    public Long saveOrganization(Organization organization) throws Exception {

        try {
            if (organization.getOrganizationId() == null) {
                entityManager.persist(organization);
            } else {
                entityManager.merge(organization);
            }
            entityManager.flush();
            return organization.getOrganizationId();
        } catch (ConstraintViolationException e) {
            throw e;
        }
    }

    public Organization getOrganizationById(Long orgId) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Organization> cq = cb.createQuery(Organization.class);
        Root<Organization> root = cq.from(Organization.class);
        cq.where(cb.equal(root.get("organizationId"), orgId));
        List<Organization> result = entityManager.createQuery(cq).getResultList();
        return result.isEmpty() ? null : result.get(0);
    }

    public List<String> getCountryNameList() {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> cq = cb.createQuery(String.class);
        Root<Organization> root = cq.from(Organization.class); // Should be Country.class, adjust if needed
        cq.select(root.get("countryName"));
        cq.orderBy(cb.asc(root.get("countryName")));
        return entityManager.createQuery(cq).getResultList();
    }

    public Integer getOrganizationAssociatedUserCount(Long orgId) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<?> root = cq.from(Object.class); // Should be User.class, adjust if needed
        cq.select(cb.count(root));
        cq.where(cb.equal(root.get("organization").get("organizationId"), orgId));
        Long count = entityManager.createQuery(cq).getSingleResult();
        return count.intValue();
    }

    public Organization getOrganizationByExactCriteria(String criteriaName, String criteriaValue) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Organization> cq = cb.createQuery(Organization.class);
        Root<Organization> root = cq.from(Organization.class);
        cq.where(cb.equal(root.get(criteriaName), criteriaValue));
        List<Organization> result = entityManager.createQuery(cq).getResultList();
        return result.isEmpty() ? null : result.get(0);
    }

    public String getOrganizationNameByOrgId(Long orgId) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> cq = cb.createQuery(String.class);
        Root<Organization> root = cq.from(Organization.class);
        cq.select(root.get("orgName"));
        cq.where(cb.equal(root.get("organizationId"), orgId));
        List<String> result = entityManager.createQuery(cq).getResultList();
        return result.isEmpty() ? null : result.get(0);
    }

}
