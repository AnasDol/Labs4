package labs.factories;

import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import labs.viewmodel.HabitatViewModel;

import java.io.FileNotFoundException;

public class ViewModelFactory {

    private HabitatViewModel habitatViewModel;

    public ViewModelFactory(Stage stage, ModelFactory modelFactory) throws FileNotFoundException {
        habitatViewModel = new HabitatViewModel(modelFactory.getDataModel());
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                habitatViewModel.getConnection().close();
            }
        });
    }

    public HabitatViewModel getHabitatViewModel() {
        return habitatViewModel;
    }

}
