package com.scaleset.geo.geojson;

import java.io.FileInputStream;

import org.junit.Assert;
import org.junit.Test;

import com.scaleset.geo.FeatureCollection;
import com.scaleset.geo.FeatureCollectionHandler;

public class GeoJsonParserTest extends Assert {

    @Test
    public void testParseFeatures() throws Exception {
        FeatureCollectionHandler handler = new FeatureCollectionHandler();
        GeoJsonParser parser = new GeoJsonParser();
        parser.handler(handler);
        parser.parse(new FileInputStream("src/test/resources/features.json"));
        FeatureCollection fc = handler.getCollection();
        System.out.println(fc.getFeatures().size());
    }

}
