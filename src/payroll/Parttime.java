package payroll;
import java.text.DecimalFormat;

/** 
 * This class extends the Employee class and includes specific data and operations to a part-time employee.
 * @author Christopher Nguyen
 * @author Toshanraju Vysyaraju
 */
public class Parttime extends Employee {
    private double hourlyRate;
    private int hoursWorked;

    /** 
     * Constructor that initialize the values for the parttime employee 
     * @param profile is the profile of the parttime employee 
     * @param payment is the payment made to the parttime employee
     * @param hourlyRate is the hourly rate of the parttime employee
     * @param hoursWorked is the hours that the parttime employee worked
     */ 
    public Parttime(Profile profile, double payment, double hourlyRate, int hoursWorked) {
        super(profile, payment);
        this.hourlyRate = hourlyRate;
        this.hoursWorked = hoursWorked;
    }

    /**
     * Getter method that returns the hourly rate of the parttime employee 
     * @return the hourly rate of the parttime employee
     */
    public double getHourlyRate() {
        return this.hourlyRate;
    }

     /**
     * Getter method that returns the hourly worked of the parttime employee 
     * @return the hours worked of the parttime employee
     */
    public int getHoursWorked() {
        return this.hoursWorked;
    }

    /**
     * Setter method that returns the sets the hours worked of the parttime employee 
     */
    public void setHoursWorked(int hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

    /**
     * Method that properly formats the status for the parttime employee in the company
     * @return representation of the parttime employee and their status in the proper format
     */
    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setGroupingUsed(true);
        df.setGroupingSize(3);
        df.setMinimumFractionDigits(2);

        String hourlyRateString = df.format(this.hourlyRate);

        return super.toString() + "::PART TIME::Hourly Rate $" + hourlyRateString + "::Hours worked this period: " + this.hoursWorked;
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
     * method to calculate payments for each parttime employee in the company
     */
    @Override
    public void calculatePayment() {
        final double OT_PAY_INCREASE = 1.5;
        final int REGULAR_HOURS = 80;

        int regularHours;
        int overtimeHours;
        
        if (this.hoursWorked > REGULAR_HOURS) {
            regularHours = REGULAR_HOURS;
            overtimeHours = this.hoursWorked - REGULAR_HOURS;
        } else {
            regularHours = this.hoursWorked;
            overtimeHours = 0;
        }

        double payForPeriod = ((regularHours * this.hourlyRate) + (overtimeHours * (this.hourlyRate * OT_PAY_INCREASE)));

        super.setPayment(payForPeriod);
    }
}