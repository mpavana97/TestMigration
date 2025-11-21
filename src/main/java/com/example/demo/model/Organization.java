/**
 * Copyright (c) 2012 TiVo Inc.
 */

package com.example.demo.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * Hold organization related information
 *
 * @author basabkumar.banerjee
 */
@Entity
@Getter
@Setter
@Table(name = "AC_ORGANIZATION", uniqueConstraints = { @UniqueConstraint(columnNames = { "orgName" }) })
public class Organization implements Serializable {

    private static final long serialVersionUID = 2835965807888298660L;

    @Id
    @GeneratedValue
    private Long organizationId;

    // Hold the organization Name
    private String orgName;

    // Hold the organization contact person FirstName
    private String contactPersonFirstName;

    // Hold the organization contact person LastName
    private String contactPersonLastName;

    // Hold the organization contact person email address
    private String contactPersonEmailAddress;

    // Hold the date format for the organization
    private String dateFormat;

    // Hold the time format for the organization
    private String timeFormat;

    // Hold the time zone type for the organization
    private String timeZone;

    @OneToMany(mappedBy = "organization", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Authorities> orgPrivileges = new HashSet<>();

    // To hold organization is enable or not
    private Boolean enable;

    // Hold the country Name
    private String country;

    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "launchDateTime", column = @Column(name = "startDate")),
        @AttributeOverride(name = "expiryDateTime", column = @Column(name = "endDate")) })
    private LaunchExpiryDate startExpiryDateTime = new LaunchExpiryDate();

    @Embedded
    private AuditFieldsInfo auditFieldsInfo = new AuditFieldsInfo();

    // To hold user selected start date
    @Transient
    private String startDateStr;

    // To hold user selected end date
    @Transient
    private String endDateStr;

    // To hold Organization status for ui
    @Transient
    private String status;

    // Hold selected PrivilegeGroup names for this organization
    @Transient
    private String selectedPrivGroupNames;

    // Holds image is update or not info for history.
    @Transient
    private boolean imageUpdateStatus = false;

    // Holds the tempImageId Which is generated on validation failed of other than image field.
    @Transient
    private Long tempImageId;

    // Holds the privilege group name which are modified.
    @Transient
    private List<String> updatedPrivGroupNames;

    /**
     * Hold the list of time format for audit log history.
     */
    @Transient
    private List<NameValueVO> timeFormatList;

    @Transient
    private List<Role> roleList;

}
