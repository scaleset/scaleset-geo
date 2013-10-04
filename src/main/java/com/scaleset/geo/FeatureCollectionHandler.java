package com.scaleset.geo;

public class FeatureCollectionHandler implements FeatureHandler {

    private FeatureCollection collection = new FeatureCollection();

    public void begin() {
    }

    public void end() {
    }

    public void handle(Feature feature) {
        collection.add(feature);
    }

    public FeatureCollection getCollection() {
        return collection;
    }

}