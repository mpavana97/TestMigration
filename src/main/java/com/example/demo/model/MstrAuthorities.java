/**
 * Copyright (c) 2012 TiVo Inc.
 */

package com.example.demo.model;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Entity
@Table(name = "SC_MSTR_AUTHORITIES")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MstrAuthorities {

    @Id
    @GeneratedValue
    private Long masAuthorityId;

    private String authName;

    private String authValue;

    private String authPrivilegeGroup;

    private Boolean enable = Boolean.TRUE;

    private Boolean isAssignable = Boolean.TRUE;

    @Transient
    private String status;

    @Embedded
    private AuditFieldsInfo auditFieldsInfo = new AuditFieldsInfo();

}
