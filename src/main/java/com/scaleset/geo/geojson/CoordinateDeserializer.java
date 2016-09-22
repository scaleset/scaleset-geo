package com.scaleset.geo.geojson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.vividsolutions.jts.geom.Coordinate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CoordinateDeserializer extends JsonDeserializer<Coordinate> {
    @Override
    public Coordinate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        // current token is "["
        List<Double> values = new ArrayList<Double>();
        while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
            Double value = jsonParser.getDoubleValue();
            values.add(value);
        }
        Coordinate result = null;
        if (values.size() == 2) {
            result = new Coordinate(values.get(0), values.get(1));
        } else if (values.size() == 3) {
            result = new Coordinate(values.get(0), values.get(1), values.get(2));
        }
        return result;

    }
}
