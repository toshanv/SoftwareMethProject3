package payroll;
import java.text.DecimalFormat;

/** 
 * This class extends the Employee class and includes specific data and operations to a full-time employee.
 * @author Christopher Nguyen
 * @author Toshanraju Vysyaraju
 */
public class Fulltime extends Employee {
    private double annualSalary;
    private static final int NUM_PAY_PERIODS = 26;
    
    /** 
     * Constructor that initialize the values for a fulltime employee 
     * @param profile is the profile of the fulltime employee
     * @param payment is the payment made to the fulltime employee
     * @param annualSalary is the employees yearly salary
     */ 
    public Fulltime (Profile profile, double payment, double annualSalary) {
        super(profile, payment);
        this.annualSalary = annualSalary;
    }

     /**
     * Getter method that returns the the annual salary of the fulltime employee 
     * @return annual salary of the fulltime employee
     */
    public double getAnnualSalary() {
        return this.annualSalary;
    }

    /**
     * Getter method that returns the number of pay periods
     * @return the numPayPeriods
     */
    public static int getNumPayPeriods() {
        return NUM_PAY_PERIODS;
    }

    /**
     * Method that properly formats the status for the fulltime employee
     * @return representation of the fulltime employee and their status in the proper format
     */
    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setGroupingUsed(true);
        df.setGroupingSize(3);
        df.setMinimumFractionDigits(2);

        String annualSalaryString = df.format(this.annualSalary);

        return super.toString() + "::FULL TIME::Annual Salary $" + annualSalaryString;
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
     * method to calculate payments for each fulltime employee in the company
     */
    @Override
    public void calculatePayment() {
        double payForPeriod = (this.annualSalary/NUM_PAY_PERIODS);

        super.setPayment(payForPeriod);
    }
}