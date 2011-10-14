package com.aimprosoft.wavilon.couch;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;


@JsonIgnoreProperties(value = {"id", "allocationDate", "liferayOrganizationId", "revision", "liberationDate"}, ignoreUnknown = true)
public class PhoneModel {
    @JsonProperty("_id")
    private String id;

    @JsonProperty("_rev")
    private String revision;


    @JsonProperty("type")
    private String type;

    @JsonProperty("subtype")
    private String subtype;

    @JsonProperty("country")
    private String country;

    @JsonProperty("locator")
    private String locator;

    @JsonProperty("liferay_organization_id")
    private Long liferayOrganizationId;

    @JsonProperty("allocation_date")
    private Long allocationDate;

    @JsonProperty("liberation_date")
    private String liberationDate;


    private PhoneModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLocator() {
        return locator;
    }

    public void setLocator(String locator) {
        this.locator = locator;
    }

    public Long getLiferayOrganizationId() {
        return liferayOrganizationId;
    }

    public void setLiferayOrganizationId(Long liferayOrganizationId) {
        this.liferayOrganizationId = liferayOrganizationId;
    }

    public Long getAllocationDate() {
        return allocationDate;
    }

    public void setAllocationDate(Long allocationDate) {
        this.allocationDate = allocationDate;
    }

    public String getLiberationDate() {
        return liberationDate;
    }

    public void setLiberationDate(String liberationDate) {
        this.liberationDate = liberationDate;
    }
}