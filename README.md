Scaleset Geo
==============

Scaleset Geo is a GeoJSON compatible framework for working with simple feature data.


Quick Start
-----------

### Feature API

Scaleset Geo provides a minimal Feature API compatible with the GeoJSON data model.

* Feature - An object with identity, type, properties and a geometry.
* Feature Collection - A collection of Feature objects with an additional bounding box.


### Dependency
```java
<dependency>
    <groupId>com.scaleset</groupId>
    <artifactId>scaleset-geo</artifactId>
    <version>0.4.0</version>
</dependency>
```

### GeoJSONParser

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

### GeoNamesParser

GeoNamesParser is a streaming parser for the data files published by geonames.org.

### GeoCSVParser

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
