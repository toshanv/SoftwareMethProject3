package payroll;
import java.util.*; 

/** 
 * This class defines the abstract data type Date, which encapsulates the data fields and methods of a Date.
 * @author Christopher Nguyen
 * @author Toshanraju Vysyaraju
 */
public class Date implements Comparable<Date> {
    // Data Fields of the DateObject
    private int year; // 4 digit value that represents the year of the date
    private int month; // 2 digit value that represents the month of the date
    private int day; // 2 digit value that represents the month of the date

    /** 
     * Constructor that takes mm/dd/yyyy and splits the values to appropriate variable to initialize the values for Date Object 
     * @param date is string format of the date
     */ 
    public Date(String date) {
        final int DATE_SIZE = 3;
        String[] input = date.split("/");

        if (input.length != DATE_SIZE) {
            this.month = 0;
            this.day = 0;
            this.year = 0;
            return;
        }

        for (String val: input) {
            if (!val.matches("-?\\d+(\\.\\d+)?")) {
                this.month = 0;
                this.day = 0;
                this.year = 0;
                return;
            }
        }
        
        this.month = Integer.valueOf(input[0]);
        this.day = Integer.valueOf(input[1]);
        this.year = Integer.valueOf(input[2]);
    }

    /** 
     * Create an object with today's date (see Calendar class)
     */
    public Date() {
        final int MONTH_CALIBRATOR = 1;
        
        final int YEAR_CALIBRATOR = 1900;

        int currMonth = Calendar.getInstance().getTime().getMonth() + MONTH_CALIBRATOR;
        int currDate = Calendar.getInstance().getTime().getDate();
        int currYear = Calendar.getInstance().getTime().getYear() + YEAR_CALIBRATOR;
        
        this.month = currMonth;
        this.day = currDate;
        this.year = currYear;
    }

    // Getter Methods
    /**
     * Getter method that returns the year value of the Date Object
     * @return integer representation of the year
     */
    public int getYear() {
        return this.year;
    }

    /**
     * Getter method that returns the month value of the Date Object
     * @return integer representation of the month
     */
    public int getMonth() {
        return this.month;
    }

    /** 
     * Getter method that returns the day of the Date object
     * @return the value of day of object
     */ 
    public int getDay() {
        return this.day;
    }

    /** 
     * Method used to check if the date is valid by making sure it is not before 1900, after today's date, and that it is a valid day for the month selected
     * @return boolean value true if the date is valid, and false if the date was not valid
     */ 
    public boolean isValid() {
        final int YEAR_CALIBRATOR = 1900;
        if (this.year < YEAR_CALIBRATOR) {
            return false;
        }

        Date today = new Date();
        if (this.compareTo(today) > 0) {
            return false;
        }
        
        final int QUADRENNIAL = 4;
        final int CENTENNIAL = 100;
        final int QUATERCENTENNIAL = 400;
        boolean leap;
        if (this.year % QUADRENNIAL == 0) {
            if (this.year % CENTENNIAL == 0) {
                if (this.year % QUATERCENTENNIAL == 0) {
                    leap = true;
                } else {
                    leap = false;
                }
            } else {
                leap = true;
            }
        } else {
            leap = false;
        }

        if (this.day < 1) {
            return false;
        }

        final int JANUARY = 1;
        final int FEBRUARY = 2;
        final int MARCH = 3;
        final int APRIL = 4;
        final int MAY = 5;
        final int JUNE = 6;
        final int JULY = 7;
        final int AUGUST = 8;
        final int SEPTEMBER = 9;
        final int OCTOBER = 10;
        final int NOVEMBER = 11;
        final int DECEMBER = 12;

        final int MAX_DAYS_LARGE = 31;
        final int MAX_DAYS_SMALL = 30;
        final int MAX_DAYS_FEB_LEAP = 29;
        final int MAX_DAYS_FEB = 28;

        switch (this.month) {
            case JANUARY:
                if (this.day > MAX_DAYS_LARGE) {
                    return false;
                }
                
                break;
            case FEBRUARY:
                if (leap) {
                    if (this.day > MAX_DAYS_FEB_LEAP) {
                        return false;
                    } 
                } else if (this.day > MAX_DAYS_FEB) {
                    return false;
                }

                break;
            case MARCH:
                if (this.day > MAX_DAYS_LARGE) {
                    return false;
                }

                break;
            case APRIL:
                if (this.day > MAX_DAYS_SMALL) {
                    return false;
                }

                break;
            case MAY:
                if (this.day > MAX_DAYS_LARGE) {
                    return false;
                }

                break;
            case JUNE:
                if (this.day > MAX_DAYS_SMALL) {
                    return false;
                }
                
                break;
            case JULY:
                if (this.day > MAX_DAYS_LARGE) {
                    return false;
                }

                break;
            case AUGUST:
                if (this.day > MAX_DAYS_LARGE) {
                    return false;
                }

                break;
            case SEPTEMBER:
                if (this.day > MAX_DAYS_SMALL) {
                    return false;
                }
                
                break;
            case OCTOBER:
                if (this.day > MAX_DAYS_LARGE) {
                    return false;
                }

                break;
            case NOVEMBER:
                if (this.day > MAX_DAYS_SMALL) {
                    return false;
                }

                break;
            case DECEMBER:
                if (this.day > MAX_DAYS_LARGE) {
                     return false;
                }

                break;
            default:
                return false;
        }

        return true;
    }

    /** 
     * Method that compares two dates
     * @param date is the date passed in to compare 
     * @return integer value one if this.year is greater than date passed, integer value negative on if this.year is less than date passed, and 0 if the dates are equal
     */ 
    @Override
    public int compareTo(Date date) {
        if (this.year > date.getYear()) {
            return 1;
        } else if (this.year == date.getYear()) {
            if (this.month > date.getMonth()) {
                return 1;
            } else if (this.month == date.getMonth()) {
                if (this.day > date.getDay()) {
                    return 1;
                } else if (this.day == date.getDay()) {
                    return 0;
                }
            } 
        }

        return -1;
    } 
}