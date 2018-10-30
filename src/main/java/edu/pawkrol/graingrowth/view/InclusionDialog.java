package edu.pawkrol.graingrowth.view;

import edu.pawkrol.graingrowth.automata.AutomataResolver;
import edu.pawkrol.graingrowth.automata.Grid;
import edu.pawkrol.graingrowth.automata.tools.inclusion.Inclusion;
import edu.pawkrol.graingrowth.automata.tools.inclusion.RadiusRandomInclusion;
import edu.pawkrol.graingrowth.automata.tools.inclusion.SquareRandomInclusion;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.util.Optional;

public class InclusionDialog extends AppDialog<Boolean> {

    private AutomataResolver automataResolver;

    InclusionDialog(AutomataResolver automataResolver) {
        super("inclusion-dialog.fxml");

        this.automataResolver = automataResolver;
    }

    @Override
    public Optional<Boolean> open() {
        dialog.setTitle("Add inclusions");

        dialog.getDialogPane()
                .getButtonTypes()
                .addAll(ButtonType.OK, ButtonType.CANCEL);

        ComboBox<Inclusion> inclusionCombo = (ComboBox<Inclusion>) dialog.getDialogPane().lookup("#inclusionCombo");
        TextField nText = (TextField) dialog.getDialogPane().lookup("#nText");
        TextField rdText = (TextField) dialog.getDialogPane().lookup("#rdText");

        inclusionCombo.getItems().addAll(
                new SquareRandomInclusion(),
                new RadiusRandomInclusion()
        );
        inclusionCombo.getSelectionModel().selectFirst();

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                Inclusion inclusion = inclusionCombo.getSelectionModel().getSelectedItem();
                Grid grid = automataResolver.getGrid();

                int n = Integer.parseInt(nText.getText());
                int rd = Integer.parseInt(rdText.getText());
                boolean isFinished = automataResolver.isFinished();

                inclusion.add(grid, isFinished, n, rd);

                return true;
            }

            return null;
        });

        return dialog.showAndWait();
    }
}
