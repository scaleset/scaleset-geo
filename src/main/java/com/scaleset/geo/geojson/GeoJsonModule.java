package com.scaleset.geo.geojson;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.vividsolutions.jts.geom.*;

public class GeoJsonModule extends SimpleModule {

    static GeometryDeserializer GEO_DE = new GeometryDeserializer();

    public GeoJsonModule() {
        this(null);
    }

    public GeoJsonModule(Integer precision) {
        // deserializers - Jackson requires a deserializer for each subclass of geometry
        GeometryDeserializer de = new GeometryDeserializer();
        addDeserializer(Geometry.class, new GeometryDeserializer<Geometry>());
        addDeserializer(GeometryCollection.class, new GeometryDeserializer<GeometryCollection>());
        addDeserializer(Point.class, new GeometryDeserializer<Point>());
        addDeserializer(LinearRing.class, new GeometryDeserializer<LinearRing>());
        addDeserializer(LineString.class, new GeometryDeserializer<LineString>());
        addDeserializer(MultiLineString.class, new GeometryDeserializer<MultiLineString>());
        addDeserializer(MultiPoint.class, new GeometryDeserializer<MultiPoint>());
        addDeserializer(MultiPolygon.class, new GeometryDeserializer<MultiPolygon>());
        addDeserializer(Polygon.class, new GeometryDeserializer<Polygon>());
        addDeserializer(Envelope.class, new EnvelopeDeserializer());
        addDeserializer(Coordinate.class, new CoordinateDeserializer());

        // serializers
        addSerializer(Geometry.class, new GeometrySerializer(precision));
        addSerializer(Envelope.class, new EnvelopeSerializer(precision));
        addSerializer(Coordinate.class, new CoordinateSerializer(precision));
    }

}
