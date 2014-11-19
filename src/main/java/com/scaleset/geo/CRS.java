package com.scaleset.geo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CRS {

    private String name;
    private Map<String, Object> properties = new HashMap<String, Object>();

    public CRS() {
    }

    public String getName() {
        return name;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }
}
