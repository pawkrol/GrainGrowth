package edu.pawkrol.graingrowth.view;

import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;

import java.util.Optional;

public class GrainRemoveDialog extends AppDialog<Boolean> {

    GrainRemoveDialog() {
        super("grain-remove-dialog.fxml");
    }

    @Override
    public Optional<Boolean> open() {
        dialog.setTitle("Structure");

        dialog.getDialogPane()
                .getButtonTypes()
                .addAll(ButtonType.OK, ButtonType.CANCEL);

        RadioButton dualPhaseRadio = (RadioButton) dialog.getDialogPane().lookup("#dualPhaseRadio");

        dialog.setResultConverter(dialogButton -> (dialogButton == ButtonType.OK) && dualPhaseRadio.isSelected());

        return dialog.showAndWait();
    }

}
