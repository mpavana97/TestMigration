/**
 * Copyright (c) 2012 TiVo Inc.
 */

package com.example.demo.model;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * Store auditing information.
 * Use as an embedded object for all domain object
 *
 * @author basabkumar.banerjee
 */
@Embeddable
@ToString
@Setter
@Getter
public class AuditFieldsInfo implements Serializable {

    private static final long serialVersionUID = 5943849134603588919L;

    /**
     * Created By User Name
     */
    private Long createdBy;

    /**
     * Updated By User Name
     */
    private Long updatedBy;

    /**
     * Created Date
     */
    private Date createdDate;

    /**
     * Updated Date
     */
    private Date updatedDate;

}
