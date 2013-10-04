package com.scaleset.geo.geojson;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;

import org.junit.Assert;
import org.junit.Test;

import com.scaleset.geo.Feature;
import com.scaleset.geo.FeatureCollection;
import com.scaleset.geo.FeatureCollectionHandler;

public class GeoJsonWriterTest extends Assert {

    @Test
    public void testWriteFeatures() throws Exception {
        FeatureCollection fc = parseFeatures();
        GeoJsonWriter writer = new GeoJsonWriter();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writer.start(out);
        for (Feature feature : fc.getFeatures()) {
            writer.feature(feature);
        }
        writer.end();
        String json = out.toString();
        assertNotNull(json);
    }

    FeatureCollection parseFeatures() throws Exception {
        FeatureCollectionHandler handler = new FeatureCollectionHandler();
        GeoJsonParser parser = new GeoJsonParser();
        parser.handler(handler);
        parser.parse(new FileInputStream("src/test/resources/features.json"));
        FeatureCollection fc = handler.getCollection();
        return fc;
    }
}
