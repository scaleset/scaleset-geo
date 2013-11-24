package com.scaleset.geo.geojson;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scaleset.geo.AbstractFeatureParser;
import com.scaleset.geo.Feature;
import com.vividsolutions.jts.geom.Envelope;

import java.io.InputStream;

public class GeoJsonParser extends AbstractFeatureParser {

    private ObjectMapper objectMapper = new ObjectMapper().registerModule(new GeoJsonModule());

    public void parse(InputStream in) throws Exception {
        begin();
        JsonFactory f = new MappingJsonFactory();
        JsonParser jp = f.createParser(in);
        JsonToken current;
        current = jp.nextToken();
        if (current != JsonToken.START_OBJECT) {
            System.out.println("Error: root should be object: quiting.");
            return;
        }
        while (jp.nextToken() != JsonToken.END_OBJECT) {
            String fieldName = jp.getCurrentName();
            // move from field name to field value
            if (fieldName.equals("features")) {
                current = jp.nextToken();
                if (current == JsonToken.START_ARRAY) {
                    // For each of the records in the array
                    while (jp.nextToken() != JsonToken.END_ARRAY) {
                        Feature feature = objectMapper.readValue(jp, Feature.class);
                        handle(feature);
                    }
                } else {
                    System.out.println("Error: records should be an array: skipping.");
                    jp.skipChildren();
                }
            } else if (fieldName.equals("bbox")) {
                // advance to '['
                jp.nextToken();
                Envelope bbox = objectMapper.readValue(jp, Envelope.class);
                handle(bbox);
            } else {
                if (!"type".equals(fieldName)) {
                    System.out.println("Unprocessed property: " + fieldName);
                }
                jp.skipChildren();
            }
        }
        in.close();
        end();
    }

}
