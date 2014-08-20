package com.scaleset.geo.geojson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.vividsolutions.jts.geom.*;

import java.io.IOException;

public class GeometryDeserializer<T extends Geometry> extends JsonDeserializer<T> {

    private GeometryFactory factory = new GeometryFactory();

    @Override
    public T deserialize(JsonParser jsonParser, DeserializationContext arg1) throws IOException,
            JsonProcessingException {
        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);
        T result = (T) geometry(node);
        return result;
    }

    Geometry geometry(JsonNode node) {
        Geometry result = null;
        String type = node.get("type").textValue();
        ArrayNode coordinates = (ArrayNode) node.get("coordinates");

        if ("Point".equals(type)) {
            result = point(coordinates);
        } else if ("MultiPoint".equals(type)) {
            result = multiPoint(coordinates);
        } else if ("LineString".equals(type)) {
            result = lineString(coordinates);
        } else if ("MultiLineString".equals(type)) {
            result = multiLineString(coordinates);
        } else if ("Polygon".equals(type)) {
            result = polygon(coordinates);
        } else if ("MultiPolygon".equals(type)) {
            result = multiPolygon(coordinates);
        } else if ("GeometryCollection".equals(type)) {
            result = geometryCollection((ArrayNode) node.get("geometries"));
        }

        return result;
    }

    Geometry point(ArrayNode coordinates) {
        Coordinate coordinate = toCoordinate(coordinates);
        Point result = factory.createPoint(coordinate);
        return result;
    }

    Geometry multiPoint(ArrayNode nodes) {
        Coordinate[] coordinates = toCoordinateArray(nodes);
        MultiPoint result = factory.createMultiPoint(coordinates);
        return result;
    }

    LineString lineString(ArrayNode nodes) {
        Coordinate[] coordinates = toCoordinateArray(nodes);
        LineString result = factory.createLineString(coordinates);
        return result;
    }

    MultiLineString multiLineString(ArrayNode nodes) {
        LineString[] lineStrings = new LineString[nodes.size()];
        for (int i = 0; i < lineStrings.length; ++i) {
            lineStrings[i] = lineString((ArrayNode) nodes.get(i));
        }
        MultiLineString result = factory.createMultiLineString(lineStrings);
        return result;
    }

    Polygon polygon(ArrayNode nodes) {
        LinearRing outerRing = toLinearRing((ArrayNode) nodes.get(0));
        LinearRing[] innerRings = new LinearRing[nodes.size() - 1];
        for (int i = 0; i < innerRings.length; ++i) {
            innerRings[i] = toLinearRing((ArrayNode) nodes.get(i + 1));
        }
        Polygon result = factory.createPolygon(outerRing, innerRings);
        return result;
    }

    MultiPolygon multiPolygon(ArrayNode nodes) {
        Polygon[] polygons = new Polygon[nodes.size()];
        for (int i = 0; i < polygons.length; ++i) {
            polygons[i] = polygon((ArrayNode) nodes.get(i));
        }
        MultiPolygon result = factory.createMultiPolygon(polygons);
        return result;
    }

    GeometryCollection geometryCollection(ArrayNode nodes) {
        Geometry[] geometries = new Geometry[nodes.size()];
        for (int i = 0; i < geometries.length; ++i) {
            geometries[i] = geometry(nodes.get(i));
        }
        GeometryCollection result = factory.createGeometryCollection(geometries);
        return result;
    }

    LinearRing toLinearRing(ArrayNode nodes) {
        Coordinate[] coordinates = toCoordinateArray(nodes);
        LinearRing result = factory.createLinearRing(coordinates);
        return result;
    }

    Coordinate[] toCoordinateArray(ArrayNode nodes) {
        Coordinate[] result = new Coordinate[nodes.size()];
        for (int i = 0; i < result.length; ++i) {
            result[i] = toCoordinate((ArrayNode) nodes.get(i));
        }
        return result;
    }

    Coordinate toCoordinate(ArrayNode node) {
        double x = 0, y = 0, z = Double.NaN;
        if (node.size() > 1) {
            x = node.get(0).asDouble();
            y = node.get(1).asDouble();
        }
        if (node.size() > 2) {
            z = node.get(2).asDouble();
        }
        Coordinate result = new Coordinate(x, y, z);
        return result;
    }
}
