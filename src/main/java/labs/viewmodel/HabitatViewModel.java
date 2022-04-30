package labs.viewmodel;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import labs.model.DataModel;
import labs.model.DataModelManager;
import labs.model.ants.Ant;
import labs.model.behaviour.BaseAI;
import labs.model.behaviour.WarriorBaseAI;
import labs.model.behaviour.WorkerBaseAI;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

public class HabitatViewModel {

    private Timeline generationTimeline, secondsTimeline;
    private long timePause, timeStart, timeStop;

    private BooleanProperty working, warriorMoving, workerMoving;
    private IntegerProperty P1, P2;
    private DoubleProperty period;
    private StringProperty timeText;
    private StringProperty statictics;
    private IntegerProperty lifetimeWorker, lifetimeWarrior;

    private ObservableList<ImageView> images;
    ObservableList<Map.Entry<Integer, Integer>> listData;
    TableView<Map.Entry<Integer, Integer>> tableView;

    private DataModel dataModel;

    public HabitatViewModel(DataModel dataModel) {
        this.dataModel = dataModel;
        images = FXCollections.observableList(new ArrayList<>());
        for (Ant ant: dataModel.getAnts()) {
            images.add(ant.getImage());
        }
        working = new SimpleBooleanProperty(false);
        warriorMoving = new SimpleBooleanProperty(false);
        workerMoving = new SimpleBooleanProperty(false);
        P1 = new SimpleIntegerProperty();
        P2 = new SimpleIntegerProperty();
        period = new SimpleDoubleProperty();
        statictics = new SimpleStringProperty("There is no data yet");
        lifetimeWarrior = new SimpleIntegerProperty();
        lifetimeWorker = new SimpleIntegerProperty();
        load();
        initGenerationTimer();
        initUpdateTimer();
        timeText = new SimpleStringProperty("seconds: 0");
        tableView = new TableView<>();
    }

    public void initGenerationTimer() {
        generationTimeline = new Timeline();
        generationTimeline.setCycleCount(Timeline.INDEFINITE);
        generationTimeline.getKeyFrames().addAll(new KeyFrame(Duration.millis(period.get()*1000), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dataModel.generateAnt(getSeconds());
                images.add(dataModel.getLast().getImage());
            }
        }));
    }

    public void initUpdateTimer() {
        secondsTimeline = new Timeline();
        secondsTimeline.setCycleCount(Timeline.INDEFINITE);
        secondsTimeline.getKeyFrames().addAll(new KeyFrame(Duration.millis(40), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                timeText.setValue("seconds: " + getSeconds());
                statictics.setValue("During the simulation were created " + Ant.n
                        + " ants.\nRed ants: " + (Ant.n_red_worker + Ant.n_red_warrior)
                        + " including " + Ant.n_red_worker + " workers and " + Ant.n_red_warrior + " warriors.\n"
                        + "Black ants: " + (Ant.n_black_worker + Ant.n_black_warrior) + " including "
                        + Ant.n_black_worker + " workers and " + Ant.n_black_warrior + " warriors.");
                getRidOfBodies();
            }
        }));
    }

    public void getRidOfBodies() {
        synchronized (dataModel.getAnts()) {
            Ant ant;
            for (int i = 0;i<dataModel.getAnts().size();i++) {
                ant = dataModel.getAnts().get(i);
                if (getSeconds()-ant.birthTime>=ant.lifeTime) {
                    images.remove(ant.getImage());
                    dataModel.getIds().remove(ant.index);
                    dataModel.getMap().remove(ant.index);
                    dataModel.getAnts().remove(ant);
                }
            }
        }
    }

    public int getSeconds() {
        return (int)((System.currentTimeMillis() - timeStart - timePause)/1000);
    }

    public void load() {
        P1.set(dataModel.getP1());
        P2.set(dataModel.getP2());
        period.set(dataModel.getPeriod());
        lifetimeWarrior.set(dataModel.getLifeTimeWarrior());
        lifetimeWorker.set(dataModel.getLifeTimeWorker());
    }

    public void generationStart() {
        working.setValue(true);
        timePause = 0;
        timeStart = System.currentTimeMillis();
        images.clear();
        dataModel.clear();
        Ant.clearStatics();
        generationTimeline.play();
        secondsTimeline.play();
        System.out.println("generationStart");
    }

    public void generationStop() {
        timeStop = System.currentTimeMillis();
        working.setValue(false);
        generationTimeline.stop();
        secondsTimeline.stop();
        getReport();
        System.out.println("generationStop");
    }

    public void generationResume() {
        timePause += (System.currentTimeMillis()-timeStop);
        working.setValue(true);
        generationTimeline.play();
        secondsTimeline.play();
    }

    public void movementStart(String who) {

        if (who.equals("warrior")) {
            WarriorBaseAI.setRunning(true);
            warriorMoving.setValue(true);
            System.out.println("warriorMovementStart");
        }
        else if (who.equals("worker")) {
            WorkerBaseAI.setRunning(true);
            workerMoving.setValue(true);
            System.out.println("workerMovementStart");
        }

    }

    public void movementStop(String who) {

        if (who.equals("warrior")) {
            WarriorBaseAI.setRunning(false);
            warriorMoving.setValue(false);
            System.out.println("warriorMovementStop");
        }
        else if (who.equals("worker")) {
            WorkerBaseAI.setRunning(false);
            workerMoving.setValue(false);
            System.out.println("workerMovementStop");
        }

        //BaseAI.setRunning(false);

        System.out.println("movementStop");
    }

    public ObservableList<ImageView> getImages() {
        return images;
    }

    public void saveSettings() {
        if (P1.get()<=100 && P1.get()>=0 && P2.get()<=100 && P2.get()>=0 && period.get()<=10.0 && period.get()>=0.1
                && lifetimeWorker.get()<=20 && lifetimeWorker.get()>=1 && lifetimeWarrior.get()<=20 && lifetimeWarrior.get()>=1)  {
            dataModel.setP1(P1.get());
            dataModel.setP2(P2.get());
            dataModel.setPeriod(period.get());
            dataModel.setLifeTimeWarrior(lifetimeWarrior.get());
            dataModel.setLifeTimeWorker(lifetimeWorker.get());
            //restartTimer();
        }
        else {
            load();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Wrong input!");
            alert.setContentText("Probabilities must be in range 0-100. Duration must be in range 0.1-10.0. Lifetime must be in range 1-20.");
            Optional<ButtonType> option = alert.showAndWait();
        }
    }

    public void getReport() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Pause");
        alert.setHeaderText("Statistics: ");
        alert.setContentText(statictics.getValue());
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == ButtonType.CANCEL) {
            generationResume();
        }
    }


    public BooleanProperty workingProperty() {
        return working;
    }
    public BooleanProperty warriorMovingProperty() {
        return warriorMoving;
    }
    public BooleanProperty workerMovingProperty() {
        return workerMoving;
    }
    public IntegerProperty p1Property() {
        return P1;
    }
    public IntegerProperty p2Property() {
        return P2;
    }
    public DoubleProperty periodProperty() {
        return period;
    }
    public StringProperty timeTextProperty() {
        return timeText;
    }
    public StringProperty statisticsProperty() {
        return statictics;
    }
    public IntegerProperty lifeimeWarriorProperty() { return lifetimeWarrior; }
    public IntegerProperty lifeimeWorkerProperty() { return lifetimeWorker; }

}