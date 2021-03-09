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

/**
 * This class is a controller that helps implement the MVC model for pa.
 * The controller is responsible for controlling the way that a user interacts with our payroll processing application
 * @author Christopher Nguyen
 * @author Toshanraju Vysyaraju
 */
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
    private Button addEmployeeButton = new Button();

    /**
     * Method used to initialize values for the department, employment, and management choice boxes.
     */
    @FXML
    public void initialize () {
        employmentStatus.getItems().addAll(employmentStatusList);
        departmentStatus.getItems().addAll(departmentList);
        managementStatus.getItems().addAll(managementList);
    }

    /**
     * Method used to import a text file into the employee database.
     * @param event action event passed when import button is pressed to call importFile method
     */
    @FXML
    void importFile(ActionEvent event) {
        final int INPUT_ARR_TYPE_INDEX = 0;
        final int INPUT_ARR_NAME_INDEX = 1;
        final int INPUT_ARR_DEPT_INDEX = 2;
        final int INPUT_ARR_DATE_INDEX = 3;
        final int INPUT_ARR_PAY_INDEX = 4;
        final int INPUT_ARR_MGMT_INDEX = 5;

        final int INPUT_DATE_MONTH_INDEX = 0;
        final int INPUT_DATE_DAY_INDEX = 1;
        final int INPUT_DATE_YEAR_INDEX = 2;

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
                name.setText(inputArr[INPUT_ARR_NAME_INDEX]);
                departmentStatus.setValue(inputArr[INPUT_ARR_DEPT_INDEX]);

                // switch date to their format
                String[] inputedDate = inputArr[INPUT_ARR_DATE_INDEX].split("/");
                LocalDate date = LocalDate.of(Integer.parseInt(inputedDate[INPUT_DATE_YEAR_INDEX]), Integer.parseInt(inputedDate[INPUT_DATE_MONTH_INDEX]), Integer.parseInt(inputedDate[INPUT_DATE_DAY_INDEX]));
                hiredDate.setValue(date);

                // type of employee
                if (inputArr[INPUT_ARR_TYPE_INDEX].equals("P")) {
                    employmentStatus.setValue("Parttime");
                    hourlyRate.setText(inputArr[INPUT_ARR_PAY_INDEX]);
                } else if (inputArr[INPUT_ARR_TYPE_INDEX].equals("F")) {
                    employmentStatus.setValue("Fulltime");
                    annualSalary.setText(inputArr[INPUT_ARR_PAY_INDEX]);
                } else {
                    employmentStatus.setValue("Management");
                    annualSalary.setText(inputArr[INPUT_ARR_PAY_INDEX]);
                    if (inputArr[INPUT_ARR_MGMT_INDEX].equals("1")) {
                        managementStatus.setValue("Manager");
                    } else if (inputArr[INPUT_ARR_MGMT_INDEX].equals("2")) {
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

    /**
     * Method used to export information from the employee database into a new file.
     * @param event action event passed when export button is pressed to call exportFile method
     */
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

        textDisplay.appendText("Database exported to " + targetFile.getName() + ".\n");
    }

    /**
     * Method used to reset the fields to the default value.
     * @param actionEvent action event passed when reset button is pressed to reset the fields on the GUI
     */
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

    /**
     * Method used to handle the restrictions on the text fields and choice boxes based on what employee type is chosen.
     * @param mouseEvent action event passed when a employee type is pressed to disable or enable restrictions on the GUI
     */
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

    /**
     * Helper method used to verify the data passed into text fields to check if it is a valid numerical value.
     * @return an integer value stating whether the input is valid and verifies what employee type it is based off the input
     */
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

    /**
     * Method used to handle the process when the add button is pressed.
     * If the input is valid, it will add the employee or return the proper response based on the input given
     * @param actionEvent passed when add button is pressed to call this method to handle adding a employee.
     */
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

    /**
     * Method used to handle printing employee information from company into the text area on the GUI.
     * @param actionEvent passed when print button is pressed to print employee information from company.
     */
    @FXML
    public void handleClickPrint(ActionEvent actionEvent) {
        textDisplay.appendText(company.print());
    }

    /**
     * Method used to handle printing employee information from company into the text area on the GUI sorted by department.
     * @param actionEvent passed when print button is pressed to print employee information sorted by department from the company.
     */
    @FXML
    public void handleClickPrintbyDept(ActionEvent actionEvent) {
        textDisplay.appendText(company.printByDepartment());
    }

    /**
     * Method used to handle printing employee information from company into the text area on the GUI sorted by date.
     * @param actionEvent passed when print button is pressed to print employee information sorted by date from the company.
     */
    @FXML
    public void handleClickPrintbyDate(ActionEvent actionEvent) {
        textDisplay.appendText(company.printByDate());
    }

    /**
     * Method used to handle the process when the remove button is pressed.
     * If the input is valid and the employee exists, it will remove the employee otherwise it will return a proper response based on the given input.
     * @param actionEvent passed when remove button is pressed to call this method to handle removing a employee.
     */
    @FXML
    public void handleClickRemove(ActionEvent actionEvent) {
        if (name.getText().equals("") || departmentStatus.getValue() == null || hiredDate.getValue() == null) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Submission is not valid");
            errorAlert.setContentText("One of the required fields is empty.");
            errorAlert.showAndWait();
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

    /**
     * Method used to handle the process when the set hours button is pressed.
     * If the input is valid, it will add the employee's hours or will return a appropriate response based on given input
     * @param actionEvent passed when set hours button is pressed to call this method to handle setting the hours for a employee
     */
    @FXML
    public void handleClickSetHours(ActionEvent actionEvent) {
        int inputHours;
        final int MAX_HOURS = 100;

        if (name.getText().equals("") || departmentStatus.getValue() == null || hiredDate.getValue() == null || hoursWorked.getText().equals("") || employmentStatus.getValue() == null) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Submission is not valid");
            errorAlert.setContentText("One of the required fields is empty.");
            errorAlert.showAndWait();
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

    /**
     * Method used to handle the process of calculating an employee's payment.
     * If the database is not empty, it will calculate the proper payment for each employee in the database.
     * @param actionEvent passed when calculate payment button is pressed to call this method to handle calculating the payment for each employee in the company database.
     */
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
