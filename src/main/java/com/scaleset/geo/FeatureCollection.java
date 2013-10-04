package com.scaleset.geo;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.index.SpatialIndex;
import com.vividsolutions.jts.index.strtree.STRtree;

@JsonIgnoreProperties({ "type", "bbox" })
public class FeatureCollection {

    private Envelope bbox;
    private List<Feature> features = new ArrayList<Feature>();
    private String id;

    public boolean add(Feature feature) {
        return features.add(feature);
    }

    public Envelope getBbox() {
        return bbox;
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return "FeatureCollection";
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SpatialIndex rtee() {
        SpatialIndex result = new STRtree();
        for (Feature feature : features) {
            if (feature.getGeometry() != null) {
                result.insert(feature.getGeometry().getEnvelopeInternal(), feature);
            }
        }
        return result;
    }
}
