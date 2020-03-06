package beanbags;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.stream.IntStream;

public class Store implements BeanBagStore{

    private ObjectArrayList stock = new ObjectArrayList();
    int nextReservation = 1;

    private void checkID(String id) throws IllegalIDException {
        if (id.length() != 8) throw new IllegalIDException("ID length invalid.");
        try{
            Integer.parseInt(id, 16);
        } catch(Exception e){
            throw new IllegalIDException("Not a hexadecimal number.");
        }
    }
    private void checkForBeanBagMismatch(String manufacturer, String name, String id)
            throws BeanBagMismatchException {
        for (int i = 0; i < stock.size(); i++) {
            BeanBag b = (BeanBag) stock.get(i);
            if (b.getID().equals(id)){
                if(!b.getName().equals(name)) throw new BeanBagMismatchException("ID already exists with different name.");
                if(!b.getManufacturer().equals(manufacturer)) throw new BeanBagMismatchException("ID already exists with different manufacturer.");
            }
        }
    }

    @Override
    public void addBeanBags(int num, String manufacturer, String name, String id, short year,
                            byte month) throws IllegalNumberOfBeanBagsAddedException,
            BeanBagMismatchException, IllegalIDException, InvalidMonthException {
        checkID(id);
        if (num<1)
            throw new IllegalNumberOfBeanBagsAddedException("Enter a positive number of beanbags to sell");
        checkForBeanBagMismatch(manufacturer, name, id);
        if((month > 12) || (month < 1)) throw new InvalidMonthException("The month entered is not valid.");
        for (int i = 0; i < num; i++) stock.add(new BeanBag(id, name, manufacturer, year, month));
    }

    @Override
    public void addBeanBags(int num, String manufacturer, String name, String id, short year,
                            byte month, String information)
            throws IllegalNumberOfBeanBagsAddedException, BeanBagMismatchException,
            IllegalIDException, InvalidMonthException {
        if (num<1) throw new IllegalNumberOfBeanBagsAddedException();
        checkForBeanBagMismatch(manufacturer, name, id);
        checkID(id);
        if((month > 12) || (month < 1)) throw new InvalidMonthException("The month entered is not valid.");
        for (int i = 0; i < num; i++)
            stock.add(new BeanBag(id, name, manufacturer, year, month, information));
    }

    @Override
    public void setBeanBagPrice(String id, int priceInPence)
            throws InvalidPriceException, BeanBagIDNotRecognisedException, IllegalIDException {
        if (priceInPence < 1) throw new InvalidPriceException("The price is less than 1");
        int counter = 0;
        for (int i = 0; i < stock.size(); i++) {
            BeanBag b = (BeanBag) stock.get(i);
            if (b.getID().equals(id)){
                b.setPrice(priceInPence);
                stock.replace(b, i);
                counter ++;
            }
        }
        if(counter == 0) throw new BeanBagIDNotRecognisedException("The id is valid but not found");
        checkID(id);
    }

    @Override
    public void sellBeanBags(int num, String id)
            throws BeanBagNotInStockException, InsufficientStockException,
            IllegalNumberOfBeanBagsSoldException, PriceNotSetException,
            BeanBagIDNotRecognisedException, IllegalIDException {
        int counter = 0;
        for (int i = 0; i < stock.size(); i++) {
            BeanBag b = (BeanBag) stock.get(i);
            if (b.getAvailable() && b.getID().equals(id)) counter++;
            if (b.getPrice() < 1 && b.getID().equals(id))
                throw new PriceNotSetException("The bean bag is in stock, but no price has been set.");
        }
        if (counter == 0)
            throw new BeanBagNotInStockException("There are no bags of the type wanted in stock.");
        if (counter < num)
            throw new InsufficientStockException("There are only " + counter + " bags in stock.");
        if (num < 1)
            throw new IllegalNumberOfBeanBagsSoldException("At least one bean bag must be sold.");
        int counter2 = 0;
        for (int i = 0; i < stock.size(); i++) {
            BeanBag b = (BeanBag) stock.get(i);
            if (b.getID().equals(id)) counter2++;
        }
        if (counter2 == 0) throw new BeanBagIDNotRecognisedException("The id is not recognised");
        checkID(id);
        counter = 0;
        counter2 = 0;
        while (counter < num) {
            BeanBag b = (BeanBag) stock.get(counter2);
            if (b.getID().equals(id) && b.getAvailable()){
                b.setSold();
                stock.replace(b, counter2);
                counter++;
            }
            counter2++;
        }
    }

    @Override
    public int reserveBeanBags(int num, String id) throws BeanBagNotInStockException,
            InsufficientStockException, IllegalNumberOfBeanBagsReservedException,
            PriceNotSetException, BeanBagIDNotRecognisedException, IllegalIDException {
        int counter = 0;
        for (int i = 0; i < stock.size(); i++) {
            BeanBag b = (BeanBag) stock.get(i);
            if (b.getAvailable() && b.getID().equals(id)){
                counter++;
            }
            if ((b.getPrice() < 1) && b.getID().equals(id))
                throw new PriceNotSetException("The bean bag is in stock, but no price has been set.");
        }
        if (counter == 0)
            throw new BeanBagNotInStockException("There are no bags of the type wanted in stock.");
        if (counter < num)
            throw new InsufficientStockException("There are only " + counter + " bags in stock.");
        String reservedExceptionMessage = "At least one bean bag must be reserved.";
        if (num < 1) throw new IllegalNumberOfBeanBagsReservedException(reservedExceptionMessage);
        int counter2 = 0;
        for (int i = 0; i < stock.size(); i++) {
            BeanBag b = (BeanBag) stock.get(i);
            if (b.getID().equals(id)) counter2++;
        }
        if (counter2 == 0) throw new BeanBagIDNotRecognisedException("The id is not recognised");
       checkID(id);
        counter = 0;
        counter2 = 0;
        while (counter < num) {
            BeanBag b = (BeanBag) stock.get(counter2);
            if (b.getID().equals(id) && b.getAvailable()){
                b.setReserved(nextReservation);
                stock.replace(b, counter2);
                counter++;
            }
            counter2++;
        }
        return nextReservation++;
    }

    @Override
    public void unreserveBeanBags(int reservationNumber)
            throws ReservationNumberNotRecognisedException {
        int counter = 0;
        for (int i = 0; i < stock.size(); i++) {
            BeanBag b = (BeanBag) stock.get(i);
            if (b.getReservationNumber()== reservationNumber){
                b.setUnreserved();
                stock.replace(b, i);
                counter++;
            }
        }
        if (counter==0) throw new ReservationNumberNotRecognisedException();
    }

    @Override
    public void sellBeanBags(int reservationNumber) throws ReservationNumberNotRecognisedException {
        int counter = 0;
        for (int i = 0; i < stock.size(); i++) {
            BeanBag b = (BeanBag) stock.get(i);
            if (b.getReservationNumber()== reservationNumber){
                b.setSold();
                stock.replace(b, i);
                counter++;
            }
        }
        if (counter==0) throw new ReservationNumberNotRecognisedException();
    }

    @Override
    public int beanBagsInStock() {
        int count = 0;
        int bound = stock.size();
        for (int i = 0; i < bound; i++) {
            BeanBag b = (BeanBag) stock.get(i);
            if (!b.getSold()) {
                count++;
            }
        }
        return count;
    }

    @Override
    public int reservedBeanBagsInStock() {
        int count = 0;
        int bound = stock.size();
        for (int i = 0; i < bound; i++) {
            BeanBag beanBag = (BeanBag) stock.get(i);
            if (beanBag.getReserved()) {
                count++;
            }
        }
        return count;
    }


    @Override
    public int beanBagsInStock(String id)
            throws BeanBagIDNotRecognisedException, IllegalIDException {
        int count = 0;
        int bound = stock.size();
        for (int i = 0; i < bound; i++) {
            BeanBag b = (BeanBag) stock.get(i);
            if (b.getID().equals(id) && !b.getSold()) {
                count++;
            }
        }
        return count;
    }

    @Override
    public void saveStoreContents(String filename) throws IOException {
        FileOutputStream scribe = new FileOutputStream(new File(filename));
        ObjectOutputStream outputWriter = new ObjectOutputStream(scribe);

        outputWriter.writeObject(stock);
        outputWriter.writeObject(nextReservation);

        scribe.close();
        outputWriter.close();
    }

    @Override
    public void loadStoreContents(String filename) throws IOException, ClassNotFoundException {
        FileInputStream reader = new FileInputStream(new File(filename));
        ObjectInputStream objectReader = new ObjectInputStream(reader);

        stock = (ObjectArrayList) objectReader.readObject();
        nextReservation = (int) objectReader.readObject();

        reader.close();
        objectReader.close();
    }

    @Override
    public int getNumberOfDifferentBeanBagsInStock() {
        ObjectArrayList knownIDs = new ObjectArrayList();
        for (int i = 0; i < stock.size(); i++) {
            BeanBag b = (BeanBag) stock.get(i);
            if(!b.getSold()) {
                boolean isIn = false;
                for (int j = 0; j < knownIDs.size(); j++) {
                    if (b.getID().equals(knownIDs.get(j))) isIn = true;
                }
                if (!isIn) knownIDs.add(b.getID());
            }
        }
        return knownIDs.size();
    }

    @Override
    public int getNumberOfSoldBeanBags() {
        int count = 0;
        int bound = stock.size();
        for (int i = 0; i < bound; i++) {
            BeanBag beanBag = (BeanBag) stock.get(i);
            if (beanBag.getSold()) {
                count++;
            }
        }
        return count;
    }

    @Override
    public int getNumberOfSoldBeanBags(String id)
            throws BeanBagIDNotRecognisedException, IllegalIDException {
        int counter = 0;
        int counter2 = 0;
        for (int i = 0; i < stock.size(); i++) {
            BeanBag b = (BeanBag) stock.get(i);
            if (b.getID().equals(id)) {
                if(b.getSold()) counter++;
                counter2++;
            }
        }
        if (counter2 == 0) throw new BeanBagIDNotRecognisedException("The id is not recognised");
        checkID(id);
        return counter;
    }

    @Override
    public int getTotalPriceOfSoldBeanBags() {
        int counter = IntStream.range(0, stock.size()).mapToObj(i -> {
            return (BeanBag) stock.get(i);
        }).filter(BeanBag::getSold).mapToInt(b -> (int) b.getPrice()).sum();
        return counter;
    }

    @Override
    public int getTotalPriceOfSoldBeanBags(String id)
            throws BeanBagIDNotRecognisedException, IllegalIDException {
        int counter = 0;
        int counter2 = 0;
        for (int i = 0; i < stock.size(); i++) {
            BeanBag b = (BeanBag) stock.get(i);
            if (b.getID().equals(id)) {
                if(b.getSold()) counter += b.getPrice();
                counter2++;
            }
        }
        if (counter2 == 0) throw new BeanBagIDNotRecognisedException("The id is not recognised");
        checkID(id);
        return counter;
    }

    @Override
    public int getTotalPriceOfReservedBeanBags() {
        int counter = IntStream.range(0, stock.size()).mapToObj(i -> {
            return (BeanBag) stock.get(i);
        }).filter(BeanBag::getReserved).mapToInt(b -> (int) b.getPrice()).sum();
        return counter;
    }

    @Override
    public String getBeanBagDetails(String id)
            throws BeanBagIDNotRecognisedException, IllegalIDException {
        checkID(id);
        for (int i = 0; i < stock.size(); i++) {
            BeanBag b = (BeanBag) stock.get(i);
            if(b.getID().equals(id)) return b.getInformation();
        }
        throw new BeanBagIDNotRecognisedException("There are none in stock with that ID.");
    }

    @Override
    public void empty() {
        stock = new ObjectArrayList();
        nextReservation = 1;
    }

    @Override
    public void resetSaleAndCostTracking() {
        for (int i = 0; i < stock.size(); i++) {
            BeanBag b = (BeanBag) stock.get(i);
            if (b.getSold()) stock.remove(i);
        }
    }

    @Override
    public void replace(String oldId, String replacementId)
            throws BeanBagIDNotRecognisedException, IllegalIDException {
        checkID(oldId);
        checkID(replacementId);
        int counter = 0;
        for (int i = 0; i < stock.size(); i++) {
            BeanBag b = (BeanBag) stock.get(i);
            if (b.getID().equals(oldId)){
                b.setID(replacementId);
                counter++;
            }
        }
        if (counter == 0) throw new BeanBagIDNotRecognisedException("The ID is not recognised.");
    }

    void printBeanbag(){
        for (int i = 0; i < stock.size(); i++) {
            BeanBag b = (BeanBag) stock.get(i);
            System.out.println("name: " + b.getName());
            System.out.println("ID: " + b.getID());
            System.out.println("Manufacturer: " + b.getManufacturer());
            System.out.println("Information: " + b.getInformation());
            System.out.println("Available: " + b.getAvailable());
            System.out.println("Date: " + b.getDate());
            System.out.println("reservation number: " + b.getReservationNumber());
            System.out.println("price set: " + b.getPrice());
            System.out.println("Sold: " + b.getSold());
            System.out.println();
        }
    }
}