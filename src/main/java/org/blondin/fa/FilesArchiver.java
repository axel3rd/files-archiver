/**
 * 
 */
package org.blondin.fa;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.blondin.fa.io.FileFilterDirectory;
import org.blondin.fa.io.FileFilterType;
import org.blondin.fa.io.IOTools;
import org.blondin.fa.io.ZipTools;

/**
 * Archiver
 */
public class FilesArchiver {

    /** Logger */
    private static final Log LOG = LogFactory.getLog(FilesArchiver.class);

    /** Input directory */
    private File in;

    /** Output directory */
    private File out;

    /** File type to compress */
    private String[] filesType;

    /** File pattern to exclude : not to compress or delete */
    private String[] patternsForbiddenPath;

    /** Sub directory recursive */
    private boolean recursive;

    /**
     * Constructor
     * 
     * @param in Input directory
     * @param out Output directory
     * @param filesType File type to compress
     * @param patternsForbiddenPath File pattern to exclude : not to compress or delete
     * @param recursive Do archives in sub directories
     */
    public FilesArchiver(File in, File out, String[] filesType, String[] patternsForbiddenPath, boolean recursive) {
        super();
        this.in = in;
        this.out = out;
        this.filesType = filesType;
        this.patternsForbiddenPath = patternsForbiddenPath;
        this.recursive = recursive;
    }

    /**
     * Constructor
     * 
     * @param in Input directory
     * @param out Output directory
     * @param filesType File type to compress
     * @param patternsForbiddenPath File pattern to exclude : not to compress or delete
     */
    public FilesArchiver(File in, File out, String[] filesType, String[] patternsForbiddenPath) {
        this(in, out, filesType, patternsForbiddenPath, true);
    }

    /**
     * Archive
     * 
     * @throws IOException Problem in file creation
     */
    public void archive() throws IOException {
        LOG.info("-------");
        LOG.info("Archive '" + this.in.getAbsolutePath() + "' in '" + out.getAbsolutePath() + "'");

        // If path contains a forbidden pattern, delete destination and break
        if (IOTools.pathContainForbiddenPattern(this.out, this.patternsForbiddenPath)) {
            boolean dirOutMustBedeleted = this.out.exists();
            LOG.info("'In/Out' directory contains forbidden pattern so doesn't archived ('Out' directory must be deleted : " + dirOutMustBedeleted
                    + ")");
            if (dirOutMustBedeleted) {
                FileUtils.deleteDirectory(this.out);
            }
            return;
        }

        //
        // List files type in input directory
        //
        FileFilter filter = new FileFilterType(this.filesType);
        File[] files = this.in.listFiles(filter);
        LOG.info("Number of files to archive in 'In' directory : " + files.length);

        // Potential output directory
        File fileOut = new File(this.out, this.in.getName() + ".zip");

        // Check if directory must be compressed, depending output directory content
        boolean mustBeCompress = filesMustBeCompressed(this.out, fileOut, files.length);

        // Compress if needed, depending number of files in input directory
        LOG.info("Directory must be archived : " + mustBeCompress);
        if (mustBeCompress) {
            ZipTools.zipFiles(files, fileOut);
        }

        // Recursive if needed
        if (!this.recursive) {
            return;
        }
        FileFilter filterDir = new FileFilterDirectory();

        // List output directory to delete orphan (after changes in input directory, ...)
        deleteOrphan(filterDir);

        // List input directories for archive execution
        for (File chieldIn : this.in.listFiles(filterDir)) {
            // Output directory creation
            File chieldOut = new File(this.out, chieldIn.getName());

            // Let's go
            FilesArchiver archiverChield = new FilesArchiver(chieldIn, chieldOut, this.filesType, this.patternsForbiddenPath);
            archiverChield.archive();
        }
    }

    /**
     * Delete orphans in out directory
     * 
     * @param filterDir Filter
     * @throws IOException Problem
     */
    private void deleteOrphan(FileFilter filterDir) throws IOException {
        File[] chieldsOut = this.out.listFiles(filterDir);
        if (chieldsOut != null) {
            for (int i = 0; i < chieldsOut.length; i++) {
                File sameIn = new File(this.in, chieldsOut[i].getName());
                if (!sameIn.exists()) {
                    LOG.info("Deleting directory " + chieldsOut[i].getAbsolutePath());
                    FileUtils.deleteDirectory(chieldsOut[i]);
                }
            }
        }
    }

    /**
     * Check output directory to determine if input directory must be compressed.<br>
     * 
     * @param dir Output directory to check
     * @param zipFile ZIP file name
     * @param numberOfFiles Number of files in ZIP
     * @return true/false
     * @throws IOException Read problem
     */
    private static boolean filesMustBeCompressed(File dir, File zipFile, int numberOfFiles) throws IOException {
        // No file, go out
        if (numberOfFiles == 0) {
            return false;
        }

        boolean res = true;

        // List ZIP files of directory
        File[] files = dir.listFiles(new FileFilterType(new String[] { "zip" }));
        if (files != null && files.length > 1) {
            // Delete all ZIP
            LOG.info("The " + files.length + " ZIP files in 'Out' directory deleted because number > 1");
            IOTools.delete(files);
        } else if (files != null && files.length == 1) {
            // Verify coherence
            int nbrFilesInZip = ZipTools.getNumberOfFiles(zipFile);
            if (files[0].getName().equals(zipFile.getName()) && numberOfFiles == nbrFilesInZip && nbrFilesInZip > 0) {
                // If problem, file will be deleted
                res = false;
            } else {
                LOG.info("Deleting " + files[0].getAbsolutePath() + " because name isn't good");
                FileUtils.forceDelete(files[0]);
            }
        } // If 0, it is OK
        return res;
    }
}
