package beanbags;

public class BeanBag {
    private int ID;
    private String name;
    private double price;
    private short year;
    private byte month;
    private String manufacturer;
    private String information;
    private boolean sold;
    private int reservationNumber;

    public BeanBag(String ID, String name, String manufacturer, short year, byte month){
        this.ID = Integer.parseInt(ID,16);
        this.name = name;
        this.manufacturer = manufacturer;
        this.year = year;
        this.month = month;
    }

    public BeanBag(String ID, String name, String manufacturer, short year, byte month, String information){
        this.ID = Integer.parseInt(ID,16);
        this.name = name;
        this.manufacturer = manufacturer;
        this.year = year;
        this.month = month;
        this.information = information;
    }

    public void setPrice(double price){
        this.price = price;
    }

    public void setSold(){
        this.sold = true;
        this.reservationNumber = 0;
    }


    public void setReserved(int reservationNumber){
        this.reservationNumber = reservationNumber;
    }

    public void setUnreserved(){
        this.reservationNumber = 0;
    }

    public void setID(String ID){
        this.ID = Integer.parseInt(ID, 16);
    }

    public String getID(){
        return Integer.toHexString(ID);
    }

    public String getDate(){
        return year + " " + month;
    }

    public String getInformation(){
        return information;
    }

    public boolean getReserved(){
        return reservationNumber!=0;
    }

    public boolean getSold(){
        return sold;
    }

    public double getPrice(){
        return price;
    }

    public String getName(){
        return name;
    }

    public String getManufacturer(){
        return manufacturer;
    }

    public boolean getAvailable(){
        return !getReserved() && !getSold();
    }

    public int getReservationNumber(){
        return reservationNumber;
    }
}