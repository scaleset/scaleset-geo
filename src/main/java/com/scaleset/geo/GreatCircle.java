package com.scaleset.geo;


import com.vividsolutions.jts.geom.Coordinate;

public class GreatCircle {

    public final static double EARTH_RADIUS = 6372797.560856;

    public static double distance(double lon1, double lat1, double lon2, double lat2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return EARTH_RADIUS * c;
    }

    public double distance(Coordinate start, Coordinate end) {
        return distance(start.x, start.y, end.x, end.y);
    }
}
