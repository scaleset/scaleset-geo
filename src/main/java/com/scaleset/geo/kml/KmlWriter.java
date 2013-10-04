package com.scaleset.geo.kml;

import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scaleset.geo.kml.KmlExtNs.GxAltitudeMode;
import com.scaleset.geo.kml.KmlNs.AltitudeMode;
import com.scaleset.utils.StringUtils;
import com.scaleset.utils.xml.StaxWriterUtils;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;

public class KmlWriter {

    public final static String ContentType = "application/vnd.google-earth.kml+xml";

    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-DD");

    private Logger log = LoggerFactory.getLogger(getClass());

    private XMLStreamWriter xmlWriter;

    public KmlWriter(Writer writer) {
        xmlWriter = StaxWriterUtils.createXMLStreamWriter(writer);
    }

    public KmlWriter(XMLStreamWriter xmlWriter) {
        this.xmlWriter = xmlWriter;
    }

    protected void attribute(String name, Object value) {
        try {
            xmlWriter.writeAttribute(name, StringUtils.valueOf(value));
        } catch (XMLStreamException e) {
            log.error("Error write xml", e);
        }
    }

    public KmlWriter coordinates(Coordinate coord) {
        textElement(KmlNs.coordinates, coord.x + "," + coord.y);
        return this;
    }

    public KmlWriter coordinates(double x, double y) {
        textElement(KmlNs.coordinates, x + "," + y);
        return this;
    }

    public KmlWriter endDocument() {
        endElement(KmlNs.Document);
        endElement(KmlNs.kml);
        try {
            xmlWriter.writeEndDocument();
        } catch (XMLStreamException e) {
            log.error("Error ending document", e);
        }
        return this;
    }

    protected void endElement(String name) {
        try {
            xmlWriter.writeEndElement();
        } catch (XMLStreamException e) {
            log.error("Error ending xml element " + name, e);
        }
    }

    public KmlWriter endFolder() {
        endElement(KmlNs.Folder);
        return this;
    }

    public KmlWriter endLod() {
        endElement(KmlNs.Lod);
        return this;
    }

    public KmlWriter endPlacemark() {
        endElement(KmlNs.Placemark);
        return this;
    }

    public KmlWriter endRegion() {
        endElement(KmlNs.Region);
        return this;
    }

    public KmlWriter extendedDataSimple(Map<String, String> extendedData) {
        startElement(KmlNs.ExtendedData);
        for (String key : extendedData.keySet()) {
            startElement(KmlNs.Data);
            attribute(KmlNs.name, key);
            String value = extendedData.get(key);
            textElement(KmlNs.value, value);
            endElement(KmlNs.Data);
        }
        endElement(KmlNs.ExtendedData);
        return this;
    }

    public KmlWriter id(String id) {
        attribute("id", id);
        return this;
    }

    public KmlWriter latLonAltBox(Envelope envelope, Double minAltitude, Double maxAltitude, AltitudeMode altitudeMode) {
        startElement(KmlNs.LatLonAltBox);
        textElement(KmlNs.north, envelope.getMaxY());
        textElement(KmlNs.south, envelope.getMinY());
        textElement(KmlNs.east, envelope.getMaxX());
        textElement(KmlNs.west, envelope.getMinX());
        textElement(KmlNs.minAltitude, minAltitude);
        textElement(KmlNs.maxAltitude, maxAltitude);
        textElement(KmlNs.altitudeMode, altitudeMode);
        endElement(KmlNs.LatLonAltBox);
        return this;
    }

    public KmlWriter latLonAltBox(Envelope envelope, Double minAltitude, Double maxAltitude, GxAltitudeMode altitudeMode) {
        startElement(KmlNs.LatLonAltBox);
        textElement(KmlNs.north, envelope.getMaxY());
        textElement(KmlNs.south, envelope.getMinY());
        textElement(KmlNs.east, envelope.getMaxX());
        textElement(KmlNs.west, envelope.getMinX());
        textElement(KmlNs.minAltitude, minAltitude);
        textElement(KmlNs.maxAltitude, maxAltitude);
        textElement(KmlExtNs.altitudeMode, altitudeMode);
        endElement(KmlNs.LatLonAltBox);
        return this;
    }

    public KmlWriter latLonBox(Envelope envelope, Double rotation) {
        startElement(KmlNs.LatLonBox);
        textElement(KmlNs.north, envelope.getMaxY());
        textElement(KmlNs.south, envelope.getMinY());
        textElement(KmlNs.east, envelope.getMaxX());
        textElement(KmlNs.west, envelope.getMinX());
        textElement(KmlNs.rotation, rotation);
        endElement(KmlNs.LatLonBox);
        return this;
    }

    public KmlWriter lod(int minLodPixels, Integer maxLodPixels, Integer minFadePixels, Integer maxFadePixels) {
        startLod();
        textElement(KmlNs.minLodPixels, minLodPixels);
        textElement(KmlNs.maxLodPixels, minFadePixels);
        textElement(KmlNs.minFadeExtent, minFadePixels);
        textElement(KmlNs.maxFadeExtent, maxFadePixels);
        endLod();
        return this;
    }

    public KmlWriter name(String name) {
        textElement(KmlNs.name, name);
        return this;
    }

    public KmlWriter point(Coordinate coord) {
        return point(coord.x, coord.y);
    }

    public KmlWriter point(double x, double y) {
        startElement(KmlNs.Point);
        coordinates(x, y);
        endElement(KmlNs.Point);
        return this;
    }

    public KmlWriter startDocument() {
        try {
            xmlWriter.writeStartDocument();
            startElement(KmlNs.kml);
            xmlWriter.writeDefaultNamespace(KmlNs.URI);
            startElement(KmlNs.Document);
        } catch (XMLStreamException e) {
            log.error("Error write xml", e);
        }
        return this;
    }

    protected void startElement(String name) {
        try {
            xmlWriter.writeStartElement(name);
        } catch (XMLStreamException e) {
            log.error("Error starting xml element " + name, e);
        }
    }

    public KmlWriter startFolder() {
        startElement(KmlNs.Folder);
        return this;
    }

    public KmlWriter startLod() {
        startElement(KmlNs.Folder);
        return this;
    }

    public KmlWriter startPlacemark() {
        startElement(KmlNs.Placemark);
        return this;
    }

    public KmlWriter startRegion() {
        startElement(KmlNs.Region);
        return this;
    }

    protected void textElement(String name, Object value) {
        if (value != null) {
            try {
                StaxWriterUtils.textElement(xmlWriter, name, StringUtils.valueOf(value), false);
            } catch (XMLStreamException e) {
                log.error("Error write xml element " + name, e);
            }
        }
    }

    public KmlWriter timestamp(Date when) {
        startElement(KmlNs.TimeStamp);
        textElement(KmlNs.when, dateFormat.format(when));
        try {
            xmlWriter.writeEndDocument();
        } catch (XMLStreamException e) {
            log.error("Error write xml document end", e);
        }
        return this;
    }

    /**
     * TODO:
     * <ul>
     * <li>geometry types
     * <li>logging
     * <li>validation
     * <li>convenience methods
     * <li>lookAt
     * <li>Region
     * <li>Style
     * <li>Overlay
     * <li>Network Link
     * <li>TimeSpan
     * </ul>
     */
    void todo() {

    }
}
