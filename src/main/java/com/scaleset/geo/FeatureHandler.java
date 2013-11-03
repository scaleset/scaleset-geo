package com.scaleset.geo;

import com.vividsolutions.jts.geom.Envelope;

public interface FeatureHandler {

    void begin() throws Exception;

    void end() throws Exception;

    void handle(Feature feature) throws Exception;

    void handle(Envelope bbox);
}