/*
 * Fontys Hogescholen ICT - Software Development
 * Modelling with Automata in Professional Practice
 * Project developer: Arjan Boschman
 * Startdate: October 2014
 */
package client;

import domain.Automaton;
import files.FileExtensionFilter;
import files.FileUtility;
import files.XmlTranslator;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

/**
 * Used to interact with the model.
 *
 * @author Arjan
 */
public class Model {

    public static final String RESOURCE_FOLDER = "automataXml/";
    private final ObservableList<Automaton> automataII = FXCollections.observableArrayList();
    private Automaton selectedAutomaton = null;
    private final ChangeListener<Automaton> selectedAutomatonChangeListener
            = (ObservableValue<? extends Automaton> ov, Automaton t, Automaton t1) -> {
                selectedAutomaton = t1;
            };

    public Model() {
        init();
    }

    public ChangeListener<Automaton> getSelectedAutomatonChangeListener() {
        return selectedAutomatonChangeListener;
    }

    public Automaton getSelectedAutomaton() {
        return selectedAutomaton;
    }

    public void addAutomataObserver(ListView<Automaton> lv) {
        lv.setItems(automataII);
    }

    public final void init() {
        final List<File> files = FileUtility.listFilesForFolder(
                new File(RESOURCE_FOLDER), new FileExtensionFilter(".xml"));
        files.stream().map((file) -> new XmlTranslator<Automaton>(file)).forEach((translator) -> {
            try {
                final Automaton automaton = translator.read();
                addAutomaton(automaton);
            } catch (IOException ex) {
                Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    public void saveAutomaton(Automaton auto) {
        final File file = new File(RESOURCE_FOLDER + auto.getName() + ".Xml");
        XmlTranslator<Automaton> translator = new XmlTranslator<>(file);
        try {
            translator.write(auto);
        } catch (IOException ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addAutomaton(Automaton automaton) {
        automataII.add(automaton);
    }

    public Automaton makeAutomaton() {
        final Automaton newAuto = new Automaton(generateNewName());
        addAutomaton(newAuto);
        return newAuto;
    }

    public Automaton makeAutomaton(String name) {
        if (automatonNameExists(name)) {
            throw new IllegalArgumentException("Name already exists!");
        }
        final Automaton newAuto = new Automaton(name);
        addAutomaton(newAuto);
        return newAuto;
    }

    private String generateNewName() {
        int number = automataII.size();
        while (automatonNameExists("automaton" + number)) {
            number++;
        }
        return "automaton" + number;
    }

    private boolean automatonNameExists(String name) {
        return automataII.stream().anyMatch(auto -> auto.getName().equals(name));
    }

    public void remove(Automaton auto) {
        automataII.remove(auto);
    }

}
