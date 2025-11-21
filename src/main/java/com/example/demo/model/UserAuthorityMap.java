/**
 * Copyright (c) 2012 TiVo Inc.
 */

package com.example.demo.model;

import java.io.Serializable;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name = "AC_USER_AUTHORITY_MAP", uniqueConstraints = { @UniqueConstraint(columnNames = { "USER_ID", "AUTHORITY_ID" }) })
public class UserAuthorityMap implements Serializable {

    private static final long serialVersionUID = 7689589130358825654L;

    @Id
    @GeneratedValue
    private Long userAuthorityMapId;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User authorisedUser;

    @ManyToOne
    @JoinColumn(name = "AUTHORITY_ID")
    private Authorities authority;

    private Boolean enable = Boolean.TRUE;

    @Embedded
    private AuditFieldsInfo auditFieldsInfo = new AuditFieldsInfo();

}
