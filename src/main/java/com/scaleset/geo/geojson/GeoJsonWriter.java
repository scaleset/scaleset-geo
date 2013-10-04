package com.scaleset.geo.geojson;

import java.io.IOException;
import java.io.OutputStream;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scaleset.geo.Feature;

public class GeoJsonWriter {

    private JsonFactory jsonFactory = new JsonFactory(); // or, for data
                                                         // binding,
    // org.codehaus.jackson.mapper.MappingJsonFactory]
    private JsonGenerator jg;
    private ObjectMapper objectMapper = new ObjectMapper();

    public void start(OutputStream out) throws IOException {
        jg = jsonFactory.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, true).createJsonGenerator(out,
                JsonEncoding.UTF8);
        jg.writeStartObject();
        jg.writeFieldName("type");
        jg.writeString("FeatureCollection");
        jg.writeFieldName("features");
        jg.writeStartArray();
    }

    public void feature(Feature feature) throws IOException {
        objectMapper.writeValue(jg, feature);
    }

    public void end() throws IOException {
        jg.writeEndArray();
        jg.writeEndObject();
        jg.close();
    }

}
