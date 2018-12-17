package edu.pawkrol.graingrowth.view;

import edu.pawkrol.graingrowth.automata.AutomataResolver;
import edu.pawkrol.graingrowth.automata.tools.GrainTools;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;

import java.util.Optional;

public class EnergyDistributionDialog  extends AppDialog<Boolean> {

    private AutomataResolver automataResolver;

    EnergyDistributionDialog(AutomataResolver automataResolver) {
        super("energy-distribution-dialog.fxml");

        this.automataResolver = automataResolver;
    }

    @Override
    public Optional<Boolean> open() {
        dialog.setTitle("Distribute energy");

        dialog.getDialogPane()
                .getButtonTypes()
                .addAll(ButtonType.OK, ButtonType.CANCEL);

        RadioButton homogenousRadio = (RadioButton) dialog.getDialogPane().lookup("#homogenousRadio");

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                if (homogenousRadio.isSelected()) {
                    GrainTools.distributeEnergyHomogenous(automataResolver.getGrid(), 5);
                } else {
                    GrainTools.distributeEnergyHeterogenous(automataResolver.getGrid(), 2, 7);
                }

                return true;
            }

            return null;
        });

        return dialog.showAndWait();
    }

}
