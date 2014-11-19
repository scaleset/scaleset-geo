package com.scaleset.geo;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Feature {

    private final static GeometryFactory factory = new GeometryFactory();
    private Envelope bbox;
    private CRS crs;
    private Geometry geometry;
    private String id;
    private Map<String, Object> properties = new HashMap<String, Object>();
    private String type = "Feature";

    public void geometry(Coordinate coordinate) {
        this.geometry = factory.createPoint(coordinate);
    }

    public Object get(String key) {
        return properties.get(key);
    }

    public Envelope getBbox() {
        return bbox;
    }

    public void setBbox(Envelope bbox) {
        this.bbox = bbox;
    }

    public CRS getCrs() {
        return crs;
    }

    public void setCrs(CRS crs) {
        this.crs = crs;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object put(String key, Object value) {
        return properties.put(key, value);
    }

}
