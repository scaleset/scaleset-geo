package com.scaleset.geo.geojson;

import java.io.FileInputStream;
import java.util.zip.GZIPInputStream;

import com.scaleset.geo.Feature;
import com.vividsolutions.jts.geom.Point;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.scaleset.geo.FeatureCollection;
import com.scaleset.geo.FeatureCollectionHandler;
import com.scaleset.geo.geocsv.GeoCsvParser;

public class GeoCsvTest extends Assert {

    @Test
    @Ignore
    public void testParseFeatures() throws Exception {
        FeatureCollectionHandler handler = new FeatureCollectionHandler();
        GeoCsvParser parser = new GeoCsvParser().id("GEOID").wkt("WKT");
        parser.handler(handler);
        parser.parse(new GZIPInputStream(new FileInputStream("tl_2012_us_county.csv.gz")));
        FeatureCollection fc = handler.getCollection();
        System.out.println(fc.getFeatures().size());
    }

    @Test
    public void testParseWithLatLng() throws Exception {
        FeatureCollectionHandler handler = new FeatureCollectionHandler();
        GeoCsvParser parser = new GeoCsvParser().id("zip_code").latitude("latitude").longitude("longitude");
        parser.handler(handler);
        parser.parse(getClass().getResourceAsStream("/zipcodes.csv"));
        FeatureCollection fc = handler.getCollection();
        assertEquals(110, fc.getFeatures().size());
        Feature f = fc.getFeatures().get(0);
        assertEquals("Springfield", f.getProperties().get("city"));
        assertTrue(f.getGeometry() instanceof Point);
    }

}
