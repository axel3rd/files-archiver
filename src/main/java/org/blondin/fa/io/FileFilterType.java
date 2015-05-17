/**
 * 
 */
package org.blondin.fa.io;

import java.io.File;
import java.io.FileFilter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * File filter
 */
public class FileFilterType implements FileFilter {

    /** Logger */
    private static final Log LOG = LogFactory.getLog(FileFilterType.class);

    /** File types authorized */
    private String[] filesType;

    /**
     * Constructor
     * 
     * @param filesType File types
     */
    public FileFilterType(String[] filesType) {
        super();
        this.filesType = filesType;
    }

    @Override
    public boolean accept(File file) {
        int delim = file.getName().lastIndexOf(".") + 1;
        String type = file.getName().substring(delim, file.getName().length());
        if (type == null || type.length() == 0) {
            LOG.warn("FileType error : " + file.getAbsolutePath());
            return false;
        }
        type = type.toUpperCase();
        for (int i = 0; i < filesType.length; i++) {
            if (filesType[i].equalsIgnoreCase(type)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Getter description
     * 
     * @return Description
     */
    public String getDescription() {
        return "File type filter";
    }

}
