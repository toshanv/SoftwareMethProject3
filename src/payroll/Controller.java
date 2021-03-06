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

    Company company = new Company();

    private static final int PARTTIME_CODE = 1;
    private static final int FULLTIME_CODE = 2;
    private static final int MANAGEMENT_CODE = 3;

    private static final int ERROR_CODE = -1;


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
    private TextArea textDisplay = new TextArea();

    @FXML
    private TextArea printDisplay = new TextArea();

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

    public void handleEmploymentRestrictions(MouseEvent mouseEvent) {
        if (employmentStatus.getValue() == "Parttime"){
            annualSalary.setDisable(true);
            hoursWorked.setDisable(false);
            hourlyRate.setDisable(false);
            managementStatus.setDisable(true);
        } else if (employmentStatus.getValue() == "Fulltime" ) {
            annualSalary.setDisable(false);
            hoursWorked.setDisable(true);
            hourlyRate.setDisable(true);
            managementStatus.setDisable(true);
        } else if (employmentStatus.getValue() == "Management") {
            annualSalary.setDisable(false);
            hoursWorked.setDisable(true);
            hourlyRate.setDisable(true);
            managementStatus.setDisable(false);
        } else {
            annualSalary.setDisable(false);
            hoursWorked.setDisable(false);
            hourlyRate.setDisable(false);
            managementStatus.setDisable(false);
        }
    }

    public int checkFieldDouble () {
        try {
            Double checkVal;
            if (employmentStatus.getValue() == "Parttime") {
                checkVal = Double.parseDouble(hourlyRate.getText());
                // checkVal = Double.parseDouble(hoursWorked.getText());
                return PARTTIME_CODE;
            } else if (employmentStatus.getValue() == "Management" || employmentStatus.getValue() == "Fulltime") {
                checkVal = Double.parseDouble(annualSalary.getText());
                if (employmentStatus.getValue() == "Management") {
                    return MANAGEMENT_CODE;
                } else {
                    return FULLTIME_CODE;
                }
            } else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Submission is not valid");
                errorAlert.setContentText("One of the required fields is empty or is not a valid type double.");
                errorAlert.showAndWait();
                return ERROR_CODE;
            }
        } catch (NumberFormatException ex) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Submission is not valid");
            errorAlert.setContentText("One of the required fields is empty or is not a valid type double.");
            errorAlert.showAndWait();
            return ERROR_CODE;
        }
    }

    public void handleClickAdd(ActionEvent actionEvent) {
        final int MANAGER_CODE = 1;
        final int DEPT_HEAD_CODE = 2;
        final int DIRECTOR_CODE = 3;

        int employeeType = checkFieldDouble();
        if (employeeType == ERROR_CODE) {
           resetTab(actionEvent);
           return;
        }

        if (name.getText().equals("") || departmentStatus.getValue() == null || hiredDate.getValue() == null) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Submission is not valid");
            errorAlert.setContentText("One of the required fields is empty.");
            errorAlert.showAndWait();
            resetTab(actionEvent);
            return;
        }

        String inputDate = hiredDate.getValue().toString();
        inputDate = inputDate.substring(5, 7) + "/" + inputDate.substring(8, 10) + "/" + inputDate.substring(0, 4);
        Date dateHired = new Date(inputDate);

        if (!dateHired.isValid()) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Submission is not valid");
            errorAlert.setContentText("Date is not a valid date. Try again.");
            errorAlert.showAndWait();
            resetTab(actionEvent);
            return;
        }

        Profile profile = new Profile(name.getText(), departmentStatus.getValue().toString(), dateHired);

        Employee employee = null;
        if (employeeType == PARTTIME_CODE) {
            double inputRate = Double.parseDouble(hourlyRate.getText());

            if (inputRate < 0) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Submission is not valid");
                errorAlert.setContentText("Pay rate cannot be negative");
                errorAlert.showAndWait();
                resetTab(actionEvent);
                return;
            }

            employee = new Parttime(profile, 0, inputRate, 0);
        } else {
            double inputPay = Double.parseDouble(annualSalary.getText());

            if (inputPay < 0) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Submission is not valid");
                errorAlert.setContentText("Salary cannot be negative");
                errorAlert.showAndWait();
                resetTab(actionEvent);
                return;
            }

            if (employeeType == FULLTIME_CODE) {
                employee = new Fulltime(profile, 0, inputPay);
            } else {
                int managementCode;

                if (managementStatus.getValue() == null) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setHeaderText("Submission is not valid");
                    errorAlert.setContentText("Management Roll is empty please fill out.");
                    errorAlert.showAndWait();
                    return;
                }

                if (managementStatus.getValue().toString().equals("Manager")) {
                    managementCode = MANAGER_CODE;
                } else if (managementStatus.getValue().toString().equals("DepartmentHead")) {
                    managementCode = DEPT_HEAD_CODE;
                } else {
                    managementCode = DIRECTOR_CODE;
                }

                employee = new Management(profile, 0, inputPay, managementCode);
            }
        }

        boolean addResult = company.add(employee);
        if (!addResult) {
            textDisplay.appendText("Employee is already in the list.\n");
        } else {
            textDisplay.appendText("Employee added.\n");
        }
        resetTab(actionEvent);
    }

    public void handleClickPrint(ActionEvent actionEvent) {
        printDisplay.appendText(company.print());
    }

    public void handleClickPrintbyDept(ActionEvent actionEvent) {
        printDisplay.appendText(company.printByDepartment());
    }

    public void handleClickPrintbyDate(ActionEvent actionEvent) {
        printDisplay.appendText(company.printByDate());
    }

    public void handClickRemove(ActionEvent actionEvent) {
        if (name.getText().equals("") || departmentStatus.getValue() == null || hiredDate.getValue() == null) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Submission is not valid");
            errorAlert.setContentText("One of the required fields is empty.");
            errorAlert.showAndWait();
            resetTab(actionEvent);
            return;
        }

        String inputDate = hiredDate.getValue().toString();
        inputDate = inputDate.substring(5, 7) + "/" + inputDate.substring(8, 10) + "/" + inputDate.substring(0, 4);
        Date dateHired = new Date(inputDate);

        if (!dateHired.isValid()) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Submission is not valid");
            errorAlert.setContentText("Date is not a valid date. Try again.");
            errorAlert.showAndWait();
            resetTab(actionEvent);
            return;
        }

        Profile profile = new Profile(name.getText(), departmentStatus.getValue().toString(), dateHired);

        Employee employeeToRemove = new Employee(profile, 0);
        Boolean isRemoved = company.remove(employeeToRemove);

        if (isRemoved) {
            textDisplay.appendText("Employee removed.\n");
        } else {
            if (company.getNumEmployee() == 0) {
                textDisplay.appendText("Employee database is empty.\n");
            } else {
                textDisplay.appendText("Employee does not exist.\n");
            }
        }

        resetTab(actionEvent);

    }

    public void handleClickSetHours(ActionEvent actionEvent) {
        int inputHours;

        if (name.getText().equals("") || departmentStatus.getValue() == null || hiredDate.getValue() == null || hoursWorked.getText().equals("")) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Submission is not valid");
            errorAlert.setContentText("One of the required fields is empty.");
            errorAlert.showAndWait();
            resetTab(actionEvent);
            return;
        }
        if (!employmentStatus.getValue().toString().equals("Parttime")) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Submission is not valid");
            errorAlert.setContentText("Cannot Set Hours for Management or Fulltime only Parttime");
            errorAlert.showAndWait();
            return;
        }

        try {
            if (hoursWorked.getText() != "") {
                inputHours = Integer.parseInt(hoursWorked.getText());
            } else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Submission is not valid");
                errorAlert.setContentText("Hours Worked is empty fill in.");
                errorAlert.showAndWait();
                return;
            }
        } catch (NumberFormatException ex) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Submission is not valid");
            errorAlert.setContentText("Hours work is not a valid type int.");
            errorAlert.showAndWait();
            return;
        }
        if (company.getNumEmployee() == 0) {
            textDisplay.appendText("Employee database is empty.\n");
            return;
        }

        String inputDate = hiredDate.getValue().toString();
        inputDate = inputDate.substring(5, 7) + "/" + inputDate.substring(8, 10) + "/" + inputDate.substring(0, 4);
        Date dateHired = new Date(inputDate);

        if (!dateHired.isValid()) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Submission is not valid");
            errorAlert.setContentText("Date is not a valid date. Try again.");
            errorAlert.showAndWait();
            resetTab(actionEvent);
            return;
        }

        if (inputHours > 100) {
            textDisplay.appendText("Invalid Hours: over 100.\n");
            return;
        } else if (inputHours < 0) {
            textDisplay.appendText("Working hours cannot be negative.\n");
            return;
        }


        Profile profile = new Profile(name.getText(), departmentStatus.getValue().toString(), dateHired);
        Employee employeeToSetHours = new Parttime(profile, 0, 0, inputHours);
        Boolean isUpdated = company.setHours(employeeToSetHours);

        if (isUpdated) {
            textDisplay.appendText("Working hours set.\n");
        } else {
            textDisplay.appendText("Employee does not exist.\n");
        }
        resetTab(actionEvent);
    }
}
