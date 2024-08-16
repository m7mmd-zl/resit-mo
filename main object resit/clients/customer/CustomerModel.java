package clients.customer;

import catalogue.Basket;
import catalogue.Product;
import debug.DEBUG;
import middle.MiddleFactory;
import middle.OrderProcessing;
import middle.StockException;
import middle.StockReader;

import javax.swing.*;
import java.util.Observable;

/**
 * Implements the Model of the customer client.
 * 
 * @version 1.1
 */
public class CustomerModel extends Observable {
    private Product theProduct = null;          // Current product
    private Basket theBasket = null;            // Bought items

    private String pn = "";                    // Product being processed

    private StockReader theStock = null;
    private OrderProcessing theOrder = null;   // If needed, uncomment and use
    private ImageIcon thePic = null;

    /*
     * Construct the model of the Customer.
     * @param mf The factory to create the connection objects
     */
    public CustomerModel(MiddleFactory mf) {
        try {
            theStock = mf.makeStockReader();           // Database access
        } catch (StockException e) {
            DEBUG.error("CustomerModel.constructor\n" +
                    "Database not created?\n%s\n", e.getMessage());
        }
        theBasket = makeBasket();                    // Initial Basket
    }

    /**
     * Return the Basket of products.
     * @return the basket of products
     */
    public Basket getBasket() {
        return theBasket;
    }

    /**
     * Check if the product is in stock.
     * @param productNum The product number
     */
    public void doCheck(String productNum) {
        theBasket.clear();                          // Clear basket
        String theAction = "";
        pn = productNum.trim();                    // Product number
        int amount = 1;                           // Quantity
        try {
            if (theStock.exists(pn)) {            // Stock exists?
                Product pr = theStock.getDetails(pn); // Get product details
                if (pr.getQuantity() >= amount) {  // In stock?
                    theAction = String.format("%s : %7.2f (%2d) ",
                            pr.getDescription(),              // Description
                            pr.getPrice(),                    // Price
                            pr.getQuantity());               // Quantity
                    pr.setQuantity(amount);             // Set required quantity
                    theBasket.add(pr);                  // Add to basket
                    thePic = theStock.getImage(pn);     // Get product image
                    theProduct = pr;                    // Set current product
                } else {                                // Not in stock
                    theAction = pr.getDescription() + " not in stock";
                }
            } else {                                  // Product not found
                theAction = "Unknown product number " + pn;
            }
        } catch (StockException e) {
            DEBUG.error("CustomerModel.doCheck()\n%s", e.getMessage());
            theAction = "Error checking product: " + e.getMessage();
        }
        setChanged();
        notifyObservers(theAction);
    }

    /**
     * Clear the products from the basket.
     */
    public void doClear() {
        theBasket.clear();                        // Clear basket
        String theAction = "Enter Product Number"; // Set display
        thePic = null;                            // No picture
        setChanged();
        notifyObservers(theAction);
    }

    /**
     * Increase the quantity of the selected item in the basket.
     */
    public void doIncrease() {
        if (theProduct != null) {
            int currentQuantity = theProduct.getQuantity();
            theProduct.setQuantity(currentQuantity + 1);
            updateView();
        }
    }

    /**
     * Decrease the quantity of the selected item in the basket.
     */
    public void doDecrease() {
        if (theProduct != null) {
            int currentQuantity = theProduct.getQuantity();
            if (currentQuantity > 1) {
                theProduct.setQuantity(currentQuantity - 1);
                updateView();
            }
        }
    }

    /**
     * Return a picture of the product.
     * @return An instance of an ImageIcon
     */
    public ImageIcon getPicture() {
        return thePic;
    }

    /**
     * Ask for an update of the view called at the start.
     */
    private void askForUpdate() {
        setChanged();
        notifyObservers("START only");
    }

    /**
     * Notify observers and update the view.
     */
    private void updateView() {
        setChanged();
        notifyObservers("UPDATE only");
    }

    /**
     * Make a new Basket.
     * @return an instance of a new Basket
     */
    protected Basket makeBasket() {
        return new Basket();
    }
}
