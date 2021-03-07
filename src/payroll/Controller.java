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
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;
import java.io.File;
import java.time.LocalDate;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;

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

    // date switching ints from their date format to our date format
    private static final int LOCALDATE_BEGIN_MONTH = 5;
    private static final int LOCALDATE_END_MONTH = 7;
    private static final int LOCALDATE_BEGIN_DAY = 8;
    private static final int LOCALDATE_END_DAY = 10;
    private static final int LOCALDATE_BEGIN_YEAR = 0;
    private static final int LOCALDATE_END_YEAR = 4;

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
    private Button addEmployeeButton = new Button();

    @FXML
    public void initialize () {
        employmentStatus.getItems().addAll(employmentStatusList);
        departmentStatus.getItems().addAll(departmentList);
        managementStatus.getItems().addAll(managementList);
    }

    @FXML
    void importFile(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open Source File for the Import");
        chooser.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"),
                new ExtensionFilter("All Files", "*.*"));
        Stage stage = new Stage();
        File sourceFile = chooser.showOpenDialog(stage); //get the reference of the source file

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(sourceFile));
            String line = reader.readLine();
            while (line != null) {
                String[] inputArr = line.split(",");

                // employee name
                name.setText(inputArr[1]);
                departmentStatus.setValue(inputArr[2]);

                // switch date to their format
                String[] inputedDate = inputArr[3].split("/");
                LocalDate date = LocalDate.of(Integer.parseInt(inputedDate[2]), Integer.parseInt(inputedDate[0]), Integer.parseInt(inputedDate[1]));
                hiredDate.setValue(date);

                // type of employee
                if (inputArr[0].equals("P")) {
                    employmentStatus.setValue("Parttime");
                    hourlyRate.setText(inputArr[4]);
                } else if (inputArr[0].equals("F")) {
                    employmentStatus.setValue("Fulltime");
                    annualSalary.setText(inputArr[4]);
                } else {
                    employmentStatus.setValue("Management");
                    annualSalary.setText(inputArr[4]);
                    if (inputArr[5].equals("1")) {
                        managementStatus.setValue("Manager");
                    } else if (inputArr[5].equals("2")) {
                        managementStatus.setValue("DepartmentHead");
                    } else {
                        managementStatus.setValue("Director");
                    }
                }

                addEmployeeButton.fireEvent(new ActionEvent());

                // read next line
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void exportFile(ActionEvent event) {
        if (company.getNumEmployee() == 0) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Database is empty");
            errorAlert.setContentText("The Database is empty. Cannot export empty database.");
            errorAlert.showAndWait();
            return;
        }

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open Target File for the Export");
        chooser.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"),
                new ExtensionFilter("All Files", "*.*"));
        Stage stage = new Stage();
        File targetFile = chooser.showSaveDialog(stage); //get the reference of the target file
        //write code to write to the file.

        try {
            FileWriter myWriter = new FileWriter(targetFile);
            myWriter.write(company.print());
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    @FXML
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

    @FXML
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

    @FXML
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

    @FXML
    public void handleClickPrint(ActionEvent actionEvent) {
        printDisplay.appendText(company.print());
    }

    @FXML
    public void handleClickPrintbyDept(ActionEvent actionEvent) {
        printDisplay.appendText(company.printByDepartment());
    }

    @FXML
    public void handleClickPrintbyDate(ActionEvent actionEvent) {
        printDisplay.appendText(company.printByDate());
    }

    @FXML
    public void handleClickRemove(ActionEvent actionEvent) {
        if (name.getText().equals("") || departmentStatus.getValue() == null || hiredDate.getValue() == null) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Submission is not valid");
            errorAlert.setContentText("One of the required fields is empty.");
            errorAlert.showAndWait();
            resetTab(actionEvent);
            return;
        }

        String inputDate = hiredDate.getValue().toString();
        inputDate = inputDate.substring(LOCALDATE_BEGIN_MONTH, LOCALDATE_END_MONTH) + "/" + inputDate.substring(LOCALDATE_BEGIN_DAY, LOCALDATE_END_DAY) + "/" + inputDate.substring(LOCALDATE_BEGIN_YEAR, LOCALDATE_END_YEAR);

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

    @FXML
    public void handleClickSetHours(ActionEvent actionEvent) {
        int inputHours;
        final int MAX_HOURS = 100;

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
        inputDate = inputDate.substring(LOCALDATE_BEGIN_MONTH, LOCALDATE_END_MONTH) + "/" + inputDate.substring(LOCALDATE_BEGIN_DAY, LOCALDATE_END_DAY) + "/" + inputDate.substring(LOCALDATE_BEGIN_YEAR, LOCALDATE_END_YEAR);
        Date dateHired = new Date(inputDate);

        if (!dateHired.isValid()) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Submission is not valid");
            errorAlert.setContentText("Date is not a valid date. Try again.");
            errorAlert.showAndWait();
            resetTab(actionEvent);
            return;
        }

        if (inputHours > MAX_HOURS) {
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

    @FXML
    public void handleCalcPay(ActionEvent actionEvent) {
        if (company.getNumEmployee() == 0) {
            textDisplay.appendText("Employee database is empty.\n");
            return;
        }

        company.processPayments();
        textDisplay.appendText("Calculation of employee payments is done.\n");
    }


}
