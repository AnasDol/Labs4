package labs.view;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import labs.model.ants.Ant;
import labs.model.behaviour.WarriorBaseAI;
import labs.model.behaviour.WorkerBaseAI;
import labs.viewmodel.HabitatViewModel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.function.UnaryOperator;

public class HabitatController {

    @FXML
    public Button generationStartButton, generationStopButton;
    @FXML
    public Button warriorMovementStartButton, warriorMovementStopButton;
    @FXML
    public Button workerMovementStartButton, workerMovementStopButton;
    @FXML
    private CheckMenuItem timeCheckMenuItem, statisticsCheckMenuItem;
    @FXML
    private CheckBox statisticsCheckBox;
    @FXML
    private RadioButton timeRadioButton1, timeRadioButton2;
    @FXML
    private Text timeText, statisticsText;
    @FXML
    private HBox timeBox;
    @FXML
    private TextField P1TextField, P2TextField, durationTextField;
    @FXML
    private TextField warriorLifetimeTextField, workerLifetimeTextField;
    @FXML
    private ComboBox<Integer> warriorComboBox, workerComboBox;
    @FXML
    private TextField NTextField, receiverTextField;
    @FXML
    private VBox vbox;
    private ListView<String> onlineListView;
    @FXML
    private Text usernameText;
    @FXML
    private Pane pane;

    private BooleanProperty isTimeVisible;
    private BooleanProperty isStatisticsVisible;

    private ImageView redAnthillImage, blackAnthillImage;

    private HabitatViewModel viewModel;

    public HabitatController(){}

    public void init(HabitatViewModel viewModel, Scene scene){

        this.viewModel=viewModel;
        Bindings.bindContent(pane.getChildren(),viewModel.getImages());

        Bindings.bindBidirectional(P1TextField.textProperty(), viewModel.p1Property().asObject(), new IntegerStringConverter());
        Bindings.bindBidirectional(P2TextField.textProperty(), viewModel.p2Property().asObject(), new IntegerStringConverter());
        Bindings.bindBidirectional(durationTextField.textProperty(), viewModel.periodProperty().asObject(), new DoubleStringConverter());
        Bindings.bindBidirectional(warriorLifetimeTextField.textProperty(), viewModel.lifeimeWarriorProperty().asObject(), new IntegerStringConverter());
        Bindings.bindBidirectional(workerLifetimeTextField.textProperty(), viewModel.lifeimeWorkerProperty().asObject(), new IntegerStringConverter());

        generationStartButton.disableProperty().bind(viewModel.workingProperty());
        generationStopButton.disableProperty().bind(viewModel.workingProperty().not());
        warriorMovementStartButton.disableProperty().bind(viewModel.warriorMovingProperty());
        warriorMovementStopButton.disableProperty().bind(viewModel.warriorMovingProperty().not());
        workerMovementStartButton.disableProperty().bind(viewModel.workerMovingProperty());
        workerMovementStopButton.disableProperty().bind(viewModel.workerMovingProperty().not());

        isTimeVisible = new SimpleBooleanProperty(false);
        changeTimeState();
        timeText.visibleProperty().bind(isTimeVisible);
        timeText.textProperty().bind(viewModel.timeTextProperty());
        timeBox.setStyle("-fx-background-color: white;");
        timeBox.visibleProperty().bind(isTimeVisible);
        pane.getChildren().add(timeBox);

        isStatisticsVisible = new SimpleBooleanProperty(false);
        changeStatisticsState();
        statisticsText.visibleProperty().bind(isStatisticsVisible);
        statisticsText.textProperty().bind(viewModel.statisticsProperty());

        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String input = change.getText();
            if (input.matches("[0-9]*")) {
                return change;
            }
            return null;
        };

        UnaryOperator<TextFormatter.Change> doubleFilter = change -> {
            String input = change.getText();
            if (input.matches("(([1-9][0-9]*)|0)?(\\.[0-9]*)?")) {
                return change;
            }
            return null;
        };

        P1TextField.setTextFormatter(new TextFormatter<String>(integerFilter));
        P2TextField.setTextFormatter(new TextFormatter<String>(integerFilter));
        NTextField.setTextFormatter(new TextFormatter<String>(integerFilter));
        durationTextField.setTextFormatter(new TextFormatter<String>(doubleFilter));
        warriorLifetimeTextField.setTextFormatter(new TextFormatter<String>(integerFilter));
        workerLifetimeTextField.setTextFormatter(new TextFormatter<String>(integerFilter));

        scene.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent key) -> {
            if (key.getCode().equals(KeyCode.B)) {
                generationStart();
            }
        });

        scene.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent key) -> {
            if (key.getCode().equals(KeyCode.E)) {
                generationStop();
            }
        });

        scene.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent key) -> {
            if (key.getCode().equals(KeyCode.T)) {
                changeTimeState();
            }
        });

        setAnthills();

        setComboBoxes();

        onlineListView = new ListView<String>(viewModel.getUserList());
        vbox.getChildren().add(onlineListView);

        MultipleSelectionModel<String> langsSelectionModel = onlineListView.getSelectionModel();
        // устанавливаем слушатель для отслеживания изменений
        langsSelectionModel.selectedItemProperty().addListener(new ChangeListener<String>(){

            public void changed(ObservableValue<? extends String> changed, String oldValue, String newValue){

                receiverTextField.setText(newValue);
            }
        });

        usernameText.setText("Ваш уникальный идентификатор: "+viewModel.getUsername());

    }

    public void setAnthills() {
        redAnthillImage = new ImageView();
        try {
            redAnthillImage.setImage(new Image(new FileInputStream("src/main/resources/labs/red anthill.png")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        redAnthillImage.setFitWidth(100);
        redAnthillImage.setFitHeight(100);
        redAnthillImage.setX(Ant.RedAnthillX - redAnthillImage.getFitWidth()/2);
        redAnthillImage.setY(Ant.RedAnthillY - redAnthillImage.getFitHeight()/2);
        //redAnthillImage.toBack();

        blackAnthillImage = new ImageView();
        try {
            blackAnthillImage.setImage(new Image(new FileInputStream("src/main/resources/labs/black anthill.png")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        blackAnthillImage.setFitWidth(100);
        blackAnthillImage.setFitHeight(100);
        blackAnthillImage.setX(Ant.BlackAnthillX - blackAnthillImage.getFitWidth()/2);
        blackAnthillImage.setY(Ant.BlackAnthillY - blackAnthillImage.getFitHeight()/2);
        //blackAnthillImage.toBack();

        pane.getChildren().addAll(redAnthillImage,blackAnthillImage);
    }

    public void generationStart() {
        viewModel.generationStart();
    }

    public void generationStop() {
        viewModel.generationStop();
    }

    public void changeTimeState() {
        isTimeVisible.setValue(isTimeVisible.not().getValue());
        timeRadioButton1.setSelected(isTimeVisible.get());
        timeRadioButton2.setSelected(!isTimeVisible.get());
        timeCheckMenuItem.setSelected(isTimeVisible.get());
    }

    public void changeStatisticsState() {
        isStatisticsVisible.setValue(isStatisticsVisible.not().getValue());
        statisticsCheckBox.setSelected(isStatisticsVisible.get());
        statisticsCheckMenuItem.setSelected(isStatisticsVisible.get());
    }

    public void saveSettings() {
        viewModel.saveSettings();
    }

    public void openTable() {
        //viewModel.openTable();
    }

    public void warriorMovementStart() {
        viewModel.movementStart("warrior");
    }

    public void warriorMovementStop() {
        viewModel.movementStop("warrior");
    }

    public void workerMovementStart() {
        viewModel.movementStart("worker");
    }

    public void workerMovementStop() {
        viewModel.movementStop("worker");
    }

    public void setComboBoxes() {

        ObservableList<Integer> comboItems = FXCollections.observableArrayList(1,2,3,4,5,6,7,8,9,10);

        warriorComboBox.setItems(comboItems);
        warriorComboBox.setValue(5);

        warriorComboBox.setOnAction((EventHandler<ActionEvent>) event -> {
            System.out.println(warriorComboBox.getValue());
            WarriorBaseAI.setUserPriority(warriorComboBox.getValue());
        });

        workerComboBox.setItems(comboItems);
        workerComboBox.setValue(5);

        workerComboBox.setOnAction((EventHandler<ActionEvent>) event -> {
            System.out.println(workerComboBox.getValue());
            WorkerBaseAI.setUserPriority(workerComboBox.getValue());
        });

    }

    public void request() {
        viewModel.request(Integer.parseInt(NTextField.getText()), receiverTextField.getText());
    }

}
