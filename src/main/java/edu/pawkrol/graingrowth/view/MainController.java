package edu.pawkrol.graingrowth.view;

import edu.pawkrol.graingrowth.automata.AutomataResolver;
import edu.pawkrol.graingrowth.automata.Grid;
import edu.pawkrol.graingrowth.automata.neighbourhood.Neighbourhood;
import edu.pawkrol.graingrowth.automata.neighbourhood.VonNeumann;
import edu.pawkrol.graingrowth.automata.strategy.NaiveSeedGrowth;
import edu.pawkrol.graingrowth.automata.strategy.Strategy;
import edu.pawkrol.graingrowth.utils.GridPainter;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private final AutomataResolver automataResolver = new AutomataResolver();

    private GridPainter gridPainter;

    private Stage stage;

    @FXML Pane canvasPane;
    @FXML Canvas canvas;
    @FXML ComboBox<Strategy> strategyCombo;
    @FXML ComboBox<Neighbourhood> neighbourhoodCombo;
    @FXML Menu toolsMenu;
    @FXML HBox actionPanel;
    @FXML TextField delayTxt;
    @FXML Label stepTxt;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gridPainter = new GridPainter(canvas);

        strategyCombo.getItems().addAll(
                NaiveSeedGrowth.getInstance()
        );
        strategyCombo.getSelectionModel().selectFirst();

        neighbourhoodCombo.getItems().addAll(
                new VonNeumann()
        );
        neighbourhoodCombo.getSelectionModel().selectFirst();
    }

    public void setStage(Stage stage) {
        this.stage = stage;

        setResizeListeners();
    }

    public void onPlay() {
        automataResolver.start();
    }

    public void onNew() throws IOException {
        new NewGridDialog()
                .open()
                .ifPresent(grid -> {
                    initAutomata(grid);
                    initCanvasAndDrawGrid(grid);

                    toolsMenu.setDisable(false);
                    actionPanel.setDisable(false);
                });
    }

    public void onQuit() {
        stage.close();
    }

    public void onSeed() {
        new SeedDialog(automataResolver)
                .open()
                .ifPresent(o -> gridPainter.paint());
    }

    public void onStrategy() {
        automataResolver.setStrategy(strategyCombo.getSelectionModel().getSelectedItem());
    }

    public void onNeighbourhood() {
        automataResolver.setNeighbourhood(neighbourhoodCombo.getSelectionModel().getSelectedItem());
    }

    private void initAutomata(Grid grid) {
        Strategy strategy = strategyCombo.getSelectionModel().getSelectedItem();
        Neighbourhood neighbourhood = neighbourhoodCombo.getSelectionModel().getSelectedItem();
        int delay = Integer.parseInt(delayTxt.getText());

        automataResolver.setGrid(grid);
        automataResolver.setStrategy(strategy);
        automataResolver.setNeighbourhood(neighbourhood);
        automataResolver.setOnSucceeded(
                event -> Platform.runLater(() -> {
                    gridPainter.paint();
                    stepTxt.setText(String.valueOf(automataResolver.getIteration()));
                })
        );
        automataResolver.setPeriod(Duration.millis(delay));
        automataResolver.init();
    }

    private void initCanvasAndDrawGrid(Grid grid) {
        canvas.setWidth(canvasPane.getWidth());
        canvas.setHeight(canvasPane.getHeight());

        gridPainter.init(grid);
        gridPainter.paint();
    }

    private void setResizeListeners() {
        if (canvasPane == null) return;

        ChangeListener<Number> resizeListener = (o, nV, oV) -> onResize();

        canvasPane.heightProperty().addListener(resizeListener);
        canvasPane.widthProperty().addListener(resizeListener);
    }

    private void onResize() {
        canvas.setWidth(canvasPane.getWidth());
        canvas.setHeight(canvasPane.getHeight());

        gridPainter.onResize();
    }

}
