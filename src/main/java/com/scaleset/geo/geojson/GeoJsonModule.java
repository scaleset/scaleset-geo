package com.scaleset.geo.geojson;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

public class GeoJsonModule extends SimpleModule {

    public GeoJsonModule() {
        addDeserializer(Geometry.class, new GeometryDeserializer());
        addSerializer(Geometry.class, new GeometrySerializer());
        addDeserializer(Envelope.class, new EnvelopeDeserializer());
        addSerializer(Envelope.class, new EnvelopeSerializer());
    }
}
