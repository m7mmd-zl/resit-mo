package clients.cashier;

import catalogue.Basket;
import middle.MiddleFactory;
import middle.OrderProcessing;
import middle.StockReadWriter;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Controller of the cashier client.
 */
public class CashierController implements Observer {
    private static final int H = 400; // Height of window pixels
    private static final int W = 400; // Width of window pixels

    private static final String CHECK = "Check";
    private static final String BUY = "Buy";
    private static final String BOUGHT = "Bought";
    private static final String INCREASE = "Increase";
    private static final String DECREASE = "Decrease";
    private static final String LOYALTY_COUPON = "Loyalty Coupon"; // Added button for loyalty coupon

    private final JLabel theAction = new JLabel();
    private final JTextField theInput = new JTextField();
    private final JTextArea theOutput = new JTextArea();
    private final JScrollPane theSP = new JScrollPane();
    private final JButton theBtCheck = new JButton(CHECK);
    private final JButton theBtBuy = new JButton(BUY);
    private final JButton theBtBought = new JButton(BOUGHT);
    private final JButton theBtIncrease = new JButton(INCREASE);
    private final JButton theBtDecrease = new JButton(DECREASE);
    private final JButton theBtLoyaltyCoupon = new JButton(LOYALTY_COUPON); // New button for loyalty coupon
    private final JLabel thePic = new JLabel();

    private StockReadWriter theStock = null;
    private OrderProcessing theOrder = null;
    private CashierModel model = null;
    private CashierView view = null;

    /**
     * Construct the controller and initialize the UI.
     * @param rpc   Window in which to construct
     * @param mf    Factory to deliver order and stock objects
     * @param x     x-coordinate of position of window on screen 
     * @param y     y-coordinate of position of window on screen  
     */
    public CashierController(RootPaneContainer rpc, MiddleFactory mf, int x, int y) {
        try {
            theStock = mf.makeStockReadWriter();
            theOrder = mf.makeOrderProcessing();
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
        Container cp = rpc.getContentPane();
        Container rootWindow = (Container) rpc;
        cp.setLayout(null);
        rootWindow.setSize(W, H);
        rootWindow.setLocation(x, y);

        // Add Check Button
        theBtCheck.setBounds(16, 25 + 60 * 0, 80, 40);
        theBtCheck.addActionListener(e -> doCheck(theInput.getText()));
        cp.add(theBtCheck);
        theBtCheck.setOpaque(true);
        theBtCheck.setBackground(Color.YELLOW);
        theBtCheck.setBorderPainted(false);

        Font f = new Font("Monospaced", Font.PLAIN, 12);

        // Add Buy Button
        theBtBuy.setBounds(16, 25 + 60 * 1, 80, 40);
        theBtBuy.addActionListener(e -> doBuy());
        cp.add(theBtBuy);

        // Add Bought Button
        theBtBought.setBounds(16, 25 + 60 * 3, 80, 40);
        theBtBought.addActionListener(e -> doBought());
        cp.add(theBtBought);

        // Add Increase Button
        theBtIncrease.setBounds(110, 270, 80, 40);
        theBtIncrease.addActionListener(e -> increaseQuantity());
        cp.add(theBtIncrease);

        // Add Decrease Button
        theBtDecrease.setBounds(200, 270, 80, 40);
        theBtDecrease.addActionListener(e -> decreaseQuantity());
        cp.add(theBtDecrease);

        // Add Loyalty Coupon Button
        theBtLoyaltyCoupon.setBounds(16, 25 + 60 * 4, 140, 40); // Adjust position for the new button
        theBtLoyaltyCoupon.addActionListener(e -> applyLoyaltyCoupon()); // New button action
        cp.add(theBtLoyaltyCoupon);

        // Add Action Label
        theAction.setBounds(110, 25, 270, 20);
        theAction.setText("");
        cp.add(theAction);

        // Add Input Area
        theInput.setBounds(110, 50, 270, 40);
        theInput.setText("");
        cp.add(theInput);

        // Add Scrolling Pane
        theSP.setBounds(110, 100, 270, 160);
        theOutput.setText("");
        theOutput.setFont(f);
        cp.add(theSP);
        theSP.getViewport().add(theOutput);

        // Add Product Picture
        thePic.setBounds(110, 150, 100, 100);
        cp.add(thePic);

        rootWindow.setVisible(true);
        theInput.requestFocus();
    }

    // Constructor to initialize with model and view
    public CashierController(CashierModel model, CashierView view) {
        this.model = model;
        this.view = view;
        this.model.addObserver(this); // Set this controller as an observer of the model
    }

    // Implementation of actions
    public void doCheck(String productNum) {
        model.doCheck(productNum);
    }

    public void doBuy() {
        model.doBuy();
    }

    public void doBought() {
        model.doBought();
    }

    public void increaseQuantity() {
        if (model != null) {
            model.increaseProductQuantity();
        }
    }

    public void decreaseQuantity() {
        if (model != null) {
            model.decreaseProductQuantity();
        }
    }

    public void applyLoyaltyCoupon() {
        if (model != null) {
            model.applyLoyaltyCoupon(); // Call the method in the model to apply the loyalty coupon
        }
    }

    @Override
    public void update(Observable modelC, Object arg) {
        CashierModel model = (CashierModel) modelC;
        String message = (String) arg;
        theAction.setText(message);
        Basket basket = model.getBasket();
        if (basket == null) {
            theOutput.setText("Customer's order");
        } else {
            theOutput.setText(basket.getDetails());
        }

        // Update the product image
        ImageIcon pic = model.getPicture();
        if (pic != null) {
            thePic.setIcon(pic);
        } else {
            thePic.setIcon(null); // Clear image if not available
        }

        theInput.requestFocus();
    }
}
