package clients.customer;

import catalogue.Basket;
import clients.Picture;
import middle.MiddleFactory;
import middle.StockReader;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Implements the Customer view.
 * @author  Mike Smith University of Brighton
 * @version 1.0
 */
public class CustomerView implements Observer {
    class Name {                              // Names of buttons
        public static final String CHECK    = "Check";
        public static final String CLEAR    = "Clear";
        public static final String INCREASE = "Increase";
        public static final String DECREASE = "Decrease";
    }

    private static final int H = 300;       // Height of window pixels
    private static final int W = 400;       // Width of window pixels

    private final JLabel      theAction  = new JLabel();
    private final JTextField  theInput   = new JTextField();
    private final JTextArea   theOutput  = new JTextArea();
    private final JScrollPane theSP      = new JScrollPane();
    private final JButton     theBtCheck = new JButton(Name.CHECK);
    private final JButton     theBtClear = new JButton(Name.CLEAR);
    private final JButton     theBtIncrease = new JButton(Name.INCREASE);
    private final JButton     theBtDecrease = new JButton(Name.DECREASE);

    private Picture thePicture = new Picture(80, 80);
    private StockReader theStock = null;
    private CustomerController cont = null;

    /**
     * Construct the view
     * @param rpc   Window in which to construct
     * @param mf    Factor to deliver order and stock objects
     * @param x     x-coordinate of position of window on the screen
     * @param y     y-coordinate of position of window on the screen
     */
    public CustomerView(RootPaneContainer rpc, MiddleFactory mf, int x, int y) {
        try {      
            theStock = mf.makeStockReader();             // Database Access
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
        Container cp = rpc.getContentPane();    // Content Pane
        Container rootWindow = (Container) rpc; // Root Window
        cp.setLayout(null);                     // No layout manager
        rootWindow.setSize(W, H);               // Size of Window
        rootWindow.setLocation(x, y);

        Font f = new Font("Monospaced", Font.PLAIN, 12);  // Font

        // Set background color to #DEAC80 (warm beige)
        cp.setBackground(new Color(0xDEAC80));

        Color buttonColor = new Color(0xC2B280); // Soft Taupe color
        Color textBoxColor = new Color(0xFAFAFA); // Off-white for text boxes
        Color textColor = Color.BLACK; // Text color

        // Buttons
        theBtCheck.setBounds(16, 25 + 60 * 0, 80, 40);    // Check button
        theBtCheck.addActionListener(e -> cont.doCheck(theInput.getText()));
        cp.add(theBtCheck);                           // Add to canvas
        theBtCheck.setOpaque(true);
        theBtCheck.setBackground(buttonColor); // Set button color
        theBtCheck.setBorderPainted(false);

        theBtClear.setBounds(16, 25 + 60 * 1, 80, 40);    // Clear button
        theBtClear.addActionListener(e -> cont.doClear());
        cp.add(theBtClear);                           // Add to canvas
        theBtClear.setOpaque(true);
        theBtClear.setBackground(buttonColor); // Set button color
        theBtClear.setBorderPainted(false);

        theBtIncrease.setBounds(16, 25 + 60 * 3, 80, 40);  // Increase button
        theBtIncrease.addActionListener(e -> cont.doIncrease());
        cp.add(theBtIncrease);                       // Add to canvas
        theBtIncrease.setOpaque(true);
        theBtIncrease.setBackground(buttonColor); // Set button color
        theBtIncrease.setBorderPainted(false);

        theBtDecrease.setBounds(16, 25 + 60 * 4, 80, 40);  // Decrease button
        theBtDecrease.addActionListener(e -> cont.doDecrease());
        cp.add(theBtDecrease);                       // Add to canvas
        theBtDecrease.setOpaque(true);
        theBtDecrease.setBackground(buttonColor); // Set button color
        theBtDecrease.setBorderPainted(false);

        // Text Fields
        theAction.setBounds(110, 25, 270, 20);       // Message area
        theAction.setText("");                        // Blank
        cp.add(theAction);                            // Add to canvas

        theInput.setBounds(110, 50, 270, 40);         // Product no area
        theInput.setBackground(textBoxColor);        // Set background color
        theInput.setForeground(textColor);           // Set text color
        theInput.setText("");                         // Blank
        cp.add(theInput);                             // Add to canvas

        theSP.setBounds(110, 100, 270, 160);          // Scrolling pane
        theOutput.setText("");                        // Blank
        theOutput.setFont(f);                         // Uses font  
        theOutput.setBackground(textBoxColor);        // Set background color
        theOutput.setForeground(textColor);           // Set text color
        cp.add(theSP);                                // Add to canvas
        theSP.getViewport().add(theOutput);           // In TextArea

        thePicture.setBounds(16, 25 + 60 * 2, 80, 80);   // Picture area
        cp.add(thePicture);                           // Add to canvas
        thePicture.clear();

        rootWindow.setVisible(true);                  // Make visible
        theInput.requestFocus();                      // Focus is here
    }

    /**
     * The controller object, used so that an interaction can be passed to the controller
     * @param c   The controller
     */
    public void setController(CustomerController c) {
        cont = c;
    }

    /**
     * Update the view
     * @param modelC   The observed model
     * @param arg      Specific args 
     */
    public void update(Observable modelC, Object arg) {
        CustomerModel model = (CustomerModel) modelC;
        String message = (String) arg;
        theAction.setText(message);
        ImageIcon image = model.getPicture();  // Image of product
        if (image == null) {
            thePicture.clear();                  // Clear picture
        } else {
            thePicture.set(image);             // Display picture
        }
        theOutput.setText(model.getBasket().getDetails());
        theInput.requestFocus();               // Focus is here
    }
}
