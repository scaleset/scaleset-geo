package com.scaleset.geo.geojson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.vividsolutions.jts.geom.Envelope;

import java.io.IOException;

public class EnvelopeSerializer extends JsonSerializer<Envelope> {

    @Override
    public void serialize(Envelope envelope, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeStartArray();
        jsonGenerator.writeNumber(envelope.getMinX());
        jsonGenerator.writeNumber(envelope.getMinY());
        jsonGenerator.writeNumber(envelope.getMaxX());
        jsonGenerator.writeNumber(envelope.getMaxY());
        jsonGenerator.writeEndArray();
    }
}
