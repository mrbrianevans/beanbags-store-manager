package beanbags;

import java.io.IOException;

public class Store implements BeanBagStore{

    ObjectArrayList stock = new ObjectArrayList();
    int nextReservation = 1;
    ObjectArrayList reservations = new ObjectArrayList();


    @Override
    public void addBeanBags(int num, String manufacturer, String name, String id, short year, byte month) throws IllegalNumberOfBeanBagsAddedException, BeanBagMismatchException, IllegalIDException, InvalidMonthException {
        if (num<1)
            throw new IllegalNumberOfBeanBagsAddedException("Enter a positive number of beanbags to sell");
        for (int i = 0; i < stock.size(); i++) {
            BeanBag b = (BeanBag) stock.get(i);
            if (b.getID().equals(id)){
                if(!b.getName().equals(name)) throw new BeanBagMismatchException("ID already exists with different name.");
                if(!b.getManufacturer().equals(manufacturer)) throw new BeanBagMismatchException("ID already exists with different manufacturer.");
            }
            if (id.length() != 8){
                throw new IllegalIDException("ID length invalid.");
            }
            try{
                Integer.parseInt(id, 16);
            } catch(Exception e){
                throw new IllegalIDException("Not a hexadecimal number.");
            }

            if(month > 12 || month < 1){
                throw new InvalidMonthException("The month entered is not valid.");
            }
        }

        for (int i = 0; i < num; i++) {
            stock.add(new BeanBag(id, name, manufacturer, year, month));
        }
    }

    @Override
    public void addBeanBags(int num, String manufacturer, String name, String id, short year, byte month, String information) throws IllegalNumberOfBeanBagsAddedException, BeanBagMismatchException, IllegalIDException, InvalidMonthException {
        if (num<1) throw new IllegalNumberOfBeanBagsAddedException();
        for (int i = 0; i < stock.size(); i++) {
            BeanBag b = (BeanBag) stock.get(i);
            if (b.getID().equals(id)){
                if(!b.getName().equals(name)) throw new BeanBagMismatchException("ID already exists with different name.");
                if(!b.getManufacturer().equals(manufacturer)) throw new BeanBagMismatchException("ID already exists with different manufacturer.");
            }
            if (id.length() != 8){
                throw new IllegalIDException("ID length invalid.");
            }
            try{
                Integer.parseInt(id, 16);
            } catch(Exception e){
                throw new IllegalIDException("Not a hexadecimal number.");
            }

            if(month > 12 || month < 1){
                throw new InvalidMonthException("The month entered is not valid.");
            }
        }

        for (int i = 0; i < num; i++) {
            stock.add(new BeanBag(id, name, manufacturer, year, month, information));
        }
    }

    @Override
    public void setBeanBagPrice(String id, int priceInPence) throws InvalidPriceException, BeanBagIDNotRecognisedException, IllegalIDException {
        if (priceInPence < 1){
            throw new InvalidPriceException("The price is less than 1");
        }

        int counter = 0;

        for (int i = 0; i < stock.size(); i++) {
            BeanBag b = (BeanBag) stock.get(i);
            if (b.getID().equals(id)){
                b.setPrice(priceInPence);
                stock.replace(b, i);
                counter ++;
            }
        }

        if(counter == 0){
            throw new BeanBagIDNotRecognisedException("The id is valid but not found");
        }

        if (id.length() != 8){
            throw new IllegalIDException("ID length invalid.");
        }
        try{
            Integer.parseInt(id, 16);
        } catch(Exception e){
            throw new IllegalIDException("Not a hexadecimal number.");
        }
    }

    @Override
    public void sellBeanBags(int num, String id) throws BeanBagNotInStockException, InsufficientStockException, IllegalNumberOfBeanBagsSoldException, PriceNotSetException, BeanBagIDNotRecognisedException, IllegalIDException {
        int counter = 0;
        int counter2 = 0;

        for (int i = 0; i < stock.size(); i++) {
            BeanBag b = (BeanBag) stock.get(i);
            if (b.getAvailable() && b.getID().equals(id)){
                counter++;
            }
            if (b.getPrice() < 1 && b.getID().equals(id)){
                throw new PriceNotSetException("The bean bag is in stock, but no price has been set.");
            }
        }
        if (counter == 0){
            throw new BeanBagNotInStockException("There are no bags of the type wanted in stock.");
        }
        if (counter < num){
            throw new InsufficientStockException("There are only "+counter+" bags in stock.");
        }
        if (num < 1){
            throw new IllegalNumberOfBeanBagsSoldException("At least one bean bag must be sold.");
        }

        for (int i = 0; i < stock.size(); i++) {
            BeanBag b = (BeanBag) stock.get(i);
            if (b.getID().equals(id)){
                counter2 ++;
            }
        }

        if (counter2 == 0){
            throw new BeanBagIDNotRecognisedException("The id is not recognised");
        }

        if (id.length() != 8){
            throw new IllegalIDException("ID length invalid.");
        }
        try{
            Integer.parseInt(id, 16);
        } catch(Exception e){
            throw new IllegalIDException("Not a hexadecimal number.");
        }

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
    public int reserveBeanBags(int num, String id) throws BeanBagNotInStockException, InsufficientStockException, IllegalNumberOfBeanBagsReservedException, PriceNotSetException, BeanBagIDNotRecognisedException, IllegalIDException {
        int counter = 0;
        int counter2 = 0;

        for (int i = 0; i < stock.size(); i++) {
            BeanBag b = (BeanBag) stock.get(i);
            if (b.getAvailable() && b.getID().equals(id)){
                counter++;
            }
            if (b.getPrice() < 1 && b.getID().equals(id)){
                throw new PriceNotSetException("The bean bag is in stock, but no price has been set.");
            }
        }
        if (counter == 0){
            throw new BeanBagNotInStockException("There are no bags of the type wanted in stock.");
        }
        if (counter < num){
            throw new InsufficientStockException("There are only "+counter+" bags in stock.");
        }
        if (num < 1){
            throw new IllegalNumberOfBeanBagsReservedException("At least one bean bag must be reserved.");
        }

        for (int i = 0; i < stock.size(); i++) {
            BeanBag b = (BeanBag) stock.get(i);
            if (b.getID().equals(id)){
                counter2 ++;
            }
        }

        if (counter2 == 0){
            throw new BeanBagIDNotRecognisedException("The id is not recognised");
        }

        if (id.length() != 8){
            throw new IllegalIDException("ID length invalid.");
        }
        try{
            Integer.parseInt(id, 16);
        } catch(Exception e){
            throw new IllegalIDException("Not a hexadecimal number.");
        }

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
    public void unreserveBeanBags(int reservationNumber) throws ReservationNumberNotRecognisedException {
        int counter = 0;
        for (int i = 0; i < stock.size(); i++) {
            BeanBag b = (BeanBag) stock.get(i);
            if (b.getReservationNumber()==(reservationNumber)){
                b.setUnreserved();
                stock.replace(b, i);
                counter++;
            }
        }
        if (counter==0)
            throw new ReservationNumberNotRecognisedException();
    }

    @Override
    public void sellBeanBags(int reservationNumber) throws ReservationNumberNotRecognisedException {
        int counter = 0;
        for (int i = 0; i < stock.size(); i++) {
            BeanBag b = (BeanBag) stock.get(i);
            if (b.getReservationNumber()==(reservationNumber)){
                b.setSold();
                stock.replace(b, i);
                counter++;
            }
        }
        if (counter==0)
            throw new ReservationNumberNotRecognisedException();
    }

    @Override
    public int beanBagsInStock() {
        int counter = 0;
        for (int i = 0; i < stock.size(); i++) {
            BeanBag b = (BeanBag) stock.get(i);
            if (!b.getSold()){
                counter++;
            }
        }
        return counter;
    }

    @Override
    public int reservedBeanBagsInStock() {
        int counter = 0;
        for (int i = 0; i < stock.size(); i++) {
            BeanBag b = (BeanBag) stock.get(i);
            if (b.getReserved()){
                counter++;
            }
        }
        return counter;
    }


    @Override
    public int beanBagsInStock(String id) throws BeanBagIDNotRecognisedException, IllegalIDException {
        int counter = 0;
        for (int i = 0; i < stock.size(); i++) {
            BeanBag b = (BeanBag) stock.get(i);
            if (b.getID().equals(id) && !b.getSold()){
                counter++;
            }
        }
        return counter;
    }

    @Override
    public void saveStoreContents(String filename) throws IOException {

    }

    @Override
    public void loadStoreContents(String filename) throws IOException, ClassNotFoundException {

    }

    @Override
    public int getNumberOfDifferentBeanBagsInStock() {
        int counter = 0;
        ObjectArrayList knownIDs = new ObjectArrayList();
        for (int i = 0; i < stock.size(); i++) {
            BeanBag b = (BeanBag) stock.get(i);
            boolean isIn = false;
            for (int j = 0; j < knownIDs.size(); j++) {
                if(b.getID()==knownIDs.get(j))
                    isIn=true;
            }
            if (!isIn){
                knownIDs.add(b.getID());
            }
        }
        return knownIDs.size();
    }

    @Override
    public int getNumberOfSoldBeanBags() {
        int counter = 0;
        for (int i = 0; i < stock.size(); i++) {
            BeanBag b = (BeanBag) stock.get(i);
            if (b.getSold()){
                counter++;
            }
        }
        return counter;
    }

    @Override
    public int getNumberOfSoldBeanBags(String id) throws BeanBagIDNotRecognisedException, IllegalIDException {
        int counter = 0;
        int counter2 = 0;
        for (int i = 0; i < stock.size(); i++) {
            BeanBag b = (BeanBag) stock.get(i);
            if (b.getID().equals(id)) {
                if(b.getSold())
                    counter++;
                counter2++;
            }
        }
        if (counter2 == 0){
            throw new BeanBagIDNotRecognisedException("The id is not recognised");
        }

        if (id.length() != 8){
            throw new IllegalIDException("ID length invalid.");
        }
        try{
            Integer.parseInt(id, 16);
        } catch(Exception e){
            throw new IllegalIDException("Not a hexadecimal number.");
        }
        return counter;
    }

    @Override
    public int getTotalPriceOfSoldBeanBags() {
        return 0;
    }

    @Override
    public int getTotalPriceOfSoldBeanBags(String id) throws BeanBagIDNotRecognisedException, IllegalIDException {
        return 0;
    }

    @Override
    public int getTotalPriceOfReservedBeanBags() {
        return 0;
    }

    @Override
    public String getBeanBagDetails(String id) throws BeanBagIDNotRecognisedException, IllegalIDException {
        return null;
    }

    @Override
    public void empty() {

    }

    @Override
    public void resetSaleAndCostTracking() {

    }

    @Override
    public void replace(String oldId, String replacementId) throws BeanBagIDNotRecognisedException, IllegalIDException {

    }

    public static void main(String[] args) {
        Store str = new Store();
        str.addBeanBags(1, "Beans Ltd.", "Coffee Bean", "10F4E771", (short)2019, (byte)12);
    }
}
