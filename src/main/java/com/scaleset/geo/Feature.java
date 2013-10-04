package com.scaleset.geo;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scaleset.geo.geojson.GeometryDeserializer;
import com.scaleset.geo.geojson.GeometrySerializer;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

public class Feature {

    private final static GeometryFactory factory = new GeometryFactory();

    @JsonDeserialize(using = GeometryDeserializer.class)
    @JsonSerialize(using = GeometrySerializer.class)
    private Geometry geometry;

    private String id;

    private Map<String, Object> properties = new HashMap<String, Object>();

    private String type = "Feature";

    public Object get(String key) {
        return properties.get(key);
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public String getId() {
        return id;
    }

    @JsonAnyGetter
    public Map<String, Object> getProperties() {
        return properties;
    }

    public String getType() {
        return type;
    }

    @JsonAnySetter
    public Object put(String key, Object value) {
        return properties.put(key, value);
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public void geometry(Coordinate coordinate) {
        this.geometry = factory.createPoint(coordinate);
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public void setType(String type) {
        this.type = type;
    }

}
