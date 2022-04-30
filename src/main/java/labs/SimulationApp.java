package labs;

import labs.factories.ModelFactory;
import labs.factories.ViewHandler;
import labs.factories.ViewModelFactory;
import javafx.application.Application;
import javafx.stage.Stage;

public class SimulationApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        stage.setHeight(700);
        stage.setWidth(1200);
        ModelFactory mf = new ModelFactory();
        ViewModelFactory vmf = new ViewModelFactory(mf);
        ViewHandler vh = new ViewHandler(stage, vmf);
        vh.start("HabitatView");

    }

}
