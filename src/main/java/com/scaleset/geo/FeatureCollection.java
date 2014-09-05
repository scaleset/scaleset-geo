package com.scaleset.geo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.index.SpatialIndex;
import com.vividsolutions.jts.index.strtree.STRtree;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FeatureCollection {

    private Envelope bbox;
    private CRS crs;
    private List<Feature> features = new ArrayList<Feature>();
    private String id;

    public boolean add(Feature feature) {
        return features.add(feature);
    }

    public Envelope getBbox() {
        return bbox;
    }

    public void setBbox(Envelope bbox) {
        this.bbox = bbox;
    }

    public CRS getCrs() {
        return crs;
    }

    public void setCrs(CRS crs) {
        this.crs = crs;
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }

    public String getId() {
        return id;
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
