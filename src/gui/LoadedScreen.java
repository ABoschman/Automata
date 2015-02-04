/*
 * Fontys Hogescholen ICT - Software Development
 * Modelling with Automata in Professional Practice
 * Project developer: Arjan Boschman
 * Startdate: October 2014
 */
package gui;

import javafx.scene.Node;

/**
 * Immutable data class. Wraps both the {@link Node} and the {@link Controller}
 * belonging to a certain screen or GUI element.
 *
 * @author Arjan
 */
public class LoadedScreen {

    private final Node node;
    private final Controller controller;

    public LoadedScreen(Node node, Controller controller) {
        this.node = node;
        this.controller = controller;
    }

    /**
     * @return Returns the scene.Node tied to this GUI element.
     */
    public Node getNode() {
        return node;
    }

    /**
     * @return Returns the controller tied to this GUI element.
     */
    public Controller getController() {
        return controller;
    }

}
