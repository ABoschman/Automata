/*
 * Fontys Hogescholen ICT - Software Development
 * Modelling with Automata in Professional Practice
 * Project developer: Arjan Boschman
 * Startdate: October 2014
 */
package files;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import domain.AutomatonType;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Used by XStream to convert ObjectProperties to Xml. Poor solution, only
 * really works for {@link AutomatonType}.
 *
 * @author Arjan
 */
public class SimpleObjectPropertyConverter implements Converter {

    @Override
    public boolean canConvert(Class clazz) {
        return clazz.equals(SimpleObjectProperty.class);
    }

    @Override
    public void marshal(Object value, HierarchicalStreamWriter writer,
            MarshallingContext context) {
        SimpleObjectProperty<?> property = (SimpleObjectProperty<?>) value;
        if (property.get() instanceof AutomatonType) {
            writer.startNode("PropertyValue");
            context.convertAnother(property.get());
            writer.endNode();
        }
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader,
            UnmarshallingContext context) {
        SimpleObjectProperty<AutomatonType> property = new SimpleObjectProperty<>();
        reader.moveDown();
        AutomatonType value = (AutomatonType) context.convertAnother(property, AutomatonType.class);
        property.set(value);
        reader.moveUp();
        return property;
    }

}
