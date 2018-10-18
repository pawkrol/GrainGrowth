package edu.pawkrol.graingrowth.view;

import edu.pawkrol.graingrowth.Main;
import edu.pawkrol.graingrowth.automata.Grid;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;

public class NewGridDialog {

    public Optional<Grid> open() throws IOException {
        URL layoutUrl = Objects.requireNonNull(Main.class.getClassLoader().getResource("new-grid-dialog.fxml"));
        FXMLLoader loader = new FXMLLoader(layoutUrl);
        Parent root = loader.load();

        Dialog<Grid> dialog = new Dialog<>();
        dialog.setTitle("New grid");

        dialog.getDialogPane()
                .getButtonTypes()
                .addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.getDialogPane()
                .setContent(root);

        TextField widthTextField = (TextField) dialog.getDialogPane().lookup("#width");
        TextField heightTextField = (TextField) dialog.getDialogPane().lookup("#height");
        CheckBox cyclicCheckBox = (CheckBox) dialog.getDialogPane().lookup("#cyclic");

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                int width = Integer.parseInt(widthTextField.getText());
                int height = Integer.parseInt(heightTextField.getText());
                boolean cyclic = cyclicCheckBox.isSelected();

                return new Grid(width, height, cyclic);
            }

            return null;
        });

        return dialog.showAndWait();
    }

}
