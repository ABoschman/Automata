/*
 * Fontys Hogescholen ICT - Software Development
 * Modelling with Automata in Professional Practice
 * Project developer: Arjan Boschman
 * Startdate: October 2014
 */
package files;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author Arjan
 * @param <T> The type of object you want to read from XML.
 */
public class XmlTranslator<T> {

    private final File file;

    public XmlTranslator(File file) {
        this.file = file;
    }

    public T read() throws IOException {
        XStream xs = new XStream(new StaxDriver());
        initXStream(xs);
        T output;
        try (FileInputStream is = new FileInputStream(file.getAbsolutePath())) {
            output = (T) xs.fromXML(is);
        }
        return output;
    }

    public void write(T object) throws IOException {
        XStream xs = new XStream(new StaxDriver());
        initXStream(xs);
        try (FileOutputStream fos = new FileOutputStream(file.getAbsolutePath())) {
            xs.toXML(object, fos);
        }
    }

    private void initXStream(XStream xs) {
        xs.registerConverter(new SimpleObjectPropertyConverter());
    }
}
