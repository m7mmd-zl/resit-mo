package clients.cashier;

import catalogue.Basket;
import middle.MiddleFactory;
import middle.OrderProcessing;
import middle.StockReadWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

/**
 * View of the model
 */
public class CashierView implements Observer {
    private static final int H = 300;       // Height of window pixels
    private static final int W = 400;       // Width  of window pixels

    private static final String CHECK = "Check";
    private static final String BUY = "Buy";
    private static final String BOUGHT = "Bought";
    private static final String INCREASE = "+";
    private static final String DECREASE = "-";
    private static final String LOYALTY_COUPON = "Loyalty Coupon";

    private final JLabel theAction = new JLabel();
    private final JTextField theInput = new JTextField();
    private final JTextArea theOutput = new JTextArea();
    private final JScrollPane theSP = new JScrollPane();
    private final JButton theBtCheck = new JButton(CHECK);
    private final JButton theBtBuy = new JButton(BUY);
    private final JButton theBtBought = new JButton(BOUGHT);
    private final JButton theBtIncrease = new JButton(INCREASE);
    private final JButton theBtDecrease = new JButton(DECREASE);
    private final JButton theBtLoyaltyCoupon = new JButton(LOYALTY_COUPON);
    private final JLabel thePic = new JLabel();

    private StockReadWriter theStock = null;
    private OrderProcessing theOrder = null;
    private CashierController cont = null;

    /**
     * Construct the view
     */
    public CashierView(RootPaneContainer rpc, MiddleFactory mf, int x, int y) {
        try {
            theStock = mf.makeStockReadWriter(); // Database access
            theOrder = mf.makeOrderProcessing(); // Process order
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
        Container cp = rpc.getContentPane(); // Content Pane
        Container rootWindow = (Container) rpc; // Root Window
        cp.setLayout(null); // No layout manager
        rootWindow.setSize(W, H); // Size of Window
        rootWindow.setLocation(x, y);

        // Set background color
        cp.setBackground(new Color(0xDEAC80)); // Beige background

        // Button Action Commands
        theBtCheck.setActionCommand(CHECK);
        theBtBuy.setActionCommand(BUY);
        theBtBought.setActionCommand(BOUGHT);
        theBtIncrease.setActionCommand(INCREASE);
        theBtDecrease.setActionCommand(DECREASE);
        theBtLoyaltyCoupon.setActionCommand(LOYALTY_COUPON);

        // Add Action Listeners
        ActionListener buttonListener = e -> {
            if (cont == null) return;
            switch (e.getActionCommand()) {
                case CHECK:
                    cont.doCheck(theInput.getText());
                    break;
                case BUY:
                    cont.doBuy();
                    break;
                case BOUGHT:
                    cont.doBought();
                    break;
                case INCREASE:
                    cont.increaseQuantity();
                    break;
                case DECREASE:
                    cont.decreaseQuantity();
                    break;
                case LOYALTY_COUPON:
                    cont.applyLoyaltyCoupon();
                    break;
            }
        };

        // Style for buttons
        Font buttonFont = new Font("Arial", Font.PLAIN, 12);
        Color buttonColor = new Color(0xC2B280); // Soft Taupe

        // Add Check Button
        theBtCheck.setBounds(16, 25 + 60 * 0, 80, 40);
        theBtCheck.addActionListener(buttonListener);
        theBtCheck.setOpaque(true);
        theBtCheck.setBackground(buttonColor);
        theBtCheck.setBorderPainted(false);
        theBtCheck.setFont(buttonFont);
        cp.add(theBtCheck);

        // Add Buy Button
        theBtBuy.setBounds(16, 25 + 60 * 1, 80, 40);
        theBtBuy.addActionListener(buttonListener);
        theBtBuy.setOpaque(true);
        theBtBuy.setBackground(buttonColor);
        theBtBuy.setBorderPainted(false);
        theBtBuy.setFont(buttonFont);
        cp.add(theBtBuy);

        // Add Bought Button
        theBtBought.setBounds(16, 25 + 60 * 3, 80, 40);
        theBtBought.addActionListener(buttonListener);
        theBtBought.setOpaque(true);
        theBtBought.setBackground(buttonColor);
        theBtBought.setBorderPainted(false);
        theBtBought.setFont(buttonFont);
        cp.add(theBtBought);

        // Add Increase Button
        theBtIncrease.setBounds(200, 270, 80, 40); // Further right
        theBtIncrease.addActionListener(buttonListener);
        theBtIncrease.setOpaque(true);
        theBtIncrease.setBackground(buttonColor);
        theBtIncrease.setBorderPainted(false);
        theBtIncrease.setFont(buttonFont);
        cp.add(theBtIncrease);

        // Add Decrease Button
        theBtDecrease.setBounds(290, 270, 80, 40); // Further right
        theBtDecrease.addActionListener(buttonListener);
        theBtDecrease.setOpaque(true);
        theBtDecrease.setBackground(buttonColor);
        theBtDecrease.setBorderPainted(false);
        theBtDecrease.setFont(buttonFont);
        cp.add(theBtDecrease);

        // Add Loyalty Coupon Button
        theBtLoyaltyCoupon.setBounds(16, 25 + 60 * 4, 140, 40);
        theBtLoyaltyCoupon.addActionListener(buttonListener);
        theBtLoyaltyCoupon.setOpaque(true);
        theBtLoyaltyCoupon.setBackground(buttonColor);
        theBtLoyaltyCoupon.setBorderPainted(false);
        theBtLoyaltyCoupon.setFont(buttonFont);
        cp.add(theBtLoyaltyCoupon);

        // Add Action Label
        theAction.setBounds(110, 25, 270, 20);
        theAction.setText("");
        theAction.setForeground(new Color(0x333333)); // Dark Gray text
        cp.add(theAction);

        // Add Input Area
        theInput.setBounds(110, 50, 270, 40);
        theInput.setText("");
        theInput.setBackground(new Color(0xFAFAFA)); // Off-White
        theInput.setForeground(new Color(0x333333)); // Dark Gray text
        cp.add(theInput);

        // Add Scrolling Pane
        theSP.setBounds(110, 100, 270, 160);
        theOutput.setText("");
        theOutput.setFont(new Font("Monospaced", Font.PLAIN, 12));
        theOutput.setBackground(new Color(0xFAFAFA)); // Off-White
        theOutput.setForeground(new Color(0x333333)); // Dark Gray text
        cp.add(theSP);
        theSP.getViewport().add(theOutput);

        // Add Product Picture
        thePic.setBounds(110, 150, 100, 100);
        cp.add(thePic);

        rootWindow.setVisible(true);
        theInput.requestFocus();
    }

    /**
     * Set the controller object
     * @param c The controller
     */
    public void setController(CashierController c) {
        cont = c;
    }

    /**
     * Update the view based on model changes
     * @param modelC The observed model
     * @param arg The argument containing the update message
     */
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

        ImageIcon pic = model.getPicture();
        if (pic != null) {
            thePic.setIcon(pic);
        } else {
            thePic.setIcon(null);
        }

        theInput.requestFocus();
    }
}
