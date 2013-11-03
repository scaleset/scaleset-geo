package com.scaleset.geo.geojson;

import com.scaleset.geo.Feature;
import com.scaleset.geo.FeatureHandler;
import com.scaleset.geo.geonames.GeoNamesParser;
import com.vividsolutions.jts.geom.Envelope;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Map;

public class GeonamesToGeoJson {

    public static void main(String[] args) throws Exception {
        toGeoJson();
    }

    protected static void toGeoJson() throws Exception {
        File dataFile = new File("US.txt");
        FeatureHandler handler = new GeoJsonTransformHandler();
        GeoNamesParser parser = new GeoNamesParser();
        parser.handler(handler);
        parser.parse(new FileInputStream(dataFile));
    }

    public static class GeoJsonTransformHandler implements FeatureHandler {

        private File file = new File("target/us.geojson");
        private GeoJsonWriter writer;

        public void begin() throws Exception {
            OutputStream out = new FileOutputStream(file);
            writer = new GeoJsonWriter();
            writer.start(out);
        }

        public void end() throws Exception {
            writer.end();
        }

        public void handle(Envelope bbox) {
        }

        public void handle(Feature feature) throws Exception {
            // add composite index
            Map<String, Object> props = feature.getProperties();
            String admin12 = props.get("admin1") + "." + props.get("admin2");
            props.put("admin12", admin12);
            props.remove("alternateNames");
            props.remove("type");
            props.remove("countryCode2");
            props.remove("timeZone");
            props.remove("admin3");
            props.remove("admin4");
            writer.feature(feature);
        }

    }

}
