package labs.factories;

import labs.viewmodel.HabitatViewModel;

import java.io.FileNotFoundException;

public class ViewModelFactory {

    private HabitatViewModel habitatViewModel;

    public ViewModelFactory(ModelFactory modelFactory) throws FileNotFoundException {
        habitatViewModel = new HabitatViewModel(modelFactory.getDataModel());
    }

    public HabitatViewModel getHabitatViewModel() {
        return habitatViewModel;
    }

}
