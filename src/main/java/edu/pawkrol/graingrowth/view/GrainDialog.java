package edu.pawkrol.graingrowth.view;

import edu.pawkrol.graingrowth.automata.Grid;
import edu.pawkrol.graingrowth.automata.tools.GrainTools;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;

import java.util.List;
import java.util.Optional;

public class GrainDialog extends AppDialog<Boolean> {

    private Grid grid;
    private List<Integer> states;

    GrainDialog(Grid grid, List<Integer> states) {
        super("grain-dialog.fxml");

        this.states = states;
        this.grid = grid;
    }

    @Override
    public Optional<Boolean> open() {
        dialog.setTitle("Grain action");

        dialog.getDialogPane()
                .getButtonTypes()
                .addAll(ButtonType.OK, ButtonType.CANCEL);

        RadioButton removeRadio = (RadioButton) dialog.getDialogPane().lookup("#removeRadio");
        RadioButton keepAllRadio = (RadioButton) dialog.getDialogPane().lookup("#keepAllRadio");

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                if (removeRadio.isSelected()) {
                    removeGrains();
                } else {
                    keepGrainBorders(keepAllRadio.isSelected());
                }

                return true;
            }

            return null;
        });

        return dialog.showAndWait();
    }

    private void removeGrains() {
        new GrainRemoveDialog()
                .open()
                .ifPresent(dualPhase -> GrainTools.keepAndLockSelectedGrains(grid, states, dualPhase));
    }

    private void keepGrainBorders(boolean keepAll) {
        new ParamDialog("Grain border", "Grain border size", 1)
                .open()
                .ifPresent(size -> {
                    if (keepAll) {
                        GrainTools.keepAndLockGrainEdges(grid, states, size);
                    } else {
                        GrainTools.keepAndLockGrainOutsideEdges(grid, states, size);
                    }
                });
    }
}
