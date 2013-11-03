package com.scaleset.geo.geojson;

import com.scaleset.geo.FeatureCollection;
import com.scaleset.geo.FeatureCollectionHandler;
import com.vividsolutions.jts.geom.Envelope;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileInputStream;

public class GeoJsonParserTest extends Assert {

    @Test
    public void testParseFeatures() throws Exception {
        FeatureCollectionHandler handler = new FeatureCollectionHandler();
        GeoJsonParser parser = new GeoJsonParser();
        parser.handler(handler);
        parser.parse(new FileInputStream("src/test/resources/features.json"));
        FeatureCollection fc = handler.getCollection();
        System.out.println(fc.getFeatures().size());

        // bbox
        Envelope bbox = fc.getBbox();
        assertNotNull(bbox);
        assertEquals(-180.0, bbox.getMinX(), 0.00001);
        assertEquals(-90.0, bbox.getMinY(), 0.00001);
        assertEquals(180.0, bbox.getMaxX(), 0.00001);
        assertEquals(90.0, bbox.getMaxY(), 0.00001);
    }

}
