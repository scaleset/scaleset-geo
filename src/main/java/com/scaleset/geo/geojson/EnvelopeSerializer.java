package com.scaleset.geo.geojson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.vividsolutions.jts.geom.Envelope;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class EnvelopeSerializer extends JsonSerializer<Envelope> {

    private Integer precision;

    public EnvelopeSerializer(Integer precision) {
        this.precision = precision;
    }

    public EnvelopeSerializer() {
        this(null);
    }

    @Override
    public void serialize(Envelope envelope, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeStartArray();
        writeNumber(envelope.getMinX(), jsonGenerator);
        writeNumber(envelope.getMinY(), jsonGenerator);
        writeNumber(envelope.getMaxX(), jsonGenerator);
        writeNumber(envelope.getMaxY(), jsonGenerator);
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
