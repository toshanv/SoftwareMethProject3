package payroll;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.time.LocalDate;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * This class is a controller that helps implement the MVC model for pa.
 * The controller is responsible for controlling the way that a user interacts with our payroll processing application
 * @author Christopher Nguyen
 * @author Toshanraju Vysyaraju
 */
public class Controller {

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

    @FXML
    private RadioButton manRB = new RadioButton();

    @FXML
    private RadioButton dhRB = new RadioButton();

    @FXML
    private RadioButton dirRB = new RadioButton();

    @FXML
    private ToggleGroup rbGroup = new ToggleGroup();

    @FXML
    private RadioButton csRB = new RadioButton();

    @FXML
    private RadioButton eceRB = new RadioButton();

    @FXML
    private RadioButton itRB = new RadioButton();

    @FXML
    private ToggleGroup departmentGroup = new ToggleGroup();

    @FXML
    private RadioButton partRB = new RadioButton();

    @FXML
    private RadioButton fullRB = new RadioButton();

    @FXML
    private RadioButton managementRB = new RadioButton();

    @FXML
    private ToggleGroup employmentGroup = new ToggleGroup();

    /**
     * Method used to initialize values for the department, employment, and management choice boxes.
     */
    @FXML
    public void initialize () {
        manRB.setUserData("Manager");
        dhRB.setUserData("DepartmentHead");
        dirRB.setUserData("Director");

        manRB.setToggleGroup(rbGroup);
        dirRB.setToggleGroup(rbGroup);
        dhRB.setToggleGroup(rbGroup);

        eceRB.setUserData("ECE");
        itRB.setUserData("IT");
        csRB.setUserData("CS");

        eceRB.setToggleGroup(departmentGroup);
        itRB.setToggleGroup(departmentGroup);
        csRB.setToggleGroup(departmentGroup);

        managementRB.setUserData("Management");
        fullRB.setUserData("Fulltime");
        partRB.setUserData("Parttime");

        managementRB.setToggleGroup(employmentGroup);
        fullRB.setToggleGroup(employmentGroup);
        partRB.setToggleGroup(employmentGroup);

        hiredDate.setEditable(false);
        hiredDate.setPromptText("Select using calendar");
        name.setPromptText("Enter name");
        annualSalary.setPromptText("Enter salary if adding");
        hourlyRate.setPromptText("Enter rate if adding");
        hoursWorked.setPromptText("Enter hours for set hours");

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
                if (inputArr[INPUT_ARR_DEPT_INDEX].equals(csRB.getText())){
                    departmentGroup.selectToggle(csRB);
                } else if (inputArr[INPUT_ARR_DEPT_INDEX].equals(eceRB.getText())) {
                    departmentGroup.selectToggle(eceRB);
                }
                else if (inputArr[INPUT_ARR_DEPT_INDEX].equals(itRB.getText())) {
                    departmentGroup.selectToggle(itRB);
                }

                // switch date to their format
                String[] inputedDate = inputArr[INPUT_ARR_DATE_INDEX].split("/");
                LocalDate date = LocalDate.of(Integer.parseInt(inputedDate[INPUT_DATE_YEAR_INDEX]), Integer.parseInt(inputedDate[INPUT_DATE_MONTH_INDEX]), Integer.parseInt(inputedDate[INPUT_DATE_DAY_INDEX]));
                hiredDate.setValue(date);

                // type of employee
                if (inputArr[INPUT_ARR_TYPE_INDEX].equals("P")) {
                    employmentGroup.selectToggle(partRB);
                    hourlyRate.setText(inputArr[INPUT_ARR_PAY_INDEX]);
                } else if (inputArr[INPUT_ARR_TYPE_INDEX].equals("F")) {
                    employmentGroup.selectToggle(fullRB);
                    annualSalary.setText(inputArr[INPUT_ARR_PAY_INDEX]);
                } else {
                    employmentGroup.selectToggle(managementRB);
                    annualSalary.setText(inputArr[INPUT_ARR_PAY_INDEX]);
                    if (inputArr[INPUT_ARR_MGMT_INDEX].equals("1")) {
                        rbGroup.selectToggle(manRB);
                    } else if (inputArr[INPUT_ARR_MGMT_INDEX].equals("2")) {
                        rbGroup.selectToggle(dhRB);
                    } else {
                        rbGroup.selectToggle(dirRB);
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
            textDisplay.appendText("The Database is empty. Cannot export empty database.\n");
            return;
        }

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open Target File for the Export");
        chooser.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"),
                new ExtensionFilter("All Files", "*.*"));
        Stage stage = new Stage();
        File targetFile = chooser.showSaveDialog(stage); //get the reference of the target file
        //write code to write to the file.

        company.exportDatabase(targetFile);

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

        if (employmentGroup.getSelectedToggle() != null) {
            employmentGroup.getSelectedToggle().setSelected(false);
            annualSalary.setDisable(false);
            hoursWorked.setDisable(false);
            hourlyRate.setDisable(false);
            dhRB.setDisable(false);
            dirRB.setDisable(false);
            manRB.setDisable(false);
        }


        if (departmentGroup.getSelectedToggle() != null) {
            departmentGroup.getSelectedToggle().setSelected(false);
        }

        if (rbGroup.getSelectedToggle() != null) {
            rbGroup.getSelectedToggle().setSelected(false);
        }

        hiredDate.setValue(null);
    }

    /**
     * Method used to handle the restrictions on the text fields and choice boxes based on what employee type is chosen.
     * @param actionEvent action event passed when a employee type is pressed to disable or enable restrictions on the GUI
     */
    @FXML
    public void handleEmploymentRestrictions(ActionEvent actionEvent) {
        if (employmentGroup.getSelectedToggle() != null){
            if (employmentGroup.getSelectedToggle().getUserData().toString().equals("Parttime")){
                annualSalary.setDisable(true);
                hoursWorked.setDisable(false);
                hourlyRate.setDisable(false);
                dhRB.setDisable(true);
                dirRB.setDisable(true);
                manRB.setDisable(true);
            } else if (employmentGroup.getSelectedToggle().getUserData().toString().equals("Fulltime")) {
                annualSalary.setDisable(false);
                hoursWorked.setDisable(true);
                hourlyRate.setDisable(true);
                dhRB.setDisable(true);
                dirRB.setDisable(true);
                manRB.setDisable(true);
            } else if (employmentGroup.getSelectedToggle().getUserData().toString().equals("Management")) {
                annualSalary.setDisable(false);
                hoursWorked.setDisable(true);
                hourlyRate.setDisable(true);
                dhRB.setDisable(false);
                dirRB.setDisable(false);
                manRB.setDisable(false);
            } else {
                annualSalary.setDisable(false);
                hoursWorked.setDisable(false);
                hourlyRate.setDisable(false);
                dhRB.setDisable(false);
                dirRB.setDisable(false);
                manRB.setDisable(false);
            }
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
            if (employmentGroup.getSelectedToggle().getUserData().toString().equals("Parttime")) {
                checkVal = Double.parseDouble(hourlyRate.getText());
                return PARTTIME_CODE;
            } else if (employmentGroup.getSelectedToggle().getUserData().toString().equals("Fulltime")) {
                checkVal = Double.parseDouble(annualSalary.getText());
                return FULLTIME_CODE;

            } else if (employmentGroup.getSelectedToggle().getUserData().toString().equals("Management")) {
                checkVal = Double.parseDouble(annualSalary.getText());
                return MANAGEMENT_CODE;
            } else {
                textDisplay.appendText("Submission is not valid. One of the required fields is empty or is not a valid type double.\n");
                return ERROR_CODE;
            }
        } catch (NumberFormatException ex) {
            textDisplay.appendText("Submission is not valid. One of the required fields is empty or is not a valid type double.\n");
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

        if (name.getText().equals("") || departmentGroup.getSelectedToggle()== null || hiredDate.getValue() == null) {
            textDisplay.appendText("Submission is not valid. One of the required fields is empty.\n");
            return;
        }

        String inputDate = hiredDate.getValue().toString();
        inputDate = inputDate.substring(5, 7) + "/" + inputDate.substring(8, 10) + "/" + inputDate.substring(0, 4);
        Date dateHired = new Date(inputDate);

        if (!dateHired.isValid()) {
            textDisplay.appendText("Submission is not valid. Date is not a valid date. Try again.\n");
            return;
        }

        Profile profile = new Profile(name.getText(), departmentGroup.getSelectedToggle().getUserData().toString(), dateHired);

        Employee employee = null;
        if (employeeType == PARTTIME_CODE) {
            double inputRate = Double.parseDouble(hourlyRate.getText());

            if (inputRate < 0) {
                textDisplay.appendText("Submission is not valid. Pay rate cannot be negative.\n");
                return;
            }

            employee = new Parttime(profile, 0, inputRate, 0);
        } else {
            double inputPay = Double.parseDouble(annualSalary.getText());

            if (inputPay < 0) {
                textDisplay.appendText("Submission is not valid. Salary cannot be negative.\n");
                return;
            }

            if (employeeType == FULLTIME_CODE) {
                employee = new Fulltime(profile, 0, inputPay);
            } else {
                int managementCode;

                if (rbGroup.getSelectedToggle()== null) {
                    textDisplay.appendText("Submission is not valid. Management Role is empty please fill out.\n");
                    return;
                }

                if (rbGroup.getSelectedToggle().getUserData().toString().equals("Manager")) {
                    managementCode = MANAGER_CODE;
                } else if (rbGroup.getSelectedToggle().getUserData().toString().equals("DepartmentHead")) {
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
        if (name.getText().equals("") || departmentGroup.getSelectedToggle()== null || hiredDate.getValue() == null) {
            textDisplay.appendText("Submission is not valid. One of the required fields is empty.\n");
            return;
        }

        String inputDate = hiredDate.getValue().toString();
        inputDate = inputDate.substring(LOCALDATE_BEGIN_MONTH, LOCALDATE_END_MONTH) + "/" + inputDate.substring(LOCALDATE_BEGIN_DAY, LOCALDATE_END_DAY) + "/" + inputDate.substring(LOCALDATE_BEGIN_YEAR, LOCALDATE_END_YEAR);

        Date dateHired = new Date(inputDate);

        if (!dateHired.isValid()) {
            textDisplay.appendText("Submission is not valid. Date is invalid.\n");
            return;
        }

        Profile profile = new Profile(name.getText(), departmentGroup.getSelectedToggle().getUserData().toString(), dateHired);

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

        if (name.getText().equals("") || departmentGroup.getSelectedToggle()== null || hiredDate.getValue() == null || hoursWorked.getText().equals("") || employmentGroup.getSelectedToggle()== null) {
            textDisplay.appendText("Submission is not valid. One of the required fields is empty.\n");
            return;
        }
        if (!employmentGroup.getSelectedToggle().getUserData().toString().equals("Parttime")) {
            textDisplay.appendText("Submission is not valid. Cannot Set Hours for Management or Fulltime only Parttime.\n");
            return;
        }

        try {
            if (hoursWorked.getText() != "") {
                inputHours = Integer.parseInt(hoursWorked.getText());
            } else {
                textDisplay.appendText("Submission is not valid. Hours Worked is empty, please fill in.\n");
                return;
            }
        } catch (NumberFormatException ex) {
            textDisplay.appendText("Submission is not valid. Hours work is not a valid int.\n");
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
            textDisplay.appendText("Submission is not valid. Date is not a valid date. Try again.\n");
            return;
        }

        if (inputHours > MAX_HOURS) {
            textDisplay.appendText("Invalid Hours: over 100.\n");
            return;
        } else if (inputHours < 0) {
            textDisplay.appendText("Working hours cannot be negative.\n");
            return;
        }

        Profile profile = new Profile(name.getText(), departmentGroup.getSelectedToggle().getUserData().toString(), dateHired);
        Employee employeeToSetHours = new Parttime(profile, 0, 0, inputHours);
        Boolean isUpdated = company.setHours(employeeToSetHours);

        if (isUpdated) {
            textDisplay.appendText("Working hours set.\n");
        } else {
            textDisplay.appendText("Parttime Employee with this profile does not exist.\n");
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
