package payroll;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import javax.swing.event.ChangeListener;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller {
    ObservableList <String> employmentStatusList = FXCollections.observableArrayList("Fulltime", "Parttime", "Management");

    ObservableList <String> departmentList = FXCollections.observableArrayList("CS", "IT", "ECE");

    ObservableList <String> managementList = FXCollections.observableArrayList("Manager", "DepartmentHead", "Director");


    @FXML
    private ChoiceBox employmentStatus = new ChoiceBox();

    @FXML
    private ChoiceBox departmentStatus = new ChoiceBox();

    @FXML
    private ChoiceBox managementStatus = new ChoiceBox();

    @FXML
    private DatePicker hiredDate = new DatePicker();

    @FXML
    private TextField name = new TextField();

    @FXML
    private TextField annualSalary = new TextField();

    @FXML
    private TextField hoursWorked = new TextField();

    @FXML
    private TextField hourlyRate = new TextField();


    @FXML
    public void initialize () {
        employmentStatus.getItems().addAll(employmentStatusList);
        departmentStatus.getItems().addAll(departmentList);
        managementStatus.getItems().addAll(managementList);
    }

    public void resetTab (ActionEvent actionEvent) {
        name.clear();
        annualSalary.clear();
        hoursWorked.clear();
        hourlyRate.clear();
        departmentStatus.setValue(null);
        employmentStatus.setValue(null);
        managementStatus.setValue(null);
        hiredDate.setValue(null);
    }
    public int checkFieldDouble () {
        try {
            Double checkVal;
            if (hoursWorked.getText().compareTo("")!=0) {
                checkVal = Double.parseDouble(hoursWorked.getText());
            }
            if (annualSalary.getText().compareTo("")!=0) {
                checkVal = Double.parseDouble(annualSalary.getText());
            }
            if (hoursWorked.getText().compareTo("")!=0) {
                checkVal = Double.parseDouble(hoursWorked.getText());
            }

            return 0;
        }
        catch (NumberFormatException ex) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Submission is not valid");
            errorAlert.setContentText("One of the fields is not a valid type double.");
            errorAlert.showAndWait();
            return -1;
        }
    }
    public void handleEmploymentRestrictions(MouseEvent mouseEvent) {
        if (employmentStatus.getValue() == "Parttime"){
            annualSalary.setDisable(true);
            hoursWorked.setDisable(false);
            hourlyRate.setDisable(false);
            managementStatus.setDisable(true);
        }
        else if (employmentStatus.getValue() == "Fulltime" ) {
            annualSalary.setDisable(false);
            hoursWorked.setDisable(true);
            hourlyRate.setDisable(true);
            managementStatus.setDisable(true);


        }
        else if (employmentStatus.getValue() == "Management") {
            annualSalary.setDisable(false);
            hoursWorked.setDisable(true);
            hourlyRate.setDisable(true);
            managementStatus.setDisable(false);
        }
        else {
            annualSalary.setDisable(false);
            hoursWorked.setDisable(false);
            hourlyRate.setDisable(false);
            managementStatus.setDisable(false);
        }
    }
    public void handleClickAdd(ActionEvent actionEvent) {
       int check = checkFieldDouble();
       if (check == -1) {
           resetTab(actionEvent);
           return;
       }
    }

}
