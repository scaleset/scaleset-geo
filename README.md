Scaleset Geo
==============

Scaleset Geo is a Jackson module for mapping GeoJSON to/from JTS Geometry objects.  Additionally, the library includes a simple Feature API for working with GeoJSON features and feature collections. 

Quick Start
-----------

### Dependency

```java
<dependency>
    <groupId>com.scaleset</groupId>
    <artifactId>scaleset-geo</artifactId>
    <version>0.12.0</version>
</dependency>
```

### GeoJSON Jackson Module (GeoJsonModule)

To read or write GeoJSON, simply register the GeoJsonModule with a Jackson ObjectMapper. 


```java
    @Test
    public void testSerdePoint() throws IOException {
        Point point = factory.createPoint(new Coordinate(-78, 39));
        String json = mapper.writeValueAsString(point);
        assertEquals("{\"type\":\"Point\",\"coordinates\":[-78.0,39.0]}", json);
        Point p2 = mapper.readValue(json, Point.class);
        assertEquals(point, p2);
    }
```

The module supports all valid GeoJSON geometry types.  

* Point            
* MultiPoint       
* LineString        
* MultiLineString   
* Polygon            
* MultiPolygon      
* GeometryCollection 


### Feature API

Scaleset Geo provides a minimal Feature API compatible with the GeoJSON data model.

* Feature - An object with identity, type, properties and a geometry.
* Feature Collection - A collection of Feature objects with an additional bounding box.

The following unit test creates a Feature object with a Point geometry and a single property.
```java
    @Test
    public void testSimpleFeature() throws IOException {
        Point point = factory.createPoint(new Coordinate(-78, 39));
        Feature feature = new Feature();
        feature.setGeometry(point);
        feature.getProperties().put("title", "Simple Point Feature");
        System.out.println(mapper.writeValueAsString(feature));
    }
```
It then writes it to stdout as GeoJSON.

```json
{
  "type": "Feature",
  "geometry": {
    "type": "Point",
    "coordinates": [
      -78,
      39
    ]
  },
  "properties": {
    "title": "Simple Point Feature"
  }
}
```


### GeoJsonParser

GeoJSONParser is a streaming GeoJSON parser. It uses the Jackson's streaming API to parse arbitrarily large GeoJSON documents.
Simple instantiate the parser and provide it with a handler to receive callbacks as each feature is encountered. It also provides
callbacks for begin / end events as well as a bbox event for feature collections.

The following example uses a FeatureCollectionHandler to build a FeatureCollection from the GeoJSON document.

```java
     FeatureCollectionHandler handler = new FeatureCollectionHandler();
     GeoJsonParser parser = new GeoJsonParser();
     parser.handler(handler);
     parser.parse(new FileInputStream("src/test/resources/features.json"));
     FeatureCollection fc = handler.getCollection();
     System.out.println(fc.getFeatures().size());
```

### GeoJsonWriter

GeoCsvWriter is a streaming GeoJsonWriter. It uses Jackson's streaming API to write arbitrarily large GeoJSON documents.

```java
    FeatureCollection fc = parseFeatures();
    GeoJsonWriter writer = new GeoJsonWriter();
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    writer.start(out);
    for (Feature feature : fc.getFeatures()) {
        writer.feature(feature);
    }
    writer.end();
    String json = out.toString();
```

### GeoCsvParser

GeoCsvParser is a streaming parser for CSV files with a WKT geometry column. Compressed GeoCSV files can be much
smaller than their equivalent zipped shapefiles. Unlike a zipped shapefile which requires unzipping before processing,
GoCSV files be parsed directly. This makes them an ideal way to distribute geo data with your application.

```java
    FeatureCollectionHandler handler = new FeatureCollectionHandler();
    GeoCsvParser parser = new GeoCsvParser().id("GEOID").wkt("WKT");
    parser.handler(handler);
    parser.parse(new GZIPInputStream(new FileInputStream("tl_2012_us_county.csv.gz")));
    FeatureCollection fc = handler.getCollection();
    System.out.println(fc.getFeatures().size());
```

### GeoNamesParser

GeoNamesParser is a streaming parser for the data files published by geonames.org.

### License

Scaleset Geo is [Apache 2.0 licensed](http://www.apache.org/licenses/LICENSE-2.0.html).
