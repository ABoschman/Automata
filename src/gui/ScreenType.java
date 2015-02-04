/*
 * Fontys Hogescholen ICT - Software Development
 * Modelling with Automata in Professional Practice
 * Project developer: Arjan Boschman
 * Startdate: October 2014
 */
package gui;

/**
 * Keeps track of all the GUI nodes and the relative path of the node's .fxml
 * files.
 *
 * @author Arjan
 */
public enum ScreenType {

    HOME("src/gui/home/HomeView.fxml"),
    AUTOMATON("src/gui/automaton/AutomatonView.fxml");

    private final String relativePath;

    private ScreenType(String relativePath) {
        this.relativePath = relativePath;
    }

    /**
     *
     * @return Returns the relative path of the screen's fxml file.
     */
    public String getRelativePath() {
        return relativePath;
    }

}
