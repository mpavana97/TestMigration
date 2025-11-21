package com.example.demo.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.Formula;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Entity
@Table(name = "AC_ROLE", uniqueConstraints = { @UniqueConstraint(columnNames = { "roleName", "organization" }) })
public class Role implements Serializable {

    private static final long serialVersionUID = -7071581009294565011L;

    @Id
    @GeneratedValue
    private Long roleId;

    /**
     * Holds the role name.
     */
    private String roleName;

    /**
     * Holds the role discription.
     */
    private String roleDescription;

    /**
     * Holds the role updated user full name
     */
    @Formula("(select concat(o.first_name,' ',o.last_name) from ac_user o where o.user_id = updated_by)")
    private String updatedByName;

    /**
     * Holds the users associated to this role
     */
    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();

    /**
     * Holds the authorities associated to this role
     */
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "AC_ROLE_AUTHORITY_MAP", joinColumns = { @JoinColumn(name = "ROLE_ID") }, inverseJoinColumns = {
        @JoinColumn(name = "AUTHORITY_ID") })
    private Set<Authorities> authorities = new HashSet<>();

    /**
     * Hold boolean value to identify role is defaultRole or not
     */
    private Boolean defaultRole = Boolean.FALSE;

    /**
     * Holds the enable filed value to check role is enable or not
     */
    private Boolean enable = Boolean.TRUE;

    /**
     * Holds the organization for which this role belong to
     */
    @OneToOne
    private Organization organization;

    @Embedded
    private AuditFieldsInfo auditFieldsInfo = new AuditFieldsInfo();

    /**
     * Selected permission names for history updated purpose
     */
    @Transient
    private String selectedPrivGroupNames;

    /**
     * Holds the modified permission's permissionset names for history update purpose
     */
    @Transient
    private List<String> updatedPrivGroupNames;

    /**
     * Holds the comma separated list of permission sets revoked.
     */
    @Transient
    private String privGroupsRevoked;

}