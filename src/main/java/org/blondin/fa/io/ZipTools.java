/**
 * 
 */
package org.blondin.fa.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * ZIP Tool
 */
public final class ZipTools {

    /** Logger */
    private static final Log LOG = LogFactory.getLog(ZipTools.class);

    /** Buffer size */
    private static final int BUFFER_SIZE = 1024;

    /**
     * Constructor
     */
    private ZipTools() {
        super();
    }

    /**
     * ZIP some files
     * 
     * @param files Files to compress
     * @param zipFile File name
     * @throws IOException Problem
     */
    public static void zipFiles(File[] files, File zipFile) throws IOException {
        // Create a buffer for reading the files
        byte[] buf = new byte[BUFFER_SIZE];

        // Create directory
        zipFile.getParentFile().mkdirs();

        // Create the ZIP file
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile));

        // Compress the files
        for (int i = 0; i < files.length; i++) {
            FileInputStream in = new FileInputStream(files[i]);

            // Add ZIP entry to output stream.
            out.putNextEntry(new ZipEntry(files[i].getName()));

            // Transfer bytes from the file to the ZIP file
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }

            // Complete the entry
            out.closeEntry();
            in.close();
        }

        // Complete the ZIP file
        out.close();
    }

    /**
     * Get number of files in a ZIP
     * 
     * @param zipFile File name
     * @return Number of file, -1 if read problem
     * @throws IOException ZIP problem
     */
    public static int getNumberOfFiles(File zipFile) throws IOException {
        // Open the ZIP file
        int res = 0;
        ZipFile zf = null;
        try {
            zf = new ZipFile(zipFile);
            res = zf.size();
        } catch (ZipException e) {
            LOG.error(e.getMessage(), e);
            res = -1;
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
            res = -1;
        } finally {
            if (zf != null) { // NOSONAR FP SONARJAVA-1295 fixed in v3.9
                zf.close();
            }
        }
        return res;
    }
}
