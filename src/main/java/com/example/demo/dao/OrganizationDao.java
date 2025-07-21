/**
 *
 */

package com.example.demo.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import com.example.demo.model.Organization;


/**
 * @author ppkumar
 */

@Repository("organizationDao")
@SuppressWarnings("unchecked")
public class OrganizationDao {

    @Repository("organizationDao")
    public class OrganizationDaoImpl {

        @PersistenceContext
        private EntityManager entityManager;

        public List<Organization> getAllOrganization(boolean onlyActive) {

            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Organization> cq = cb.createQuery(Organization.class);
            Root<Organization> root = cq.from(Organization.class);

            List<Predicate> predicates = new ArrayList<>();
            if (onlyActive) {
                predicates.add(cb.equal(root.get("enable"), true));
            }

            predicates.add(cb.notEqual(root.get("orgName"), "System"));
            cq.where(predicates.toArray(new Predicate[0]));
            cq.orderBy(cb.asc(root.get("orgName")));

            List<Organization> resultList = entityManager.createQuery(cq).getResultList();
            return resultList;
        }

        public Organization fetchOrgById(Long orgId) {

            return entityManager.find(Organization.class, orgId);
        }

        public Long saveOrganization(Organization organization) throws Exception {

            try {
                if (organization.getOrganizationId() == null) {
                    entityManager.persist(organization);
                } else {
                    organization = entityManager.merge(organization);
                }
                entityManager.flush();
                return organization.getOrganizationId();
            } catch (PersistenceException e) {
                return null; // never reached
            }
        }

        public Organization getOrganizationById(Long orgId) {

            Organization org = entityManager.find(Organization.class, orgId);
            if (org != null) {
                // Force load of LAZY collections
                org.getOrgPrivileges().size();
            }
            return org;
        }

        public Integer getOrganizationAssociatedUserCount(Long orgId) {

            Long count = entityManager.createQuery("SELECT COUNT(u) FROM User u WHERE u.organization.organizationId = :orgId",
                Long.class).setParameter("orgId", orgId).getSingleResult();
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

    }

}
