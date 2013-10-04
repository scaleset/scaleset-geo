package com.scaleset.geo;


public interface FeatureFilter {
    boolean accept(Feature feature);
}