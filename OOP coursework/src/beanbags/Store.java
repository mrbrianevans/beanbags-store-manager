package beanbags;

import java.io.IOException;

public class Store {


    public static void main(String[] args){
        ObjectArrayList stock = new ObjectArrayList();
        ObjectArrayList reservations = new ObjectArrayList();

        BeanBagStore store = new BeanBagStore() {
            @Override
            public void addBeanBags(int num, String manufacturer, String name, String id, short year, byte month) throws IllegalNumberOfBeanBagsAddedException, BeanBagMismatchException, IllegalIDException, InvalidMonthException {
                if (num<1) throw IllegalNumberOfBeanBagsAddedException;
                for (BeanBag b :
                        stock) {
                    if (Integer.toHexString(b.getID).equals(id)){
                        if(!b.getName().equals(name)) throw BeanBagMismatchException;
                        if(!b.getManufacturer().equals(manufacturer)) throw BeanBagMismatchException;
                    }
                }

                for (int i = 0; i < num; i++) {
                    stock.add(new BeanBag(id, name, manufacturer, year, month));
                }
                
            }

            @Override
            public void addBeanBags(int num, String manufacturer, String name, String id, short year, byte month, String information) throws IllegalNumberOfBeanBagsAddedException, BeanBagMismatchException, IllegalIDException, InvalidMonthException {
                for (int i = 0; i < num; i++) {
                    stock.add(new BeanBag(id, name, manufacturer, year, month, information));
                }
            }

            @Override
            public void setBeanBagPrice(String id, int priceInPence) throws InvalidPriceException, BeanBagIDNotRecognisedException, IllegalIDException {
                for (BeanBag b :
                        stock) {
                    if (Integer.toHexString(b.getID).equals(id)){
                        b.setPrice(priceInPence);
                    }
                }
            }

            @Override
            public void sellBeanBags(int num, String id) throws BeanBagNotInStockException, InsufficientStockException, IllegalNumberOfBeanBagsSoldException, PriceNotSetException, BeanBagIDNotRecognisedException, IllegalIDException {
                int counter = 0;
                int counter2 = 0;
                while (counter < num) {
                    if (Integer.toHexString(stock.get(counter2).getID).equals(id) && stock.get(counter2).getAvailable()){
                        stock.get(counter2).setSold();
                        counter++;
                    }
                    counter2++;
                }
            }

            @Override
            public int reserveBeanBags(int num, String id) throws BeanBagNotInStockException, InsufficientStockException, IllegalNumberOfBeanBagsReservedException, PriceNotSetException, BeanBagIDNotRecognisedException, IllegalIDException {
                int counter = 0;
                int counter2 = 0;
                int reservationNumber;
                while (counter < num) {
                    if (stock.get(counter2).getID.equals(id) && stock.get(counter2).getAvailable()){
                        stock.get(counter2).setReserved();
                        reservationNumber = counter2;
                        counter++;
                    }
                    counter2++;
                }
            }

            @Override
            public void unreserveBeanBags(int reservationNumber) throws ReservationNumberNotRecognisedException {

            }

            @Override
            public void sellBeanBags(int reservationNumber) throws ReservationNumberNotRecognisedException {

            }

            @Override
            public int beanBagsInStock() {
                return 0;
            }

            @Override
            public int reservedBeanBagsInStock() {
                return 0;
            }

            @Override
            public int beanBagsInStock(String id) throws BeanBagIDNotRecognisedException, IllegalIDException {
                return 0;
            }

            @Override
            public void saveStoreContents(String filename) throws IOException {

            }

            @Override
            public void loadStoreContents(String filename) throws IOException, ClassNotFoundException {

            }

            @Override
            public int getNumberOfDifferentBeanBagsInStock() {
                return 0;
            }

            @Override
            public int getNumberOfSoldBeanBags() {
                return 0;
            }

            @Override
            public int getNumberOfSoldBeanBags(String id) throws BeanBagIDNotRecognisedException, IllegalIDException {
                return 0;
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
        };
    }
}
