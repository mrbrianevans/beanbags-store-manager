package beanbags;

import java.io.*;

/**
 * Store interface. This class provides a fully functioning implementor
 * for the BeanBag class.
 * The stock of the store includes all products currently on the premises (those
 * available for immediate sale and those reserved).
 * @author 690024916 and 690023094
 * @version 1
 * @since 09/03/2020
 */

public class Store implements BeanBagStore{

    private ObjectArrayList stock = new ObjectArrayList(); // Initialising new ObjectArrayList
    int nextReservation = 1;

    // error messages - standardised throughout file
    String idNotHexMessage = "Not a hexadecimal number.";
    String idLengthNot8Message = "ID length is not eight";
    String idNameMismatchMessage = "A beanbag with the same ID already exists with a different name";
    String idManufacturerMismatchMessage = "A beanbag with the same ID already exists with a different manufacturer";
    String nonNaturalNumberMessage = "The quantity of beanbags entered is not positive and greater than zero";
    String invalidMonthMessage = "The month entered is not between 1 and 12 inclusive";
    String invalidPriceMessage = "The price entered is less than 1. Price in pence must be greater than 1";
    String BeanBagIDNotRecognisedMessage = "The ID entered is valid form, but not found in stock";
    String PriceNotSetMessage = "The price of this beanbag has not been set, so this beanbag can't be sold yet";
    String BeanBagNotInStockMessage = "The beanbag requested is not currently in stock";
    String IllegalNumberOfBeanBagsSoldMessage = "You have to sell at least one beanbag";
    String reservedExceptionMessage = "At least one bean bag must be reserved.";
    String reservationNumberNotRecognisedException = "There are no current reservations with that reservation number";
    /**
     * Checks the ID to ensure that is is in the required form and throws exceptions if not.
     *
     * @param id ID of bean bag
     * @throws IllegalIDException if the ID is not a positive eight character hexadecimal number
     */
    private void checkID(String id) throws IllegalIDException {
        if (id.length() != 8)
            /*Handle the condition*/
            throw new IllegalIDException(idLengthNot8Message); // If ID is not required length, throw IllegalIDException
        try{
            Integer.parseInt(id, 16); // Try parseInt() with the current ID
        } catch(Exception e){
            throw new IllegalIDException(idNotHexMessage); // Throw exception if not valid
        }
    }

    /**
     * Method checks to see if the ID already exists, and if the other stored elements do not match the pre-existing
     * version.
     *
     * @param manufacturer  bean bag manufacturer
     * @param name          bean bag name
     * @param id            ID of bean bag
     * @throws BeanBagMismatchException if the id already exists (as a current in
     *                                  stock bean bag, or one that has been previously
     *                                  stocked in the store, but the other stored
     *                                  elements (manufacturer, name and free text) do
     *                                  not match the pre-existing version
     */
    private void checkForBeanBagMismatch(String manufacturer, String name, String id)
            throws BeanBagMismatchException {
        id = id.toLowerCase();
        for (int i = 0; i < stock.size(); i++) {
            /* Loop through all stock, incrementing by one each time. */
            BeanBag b = (BeanBag) stock.get(i); // Obtain the object stored with index i in stock
            if (b.getID().equals(id)){
                if(!b.getName().equals(name))
                    throw new BeanBagMismatchException(idNameMismatchMessage); // Throw exception if condition is met
                if(!b.getManufacturer().equals(manufacturer))
                    throw new BeanBagMismatchException(idManufacturerMismatchMessage);
            }
        }
    }

    @Override
    public void addBeanBags(int num, String manufacturer, String name, String id, short year,
                            byte month) throws IllegalNumberOfBeanBagsAddedException,
            BeanBagMismatchException, IllegalIDException, InvalidMonthException {
        id = id.toLowerCase();
        checkID(id);
        if (num<1)
            /* Handle the condition */
            throw new IllegalNumberOfBeanBagsAddedException(nonNaturalNumberMessage);

        checkForBeanBagMismatch(manufacturer, name, id);
        if((month > 12) || (month < 1)) throw new InvalidMonthException(invalidMonthMessage);
        for (int i = 0; i < num; i++) stock.add(new BeanBag(id, name, manufacturer, year, month)); // Add bean bag to stock
    }

    @Override
    public void addBeanBags(int num, String manufacturer, String name, String id, short year,
                            byte month, String information)
            throws IllegalNumberOfBeanBagsAddedException, BeanBagMismatchException,
            IllegalIDException, InvalidMonthException {
        id = id.toLowerCase();
        checkID(id); // Check ID for IllegalIDException
        if (num<1) throw new IllegalNumberOfBeanBagsAddedException(nonNaturalNumberMessage);
        // Throws exception if num of bags is less than 1
        checkForBeanBagMismatch(manufacturer, name, id); // Checks for BeanBagMismatchException

        if((month > 12) || (month < 1)) throw new InvalidMonthException(invalidMonthMessage);
        for (int i = 0; i < num; i++) // Loop for the number of bags "num"
            stock.add(new BeanBag(id, name, manufacturer, year, month, information)); // Add bean bag to stock
    }

    @Override
    public void setBeanBagPrice(String id, int priceInPence)
            throws InvalidPriceException, BeanBagIDNotRecognisedException, IllegalIDException {
        id = id.toLowerCase();
        checkID(id); // Check for IllegalIDException
        if (priceInPence < 1) throw new InvalidPriceException(invalidPriceMessage);
        int counter = 0;
        for (int i = 0; i < stock.size(); i++) { // Loop through entire stock
            BeanBag b = (BeanBag) stock.get(i);
            if (b.getID().equals(id)) { // check if the ID matches
                counter ++;
                if (b.getReserved() && 0 < b.getPrice() && b.getPrice() < priceInPence) { // check if its reserved
                    // this gives the customer the benefit if they reserved a beanbag. They will always get the lower price
                    continue;
                } else if (b.getSold()) {
                    // if a beanbag has already been sold, then don't adjust the price as this would alter sales figures
                    continue;
                }
                b.setPrice(priceInPence); // If the beanbag is not sold, or reserved with a lower price, then setPrice
                stock.replace(b, i); // Replace the original stock item, with the updated one
            }
        }
        if(counter == 0) throw new BeanBagIDNotRecognisedException(BeanBagIDNotRecognisedMessage);
        // if counter has not incremented, then no ID's matched the search and a BeanBagIDNotRecognisedException is thrown
    }

    @Override
    public void sellBeanBags(int num, String id)
            throws BeanBagNotInStockException, InsufficientStockException,
            IllegalNumberOfBeanBagsSoldException, PriceNotSetException,
            BeanBagIDNotRecognisedException, IllegalIDException {
        id = id.toLowerCase();
        checkID(id); // Check id for IllegalIDException
        int counter = 0;
        for (int i = 0; i < stock.size(); i++) { // Loop through entire stock
            BeanBag b = (BeanBag) stock.get(i);
            if (b.getAvailable() && b.getID().equals(id)) counter++; // If the bag is available and the ID's match, increment counter
            if (b.getPrice() < 1 && b.getID().equals(id)) // Exception handling for illegal stock number
                throw new PriceNotSetException(PriceNotSetMessage);
        }
        if (counter == 0)
            throw new BeanBagNotInStockException(BeanBagNotInStockMessage);
        if (counter < num)
            throw new InsufficientStockException("There are only " + counter + " bags in stock.");
        if (num < 1)
            throw new IllegalNumberOfBeanBagsSoldException(IllegalNumberOfBeanBagsSoldMessage);

        int counter2 = 0;
        for (int i = 0; i < stock.size(); i++) {
            /* Increments counter 2 if ID equals id */
            BeanBag b = (BeanBag) stock.get(i);
            if (b.getID().equals(id)) counter2++;
        }

        if (counter2 == 0) throw new BeanBagIDNotRecognisedException(BeanBagIDNotRecognisedMessage);


        counter = 0;
        counter2 = 0;
        while (counter < num) {
            BeanBag b = (BeanBag) stock.get(counter2);
            if (b.getID().equals(id) && b.getAvailable()){
                b.setSold(); // If ID equals id and the bag is available then set the bag to sold
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
        id = id.toLowerCase();
        checkID(id); // Check id for IllegalIDException
        int counter = 0;
        for (int i = 0; i < stock.size(); i++) { // Loop through entire stock
            BeanBag b = (BeanBag) stock.get(i); // Get the object stored at index i
            if (b.getAvailable() && b.getID().equals(id)){
                /*Handle condition*/
                counter++;
            }
            if ((b.getPrice() < 1) && b.getID().equals(id))
                /*Handles condition that will throw an exception if condition is met*/
                throw new PriceNotSetException(PriceNotSetMessage);
        }

        if (counter == 0)
            /*Exception handling for invalid data*/
            throw new BeanBagNotInStockException(BeanBagNotInStockMessage);
        if (counter < num)
            throw new InsufficientStockException("There are only " + counter + " bags in stock.");
        if (num < 1) throw new IllegalNumberOfBeanBagsReservedException(reservedExceptionMessage);

        int counter2 = 0;
        for (int i = 0; i < stock.size(); i++) {
            BeanBag b = (BeanBag) stock.get(i);
            if (b.getID().equals(id)) counter2++; // Gathers the number of bags available matching the ID required
        }

        if (counter2 == 0) throw new BeanBagIDNotRecognisedException(BeanBagIDNotRecognisedMessage);


        counter = 0;
        counter2 = 0;
        while (counter < num) {
            BeanBag b = (BeanBag) stock.get(counter2);
            if (b.getID().equals(id) && b.getAvailable()){
                b.setReserved(nextReservation); // Set the bean bag to reserved
                stock.replace(b, counter2); // Replace the bean bag in stock with new relevant info
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
        for (int i = 0; i < stock.size(); i++) { // Loop through entire stock of bean bags
            BeanBag b = (BeanBag) stock.get(i); // Gets the bean bag at index i in stock
            if (b.getReservationNumber()== reservationNumber){ // If the two reservation numbers are the same
                b.setUnreserved(); // Set to unreserved
                stock.replace(b, i); // replace the bag at i in stock with bean bag b
                counter++;
            }
        }
        if (counter==0) throw new ReservationNumberNotRecognisedException(); // Exception handling for unrecognised ID
    }

    @Override
    public void sellBeanBags(int reservationNumber) throws ReservationNumberNotRecognisedException {

        int counter = 0;
        for (int i = 0; i < stock.size(); i++) { // Loop through stock of bean bags
            BeanBag b = (BeanBag) stock.get(i); // Get beanbag b at index i in stock
            if (b.getReservationNumber()== reservationNumber){
                b.setSold(); // Set bean bag b to sold
                stock.replace(b, i); // replace the bag at i in stock with bean bag b
                counter++; // increment counter by 1
            }
        }
        if (counter==0) throw new ReservationNumberNotRecognisedException(reservationNumberNotRecognisedException);
    }

    @Override
    public int beanBagsInStock() {
        int counter = 0;
        for (int i = 0; i < stock.size(); i++) { // Loop through stock up to bound stock.size()
            BeanBag b = (BeanBag) stock.get(i); // Get bean bag b at index i
            if (!b.getSold()) { // If the bag is not sold
                counter++;
            }
        }
        return counter; // Count represents the number of bags in stock
    }

    @Override
    public int reservedBeanBagsInStock() {
        int counter = 0;
        for (int i = 0; i < stock.size(); i++) {  // Loop through stock up to bound stock.size()
            BeanBag beanBag = (BeanBag) stock.get(i); // Get bean bag b at index i
            if (beanBag.getReserved()) { // If the bag is reserved
                counter++;
            }
        }
        return counter; // Counter represents the number of reserved bags in stock
    }


    @Override
    public int beanBagsInStock(String id)
            throws BeanBagIDNotRecognisedException, IllegalIDException {
        id = id.toLowerCase();
        checkID(id); // Checks for IllegalIDException
        int counter = 0;
        for (int i = 0; i < stock.size(); i++) { // Loop through entire stock of bean bags
            BeanBag b = (BeanBag) stock.get(i);
            if (b.getID().equals(id) && !b.getSold()) { // If the id's match and the bag is not sold
                counter++; // Increment counter
            }
        }
        if (counter == 0) throw new BeanBagIDNotRecognisedException(BeanBagIDNotRecognisedMessage);
        return counter;
    }

    @Override
    public void saveStoreContents(String filename) throws IOException {

        FileOutputStream scribe = new FileOutputStream(new File(filename)); // Create a new FileOutputStream
        ObjectOutputStream outputWriter = new ObjectOutputStream(scribe); // Initialise it as an object

        outputWriter.writeObject(stock); // Write the stock object to the output writer
        outputWriter.writeObject(nextReservation);

        scribe.close(); // End the saving of store contents
        outputWriter.close();
    }

    @Override
    public void loadStoreContents(String filename) throws IOException, ClassNotFoundException {

        FileInputStream reader = new FileInputStream(new File(filename)); // Read the file with the para filename
        ObjectInputStream objectReader = new ObjectInputStream(reader); // Initialise it as an object

        stock = (ObjectArrayList) objectReader.readObject(); // Add the file to the stock as an object
        nextReservation = (int) objectReader.readObject(); // Add the reservations

        reader.close(); // Stop the reading of the file
        objectReader.close();
    }

    @Override
    public int getNumberOfDifferentBeanBagsInStock() {
        ObjectArrayList knownIDs = new ObjectArrayList(); // Create a new object array list of know ID's
        for (int i = 0; i < stock.size(); i++) { // Loop through the entire stock of bean bags
            BeanBag b = (BeanBag) stock.get(i);
            if(!b.getSold()) { // If the bag is not sold
                boolean isIn = false;
                for (int j = 0; j < knownIDs.size(); j++) { // Loop through knownIDs
                    if (b.getID().equals(knownIDs.get(j))) isIn = true;
                }
                if (!isIn) knownIDs.add(b.getID());
            }
        }
        return knownIDs.size(); // Returns the number of unique bean bags
    }

    @Override
    public int getNumberOfSoldBeanBags() {
        int counter = 0;
        for (int i = 0; i < stock.size(); i++) { // Loop through the entire stock of bean bags
            BeanBag beanBag = (BeanBag) stock.get(i);
            if (beanBag.getSold()) { // If the bag is set as sold
                counter++; // Increment counter representing number of sold bags
            }
        }
        return counter;
    }

    @Override
    public int getNumberOfSoldBeanBags(String id)
            throws BeanBagIDNotRecognisedException, IllegalIDException {
        id = id.toLowerCase();
        checkID(id);
        int counter = 0;
        int counter2 = 0;
        for (int i = 0; i < stock.size(); i++) { // Loop through entire stock of bean bags
            BeanBag b = (BeanBag) stock.get(i);
            if (b.getID().equals(id)) { // If the id's are the same
                if(b.getSold()) counter++; // If getSold is true increment counter
                counter2++;
            }
        }
        if (counter2 == 0) throw new BeanBagIDNotRecognisedException(BeanBagIDNotRecognisedMessage); // Exception handling

        return counter;
    }

    @Override
    public int getTotalPriceOfSoldBeanBags() {
        /*For the entirety of the stock, get the total price of sold bean bags*/
        // Counter is set to the sum of sold bags
        int counter = 0;
        int bound = stock.size();
        for (int i = 0; i < bound; i++) {
            BeanBag b = (BeanBag) stock.get(i);
            if (b.getSold()) {
                int price = (int) b.getPrice();
                counter += price;
            }
        }
        return counter;
    }

    @Override
    public int getTotalPriceOfSoldBeanBags(String id)
            throws BeanBagIDNotRecognisedException, IllegalIDException {
        id = id.toLowerCase();
        checkID(id); // Check ID for IllegalIDException
        int counter = 0;
        int counter2 = 0;
        for (int i = 0; i < stock.size(); i++) { // Loop through entire stock of bean bags
            BeanBag b = (BeanBag) stock.get(i);
            if (b.getID().equals(id)) {
                if(b.getSold()) counter += b.getPrice(); // If the bag is sold increment the counter by the price of b
                counter2++;
            }
        }
        if (counter2 == 0) throw new BeanBagIDNotRecognisedException(BeanBagIDNotRecognisedMessage);

        return counter;
    }

    @Override
    public int getTotalPriceOfReservedBeanBags() {
        /*Loops through the stock for reserved bean bags and adds them to the
        counter for a final price sum for reservations*/
        int counter = 0;
        int bound = stock.size();
        for (int i = 0; i < bound; i++) {
            BeanBag b = (BeanBag) stock.get(i);
            if (b.getReserved()) {
                int price = (int) b.getPrice();
                counter += price;
            }
        }
        return counter;
    }

    @Override
    public String getBeanBagDetails(String id)
            throws BeanBagIDNotRecognisedException, IllegalIDException {
        id = id.toLowerCase();
        checkID(id); // Check ID for IllegalIDException
        for (int i = 0; i < stock.size(); i++) { // Loop through the entire of the stock of bean bags
            BeanBag b = (BeanBag) stock.get(i);
            if(b.getID().equals(id)) return b.getInformation(); // Returns the bean bags details
        }
        throw new BeanBagIDNotRecognisedException(BeanBagIDNotRecognisedMessage);
    }

    @Override
    public void empty() {
        /*Empty the object array*/
        stock = new ObjectArrayList();
        nextReservation = 1; // Set the next reservation to 1, resetting reservations
    }

    @Override
    public void resetSaleAndCostTracking() {
        for (int i = 0; i < stock.size(); i++) { // Loop through entire stock of beanbags
            BeanBag b = (BeanBag) stock.get(i);
            if (b.getSold()) stock.remove(i); // Remove the beanbag at index i if sold is true
        }
    }

    @Override
    public void replace(String oldId, String replacementId)
            throws BeanBagIDNotRecognisedException, IllegalIDException {
        checkID(oldId); // Check the old ID for IllegalIDException
        checkID(replacementId); // Check the new ID for IllegalIDException

        int counter = 0;
        for (int i = 0; i < stock.size(); i++) { // Loop through entire stock of beanbags
            BeanBag b = (BeanBag) stock.get(i);
            if (b.getID().equals(oldId)){ // If the ID's equals the old id
                b.setID(replacementId); // Replace the ID's
                counter++;
            }
        }
        if (counter == 0) throw new BeanBagIDNotRecognisedException(BeanBagIDNotRecognisedMessage);
    }
} // end of class Store

