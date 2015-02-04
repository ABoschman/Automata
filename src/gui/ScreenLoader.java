/*
 * Fontys Hogescholen ICT - Software Development
 * Modelling with Automata in Professional Practice
 * Project developer: Arjan Boschman
 * Startdate: October 2014
 */
package gui;

import client.Model;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

/**
 *
 * @author Arjan
 */
public final class ScreenLoader {

    /**
     * Loads the screen or GUI element of given type and passes references of
     * the Model and containing Window to its {@link Controller}.
     *
     * @param type Describes exactly what GUI element needs to be loaded and
     * where it can be found.
     * @param model A reference of the application's data model.
     * @param parent A reference to the Window that is to contain the GUI
     * element.
     * @return Returns a LoadedScreen, wrapping both the Node and the
     * Controller.
     * @throws IOException If an IOException occurs while loading the resource.
     */
    public static LoadedScreen load(ScreenType type, Model model, Window parent) throws IOException {
        final URL uri = new File(type.getRelativePath()).toURI().toURL();
        final FXMLLoader loader = new FXMLLoader(uri);
        final Node node = loader.<Node>load();
        final Controller controller = loader.<Controller>getController();
        controller.setModel(model);
        controller.setWindow(parent);
        return new LoadedScreen(node, controller);
    }

    private ScreenLoader() {
    }

}
