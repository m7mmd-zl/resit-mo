package clients.cashier;

import catalogue.Basket;
import catalogue.Product;
import debug.DEBUG;
import middle.*;

import javax.swing.*;
import java.util.Observable;

/**
 * Implements the Model of the cashier client
 */
public class CashierModel extends Observable {
    private enum State { process, checked }

    private State theState = State.process;
    private Product theProduct = null;
    private Basket theBasket = null;

    private String pn = "";
    private StockReadWriter theStock = null;
    private OrderProcessing theOrder = null;
    private ImageIcon thePic = null;
    private boolean loyaltyCouponUsed = false; // Flag to check if loyalty coupon has been used

    /**
     * Construct the model of the Cashier
     * @param mf The factory to create the connection objects
     */
    public CashierModel(MiddleFactory mf) {
        try {
            theStock = mf.makeStockReadWriter();
            theOrder = mf.makeOrderProcessing();
        } catch (Exception e) {
            DEBUG.error("CashierModel.constructor\n%s", e.getMessage());
        }
        theState = State.process;
    }

    public Basket getBasket() {
        return theBasket;
    }

    public void doCheck(String productNum) {
        String theAction = "";
        theState = State.process;
        pn = productNum.trim();
        int amount = 1;
        try {
            if (theStock.exists(pn)) {
                Product pr = theStock.getDetails(pn);
                if (pr.getQuantity() >= amount) {
                    theAction = String.format("%s : %7.2f (%2d)",
                            pr.getDescription(),
                            pr.getPrice(),
                            pr.getQuantity());
                    theProduct = pr;
                    theProduct.setQuantity(amount);
                    thePic = theStock.getImage(pn);
                    theState = State.checked;
                } else {
                    theAction = pr.getDescription() + " not in stock";
                }
            } else {
                theAction = "Unknown product number " + pn;
            }
        } catch (StockException e) {
            DEBUG.error("%s\n%s",
                    "CashierModel.doCheck", e.getMessage());
            theAction = e.getMessage();
        }
        setChanged();
        notifyObservers(theAction);
    }

    public void doBuy() {
        String theAction = "";
        int amount = 1;
        try {
            if (theState != State.checked) {
                theAction = "Check if OK with customer first";
            } else {
                boolean stockBought = theStock.buyStock(
                        theProduct.getProductNum(),
                        theProduct.getQuantity());
                if (stockBought) {
                    makeBasketIfReq();
                    theBasket.add(theProduct);
                    theAction = "Purchased " + theProduct.getDescription();
                } else {
                    theAction = "!!! Not in stock";
                }
            }
        } catch (StockException e) {
            DEBUG.error("%s\n%s",
                    "CashierModel.doBuy", e.getMessage());
            theAction = e.getMessage();
        }
        theState = State.process;
        setChanged();
        notifyObservers(theAction);
    }

    public void doBought() {
        String theAction = "";
        try {
            if (theBasket != null && theBasket.size() >= 1) {
                theOrder.newOrder(theBasket);
                theBasket = null;
            }
            theAction = "Next customer";
            theState = State.process;
            theBasket = null;
            loyaltyCouponUsed = false; // Reset the loyalty coupon flag for the next customer
        } catch (OrderException e) {
            DEBUG.error("%s\n%s",
                    "CashierModel.doCancel", e.getMessage());
            theAction = e.getMessage();
        }
        theBasket = null;
        setChanged();
        notifyObservers(theAction);
    }

    public void askForUpdate() {
        setChanged();
        notifyObservers("Welcome");
    }

    private void makeBasketIfReq() {
        if (theBasket == null) {
            try {
                int uon = theOrder.uniqueNumber();
                theBasket = makeBasket();
                theBasket.setOrderNum(uon);
            } catch (OrderException e) {
                DEBUG.error("Comms failure\n" +
                        "CashierModel.makeBasket()\n%s", e.getMessage());
            }
        }
    }

    protected Basket makeBasket() {
        return new Basket();
    }

    public ImageIcon getPicture() {
        return thePic;
    }

    public void increaseProductQuantity() {
        if (theProduct != null) {
            int newQuantity = theProduct.getQuantity() + 1;
            theProduct.setQuantity(newQuantity);
            setChanged();
            notifyObservers("Quantity increased to " + newQuantity);
        }
    }

    public void decreaseProductQuantity() {
        if (theProduct != null) {
            int newQuantity = theProduct.getQuantity() - 1;
            if (newQuantity >= 0) {
                theProduct.setQuantity(newQuantity);
                setChanged();
                notifyObservers("Quantity decreased to " + newQuantity);
            } else {
                setChanged();
                notifyObservers("Quantity cannot be less than 0");
            }
        }
    }

    public void applyLoyaltyCoupon() {
        if (theBasket != null && !theBasket.isEmpty()) {
            if (!loyaltyCouponUsed) {
                
                for (Product product : theBasket) {
                    // Check if the product quantity is 1
                    if (product.getQuantity() == 1) {
                        // Add one more of the same product
                        product.setQuantity(2);
                        // Apply a 50% discount on the product
                        double discountedPrice = product.getPrice() / 2;
                        product.setPrice(discountedPrice);
                    }
                }

                // Mark loyalty coupon as used
                loyaltyCouponUsed = true;

                // Notify about the loyalty coupon application
                setChanged();
                notifyObservers("Loyalty coupon applied: Added an extra product and applied a 50% discount.");
            } else {
                setChanged();
                notifyObservers("Loyalty coupon has already been used.");
            }
        } else {
            setChanged();
            notifyObservers("No items in the basket to apply the loyalty coupon.");
        }
    }
}
