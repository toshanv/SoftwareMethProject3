package payroll;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller {
    ObservableList <String> list = FXCollections.observableArrayList("Fulltime", "Parttime", "Management");
    @FXML
    private ChoiceBox series = new ChoiceBox();

    @FXML
    public void initialize () {
        series.getItems().addAll(list);
    }

}
