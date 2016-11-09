/**
 * 
 */
package org.blondin.fa;

import java.io.File;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Main class<br>
 * 1 - Input directory to compress<br>
 * 2 - Output directory<br>
 * 3 - File types (comma separated)
 */
public final class Main {

    /** Logger */
    private static final Log LOG = LogFactory.getLog(Main.class);

    /** Number of arguments minimum */
    private static final int ARGS_MIN = 3;

    /**
     * Constructeur
     */
    private Main() {
        super();
    }

    /**
     * Main
     * 
     * @param args Arguments
     * @throws IOException Read problem
     */
    public static void main(String[] args) throws IOException {

        if (args == null || args.length < ARGS_MIN) {
            exitWithError(
                    "Usage : java -jar files-archiver-x.y.z.jar inputDirectory outputDirectory fileType1,fileType2 [patternForbidden1,patternForbiddenX]");
        }

        LOG.info("--- Files Archiver ---");
        LOG.info("In directory : " + args[0]);
        LOG.info("Out directory : " + args[1]);
        LOG.info("Files type : " + args[2]);
        if (args.length >= ARGS_MIN) {
            LOG.info(String.format("Forbidden Pattern(s)  : %s", args[ARGS_MIN]));
        }

        // Main directories
        File dirIn = new File(args[0]);
        File dirOut = new File(args[1]);
        String filesType = args[2];

        // Test existence
        if (!dirIn.exists()) {
            exitWithError("Directory 'In' doesn't exist");
        }
        if (!dirOut.exists()) {
            exitWithError("Directory 'Out' doesn't exist");
        }
        final String separator = ",";
        if (filesType == null || filesType.length() == 0 || filesType.split(separator).length == 0) {
            exitWithError("Files types doesn't valid");
        }

        // On y go
        String[] patternsForbidden = null;
        if (args.length >= ARGS_MIN) {
            patternsForbidden = args[ARGS_MIN].split(separator);
        }
        FilesArchiver archiver = new FilesArchiver(dirIn, dirOut, filesType.split(separator), patternsForbidden);
        archiver.archive();
    }

    /**
     * Exit error with message
     * 
     * @param message Message
     */
    private static void exitWithError(String message) {
        LOG.fatal(message);
        // CHECKSTYLE:OFF Error exit
        System.exit(1); // NOSONAR : Error exit
        // CHECKSTYLE:ON
    }
}
