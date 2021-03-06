package edu.pawkrol.graingrowth.view;

import edu.pawkrol.graingrowth.automata.AutomataResolver;
import edu.pawkrol.graingrowth.automata.strategy.*;
import edu.pawkrol.graingrowth.automata.tools.GrainTools;
import edu.pawkrol.graingrowth.automata.Grid;
import edu.pawkrol.graingrowth.automata.neighbourhood.*;
import edu.pawkrol.graingrowth.utils.SingleAction;
import edu.pawkrol.graingrowth.utils.GridExporter;
import edu.pawkrol.graingrowth.utils.GridPainter;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private final AutomataResolver automataResolver = new AutomataResolver();

    private GridPainter gridPainter;

    private SingleAction doneAction;

    private Stage stage;

    @FXML Pane canvasPane;
    @FXML Canvas canvas;
    @FXML ComboBox<Strategy> strategyCombo;
    @FXML ComboBox<Neighbourhood> neighbourhoodCombo;
    @FXML Menu toolsMenu;
    @FXML Menu viewMenu;
    @FXML HBox actionPanel;
    @FXML Label stepTxt;
    @FXML Button playBtn;
    @FXML Button stopBtn;
    @FXML Button doneBtn;
    @FXML TextField stepsTxt;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gridPainter = new GridPainter(canvas);

        strategyCombo.getItems().addAll(
                NaiveSeedGrowth.getInstance(),
                ShapeControlSeedGrowth.getInstance(),
                MonteCarlo.getInstance(),
                Recrystallization.getInstance()
        );
        strategyCombo.getSelectionModel().selectFirst();

        neighbourhoodCombo.getItems().addAll(
                new Moore(),
                new VonNeumann(),
                new FurtherMoore()
        );
        neighbourhoodCombo.getSelectionModel().selectFirst();
    }

    public void setStage(Stage stage) {
        this.stage = stage;

        setResizeListeners();
    }

    @FXML
    public void onPlay() {
        disableControls(true);

        Strategy strategy = strategyCombo.getSelectionModel().getSelectedItem();
        Neighbourhood neighbourhood = neighbourhoodCombo.getSelectionModel().getSelectedItem();

        automataResolver.setStrategy(strategy);
        automataResolver.setNeighbourhood(neighbourhood);

        int maxSteps = Integer.parseInt(stepsTxt.getText());
        automataResolver.setMaxIterations(maxSteps);

        if (automataResolver.getState() != Worker.State.READY) {
            automataResolver.restart();
        } else {
            automataResolver.init();
            automataResolver.start();
        }
    }

    @FXML
    public void onStop() {
        disableControls(false);
        automataResolver.cancel();
    }

    @FXML
    public void onDone() {
        if (doneAction == null) return;
        doneAction.onDone();
    }

    @FXML
    public void onNew() {
        new NewGridDialog()
                .open()
                .ifPresent(this::setUpNewGrid);
    }

    @FXML
    public void onSeed() {
        new SeedDialog(automataResolver.getGrid())
                .open()
                .ifPresent(o -> gridPainter.paint());
    }

    @FXML
    public void onInclusion() {
        new InclusionDialog(automataResolver)
                .open()
                .ifPresent(o -> gridPainter.paint());
    }

    @FXML
    public void onManualGrainSelector() {
        disableControls(true);

        List<Integer> states = new LinkedList<>();
        gridPainter.setGridSelectionEnabled(true)
                .observe(states::add);

        doneBtn.setVisible(true);
        doneAction = () -> {
            Grid grid = automataResolver.getGrid();
            new GrainDialog(grid, states)
                    .open()
                    .ifPresent(ok -> {
                        if (!ok) return;

                        disableControls(false);
                        gridPainter.setGridSelectionEnabled(false);
                        doneBtn.setVisible(false);
                    });
        };
    }

    @FXML
    public void onRandomGrainSelector() {
        new ParamDialog("Random grain selector", "Number of grains")
                .open()
                .ifPresent(n -> {
                    Grid grid = automataResolver.getGrid();
                    List<Integer> states = GrainTools.selectRandomGrains(grid, n.intValue());

                    new GrainDialog(grid, states)
                            .open()
                            .ifPresent(ok -> {
                                if (!ok) return;
                                gridPainter.paint();
                            });
                });
    }

    @FXML
    public void onAllGrainSelector() {
        Grid grid = automataResolver.getGrid();
        List<Integer> states = GrainTools.selectAllGrains(grid);

        new GrainDialog(grid, states)
                .open()
                .ifPresent(ok -> {
                    if (!ok) return;
                    gridPainter.paint();
                });
    }

    @FXML
    public void onOptions() {
        new ParamDialog("Options", "Step delay [ms]")
                .open()
                .ifPresent(delay -> automataResolver.setPeriod(Duration.millis(delay)));
    }

    @FXML
    public void onStrategy() {
        Strategy strategy = strategyCombo.getSelectionModel().getSelectedItem();

        if (strategy instanceof ShapeControlSeedGrowth) {
            neighbourhoodCombo.setDisable(true);
        } else {
            neighbourhoodCombo.setDisable(false);
        }
    }

    @FXML
    public void onNeighbourhood() {
        automataResolver.setNeighbourhood(neighbourhoodCombo.getSelectionModel().getSelectedItem());
    }

    @FXML
    public void onQuit() {
        stage.close();
    }

    @FXML
    public void onExportText() {
        GridExporter.exportGridText(stage.getScene().getWindow(), automataResolver.getGrid());
    }

    @FXML
    public void onImportText() {
        Grid grid = GridExporter.importGridText(stage.getScene().getWindow());
        if (grid == null) return;

        setUpNewGrid(grid);
    }

    @FXML
    public void onExportBitmap() {
        GridExporter.exportGridBitmap(stage.getScene().getWindow(), automataResolver.getGrid());
    }

    @FXML
    public void onImportBitmap() {
        Grid grid = GridExporter.importGridBitmap(stage.getScene().getWindow());
        if (grid == null) return;

        setUpNewGrid(grid);
    }

    @FXML
    public void onDistributeEnergy() {
        new EnergyDistributionDialog(automataResolver)
                .open()
                .ifPresent(ok -> {
                    if (ok) gridPainter.paint();
                });
    }

    @FXML
    public void onViewEnergy() {
        gridPainter.setMode(GridPainter.PaintMode.ENERGY);
        gridPainter.paint();
    }

    @FXML
    public void onViewNormal() {
        gridPainter.setMode(GridPainter.PaintMode.NORMAL);
        gridPainter.paint();
    }

    private void setUpNewGrid(Grid grid) {
        initAutomata(grid);
        initCanvasAndDrawGrid(grid);

        toolsMenu.setDisable(false);
        actionPanel.setDisable(false);
    }

    private void initAutomata(Grid grid) {
        automataResolver.setGrid(grid);
        automataResolver.setOnSucceeded(
                event -> Platform.runLater(() -> {
                    gridPainter.paint();
                    stepTxt.setText(String.valueOf(automataResolver.getIteration()));

                    if (automataResolver.isFinished()) {
                        disableControls(false);
                    }
                })
        );
        automataResolver.setOnCancelled(event -> disableControls(false));
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

    private void disableControls(boolean disabled) {
        playBtn.setDisable(disabled);
        stopBtn.setDisable(!disabled);
        toolsMenu.setDisable(disabled);
        viewMenu.setDisable(disabled);
    }

}
