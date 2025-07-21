/**
 * Copyright (c) 2012 TiVo Inc.
 */

package com.example.demo.model;

import lombok.Getter;
import lombok.Setter;


/**
 * Common value object to hold name/value pair
 *
 * @author vinay.mundra
 */
@Getter
@Setter
public class NameValueVO {

    private String name;

    private String value;

    private String description;

    /**
     * Default constructor.
     */
    public NameValueVO() {

    }

    public NameValueVO(String nameValue) {

        this.name = nameValue;
        this.value = nameValue;
    }

    /**
     * @param name
     * @param value
     */
    public NameValueVO(String name, String value) {

        this.name = name;
        this.value = value;
    }

}