package com.scaleset.geo.geojson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.vividsolutions.jts.geom.Coordinate;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class CoordinateSerializer extends JsonSerializer<Coordinate> {

    private Integer precision;

    public CoordinateSerializer(Integer precision) {
        this.precision = precision;
    }

    public CoordinateSerializer() {
        this(null);
    }

    @Override
    public void serialize(Coordinate coordinate, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartArray();
        writeNumber(coordinate.x, jsonGenerator);
        writeNumber(coordinate.y, jsonGenerator);
        jsonGenerator.writeEndArray();
    }

    void writeNumber(double number, JsonGenerator gen) throws IOException {
        if (precision != null) {
            gen.writeNumber(new BigDecimal(number).setScale(precision, RoundingMode.HALF_UP));
        } else {
            gen.writeNumber(number);
        }
    }

}
