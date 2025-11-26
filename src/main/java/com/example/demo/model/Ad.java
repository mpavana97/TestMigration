package com.example.demo.model;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "AD")
@Getter
@Setter
public class Ad implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "ADVERTISEMENT_ID")
    private Long advertisementId;

    private String uiElementId;

    private String adType;

    private Long environmentId;

    private String displayName;

    private String displayArea;

    private Integer displayRank;

    private String deviceType;

    private String creativeName;

    private String advertiserName;

    private String internalTitle;

    private String description;

    private String shortDescription;

    private Date startTime;

    private Date endTime;

    @Transient
    private String startTimeStr;

    @Transient
    private String endTimeStr;

    private String stationId;

    private String action;

    private String trailerUrl;

    private String originAdUiElementId;

    private String targetType;

    private String state;

    public Ad() {

    }

    @Override
    public String toString() {

        return ReflectionToStringBuilder.toString(this);
    }

}

