package com.scaleset.geo.geojson;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scaleset.geo.Feature;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;

public class GeoJsonModuleTest extends Assert {

    private static double MM_PRECISION = 0.000000001;
      
    private GeometryFactory factory = new GeometryFactory();
    private ObjectMapper mapper = new ObjectMapper().registerModule(new GeoJsonModule());

    @Test
    public void testSerializePoint() throws IOException {
        Point point = factory.createPoint(new Coordinate(-78, 39));
        String json = mapper.writeValueAsString(point);
        assertEquals("{\"type\":\"Point\",\"coordinates\":[-78.0,39.0]}", json);
        Point p2 = mapper.readValue(json, Point.class);
        assertEquals(point, p2);
        assertTrue(point.getCoordinate().equals3D(p2.getCoordinate()));
        
        point = factory.createPoint(new Coordinate(24, -56, 78));
        json = mapper.writeValueAsString(point);
        assertEquals("{\"type\":\"Point\",\"coordinates\":[24.0,-56.0,78.0]}", json);
        p2 = mapper.readValue(json, Point.class);
        assertEquals(point, p2);
        assertTrue(point.getCoordinate().equals3D(p2.getCoordinate()));
    }

    @Test
    public void testSimpleFeature() throws IOException {
        Point point = factory.createPoint(new Coordinate(-78, 39));
        Feature feature = new Feature();
        feature.setGeometry(point);
        feature.getProperties().put("title", "Simple Point Feature");
        String json = mapper.writeValueAsString(feature);
    }

    @Test
    public void testDeserializePoint() throws IOException {
        String json = "{\"point\": {\"type\":\"Point\",\"coordinates\":[-78.0,39.0]}}";
        HasGeometry hasGeometry = mapper.readValue(json, HasGeometry.class);
        assertNotNull(hasGeometry);
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    static class HasGeometry {
        public Point point;
    }
    




  @Test
  public void testPoint2D() throws IOException, JSONException {
    String expected = "{\"type\": \"Point\", \"coordinates\": [102.0, 0.5]}";
    Geometry g = mapper.readValue(expected, Geometry.class);
    assertNotNull(g);
    assertTrue(g instanceof Point);
    Point p = (Point) g;
    assertEquals(102.0, p.getCoordinate().x, MM_PRECISION);
    assertEquals(0.5, p.getCoordinate().y, MM_PRECISION);
    assertTrue(Double.isNaN(p.getCoordinate().z));

    String json = mapper.writeValueAsString(p);
    JSONAssert.assertEquals(expected, json, true);
  }

  @Test
  public void testPoint3D() throws IOException, JSONException {
    String expected = "{\"type\": \"Point\", \"coordinates\": [22.0, -10.5, 42.0]}";
    Geometry g = mapper.readValue(expected, Geometry.class);
    assertNotNull(g);
    assertTrue(g instanceof Point);
    Point p = (Point) g;
    assertEquals(22.0, p.getCoordinate().x, MM_PRECISION);
    assertEquals(-10.5, p.getCoordinate().y, MM_PRECISION);
    assertEquals(42.0, p.getCoordinate().z, MM_PRECISION);

    String json = mapper.writeValueAsString(p);
    JSONAssert.assertEquals(expected, json, true);
  }

  @Test
  public void testLineString() throws IOException, JSONException {
    String expected = "{\"type\": \"LineString\","
            + "          \"coordinates\": ["
            + "            [102.0, 0.0], [103.0, 1.0], [104.0, 2.0], [105.0, 3.0]"
            + "            ]"
            + "          }";
    Geometry g = mapper.readValue(expected, Geometry.class);
    assertNotNull(g);
    assertTrue(g instanceof LineString);
    LineString ls = (LineString) g;
    assertEquals(4, ls.getCoordinates().length);
    assertEquals(102.0, ls.getCoordinateN(0).x, MM_PRECISION);
    assertEquals(0.0, ls.getCoordinateN(0).y, MM_PRECISION);
    assertEquals(103.0, ls.getCoordinateN(1).x, MM_PRECISION);
    assertEquals(1.0, ls.getCoordinateN(1).y, MM_PRECISION);
    assertEquals(104.0, ls.getCoordinateN(2).x, MM_PRECISION);
    assertEquals(2.0, ls.getCoordinateN(2).y, MM_PRECISION);
    assertEquals(105.0, ls.getCoordinateN(3).x, MM_PRECISION);
    assertEquals(3.0, ls.getCoordinateN(3).y, MM_PRECISION);

    String json = mapper.writeValueAsString(ls);
    JSONAssert.assertEquals(expected, json, true);
  }

  @Test
  public void testPolygon() throws IOException, JSONException {
    String expected = "{\"type\": \"Polygon\","
            + "           \"coordinates\": ["
            + "             [ [100.0, 0.0], [101.0, 0.0], [101.0, 1.0],"
            + "               [100.0, 1.0], [100.0, 0.0] ],"
            + "             [ [100.25, 0.25], [100.75, 0.25], [100.75, 0.75],"
            + "               [100.25, 0.75], [100.25, 0.25] ]"
            + "             ]"
            + "         }";
    Geometry g = mapper.readValue(expected, Geometry.class);
    assertNotNull(g);
    assertTrue(g instanceof Polygon);
    Polygon p = (Polygon) g;
    assertEquals(100.0, p.getExteriorRing().getCoordinateN(0).x, MM_PRECISION);
    assertEquals(1, p.getNumInteriorRing());
    assertEquals(100.25, p.getInteriorRingN(0).getCoordinateN(0).x, MM_PRECISION);

    String json = mapper.writeValueAsString(p);
    JSONAssert.assertEquals(expected, json, true);
  }

  @Test
  public void testMultiPoint() throws IOException, JSONException {
    String expected = "{\"type\": \"MultiPoint\", "
            + "    \"coordinates\": ["
            + "        [10, 40], [40, 30], [20, 20], [30, 10]"
            + "    ]"
            + "}";
    Geometry g = mapper.readValue(expected, Geometry.class);
    assertNotNull(g);
    assertTrue(g instanceof MultiPoint);
    MultiPoint mp = (MultiPoint) g;
    assertEquals(4, mp.getNumGeometries());
    assertEquals(10.0, mp.getGeometryN(0).getCoordinate().x, MM_PRECISION);
    assertEquals(40.0, mp.getGeometryN(0).getCoordinate().y, MM_PRECISION);
    assertTrue(Double.isNaN(mp.getGeometryN(0).getCoordinate().z));
    assertEquals(40.0, mp.getGeometryN(1).getCoordinate().x, MM_PRECISION);
    assertEquals(30.0, mp.getGeometryN(1).getCoordinate().y, MM_PRECISION);
    assertTrue(Double.isNaN(mp.getGeometryN(1).getCoordinate().z));
    assertEquals(20.0, mp.getGeometryN(2).getCoordinate().x, MM_PRECISION);
    assertEquals(20.0, mp.getGeometryN(2).getCoordinate().y, MM_PRECISION);
    assertTrue(Double.isNaN(mp.getGeometryN(2).getCoordinate().z));
    assertEquals(30.0, mp.getGeometryN(3).getCoordinate().x, MM_PRECISION);
    assertEquals(10.0, mp.getGeometryN(3).getCoordinate().y, MM_PRECISION);
    assertTrue(Double.isNaN(mp.getGeometryN(3).getCoordinate().z));

    String json = mapper.writeValueAsString(mp);
    JSONAssert.assertEquals(expected, json, true);
  }

  @Test
  public void testMultiLineString() throws IOException, JSONException {
    String expected = "{\"type\": \"MultiLineString\", "
            + "    \"coordinates\": ["
            + "        [[10, 10], [20, 20], [10, 40]], "
            + "        [[40, 40], [30, 30], [40, 20], [30, 10]]"
            + "    ]"
            + "}";
    Geometry g = mapper.readValue(expected, Geometry.class);
    assertNotNull(g);
    assertTrue(g instanceof MultiLineString);
    MultiLineString mls = (MultiLineString) g;
    assertEquals(2, mls.getNumGeometries());
    assertTrue(mls.getGeometryN(0) instanceof LineString);
    LineString ls1 = (LineString) mls.getGeometryN(0);
    assertEquals(10.0, ls1.getCoordinate().x, MM_PRECISION);
    assertTrue(mls.getGeometryN(1) instanceof LineString);
    LineString ls2 = (LineString) mls.getGeometryN(1);
    assertEquals(40.0, ls2.getCoordinate().x, MM_PRECISION);

    String json = mapper.writeValueAsString(mls);
    JSONAssert.assertEquals(expected, json, true);
  }

  @Test
  public void testMultiPolygon() throws IOException, JSONException {
    String expected = "{\"type\": \"MultiPolygon\","
            + "    \"coordinates\": ["
            + "        ["
            + "          ["
            + "            [101.2, 1.2], [101.8, 1.2], [101.8, 1.8], [101.2, 1.8], [101.2, 1.2]"
            + "          ],"
            + "          ["
            + "            [101.2, 1.2], [101.3, 1.2], [101.3, 1.3], [101.2, 1.3], [101.2, 1.2]"
            + "          ],"
            + "          ["
            + "            [101.6, 1.4], [101.7, 1.4], [101.7, 1.5], [101.6, 1.5], [101.6, 1.4]"
            + "          ],"
            + "          ["
            + "            [101.5, 1.6], [101.6, 1.6], [101.6, 1.7], [101.5, 1.7], [101.5, 1.6]"
            + "          ]"
            + "        ],"
            + "        ["
            + "          ["
            + "            [100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0, 0.0]"
            + "          ],"
            + "          ["
            + "            [100.35, 0.35], [100.65, 0.35], [100.65, 0.65], [100.35, 0.65], [100.35, 0.35]"
            + "          ]"
            + "        ]"
            + "      ]"
            + "  }";

    Geometry g = mapper.readValue(expected, Geometry.class);
    assertNotNull(g);
    assertTrue(g instanceof MultiPolygon);
    MultiPolygon mp = (MultiPolygon) g;
    assertEquals(2, mp.getNumGeometries());
    assertTrue(mp.getGeometryN(0) instanceof Polygon);
    Polygon p1 = (Polygon) mp.getGeometryN(0);
    assertEquals(101.2, p1.getExteriorRing().getCoordinateN(0).x, MM_PRECISION);
    assertEquals(3, p1.getNumInteriorRing());
    assertTrue(mp.getGeometryN(1) instanceof Polygon);
    Polygon p2 = (Polygon) mp.getGeometryN(1);
    assertEquals(100.0, p2.getExteriorRing().getCoordinateN(0).x, MM_PRECISION);
    assertEquals(1, p2.getNumInteriorRing());

    String json = mapper.writeValueAsString(mp);
    JSONAssert.assertEquals(expected, json, true);
  }

  @Test
  public void testGeometryCollection() throws IOException, JSONException {
    String expected = "{\"type\": \"GeometryCollection\","
            + "    \"geometries\": ["
            + "        {"
            + "            \"type\": \"Point\","
            + "            \"coordinates\": ["
            + "                -80.66080570220947,"
            + "                35.04939206472683"
            + "            ]"
            + "        },"
            + "        {"
            + "            \"type\": \"Polygon\","
            + "            \"coordinates\": ["
            + "                ["
            + "                    ["
            + "                        -80.66458225250244,"
            + "                        35.04496519190309"
            + "                    ],"
            + "                    ["
            + "                        -80.66344499588013,"
            + "                        35.04603679820616"
            + "                    ],"
            + "                    ["
            + "                        -80.66258668899536,"
            + "                        35.045580049697556"
            + "                    ],"
            + "                    ["
            + "                        -80.66387414932251,"
            + "                        35.044280059194946"
            + "                    ],"
            + "                    ["
            + "                        -80.66458225250244,"
            + "                        35.04496519190309"
            + "                    ]"
            + "                ]"
            + "            ]"
            + "        },"
            + "        {"
            + "            \"type\": \"LineString\","
            + "            \"coordinates\": ["
            + "                ["
            + "                    -80.66237211227417,"
            + "                    35.05950973022538"
            + "                ],"
            + "                ["
            + "                    -80.66303730010986,"
            + "                    35.043717894732545"
            + "                ]"
            + "            ]"
            + "        }"
            + "    ]"
            + "}";
    
    Geometry g = mapper.readValue(expected, Geometry.class);
    assertNotNull(g);
    assertTrue(g instanceof GeometryCollection);
    GeometryCollection gc = (GeometryCollection) g;
    assertEquals(3,gc.getNumGeometries());
    assertTrue(gc.getGeometryN(0) instanceof Point);
    assertTrue(gc.getGeometryN(1) instanceof Polygon);
    assertTrue(gc.getGeometryN(2) instanceof LineString);
  }
}
