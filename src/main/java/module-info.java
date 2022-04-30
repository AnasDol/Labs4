module labs {
    requires javafx.controls;
    requires javafx.fxml;

    opens labs to javafx.fxml;
    exports labs;
    exports labs.view;
    opens labs.view to javafx.fxml;
    exports labs.factories;
    opens labs.factories to javafx.fxml;

}