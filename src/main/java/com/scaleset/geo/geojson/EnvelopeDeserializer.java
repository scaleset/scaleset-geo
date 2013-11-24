package com.scaleset.geo.geojson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.vividsolutions.jts.geom.Envelope;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EnvelopeDeserializer extends JsonDeserializer<Envelope> {
    @Override
    public Envelope deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        // current token is "["
        List<Double> values = new ArrayList<Double>();
        while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
            Double value = jsonParser.getDoubleValue();
            values.add(value);
        }
        Envelope result = null;
        if (values.size() == 4) {
            result = new Envelope(values.get(0), values.get(2), values.get(1), values.get(3));
        }
        return result;

    }
}
