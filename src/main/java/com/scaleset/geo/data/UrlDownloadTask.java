package com.scaleset.geo.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.concurrent.Callable;


public class UrlDownloadTask implements Callable<File> {

    private File file;
    private String href;

    public UrlDownloadTask(String href, File file) {
        this.href = href;
        this.file = file;
    }

    public File call() throws Exception {
        File result = file;

        if (!file.exists()) {
            FileOutputStream fos = null;
            try {
                URL url = new URL(href);
                ReadableByteChannel rbc = Channels.newChannel(url.openStream());
                fos = new FileOutputStream(result);
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return result;
    }
}
