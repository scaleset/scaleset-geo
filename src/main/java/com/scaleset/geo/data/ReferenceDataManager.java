package com.scaleset.geo.data;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Simple utility class to download data from the web and cache it locally. Why not limited to geospatial
 * uses, it helps when downloading reference datasets for testing and production use. Eventually I will add
 * support for S3 and other types of remote sources.
 */
public class ReferenceDataManager {

    File cacheDir;
    private Map<String, String> sources = new HashMap<String, String>();
    private ExecutorService executor = Executors.newFixedThreadPool(2);

    public ReferenceDataManager(Map<String, String> sources) {
        this.sources.putAll(sources);
        initCacheDir();
    }

    // TODO: Use a .partial suffix while downloading and then rename it
    public Future<File> getFile(String key) {
        String url = sources.get(key);
        return getFile(key, url);
    }

    // TODO: Use a .partial suffix while downloading and then rename it
    public Future<File> getFile(String key, String url) {
        Future<File> result;
        File file = new File(cacheDir, key);
        File parent = file.getParentFile();
        parent.mkdirs();
        result = executor.submit(new UrlDownloadTask(url, file));
        return result;
    }

    protected void initCacheDir() {
        File userHome = new File(System.getProperty("user.home"));
        cacheDir = new File(userHome, ".geo_data_cache");
        if (!cacheDir.exists()) {
            cacheDir.mkdir();
        }
    }

}
