package com.scaleset.geo.math;


import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
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
        assertEquals(-1.144694094E7, meters.x, 0.01);
        assertEquals(5385269.85689, meters.y, .001);
    }

    @Test
    public void testMetersToLngLat() {
        Geometry geom = new GeometryFactory().createPoint(new Coordinate(-1.144694094E7, 5385269.85689));
        Geometry lnglat = tileMath.metersToLngLat(geom);
        assertEquals(-102.8296, lnglat.getCoordinate().x, 0.001);
        assertEquals(43.47967, lnglat.getCoordinate().y, 0.001);
    }

    @Test
    public void testTileAlignBounds() {
        Coordinate ll = new Coordinate(-120, 20);
        Coordinate ur = new Coordinate(-80, 40);
        Envelope bbox = new Envelope(ll.x, ur.x, ll.y, ur.y);
        Coordinate tileLL = tileMath.lngLatToTile(ll, 4);
        Coordinate tileUR = tileMath.lngLatToTile(ur, 4);
        Envelope tileLLBBox = tileMath.tileBbox((int) tileLL.x, (int) tileLL.y, (int) tileLL.z);
        assertNotNull(tileLL);
    }

    @Test
    public void testLatLngToPixels() {
        Coordinate ll = new Coordinate(-122.1240234375, 47.67278567576541);
        Coordinate ur = new Coordinate(-122.113037109375, 47.68018294648414);
        Envelope bbox = tileMath.tileBboxLngLat(5268, 11434, 15);

        assertEquals(ll.x, bbox.getMinX(), 0.000000001);
        assertEquals(ll.y, bbox.getMinY(), 0.000000001);
        Coordinate c1 = new Coordinate(-122.115, 46.677);
        Coordinate c0 = new Coordinate((ll.x + ur.x) / 2.0, (ll.y + ur.y) / 2.0);

        Coordinate m1 = tileMath.lngLatToMeters(c1);
        Coordinate p1 = tileMath.metersToPixels(m1.x, m1.y, 15);
        long left = 256 * 5268;
        long top = 256 * 11434;

    }

}
