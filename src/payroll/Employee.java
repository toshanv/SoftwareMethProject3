package payroll;
import java.text.DecimalFormat;

/** 
 * This class defines the common data and operations for all employee type.
 * Each employee has a profile that uniquely identifies the employee.
 * @author Christopher Nguyen
 * @author Toshanraju Vysyaraju
 */
public class Employee {
    private Profile profile;
    private double payment;

    /** 
     * Constructor that initialize the values for employee object 
     * @param profile is the profile of the employee
     * @param payment is the payment made to the employee
     */ 
    public Employee(Profile profile, double payment) {
        this.profile = profile;
        this.payment = payment;
    }
    
    /**
     * Getter method that returns the the profile of the employee object
     * @return profile of employee
     */
    public Profile getProfile() {
        return this.profile;
    }

    /**
     * Getter method that returns the the payment of the employee object
     * @return payment made to employee
     */
    public double getPayment() {
        return this.payment;
    }

    /**
     * Setter method that sets the value of the payment in the employee object
     * @param payment made to employee
     */
    public void setPayment(double payment) {
        this.payment = payment;
    }

    /**
     * Method represents the status of the employee in proper format 
     * @return representation of employee and their status in the proper format
     */
    @Override
    public String toString() {
        String profileString = this.profile.toString();
        
        DecimalFormat df = new DecimalFormat("#.##");
        df.setGroupingUsed(true);
        df.setGroupingSize(3);
        df.setMinimumFractionDigits(2);

        String paymentString = df.format(this.payment);

        return profileString + "::Payment $" + paymentString;
    }
    
    /**
     * Method that compares two objects and see if they equal each other based on profile
     * @param obj object passed in to compare if equal
     * @return true if the objects equal each other based on profile and false if one of them do not
     */
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Employee)){
            return false;
        }

        Employee objEmployee = (Employee)obj;

        return this.getProfile().equals(objEmployee.getProfile());
    }

    /** 
     * empty method in employee for calculate payments
     */
    public void calculatePayment() {

    }
}