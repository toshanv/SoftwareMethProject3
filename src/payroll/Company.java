package payroll;

/** 
 * This class is an array-based container class that implements the employee database.
 * The array will store a list of employees, which may include the instances of full-time, part-time and management.
 * @author Christopher Nguyen
 * @author Toshanraju Vysyaraju
 */
public class Company {
    private Employee[] emplist;
    private int numEmployee;
    
    // static int that is used for initial company size as well as in grow to increase capacity
    private static final int COMPANY_SIZE = 4;

    // static int that is used when find() is unable to find the employee
    private static final int NOT_FOUND = -1;
    
    /** 
     * default constructor to create an empty company
     */
    public Company() {
        this.emplist = new Employee[COMPANY_SIZE];
        this.numEmployee = 0;
    }

    /**
     * getter method to get numEmployee
     * @return number of employees in emplists
     */
    public int getNumEmployee() {
        return this.numEmployee;
    }

    /** 
     * find helper method to find a employee in the emplist
     * @param employee object that is to be found
     * @return index of the employee in the emplist
     */
    private int find(Employee employee) {
        if (numEmployee == 0) {
            return NOT_FOUND;
        }

        int i = 0;
        do {
            if (emplist[i].equals(employee)) {
                return i;
            }
            
            i++;
        } while (i < this.numEmployee);

        return NOT_FOUND;
    }

    /** 
     * helper method to grow the capacity of the company by 4
     */
    private void grow() {
        Employee[] temp = new Employee[emplist.length + COMPANY_SIZE];

        for (int i = 0; i < emplist.length; i++) {
            temp[i] = emplist[i];
        }

        emplist = temp;
    }
    
    /** 
     * method to add a employee to the company
     * @param employee object that is to be added
     * @return returns true if the employee is added and false if the employee is not added
     */
    public boolean add(Employee employee) {
        if (find(employee) != NOT_FOUND) {
            return false;
        }
        
        if (emplist.length == numEmployee) {
            grow();
        }
        
        emplist[numEmployee] = employee;
        this.numEmployee++;

        return true;
    }
    
    /** 
     * method to remove a employee to the company
     * @param employee object that is to be removed
     * @return returns true if the employee is removed and false if the employee is not removed
     */
    public boolean remove(Employee employee) {
        if (numEmployee == 0) {
            return false;
        }
        
        int indexOfEmployee = find(employee);
        
        if (indexOfEmployee == NOT_FOUND) {
            return false;
        }

        for (int i = indexOfEmployee; i < numEmployee; i++) {
            emplist[i] = emplist[i+1];
        }
        emplist[emplist.length - 1] = null;
        
        numEmployee--;

        return true;
    } 
    
    /** 
     * method to set hours for a parttime employee in the company
     * @param employee object that passed in to set hours for the parttime employee in the company
     * @return returns true if the hours have been set for the employee and false if the hours have not been set for the employee
     */
    public boolean setHours(Employee employee) {
        if (this.numEmployee == 0) {
            return false;
        }

        int empLocation = find(employee);
        
        if (empLocation == NOT_FOUND) {
            return false;
        }

        Employee emp = emplist[empLocation];

        if (!(emp instanceof Parttime)) {
            return false;
        }

        Parttime empParttime = (Parttime)emp;
        Parttime empToAddParttime = (Parttime)employee;

        empParttime.setHoursWorked(empToAddParttime.getHoursWorked());
        
        return true;
    } 
    
    /** 
     * method to process payments for each employee in the company
     */
    public void processPayments() {
        for (int i = 0; i < numEmployee; i++) {
            emplist[i].calculatePayment();
        }
    } 
    
    /** 
     * method to print each employee in the company in the current order
     */
    public String print() {
        String output;
        if (this.numEmployee == 0) {
            output = "Employee database is empty.\n";
            return output;
        }

        output = "--Printing earning statements for all employees--\n";
        
        for (Employee emp : emplist) {
            if (emp instanceof Parttime) {
                Parttime empParttime = (Parttime)emp;
                output = output + (empParttime.toString()) + "\n";
            } else if (emp instanceof Management) {
                Management empManagement = (Management)emp;
                output = output + (empManagement.toString()) + "\n";
            } else if (emp instanceof Fulltime) {
                Fulltime empFulltime = (Fulltime)emp;
                output = output + (empFulltime.toString()) + "\n";
            }
        }

        return output;
    } 
    
    /** 
     * method to print each employee in the company ordered by department 
     */
    public String printByDepartment() {
        String output;
        if (this.numEmployee == 0) {
            output = "Employee database is empty.\n";
            return output;
        }

        sortByDept();
        
        output = "--Printing earning statements by department--\n";

        for (int i = 0; i < numEmployee; i++) {
            output = output + emplist[i].toString() + "\n";
        }

        return output;
    } 
    
    /** 
     * method to print each employee in the company ordered by date hired 
     */
    public String printByDate() {
        String output;
        if (this.numEmployee == 0) {
            output = "Employee database is empty.\n";
            return output;
        }

        sortByDate();

        output = "--Printing earning statements by date hired--\n";

        for (int i = 0; i < numEmployee; i++) {
             output = output + emplist[i].toString() + "\n";
        }

        return output;
    } 

     /** 
     * helper method that sorts each employee in the company by department 
     */
    private void sortByDept() {
        Employee temp;

        for (int i = 0; i < numEmployee; i++) {
            for (int j = i + 1; j < numEmployee; j++) { 
                if (emplist[i].getProfile().getDepartment().compareTo(emplist[j].getProfile().getDepartment()) > 0) {
                    temp = emplist[i];
                    emplist[i] = emplist[j];
                    emplist[j] = temp;
                }
            }
        }
    }
    
    /**
     * helper method that sorts each employee in the company by date hired 
     */
    private void sortByDate() {
        Employee temp;

        for (int i = 0; i < numEmployee; i++) {
            for (int j = i + 1; j < numEmployee; j++) { 
                if (emplist[i].getProfile().getDateHired().compareTo(emplist[j].getProfile().getDateHired()) > 0) {
                    temp = emplist[i];
                    emplist[i] = emplist[j];
                    emplist[j] = temp;
                }
            }
        }
    }
}