package com.scaleset.geo.geojson;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scaleset.geo.Feature;

import java.io.IOException;
import java.io.OutputStream;

public class GeoJsonWriter {

    private JsonFactory jsonFactory = new JsonFactory();

    private JsonGenerator jg;
    private ObjectMapper objectMapper = new ObjectMapper().registerModule(new GeoJsonModule());

    public void end() throws IOException {
        jg.writeEndArray();
        jg.writeEndObject();
        jg.close();
    }

    public void feature(Feature feature) throws IOException {
        objectMapper.writeValue(jg, feature);
    }

    public void start(OutputStream out) throws IOException {
        jg = jsonFactory.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, true).createGenerator(out,
                JsonEncoding.UTF8);
        jg.writeStartObject();
        jg.writeFieldName("type");
        jg.writeString("FeatureCollection");
        jg.writeFieldName("features");
        jg.writeStartArray();
    }

}
