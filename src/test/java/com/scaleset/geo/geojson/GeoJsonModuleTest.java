package com.scaleset.geo.geojson;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scaleset.geo.Feature;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class GeoJsonModuleTest extends Assert {

    private GeometryFactory factory = new GeometryFactory();
    private ObjectMapper mapper = new ObjectMapper().registerModule(new GeoJsonModule());

    @Test
    public void testSerializePoint() throws IOException {
        Point point = factory.createPoint(new Coordinate(-78, 39));
        String json = mapper.writeValueAsString(point);
        assertEquals("{\"type\":\"Point\",\"coordinates\":[-78.0,39.0]}", json);
        Point p2 = mapper.readValue(json, Point.class);
        assertEquals(point, p2);
    }

    @Test
    public void testSimpleFeature() throws IOException {
        Point point = factory.createPoint(new Coordinate(-78, 39));
        Feature feature = new Feature();
        feature.setGeometry(point);
        feature.getProperties().put("title", "Simple Point Feature");
        String json = mapper.writeValueAsString(feature);
    }

    @Test
    public void testDeserializePoint() throws IOException {
        String json = "{\"point\": {\"type\":\"Point\",\"coordinates\":[-78.0,39.0]}}";
        HasGeometry hasGeometry = mapper.readValue(json, HasGeometry.class);
        assertNotNull(hasGeometry);
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    static class HasGeometry {
        public Point point;
    }
}
