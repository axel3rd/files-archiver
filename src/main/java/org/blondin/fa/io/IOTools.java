/**
 * 
 */
package org.blondin.fa.io;

import java.io.File;

/**
 * IO Tools
 */
public final class IOTools {

    /**
     * Constructor
     */
    private IOTools() {
        super();
    }

    /**
     * If path contain forbidden pattern
     * 
     * @param dir Directory
     * @param patternsForbidden List of forbidden patterns
     * @return true/false
     */
    public static boolean pathContainForbiddenPattern(File dir, String[] patternsForbidden) {
        if (patternsForbidden != null && patternsForbidden.length > 0) {
            for (int i = 0; i < patternsForbidden.length; i++) {
                if (dir.getAbsolutePath().indexOf(patternsForbidden[i]) >= 0) {
                    return true;
                }
            }
        }
        return false;
    }
}
