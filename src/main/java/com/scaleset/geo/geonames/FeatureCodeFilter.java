package com.scaleset.geo.geonames;

import com.scaleset.geo.Feature;
import com.scaleset.geo.FeatureFilter;

public class FeatureCodeFilter implements FeatureFilter {

    private String featureCode;

    public FeatureCodeFilter(String featureCode) {
        this.featureCode = featureCode;
    }

    public boolean accept(Feature place) {
        boolean result = false;
        if (featureCode.equals(place.get("featureCode"))) {
            result = true;
        }
        return result;
    }

}