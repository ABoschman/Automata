/*
 * Fontys Hogescholen ICT - Software Development
 * Modelling with Automata in Professional Practice
 * Project developer: Arjan Boschman
 * Startdate: October 2014
 */
package gui;

import client.Model;
import java.io.IOException;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Wraps a {@link Stage}. Is responsible for maintaining a GUI window and
 * launching new windows.
 *
 * @author Arjan
 */
public class Window {

    private final Stage stage;
    private final Model model;

    public Window(Stage stage, Model model) {
        this.stage = stage;
        this.model = model;
    }

    /**
     * Set the screen that is to be displayed on this {@link Stage}.
     *
     * @param type The type of GUI element to load into this window.
     * @throws IOException If an IOException occurs trying to load the fxml
     * resource.
     */
    public void setScreen(ScreenType type) throws IOException {
        setScreen(type, stage.getTitle());
    }

    /**
     * Set the screen that is to be displayed on this {@link Stage}.
     *
     * @param type The type of GUI element to load into this window.
     * @param title The title of the window.
     * @throws IOException If an IOException occurs trying to load the fxml
     * resource.
     */
    public void setScreen(ScreenType type, String title) throws IOException {
        final LoadedScreen screen = ScreenLoader.load(type, model, this);
        final Scene scene = new Scene((Parent) screen.getNode());
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
    }

    /**
     * Creates a new window and sets its screen.
     *
     * @param type The type of GUI element to load into this window.
     * @param title The title of the window.
     * @throws IOException If an IOException occurs trying to load the fxml
     * resource.
     */
    public void launchNewStage(ScreenType type, String title) throws IOException {
        final Window window = new Window(new Stage(), model);
        window.setScreen(type, title);
    }

}
