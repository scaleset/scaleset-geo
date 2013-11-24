package com.scaleset.geo.geojson;

import com.scaleset.geo.Feature;
import com.scaleset.geo.FeatureCollection;
import com.scaleset.geo.FeatureCollectionHandler;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileInputStream;

public class GeoJsonParserTest extends Assert {

    @Test
    public void testParseFeatures() throws Exception {
        FeatureCollectionHandler handler = new FeatureCollectionHandler();
        GeoJsonParser parser = new GeoJsonParser();
        parser.handler(handler);
        parser.parse(getClass().getResourceAsStream("/features.json"));
        FeatureCollection fc = handler.getCollection();
        assertEquals(4, fc.getFeatures().size());

        // Feature Collection bbox
        Envelope bbox = fc.getBbox();
        assertNotNull(bbox);
        assertEquals(-180.0, bbox.getMinX(), 0.00001);
        assertEquals(-90.0, bbox.getMinY(), 0.00001);
        assertEquals(180.0, bbox.getMaxX(), 0.00001);
        assertEquals(90.0, bbox.getMaxY(), 0.00001);

        Feature f1 = fc.getFeatures().get(1);
        Geometry g = f1.getGeometry();
        assertTrue(g instanceof LineString);

        Envelope e = f1.getBbox();
        assertNotNull(e);
    }

}
