package edu.pawkrol.graingrowth.view;

import edu.pawkrol.graingrowth.automata.AutomataResolver;
import edu.pawkrol.graingrowth.automata.Grid;
import edu.pawkrol.graingrowth.automata.seed.RandomSeeder;
import edu.pawkrol.graingrowth.automata.seed.Seeder;
import edu.pawkrol.graingrowth.automata.strategy.Strategy;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.util.Optional;

public class SeedDialog extends AppDialog<Boolean> {

    private AutomataResolver automataResolver;

    SeedDialog(AutomataResolver automataResolver) {
        super("seed-dialog.fxml");

        this.automataResolver = automataResolver;
    }

    @Override
    public Optional<Boolean> open() {
        dialog.setTitle("Seed");

        dialog.getDialogPane()
                .getButtonTypes()
                .addAll(ButtonType.OK, ButtonType.CANCEL);

        ComboBox<Seeder> seederCombo = (ComboBox<Seeder>) dialog.getDialogPane().lookup("#seederCombo");
        TextField nText = (TextField) dialog.getDialogPane().lookup("#nText");
        TextField rdText = (TextField) dialog.getDialogPane().lookup("#rdText");

        seederCombo.getItems().addAll(
                new RandomSeeder()
        );
        seederCombo.getSelectionModel().selectFirst();

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                Strategy strategy = automataResolver.getStrategy();
                Grid grid = automataResolver.getGrid();
                Seeder seeder = seederCombo.getSelectionModel().getSelectedItem();

                int n = Integer.parseInt(nText.getText());
                int rd = Integer.parseInt(rdText.getText());

                seeder.seed(strategy, grid, n, rd);

                return true;
            }

            return false;
        });

        return dialog.showAndWait();
    }
}
