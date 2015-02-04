/*
 * Fontys Hogescholen ICT - Software Development
 * Modelling with Automata in Professional Practice
 * Project developer: Arjan Boschman
 * Startdate: October 2014
 */
package gui.home;

import client.Model;
import domain.Automaton;
import domain.DeadlockFinder;
import gui.AbstractController;
import gui.ScreenType;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class HomeController extends AbstractController {

    private Automaton auto1 = null;
    private Automaton auto2 = null;

    @FXML
    private TextField tfEnterInput1;

    @FXML
    private TextField tfEnterInput2;

    @FXML
    private TextField tfAutomatonName1;

    @FXML
    private Label lblAutomatonType1;

    @FXML
    private TextField tfAutomatonName2;

    @FXML
    private Label lblAutomatonType2;

    @FXML
    private ListView<Automaton> lvAutomata;

    @FXML
    private Label lblInputValidity1;

    @FXML
    private TextArea taDeadlock;

    @FXML
    private Label lblInputValidity2;

    @FXML
    void btnNewAutomaton_OnAction(ActionEvent event) throws IOException {
        final Automaton newAuto = getModel().makeAutomaton();
        lvAutomata.getSelectionModel().select(newAuto);
        getParent().setScreen(ScreenType.AUTOMATON);
    }

    @FXML
    void btnEditAutomaton_OnAction(ActionEvent event) throws IOException {
        if (getModel().getSelectedAutomaton() == null) {
            return;
        }
        getParent().setScreen(ScreenType.AUTOMATON);
    }

    @FXML
    void btnSelect1_OnAction(ActionEvent event) {
        auto1 = getModel().getSelectedAutomaton();
        tfAutomatonName1.setText(auto1.getName());
        lblAutomatonType1.setText("Automaton Type: " + auto1.getTypeProperty().get());
        taDeadlock.setText("Deadlock info: ");
        tfEnterInput1.setEditable(true);
        checkInputValidity1();
    }

    @FXML
    void btnClear1_OnAction(ActionEvent event) {
        auto1 = null;
        tfEnterInput1.setText("");
        tfAutomatonName1.setText("");
        lblAutomatonType1.setText("Automaton Type: ");
        taDeadlock.setText("Deadlock info: ");
        lblInputValidity1.setText("Input Validity: ");
        tfEnterInput1.setEditable(false);
    }

    @FXML
    void tfEnterInput1_OnAction(ActionEvent event) {
        checkInputValidity1();
    }

    private void checkInputValidity1() {
        if (auto1.parseInput(tfEnterInput1.getText())) {
            lblInputValidity1.setText("Input Validity: Valid!");
        } else {
            lblInputValidity1.setText("Input Validity: Invalid!");
        }
    }

    @FXML
    void btnCheckDeadlock_OnAction(ActionEvent event) {
        if (auto1 == null || auto2 == null) {
            return;
        }
        final DeadlockFinder dlFinder = new DeadlockFinder(auto1, auto2);
        if (dlFinder.hasDeadlock()) {
            taDeadlock.setText("Deadlock info: " + dlFinder.getLog());
        } else {
            taDeadlock.setText("Deadlock info: No deadlock detected!");
        }
    }

    @FXML
    void btnSelect2_OnAction(ActionEvent event) {
        auto2 = getModel().getSelectedAutomaton();
        tfAutomatonName2.setText(auto2.getName());
        lblAutomatonType2.setText("Automaton Type: " + auto2.getTypeProperty().get());
        taDeadlock.setText("Deadlock info: ");
        tfEnterInput2.setEditable(true);
        checkInputValidity2();
    }

    @FXML
    void btnClear2_OnAction(ActionEvent event) {
        auto2 = null;
        tfEnterInput2.setText("");
        tfAutomatonName2.setText("");
        lblAutomatonType2.setText("Automaton Type: ");
        taDeadlock.setText("Deadlock info: ");
        lblInputValidity2.setText("Input Validity: ");
        tfEnterInput2.setEditable(false);
    }

    @FXML
    void tfEnterInput2_OnAction(ActionEvent event) {
        checkInputValidity2();
    }

    private void checkInputValidity2() {
        if (auto2.parseInput(tfEnterInput2.getText())) {
            lblInputValidity2.setText("Input Validity: Valid!");
        } else {
            lblInputValidity2.setText("Input Validity: Invalid!");
        }
    }

    @Override
    public void setModel(Model model) {
        super.setModel(model);
        getModel().addAutomataObserver(lvAutomata);
        lvAutomata.getSelectionModel().selectedItemProperty().
                addListener(getModel().getSelectedAutomatonChangeListener());
    }

}
