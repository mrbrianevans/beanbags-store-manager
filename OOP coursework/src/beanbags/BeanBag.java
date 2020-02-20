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
    private boolean reserved;

    public BeanBag(String ID, String name, String manufacturer, short year, byte month){
        this.ID = Integer.parseInt(ID,16);
        this.name = name;
        this.manufacturer = manufacturer;
        this.year = year;
        this.month = month;
    }

    public BeanBag(String ID, String name, String manufacturer, String year, String month, String information){
        this.ID = Integer.parseInt(ID,16);
        this.name = name;
        this.manufacturer = manufacturer;
        this.year = (short)Integer.parseInt(year);
        this.month = (byte)Integer.parseInt(month);
        this.information = information;
    }

    public void setPrice(double price){
        this.price = price;
    }

    public void setSold(){
        this.sold = true;
    }

    public void setReserved(){
        this.reserved = true;
    }

    public void setUnreserved(){
        this.reserved = false;
    }

    public void setID(String ID){
        this.ID = Integer.parseInt(ID, 16);
    }

    public int getID(){
        return this.ID;
    }

    public String getInformation(){
        return this.information;
    }

    public boolean getReserved(){
        return this.reserved;
    }

    public boolean getSold(){
        return this.sold;
    }

    public double getPrice(){
        return this.price;
    }
}
