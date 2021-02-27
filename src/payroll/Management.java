package payroll;
import java.text.DecimalFormat;

/** 
 * This class extends the Fulltime class and includes specific data and operations to a full-time employee with a management role.
 * @author Christopher Nguyen
 * @author Toshanraju Vysyaraju
 */
public class Management extends Fulltime {
    private int managementRole;
    private double additionalCompensation;

    private static final int MANAGER_CODE = 1;
    private static final int DEPT_HEAD_CODE = 2;
    private static final int DIRECTOR_CODE = 3;
    
    private static final double MANAGER_AC = 5000;
    private static final double DEPT_AC = 9500;
    private static final double DIRECTOR_AC = 12000;
    
    /** 
     * Constructor that initialize the values for the management
     * @param profile is the profile of the management
     * @param payment is the payment made to management
     * @param annualSalary is the annual salary of the management
     * @param managementRole is the role of the management
     */ 
    public Management(Profile profile, double payment, double annualSalary, int managementRole) {
        super(profile, payment, annualSalary);
        this.managementRole = managementRole;

        if (managementRole == MANAGER_CODE) {
            this.additionalCompensation = MANAGER_AC;
        } else if (managementRole == DEPT_HEAD_CODE) {
            this.additionalCompensation = DEPT_AC;
        } else if (managementRole == DIRECTOR_CODE) {
            this.additionalCompensation = DIRECTOR_AC;
        }
    }

    /**
     * Getter method that returns the specific role of the management 
     * @return the role of the specified management employee
     */
    public int getManagementRole() {
        return this.managementRole;
    }

    /**
     * Getter method that returns the additional compensation for the management
     * @return the additional compensation for the management
     */
    public double getAdditionalCompensation() {
        return this.additionalCompensation;
    }

    /**
     * Method that properly formats the status for the management in the company
     * @return representation of the management and their status in the proper format
     */
    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setGroupingUsed(true);
        df.setGroupingSize(3);
        df.setMinimumFractionDigits(2);

        String additionalCompensationString = df.format(this.additionalCompensation/super.getNumPayPeriods());

        String managementRoleString = "";
        if (managementRole == MANAGER_CODE) {
            managementRoleString = "Manager";
        } else if (managementRole == DEPT_HEAD_CODE) {
            managementRoleString = "DepartmentHead";
        } else if (managementRole == DIRECTOR_CODE) {
            managementRoleString = "Director";
        }

        return super.toString() + "::" + managementRoleString + " Compensation $" + additionalCompensationString;
    }

    /**
     * Extends equals method from employee class
     * @param obj object passed in to compare if equal
     * @return true if the objects equal each other based on profile and false if one of them do not
     */
    @Override
    public boolean equals(Object obj) {
       return super.equals(obj);
    }
    
    /** 
     * method to calculate payments for each management employee in the company
     */
    @Override
    public void calculatePayment() {
        super.calculatePayment();
        double payForPeriod = (super.getPayment() + (this.additionalCompensation/super.getNumPayPeriods()));
        super.setPayment(payForPeriod);
    }
}