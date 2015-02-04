/*
 * Fontys Hogescholen ICT - Software Development
 * Modelling with Automata in Professional Practice
 * Project developer: Arjan Boschman
 * Startdate: October 2014
 */
package gui.automaton;

import client.Model;
import domain.Automaton;
import domain.AutomatonType;
import domain.State;
import domain.Transition;
import gui.AbstractController;
import gui.ScreenType;
import java.io.IOException;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Arjan
 */
public class AutomatonController extends AbstractController {

    private ChangeListener<AutomatonType> typeListener;

    @FXML
    private TextField tfInput;

    @FXML
    private TextField tfStackPush;

    @FXML
    private TextField tfTarget;

    @FXML
    private TextField tfName;

    @FXML
    private TextField tfAddTerminator;

    @FXML
    private ListView<Transition> lvTransitions;

    @FXML
    private TextField tfStackPop;

    @FXML
    private TextField tfSource;

    @FXML
    private ListView<State> lvTerminators;

    @FXML
    private Label lblAutomatonType;

    @FXML
    void btnSave_OnAction(ActionEvent event) throws IOException {
        clearListeners();
        getModel().getSelectedAutomaton().setName(tfName.getText());
        getModel().saveAutomaton(getModel().getSelectedAutomaton());
        getParent().setScreen(ScreenType.HOME);
    }

    private void clearListeners() {
        lvTransitions.setItems(null);
        lvTerminators.setItems(null);
        getModel().getSelectedAutomaton().getTypeProperty().removeListener(typeListener);
    }

    @FXML
    void btnDeleteTransition_OnAction(ActionEvent event) {
        final Transition transition = lvTransitions.getSelectionModel().getSelectedItem();
        final Automaton auto = getModel().getSelectedAutomaton();
        if (transition == null || auto == null) {
            return;
        }
        auto.removeTransition(transition);
    }

    @FXML
    void btnDeleteTerminator_OnAction(ActionEvent event) {
        final Automaton auto = getModel().getSelectedAutomaton();
        if (!tfAddTerminator.getText().isEmpty()) {
            auto.removeEndState(tfAddTerminator.getText());
        }
    }

    @FXML
    void btnAddTerminator_OnAction(ActionEvent event) {
        final Automaton auto = getModel().getSelectedAutomaton();
        if (!tfAddTerminator.getText().isEmpty()) {
            auto.addEndState(tfAddTerminator.getText());
        }
    }

    @FXML
    void btnAddTransition_OnAction(ActionEvent event) {
        final Automaton auto = getModel().getSelectedAutomaton();
        if (tfSource.getText().isEmpty() || tfTarget.getText().isEmpty() || auto == null) {
            return;
        }
        final String input
                = tfInput.getText().isEmpty() ? String.valueOf(Transition.EPSILON) : tfInput.getText();
        final AutomatonType type = getModel().getSelectedAutomaton().getTypeProperty().get();
        if ((type.performsStackOperation()
                || !tfStackPop.getText().isEmpty() || !tfStackPush.getText().isEmpty())
                && (input.charAt(0) == 'R' || input.charAt(0) == 'W')) {
            return;
        }
        final char read = tfStackPop.getText().isEmpty() ? Transition.EPSILON : tfStackPop.getText().charAt(0);
        final char write = tfStackPush.getText().isEmpty() ? Transition.EPSILON : tfStackPush.getText().charAt(0);
        auto.addTransition(tfSource.getText(), tfTarget.getText(), input, read, write);
    }

    @Override
    public void setModel(Model model) {
        super.setModel(model);
        this.typeListener = (ObservableValue<? extends AutomatonType> ov, AutomatonType t, AutomatonType t1) -> {
            Platform.runLater(() -> automatonType_OnChanged(t1));
        };
        tfName.setText(getModel().getSelectedAutomaton().getName());
        getModel().getSelectedAutomaton().getTypeProperty().addListener(typeListener);
        automatonType_OnChanged(getModel().getSelectedAutomaton().getTypeProperty().get());
        getModel().getSelectedAutomaton().addTransitionsObserver(lvTransitions);
        getModel().getSelectedAutomaton().addTerminatorsObserver(lvTerminators);
    }

    private void automatonType_OnChanged(AutomatonType type) {
        lblAutomatonType.setText("Automaton Type: " + type.name());
        if (type.performsIOOperation()) {
            tfStackPop.setDisable(true);
            tfStackPush.setDisable(true);
        } else {
            tfStackPop.setDisable(false);
            tfStackPush.setDisable(false);
        }
    }

}
