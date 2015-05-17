/**
 * 
 */
package org.blondin.fa.io;

import java.io.File;
import java.io.FileFilter;

/**
 * Directory filter
 */
public class FileFilterDirectory implements FileFilter {

    @Override
    public boolean accept(File file) {
        if (file == null) {
            return false;
        }
        return file.isDirectory();
    }

    /**
     * Getter description
     * 
     * @return Description
     */
    public String getDescription() {
        return "File directory filter";
    }

}
