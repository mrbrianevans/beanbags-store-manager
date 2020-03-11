package beanbags;
import java.io.Serializable;

/**
 * BeanBag class contains the BeanBag objects used in Store.java
 * Constructors, attributes and getters/setters
 */
class BeanBag implements Serializable{
    private int ID; //note the ID is stored as an integer
    private String name;
    private double price;
    private short year; // year of manufacture
    private byte month; // month of manufacture
    private String manufacturer;
    private String information;
    private boolean sold;
    private int reservationNumber;

    /**
     * Constructor for BeanBag with no free text information
     * @param ID hexadecimal ID in String form
     * @param name of BeanBag (String)
     * @param manufacturer String of the name of the manufacturer
     * @param year of manufacture as a short type (eg 1984)
     * @param month of manufacture as a byte type (eg 12 for December)
     */
    BeanBag(String ID, String name, String manufacturer, short year, byte month){
        this.ID = Integer.parseInt(ID,16);
        this.name = name;
        this.manufacturer = manufacturer;
        this.year = year;
        this.month = month;
    }

    /**
     * Constructor for BeanBag with free text information
     * @param ID hexadecimal ID in String form
     * @param name of BeanBag (String)
     * @param manufacturer String of the name of the manufacturer
     * @param year of manufacture as a short type (eg 1984)
     * @param month of manufacture as a byte type (eg 12 for December)
     * @param information A text field to contain free trade information etc
     */
    BeanBag(String ID, String name, String manufacturer, short year, byte month, String information){
        // assert statements to check that internal methods are passing the right values to the constructor.
        // this checks pre-conditions. The methods in Store.java should throw exceptions for invalid input
        assert 0 < month;
        assert month < 13;
        assert ID.length() == 8;

        this.ID = Integer.parseInt(ID,16);
        this.name = name;
        this.manufacturer = manufacturer;
        this.year = year;
        this.month = month;
        this.information = information;
        assert Integer.toHexString(this.ID).length() == 8; // post condition to check the conversion was correct
    }

    /**
     * price attribute setter for BeanBag objects
     * @param price Price to set the current instance of BeanBag
     */
    void setPrice(double price){
        assert price > 0; // checks that the internal method only passes a positive price to the package-private setter method
        this.price = price;
    }

    /**
     * Sets the current BeanBag to sold. (sold=true)
     */
    void setSold(){
        this.sold = true;
        this.reservationNumber = 0;
    }


    /**
     * Sets a BeanBag to reserved
     * @param reservationNumber the id of the reservation
     */
    void setReserved(int reservationNumber){
        this.reservationNumber = reservationNumber;
    }

    /**
     * If a reservation is cancelled or sold, this will set the object as though it had never been reserved
     */
    void setUnreserved(){
        this.reservationNumber = 0; // reservationNumber zero indicates not reserved
    }

    /**
     * This method is used to change the ID of an existing BeanBag
     * @param ID the new ID to assign to the current BeanBag object
     */
    void setID(String ID){
        this.ID = Integer.parseInt(ID, 16);
    }

    /**
     * Getter of ID for BeanBag
     * @return A String object of the BeanBag's ID
     */
    String getID(){
        assert Integer.toHexString(ID).length() == 8; // pre-condition to make sure its returning a valid ID
        return Integer.toHexString(ID);
    }

    /**
     * Getter of date of manufacture for BeanBags
     * @return String of manufacture date in the form "YYYY-MM"
     */
    String getDate(){
        return year + "-" + month;
    }

    /**
     * Getter for the information attribute
     * @return the free trade status of the BeanBag, if set
     */
    String getInformation(){
        return information;
    }



    /**
     * Getter method for sold attribute
     * @return boolean of whether or not the BeanBag has been marked as sold
     */
    boolean getSold(){
        return sold;
    }

    /**
     * Getter method of price attribute
     * @return price in pence of the BeanBag
     */
    double getPrice(){
        return price;
    }

    /**
     * Getter method for the name attribute
     * @return String of the name assigned to the BeanBag
     */
    String getName(){
    return name;
}

    /**
     * Getter method for the manufacturer attribute
     * @return String of the manufacturer
     */
    String getManufacturer(){
        return manufacturer;
    }

    /**
     * Checks if the BeanBag has been assigned a reservationNumber
     * @return boolean where true is reserved, and false is not reserved
     */
    boolean getReserved(){
        return reservationNumber!=0; // if reservationNumber is zero, then it is not reserved
    }

    /**
     * Checks if the BeanBag is available to sell or reserve
     * @return boolean where available=true and unavailable=false
     */
    boolean getAvailable(){
        return !getReserved() && !getSold(); // boolean operator for if not reserved and not sold.
    }

    /**
     * Getter method for reservationNumber attribute
     * @return int reservationNumber of the BeanBag
     */
    int getReservationNumber(){
        return reservationNumber;
    }
}