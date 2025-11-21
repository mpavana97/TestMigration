/**
 * Copyright (c) 2012 TiVo Inc.
 */

package com.example.demo.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Formula;


/**
 * User information
 *
 * @author basabkumar.banerjee
 */
@Entity
@Getter
@Setter
@Table(name = "AC_USER", uniqueConstraints = { @UniqueConstraint(columnNames = { "userLoginId" }) })
public class User implements Serializable {

    private static final long serialVersionUID = 95855756355590503L;

    @Id
    @GeneratedValue
    private Long userId;

    /**
     * Hold Username
     */
    private String userLoginId;

    /**
     * To Hold is New User or not value
     */
    @Transient
    private boolean isNewUser;

    /**
     * if isLDAP is true user belongs to LDAP
     * if isLDAP is false user belongs to data base.
     */
    private boolean ldapUser = false;

    /**
     * To hold User is enable or not
     */
    private boolean active = true;

    /**
     * hold User password
     */
    private String cryptPassword;

    /**
     * hold User first name
     */
    private String firstName;

    /**
     * hold User last name
     */
    private String lastName;

    /**
     * hold User designation
     */
    private String designation;

    private String phoneNumber;

    private String mobileNumber;

    /**
     * Use for To Display Updated By Name in User Search Page
     */
    @Formula("(select concat(o.first_name,' ',o.last_name) from ac_user o where o.user_id = updated_by)")
    private String updatedByName;

    @Formula("(select concat(o.first_name,' ',o.last_name) from ac_user o where o.user_id = created_by)")
    private String createdByName;

    /**
     * hold User email address
     */
    private String emailAddress;

    private boolean passwordReset = false;

    private Date passwordResetDate;

    private Date lastLogin;

    private String passwordHistory;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "AC_USER_ROLE_MAP", joinColumns = { @JoinColumn(name = "USER_ID") }, inverseJoinColumns = {
        @JoinColumn(name = "ROLE_ID") })
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "authorisedUser", fetch = FetchType.EAGER, orphanRemoval = true)
    @Cascade(value = { org.hibernate.annotations.CascadeType.SAVE_UPDATE })
    private Set<UserAuthorityMap> userAuthorityMaps = new HashSet<>();

    /**
     * hold User from which organization
     */
    @OneToOne
    private Organization organization;

    @Embedded
    private AuditFieldsInfo auditFieldsInfo = new AuditFieldsInfo();

    /**
     * Modified PrivilegeGroup names set
     */
    @Transient
    private Set<String> modifiedPrivGroupName;

    // To hold User status for ui
    @Transient
    private String status;

}
