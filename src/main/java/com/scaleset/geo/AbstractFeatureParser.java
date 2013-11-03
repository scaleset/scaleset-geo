package com.scaleset.geo;

import com.vividsolutions.jts.geom.Envelope;

public abstract class AbstractFeatureParser implements FeatureParser {

    private FeatureHandler handler;
    private FeatureFilter filter;

    protected void begin() throws Exception {
        handler.begin();
    }

    protected void end() throws Exception {
        handler.end();
    }

    public FeatureParser filter(FeatureFilter filter) {
        this.filter = filter;
        return this;
    }

    protected void handle(Envelope bbox) {
        handler.handle(bbox);
    }

    protected void handle(Feature feature) {
        try {
            if (filter == null || filter.accept(feature)) {
                handler.handle(feature);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public FeatureParser handler(FeatureHandler handler) {
        this.handler = handler;
        return this;
    }

}
