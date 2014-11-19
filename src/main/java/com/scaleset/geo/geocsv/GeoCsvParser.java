package com.scaleset.geo.geocsv;

import java.io.InputStream;
import java.io.InputStreamReader;

import au.com.bytecode.opencsv.CSVReader;

import com.scaleset.geo.AbstractFeatureParser;
import com.scaleset.geo.Feature;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class GeoCsvParser extends AbstractFeatureParser {

    private String wktFieldName = "WKT";
    private String idFieldName = "ID";
    private WKTReader reader = new WKTReader();
    private String latitudeFieldName = "LAT";
    private String longitudeFieldName = "LNG";
    private GeometryFactory geometryFactory = new GeometryFactory();

    public GeoCsvParser id(String idFieldName) {
        this.idFieldName = idFieldName;
        return this;
    }

    public GeoCsvParser wkt(String wktFieldName) {
        this.wktFieldName = wktFieldName;
        return this;
    }

    public GeoCsvParser latitude(String latitudeFieldName) {
        this.latitudeFieldName = latitudeFieldName;
        return this;
    }

    public GeoCsvParser longitude(String longitudeFieldName) {
        this.longitudeFieldName = longitudeFieldName;
        return this;
    }

    public void parse(InputStream in) throws Exception {
        begin();
        CSVReader reader = null;
        try {
            reader = new CSVReader(new InputStreamReader(in));
            String[] headers = reader.readNext();
            Integer wktColumn = findColumn(wktFieldName, headers);
            Integer idColumn = findColumn(idFieldName, headers);
            Integer latColumn = findColumn(latitudeFieldName, headers);
            Integer lngColumn = findColumn(longitudeFieldName, headers);

            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                Feature feature = new Feature();
                Double latitude = null;
                Double longitude = null;
                for (int i = 0; i < nextLine.length; ++i) {
                    String val = nextLine[i];
                    if (val != null) {
                        if (wktColumn != null && i == wktColumn) {
                            String wkt = nextLine[i];
                            Geometry geometry = parseGeometry(wkt);
                            if (geometry != null) {
                                feature.setGeometry(geometry);
                            }
                        } else if (latColumn != null && i == latColumn) {
                            latitude = toDouble(val, null);
                        } else if (lngColumn != null && i == lngColumn) {
                            longitude = toDouble(val, null);
                        } else if (idColumn != null && i == idColumn) {
                            String id = nextLine[i];
                            feature.setId(id);
                        } else {
                            feature.put(headers[i], nextLine[i]);
                        }
                    }
                }
                if (latitude != null && longitude != null) {
                    feature.setGeometry(geometryFactory.createPoint(new Coordinate(longitude, latitude)));
                }
                handle(feature);
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        end();
    }

    Geometry parseGeometry(String wkt) {
        Geometry result = null;
        if (wkt != null) {
            try {
                result = reader.read(wkt);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return result;
    }

    Double toDouble(String value, Double fallback) {
        Double result = fallback;
        try {
            result = Double.valueOf(value);
        } catch (NumberFormatException e) {
        }
        return result;
    }

    Integer findColumn(String key, String[] headers) {
        Integer result = null;
        for (int i = 0; i < headers.length; ++i) {
            if (key.equalsIgnoreCase(headers[i])) {
                result = i;
                break;
            }
        }
        return result;
    }

}
