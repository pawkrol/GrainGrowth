package edu.pawkrol.graingrowth.view;

import edu.pawkrol.graingrowth.automata.tools.recrystallization.RecrystallizationOptions;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

import java.util.Optional;

public class RecrystallizationDialog extends AppDialog<RecrystallizationOptions> {

    public RecrystallizationDialog() {
        super("recrystallization-dialog.fxml");
    }

    @Override
    public Optional<RecrystallizationOptions> open() {
        dialog.setTitle("Recrystallization mode");

        dialog.getDialogPane()
                .getButtonTypes()
                .addAll(ButtonType.OK, ButtonType.CANCEL);

        RadioButton constantRadio = (RadioButton) dialog.getDialogPane().lookup("#constantRadio");
        RadioButton increasingRadio = (RadioButton) dialog.getDialogPane().lookup("#increasingRadio");

        TextField nucleationValueText = (TextField) dialog.getDialogPane().lookup("#nucleationValueText");

        RadioButton gbRadio = (RadioButton) dialog.getDialogPane().lookup("#gbRadio");

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                RecrystallizationOptions options = new RecrystallizationOptions();

                if (constantRadio.isSelected()) {
                    options.setNucleationType(RecrystallizationOptions.NucleationType.CONSTANT);
                } else if (increasingRadio.isSelected()) {
                    options.setNucleationType(RecrystallizationOptions.NucleationType.INCREASING);
                } else {
                    options.setNucleationType(RecrystallizationOptions.NucleationType.STATIC);
                }

                options.setNucleationValue(Integer.parseInt(nucleationValueText.getText()));

                if (gbRadio.isSelected()) {
                    options.setNucleationGrainBoundary(true);
                } else {
                    options.setNucleationGrainBoundary(false);
                }

                return options;
            }

            return null;
        });

        return dialog.showAndWait();
    }

}
