package com.scaleset.geo.math;


import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GoogleMapsTileMathTest extends Assert {

    private GoogleMapsTileMath tileMath = new GoogleMapsTileMath();
    private Coordinate topLeft;

    @Before
    public void before() {
        topLeft = tileMath.topLeft();
    }

    @Test
    public void testTopLeft() {
        Coordinate topLeft = tileMath.topLeft();
        assertNotNull(topLeft);
        Coordinate topLeft0_0_0 = tileMath.tileTopLeft(0, 0, 0);
        assertEquals(topLeft.x, topLeft0_0_0.x, 0.00001);
        assertEquals(topLeft.y, topLeft0_0_0.y, 0.00001);

        Coordinate topLeftLL = tileMath.metersToLngLat(tileMath.topLeft());
    }

    @Test
    public void testPixelToMeters() {
        Coordinate pixel0_0_0 = tileMath.pixelsToMeters(0, 0, 0);
        assertEquals(topLeft.x, pixel0_0_0.x, 0.00001);
        assertEquals(topLeft.y, pixel0_0_0.y, 0.00001);
        Coordinate pixel255 = tileMath.pixelsToMeters(255, 255, 0);
    }

    @Test
    public void testTileBbox() {
        Envelope bbox = tileMath.tileBbox(0, 0, 0);
        assertNotNull(bbox);
    }

    @Test
    public void testLngLatToMeters() {
        double lng = -102.82962;
        double lat = 43.47967;
        Coordinate lngLat = new Coordinate(lng, lat);
        Coordinate meters = tileMath.lngLatToMeters(lngLat);
    }
}
