package com.scaleset.geo.geojson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

public class GeometrySerializer extends JsonSerializer<Geometry> {

    @Override
    public void serialize(Geometry value, JsonGenerator generator, SerializerProvider provider) throws IOException,
            JsonProcessingException {
        writeGeometry(value, generator);
    }

    void writeGeometry(Geometry geom, JsonGenerator gen) throws JsonGenerationException, IOException {
        if (geom instanceof Point) {
            write((Point) geom, gen);
        } else if (geom instanceof MultiPoint) {
            write((MultiPoint) geom, gen);
        } else if (geom instanceof LineString) {
            write((LineString) geom, gen);
        } else if (geom instanceof MultiLineString) {
            write((MultiLineString) geom, gen);
        } else if (geom instanceof Polygon) {
            write((Polygon) geom, gen);
        } else if (geom instanceof MultiPolygon) {
            write((MultiPolygon) geom, gen);
        } else if (geom instanceof GeometryCollection) {
            write((GeometryCollection) geom, gen);
        } else {
            throw new RuntimeException("Unsupported Geometry type");
        }
    }

    void write(Point point, JsonGenerator gen) throws JsonGenerationException, IOException {
        gen.writeStartObject();
        gen.writeStringField("type", "Point");
        gen.writeFieldName("coordinates");
        writeCoordinate(point.getCoordinate(), gen);
        gen.writeEndObject();
    }

    void write(MultiPoint points, JsonGenerator gen) throws JsonGenerationException, IOException {
        gen.writeStartObject();
        gen.writeStringField("type", "MultiPoint");
        gen.writeFieldName("coordinates");
        writeCoordinates(points.getCoordinates(), gen);
        gen.writeEndObject();
    }

    void write(LineString geom, JsonGenerator gen) throws JsonGenerationException, IOException {
        gen.writeStartObject();
        gen.writeStringField("type", "LineString");
        gen.writeFieldName("coordinates");
        writeCoordinates(geom.getCoordinates(), gen);
        gen.writeEndObject();
    }

    void write(MultiLineString geom, JsonGenerator gen) throws JsonGenerationException, IOException {
        gen.writeStartObject();
        gen.writeStringField("type", "MultiLineString");
        gen.writeFieldName("coordinates");
        gen.writeStartArray();
        for (int i = 0; i < geom.getNumGeometries(); ++i) {
            writeCoordinates(geom.getGeometryN(i).getCoordinates(), gen);
        }
        gen.writeEndArray();
        gen.writeEndObject();
    }

    void write(GeometryCollection coll, JsonGenerator gen) throws JsonGenerationException, IOException {
        gen.writeStartObject();
        gen.writeStringField("type", "GeometryCollection");
        gen.writeArrayFieldStart("geometries");
        for (int i = 0; i < coll.getNumGeometries(); ++i) {
            writeGeometry(coll.getGeometryN(i), gen);
        }
        gen.writeEndArray();
        gen.writeEndObject();
    }

    void write(Polygon geom, JsonGenerator gen) throws JsonGenerationException, IOException {
        gen.writeStartObject();
        gen.writeStringField("type", "Polygon");
        gen.writeFieldName("coordinates");
        gen.writeStartArray();
        writeCoordinates(geom.getExteriorRing().getCoordinates(), gen);
        for (int i = 0; i < geom.getNumInteriorRing(); ++i) {
            writeCoordinates(geom.getInteriorRingN(i).getCoordinates(), gen);
        }
        gen.writeEndArray();
        gen.writeEndObject();
    }

    void write(MultiPolygon geom, JsonGenerator gen) throws JsonGenerationException, IOException {
        gen.writeStartObject();
        gen.writeStringField("type", "MultiPolygon");
        gen.writeFieldName("coordinates");
        gen.writeStartArray();
        for (int i = 0; i < geom.getNumGeometries(); ++i) {
            Polygon p = (Polygon) geom.getGeometryN(i);
            writeCoordinates(p.getExteriorRing().getCoordinates(), gen);
            for (int j = 0; j < p.getNumInteriorRing(); ++j) {
                writeCoordinates(p.getInteriorRingN(j).getCoordinates(), gen);
            }
        }
        gen.writeEndArray();
        gen.writeEndObject();
    }

    void writeCoordinate(Coordinate coordinate, JsonGenerator gen) throws JsonGenerationException, IOException {
        gen.writeStartArray();
        gen.writeNumber(coordinate.x);
        gen.writeNumber(coordinate.y);
        if (! Double.isNaN(coordinate.z)) {
            gen.writeNumber(coordinate.z);
        }
        gen.writeEndArray();
    }

    void writeCoordinates(Coordinate[] coordinates, JsonGenerator gen) throws JsonGenerationException, IOException {
        gen.writeStartArray();
        for (Coordinate coord : coordinates) {
            writeCoordinate(coord, gen);
        }
        gen.writeEndArray();
    }
}
