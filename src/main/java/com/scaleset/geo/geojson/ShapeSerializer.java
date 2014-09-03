package com.scaleset.geo.geojson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.spatial4j.core.shape.*;
import com.spatial4j.core.shape.jts.JtsGeometry;
import com.vividsolutions.jts.geom.Geometry;

import java.io.IOException;

/**
 * Serializers for Spatial4J Shape types based on the Elasticsearch usage of Spatial4J
 */
public class ShapeSerializer extends JsonSerializer<Shape> {

    private GeometrySerializer jtsSerializer = new GeometrySerializer();

    @Override
    public void serialize(Shape value, JsonGenerator generator, SerializerProvider provider) throws IOException {
        writeShape(value, generator);
    }

    void writeShape(Shape shape, JsonGenerator gen) throws IOException {
        if (shape instanceof Point) {
            write((Point) shape, gen);
        } else if (shape instanceof Circle) {
            write((Circle) shape, gen);
        } else if (shape instanceof Rectangle) {
            write((Rectangle) shape, gen);
        } else if (shape instanceof ShapeCollection<?>) {
            write((ShapeCollection<?>) shape, gen);
        } else if (shape instanceof JtsGeometry) {
            write((JtsGeometry) shape, gen);
        } else {
            throw new RuntimeException("Unsupported Geometry type");
        }
    }

    void write(JtsGeometry jtsGeometry, JsonGenerator gen) throws IOException {
        Geometry geometry = jtsGeometry.getGeom();
        jtsSerializer.writeGeometry(geometry, gen);
    }

    void write(Point point, JsonGenerator gen) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("type", "Point");
        gen.writeFieldName("coordinates");
        writeCoordinate(point.getX(), point.getY(), gen);
        gen.writeEndObject();
    }

    void write(Circle circle, JsonGenerator gen) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("type", "Circle");
        gen.writeFieldName("coordinates");
        Point center = circle.getCenter();
        writeCoordinate(center.getX(), center.getY(), gen);
        gen.writeNumberField("radius", circle.getRadius());
        gen.writeEndObject();
    }

    void write(Rectangle rectangle, JsonGenerator gen) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("type", "Rectangle");
        gen.writeFieldName("coordinates");
        gen.writeStartArray();
        writeCoordinate(rectangle.getMinX(), rectangle.getMaxY(), gen);
        writeCoordinate(rectangle.getMaxX(), rectangle.getMinY(), gen);
        gen.writeEndArray();
        gen.writeEndObject();
    }

    void write(ShapeCollection coll, JsonGenerator gen) throws IOException {
        // can we determine shape type?  Should we iterate and check the write out the specific type of geojson?
        gen.writeStartObject();
        gen.writeStringField("type", "GeometryCollection");
        gen.writeArrayFieldStart("geometries");
        for (Object obj : coll) {
            Shape shape = (Shape) obj;
            writeShape(shape, gen);
        }
        gen.writeEndArray();
        gen.writeEndObject();
    }

    void writeCoordinate(double x, double y, JsonGenerator gen) throws IOException {
        gen.writeStartArray();
        gen.writeNumber(x);
        gen.writeNumber(y);
        gen.writeEndArray();
    }

}
