package edu.pawkrol.graingrowth.view;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.Optional;

public class ParamDialog extends AppDialog<Integer>{

    private String title;
    private String paramName;

    public ParamDialog(String title, String paramName) {
        super("param-dialog.fxml");

        this.title = title;
        this.paramName = paramName;
    }

    @Override
    public Optional<Integer> open() {
        dialog.setTitle(title);

        dialog.getDialogPane()
                .getButtonTypes()
                .addAll(ButtonType.OK, ButtonType.CANCEL);

        TextField paramText = (TextField) dialog.getDialogPane().lookup("#paramText");
        Label paramLabel = (Label) dialog.getDialogPane().lookup("#paramLabel");

        paramLabel.setText(paramName);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return Integer.parseInt(paramText.getText());
            }

            return null;
        });

        return dialog.showAndWait();
    }

}
