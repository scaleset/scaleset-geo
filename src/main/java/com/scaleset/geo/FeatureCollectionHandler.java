package com.scaleset.geo;

import com.vividsolutions.jts.geom.Envelope;

public class FeatureCollectionHandler implements FeatureHandler {

    private FeatureCollection collection = new FeatureCollection();

    public void begin() {
    }

    public void end() {
    }

    public FeatureCollection getCollection() {
        return collection;
    }

    public void handle(Feature feature) {
        collection.add(feature);
    }

    public void handle(Envelope bbox) {
        collection.setBbox(bbox);
    }

}