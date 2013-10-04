package com.scaleset.geo;

import java.io.InputStream;

public interface FeatureParser {

    public FeatureParser handler(FeatureHandler handler);

    public FeatureParser filter(FeatureFilter filter);

    public void parse(InputStream in) throws Exception;

}
