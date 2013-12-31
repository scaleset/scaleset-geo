package com.scaleset.geo.data;


import com.scaleset.geo.FeatureCollection;
import com.scaleset.geo.FeatureCollectionHandler;
import com.scaleset.geo.geonames.FeatureCodeFilter;
import com.scaleset.geo.geonames.GeoNamesParser;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ReferenceDataManagerTest extends Assert {

    @Test
    public void testDownloadGeonames() throws Exception {
        // could load this from a properties file or a json file
        Map<String, String> sources = new HashMap<String, String>();
        // nf.zip is only 5.4K vs 57M for us.zip
        sources.put("com.scaleset.geo.data/nf.zip", "http://download.geonames.org/export/dump/NF.zip");
        ReferenceDataManager dm = new ReferenceDataManager(sources);
        File nfZip = dm.getFile("com.scaleset.geo.data/nf.zip").get();
        assertNotNull(nfZip);
        assertTrue(nfZip.exists());

        // extract data from zip
        // - gz makes parsing much easier!
        // - might be useful to have a utility for this case since all of geonames is zipped
        ZipFile zipFile = new ZipFile(nfZip);
        ZipEntry zipEntry = zipFile.getEntry("NF.txt");
        InputStream in = zipFile.getInputStream(zipEntry);
        GeoNamesParser parser = new GeoNamesParser();
        FeatureCollectionHandler handler = new FeatureCollectionHandler();
        parser.handler(handler);
        parser.parse(in);
        in.close();
        zipFile.close();
        FeatureCollection fc = handler.getCollection();
        assertNotNull(fc);
        // last count size = 47
        assertTrue(fc.getFeatures().size() > 0);
    }


}
