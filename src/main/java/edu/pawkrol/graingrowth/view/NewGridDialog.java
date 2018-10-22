package edu.pawkrol.graingrowth.view;

import edu.pawkrol.graingrowth.automata.Grid;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.Optional;

public class NewGridDialog extends AppDialog<Grid> {

    NewGridDialog() {
        super("new-grid-dialog.fxml");
    }

    @Override
    public Optional<Grid> open() {
        dialog.setTitle("New grid");

        dialog.getDialogPane()
                .getButtonTypes()
                .addAll(ButtonType.OK, ButtonType.CANCEL);

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
