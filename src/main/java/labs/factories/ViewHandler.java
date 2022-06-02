package labs.factories;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import labs.model.Connection;
import labs.view.HabitatController;

import java.io.IOException;
import java.util.Random;

public class ViewHandler {

    private Stage stage;
    private ViewModelFactory viewModelFactory;

    public ViewHandler(Stage stage, ViewModelFactory viewModelFactory) {
        this.stage = stage;
        this.viewModelFactory = viewModelFactory;
    }

    public void start(String viewToOpen) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        Parent root = null;
        Scene scene = null;

        if (viewToOpen.equals("HabitatView")) {
            loader.setLocation(getClass().getResource("/labs/HabitatView.fxml"));
            root = loader.load();
            scene = new Scene(root);
            HabitatController view = loader.getController();
            view.init(viewModelFactory.getHabitatViewModel(), scene);
        }

        stage.setScene(scene);
        stage.show();


    }

}
