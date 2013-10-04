package com.scaleset.geo.kml;

public class KmlExtNs {

    public final static String URI = "http://earth.google.com/kml/ext/2.2";

    public final static String PREFIX = "gx";

    public final static String h = "h";

    /**
     * Can be used instead of the OGC KML standard
     * <code>&lt;altitudeMode&gt;</code> element, and
     * accepts the following values in addition to the standard altitudeMode
     * values:
     * <ul>
     * <li>relativeToSeaFloor - Interprets the altitude as a value in meters
     * above the sea floor. If the KML feature is above land rather than sea,
     * the altitude will be interpreted as being above the ground.</li>
     * 
     * <li>clampToSeaFloor - The altitude specification is ignored, and the KML
     * feature will be positioned on the sea floor. If the KML feature is on
     * land rather than at sea, clampToSeaFloor will instead clamp to ground.</li>
     * </ul>
     */
    public final static String altitudeMode = "altitudeMode";

    public enum GxAltitudeMode {
        relativeToSeaFloor, clampToSeaFloor, relativeToGround, clampToGround, absolute
    }

}
