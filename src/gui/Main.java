/*
 * Fontys Hogescholen ICT - Software Development
 * Modelling with Automata in Professional Practice
 * Project developer: Arjan Boschman
 * Startdate: October 2014
 */
package gui;

import client.Model;
import java.io.IOException;
import java.net.UnknownHostException;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author Arjan
 */
public class Main extends Application {

    /**
     * The Screen initially displayed when the application is started through
     * this class.
     */
    public static final ScreenType STARTING_SCREEN = ScreenType.HOME;
    /**
     * The title of the application.
     */
    public static final String TITLE = "Arjan Boschman Automata";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Application.launch(Main.class);
    }

    @Override
    public void start(Stage primaryStage) throws UnknownHostException, IOException {
        final Model model = new Model();
        final Window window = new Window(primaryStage, model);
        window.setScreen(STARTING_SCREEN, TITLE);
    }

}
