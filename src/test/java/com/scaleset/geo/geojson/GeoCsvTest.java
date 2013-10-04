package com.scaleset.geo.geojson;

import java.io.FileInputStream;
import java.util.zip.GZIPInputStream;

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

}
