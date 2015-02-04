/*
 * Fontys Hogescholen ICT - Software Development
 * Modelling with Automata in Professional Practice
 * Project developer: Arjan Boschman
 * Startdate: October 2014
 */
package gui;

import client.Model;

/**
 * A GUI controller interface used by the GUI framework.
 *
 * @author Arjan
 */
public interface Controller {

    /**
     * Pass a reference to the data model to this controller.
     *
     * @param model
     */
    void setModel(Model model);

    /**
     * Pass a reference of the containing window to this controller.
     *
     * @param window
     */
    void setWindow(Window window);

}
