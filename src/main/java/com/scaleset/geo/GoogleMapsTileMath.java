package com.scaleset.geo;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;
import com.vividsolutions.jts.geom.util.GeometryTransformer;

/*
 Based on GDAL2Tiles / globalmaptiles.py
 Original python version Copyright (c) 2008 Klokan Petr Pridal. All rights reserved.
 http://www.klokan.cz/projects/gdal2tiles/
 */

public class GoogleMapsTileMath {

    private double initialResolution;

    private double originShift;

    private Coordinate topLeft;

    /**
     * Default constructor
     */
    public GoogleMapsTileMath() {
    }

    /**
     * Construct a new projection with
     * 
     * @param tileSize
     *            The size of a square tile in pixels
     */
    public GoogleMapsTileMath(int tileSize) {

        // 156543.03392804062 for tileSize 256 Pixels
        initialResolution = 2 * Math.PI * 6378137 / tileSize;

        // 20037508.342789244
        originShift = 2 * Math.PI * 6378137 / 2.0;

        // -20037508.342789244, 20037508.342789244
        topLeft = new Coordinate(-originShift, originShift);

    }

    /**
     * Converts given lat/lon in WGS84 Datum to XY in Spherical Mercator
     * EPSG:900913
     */
    public Coordinate latLonToMeters(Coordinate latLon) {
        double mx = latLon.x * originShift / 180.0;
        double my = Math.log(Math.tan((90 + latLon.y) * Math.PI / 360.0)) / (Math.PI / 180.0);
        my *= originShift / 180.0;
        return new Coordinate(mx, my);
    }

    /**
     * Converts given lat/lon in WGS84 Datum to XY in Spherical Mercator
     * EPSG:900913
     */
    public Coordinate latLonToMeters(double lat, double lon) {
        double mx = lon * originShift / 180.0;
        double my = Math.log(Math.tan((90 + lat) * Math.PI / 360.0)) / (Math.PI / 180.0);

        my *= originShift / 180.0;

        return new Coordinate(mx, my);
    }

    /**
     * Converts given lat/lon in WGS84 Datum to XY in Spherical Mercator
     * EPSG:900913
     */
    public Envelope latLonToMeters(Envelope env) {
        Coordinate min = latLonToMeters(env.getMinX(), env.getMinY());
        Coordinate max = latLonToMeters(env.getMaxX(), env.getMaxY());
        Envelope result = new Envelope(min.x, max.x, min.y, max.y);
        return result;
    }

    /**
     * Converts geometry from lat/lon (EPSG:4326)) to Spherical Mercator
     * (EPSG:3785)
     * 
     * @param geometry
     * @return
     */
    public Geometry latLonToMeters(Geometry geometry) {
        GeometryTransformer transformer = new GeometryTransformer() {
            @Override
            protected CoordinateSequence transformCoordinates(CoordinateSequence coords, Geometry parent) {
                Coordinate[] newCoords = new Coordinate[coords.size()];
                for (int i = 0; i < coords.size(); ++i) {
                    Coordinate coord = coords.getCoordinate(i);
                    newCoords[i] = latLonToMeters(coord);
                }
                return new CoordinateArraySequence(newCoords);
            }
        };
        Geometry result = transformer.transform(geometry);
        return result;
    }

    public int matrixSize(int zoomLevel) {
        return 1 << zoomLevel;
    }

    /**
     * Converts XY point from Spherical Mercator (EPSG:3785) to lat/lon
     * (EPSG:4326)
     */
    public Coordinate metersToLatLon(double mx, double my) {
        double lon = (mx / originShift) * 180.0;
        double lat = (my / originShift) * 180.0;

        lat = 180 / Math.PI * (2 * Math.atan(Math.exp(lat * Math.PI / 180.0)) - Math.PI / 2.0);

        return new Coordinate(lon, lat);
    }

    /**
     * Converts EPSG:900913 to pyramid pixel coordinates in given zoom level
     */
    public Coordinate metersToPixels(double mx, double my, int zoom) {
        double res = resolution(zoom);

        double px = (mx + originShift) / res;
        double py = (my + originShift) / res;

        return new Coordinate(px, py);
    }

    /**
     * Converts pixel coordinates in given zoom level of pyramid to EPSG:900913
     */
    public Coordinate pixelsToMeters(double px, double py, int zoom) {
        double res = resolution(zoom);
        double mx = px * res - originShift;
        double my = py * res - originShift;

        return new Coordinate(mx, my);
    }

    /**
     * Resolution (meters/pixel) for given zoom level (measured at Equator)
     */
    public double resolution(int zoomLevel) {
        return initialResolution / matrixSize(zoomLevel);
    }

    /**
     * Compute the scale denominator of the resolution in degrees / pixel
     * 
     * @param zoom
     * @return
     */
    public double scaleDenominator(int zoom) {
        return resolution(zoom) * (1 / 0.00028);
    }

    /**
     * Returns the top-left corner of the top-left tile
     */
    public Coordinate topLeft() {
        return topLeft;
    }

}
