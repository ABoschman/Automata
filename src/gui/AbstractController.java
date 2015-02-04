/*
 * Fontys Hogescholen ICT - Software Development
 * Modelling with Automata in Professional Practice
 * Project developer: Arjan Boschman
 * Startdate: October 2014
 */
package gui;

import client.Model;

/**
 * An abstract implementation of the Controller interface. All GUI controllers
 * should extend this class.
 *
 * @author Arjan
 */
public class AbstractController implements Controller {

    private Model model;
    private Window window;

    @Override
    public void setModel(Model model) {
        this.model = model;
    }

    /**
     * Returns the model that contains all the data for this application.
     *
     * @return
     */
    protected Model getModel() {
        return model;
    }

    @Override
    public void setWindow(Window window) {
        this.window = window;
    }

    /**
     *
     * @return Returns the Window this controller's Node is displayed within.
     */
    protected Window getParent() {
        return window;
    }

}
