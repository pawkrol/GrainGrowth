package edu.pawkrol.graingrowth.view;

import edu.pawkrol.graingrowth.Main;
import edu.pawkrol.graingrowth.automata.Grid;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Dialog;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;

public abstract class AppDialog<T> {

    protected Dialog<T> dialog;

    AppDialog(String layoutName) {
        URL layoutUrl = Objects.requireNonNull(Main.class.getClassLoader().getResource(layoutName));
        FXMLLoader loader = new FXMLLoader(layoutUrl);
        Parent root = null;

        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        dialog = new Dialog<>();

        dialog.getDialogPane()
                .setContent(root);
    }

    public abstract Optional<T> open();

}
