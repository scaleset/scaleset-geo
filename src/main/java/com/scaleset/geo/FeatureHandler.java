package com.scaleset.geo;

public interface FeatureHandler {

    void begin() throws Exception;

    void end() throws Exception;

    void handle(Feature feature) throws Exception;
}