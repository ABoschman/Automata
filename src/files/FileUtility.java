/*
 * Fontys Hogescholen ICT - Software Development
 * Modelling with Automata in Professional Practice
 * Project developer: Arjan Boschman
 * Startdate: October 2014
 */
package files;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Arjan
 */
public final class FileUtility {

    public static List<File> listFilesForFolder(final File folder, FileFilter fileFilter) {
        final List<File> files = new ArrayList<>();
        if (!folder.isDirectory()) {
            return files;
        }
        for (final File fileEntry : folder.listFiles(fileFilter)) {
            if (fileEntry.isDirectory()) {
                files.addAll(listFilesForFolder(fileEntry, fileFilter));
            } else {
                files.add(fileEntry);
            }
        }
        return files;
    }

    private FileUtility() {
    }
}
