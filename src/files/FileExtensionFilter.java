/*
 * Fontys Hogescholen ICT - Software Development
 * Modelling with Automata in Professional Practice
 * Project developer: Arjan Boschman
 * Startdate: October 2014
 */
package files;

import java.io.File;
import java.io.FileFilter;
import java.util.Locale;

/**
 * Filters files based on their extensions.
 *
 * @author Arjan
 */
public class FileExtensionFilter implements FileFilter {

    private final String[] extensions;

    /**
     * Create an instance of FileExtensionFilter that filters java.io.Files
     * based on the suffixes you provide.
     *
     * @param extensions The suffixes that the filter will check for. If the
     * file that's being tested ends with one of these suffixes, the test will
     * pass. File extensions are not case sensitive.
     */
    public FileExtensionFilter(String... extensions) {
        this.extensions = new String[extensions.length];
        for (int i = 0; i < extensions.length; i++) {
            this.extensions[i] = getExtension(extensions[i]);
        }
    }

    private String getExtension(String extInput) {
        String modified = extInput;
        if (extInput.charAt(0) != '.') {
            modified = "." + extInput;
        }
        return modified.toLowerCase(Locale.ENGLISH);
    }

    /**
     * Tests a File.
     *
     * @param pathname The File to test. Directories will automatically return
     * false.
     * @return Whether or not it has the right suffix. Only one of the suffixes
     * has to match, and file extensions are not case sensitive.
     */
    @Override
    public boolean accept(File pathname) {
        if (!pathname.isFile() || !pathname.exists()) {
            return false;
        }
        String name = pathname.getName().toLowerCase();
        for (String ext : extensions) {
            if (name.endsWith(ext)) {
                return true;
            }
        }
        return false;
    }
}
