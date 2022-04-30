package labs.factories;

import labs.model.DataModel;
import labs.model.DataModelManager;

public class ModelFactory {

    private DataModel dataModel;

    public DataModel getDataModel() {
        if (dataModel == null) {
            dataModel = DataModelManager.getInstance();
        }
        return dataModel;
    }
}
