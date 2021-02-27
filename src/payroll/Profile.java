package payroll;

/** 
 * This class defines the  abstract data type profile, which encapsulates the data fields and methods of a profile.
 * @author Christopher Nguyen
 * @author Toshanraju Vysyaraju
 */
public class Profile {
    private String name; //employee’s name in the form “lastname,firstname”
    private String department; //department code: CS, ECE, IT
    private Date dateHired;

    /**
     * Constructor that initialize the values for the profile object for a employee
     * @param name is the the name of the employee 
     * @param department is the department of the employee
     * @param dateHired is the date that the employee was hired
     */ 
    public Profile(String name, String department, Date dateHired) {
        this.name = name;
        this.department = department;
        this.dateHired = dateHired;
    }

    /**
     * Getter method that returns the the name of the employee in the profile
     * @return name of employee in profile
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter method that returns the the department of the employee in the profile
     * @return department of employee in the profile
     */
    public String getDepartment() {
        return this.department;
    }

    /**
     * Getter method that returns the the date that the employee was hired on the profile
     * @return date the employee was hired in the profile
     */
    public Date getDateHired() {
        return this.dateHired;
    }

    /**
     * Method that properly formats the representation of employees profile
     * @return representation of the employees profile
     */
    @Override
    public String toString() {
        String dateString = "";
        dateString += (String.valueOf(this.dateHired.getMonth()) + "/");
        dateString += (String.valueOf(this.dateHired.getDay()) + "/");
        dateString += String.valueOf(this.dateHired.getYear());

        return this.name + "::" + this.department + "::" + dateString;
    }

    /**
     * Method that compares the name, deparment, and date hired 
     * @param obj object passed in to compare if equal
     * @return true if either the name, department or date hired are equal and false if none of them do not
     */
    @Override
    public boolean equals(Object obj) { 
        if(!(obj instanceof Profile)){
            return false;
        }

        Profile objProfile = (Profile)obj;

        if ((!this.name.equals(objProfile.name)) || (!this.department.equals(objProfile.department)) || (this.dateHired.compareTo(objProfile.dateHired) != 0)) {
            return false;
        }

        return true;
    } 
}