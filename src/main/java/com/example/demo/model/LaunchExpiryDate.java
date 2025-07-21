package com.example.demo.model;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Transient;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class LaunchExpiryDate implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -3317859917702750884L;

    // check the gmt and ui date when launchDateTime is null

    private Date launchDateTime;

    @Transient
    private Date gmtLaunchDateTime;

    @Transient
    private String uiLaunchDateTime;

    private Date expiryDateTime;

    @Transient
    private Date gmtExpiryDateTime;

    @Transient
    private String uiExpiryDateTime;

}
