/**
 * Copyright (c) 2012 TiVo Inc.
 */

package com.example.demo.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * Keep the authorities
 *
 * @author basabkumar.banerjee
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "SC_AUTHORITIES")
public class Authorities implements Serializable {

    private static final long serialVersionUID = 3657781528922224313L;

    @Id
    @GeneratedValue
    private Long authorityId;

    @OneToOne
    private MstrAuthorities mstrAuthority;

    @ManyToMany(mappedBy = "authorities")
    private Set<Role> roles = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    private Boolean enable = Boolean.TRUE;

    @Embedded
    private AuditFieldsInfo auditFieldsInfo;

}
