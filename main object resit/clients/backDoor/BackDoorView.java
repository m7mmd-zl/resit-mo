package clients.backDoor;

import middle.MiddleFactory;
import middle.StockReadWriter;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Implements the Customer view.
 * @author  Mike Smith University of Brighton
 * @version 1.0
 */

public class BackDoorView implements Observer {
    private static final String RESTOCK  = "Add";
    private static final String CLEAR    = "Clear";
    private static final String QUERY    = "Query";

    private static final int H = 300;       // Height of window pixels
    private static final int W = 400;       // Width  of window pixels

    private final JLabel      theAction  = new JLabel();
    private final JTextField  theInput   = new JTextField();
    private final JTextField  theInputNo = new JTextField();
    private final JTextArea   theOutput  = new JTextArea();
    private final JScrollPane theSP      = new JScrollPane();
    private final JButton     theBtClear = new JButton(CLEAR);
    private final JButton     theBtRStock = new JButton(RESTOCK);
    private final JButton     theBtQuery = new JButton(QUERY);

    private StockReadWriter theStock     = null;
    private BackDoorController cont = null;

    /**
     * Construct the view
     * @param rpc   Window in which to construct
     * @param mf    Factor to deliver order and stock objects
     * @param x     x-cordinate of position of window on screen 
     * @param y     y-cordinate of position of window on screen  
     */
    public BackDoorView(RootPaneContainer rpc, MiddleFactory mf, int x, int y) {
        try {
            theStock = mf.makeStockReadWriter(); // Database access
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
        Container cp = rpc.getContentPane();    // Content Pane
        Container rootWindow = (Container) rpc; // Root Window
        cp.setLayout(null);                     // No layout manager
        rootWindow.setSize(W, H);               // Size of Window
        rootWindow.setLocation(x, y);

        // Set background color
        cp.setBackground(new Color(0xDEAC80)); // Warm Beige

        // Font for text areas
        Font f = new Font("Monospaced", Font.PLAIN, 12);

        // Button style
        Color buttonColor = new Color(0xC2B280); // Soft Taupe
        Font buttonFont = new Font("Arial", Font.PLAIN, 12);

        // Add Query Button
        theBtQuery.setBounds(16, 25 + 60 * 0, 80, 40);
        theBtQuery.setOpaque(true);
        theBtQuery.setBackground(buttonColor);
        theBtQuery.setBorderPainted(false);
        theBtQuery.setFont(buttonFont);
        theBtQuery.addActionListener(e -> cont.doQuery(theInput.getText()));
        cp.add(theBtQuery);

        // Add Restock Button
        theBtRStock.setBounds(16, 25 + 60 * 1, 80, 40);
        theBtRStock.setOpaque(true);
        theBtRStock.setBackground(buttonColor);
        theBtRStock.setBorderPainted(false);
        theBtRStock.setFont(buttonFont);
        theBtRStock.addActionListener(e -> cont.doRStock(theInput.getText(), theInputNo.getText()));
        cp.add(theBtRStock);

        // Add Clear Button
        theBtClear.setBounds(16, 25 + 60 * 2, 80, 40);
        theBtClear.setOpaque(true);
        theBtClear.setBackground(buttonColor);
        theBtClear.setBorderPainted(false);
        theBtClear.setFont(buttonFont);
        theBtClear.addActionListener(e -> cont.doClear());
        cp.add(theBtClear);

        // Add Action Label
        theAction.setBounds(110, 25, 270, 20);
        theAction.setText("");
        theAction.setForeground(Color.BLACK); // Black text
        cp.add(theAction);

        // Add Input Area
        theInput.setBounds(110, 50, 120, 40);
        theInput.setText("");
        theInput.setBackground(new Color(0xFAFAFA)); // Off-white
        theInput.setForeground(Color.BLACK); // Black text
        cp.add(theInput);

        // Add Input Number Area
        theInputNo.setBounds(260, 50, 120, 40);
        theInputNo.setText("0");
        theInputNo.setBackground(new Color(0xFAFAFA)); // Off-white
        theInputNo.setForeground(Color.BLACK); // Black text
        cp.add(theInputNo);

        // Add Scrolling Pane
        theSP.setBounds(110, 100, 270, 160);
        theOutput.setText("");
        theOutput.setFont(f);
        theOutput.setBackground(new Color(0xFAFAFA)); // Off-white
        theOutput.setForeground(Color.BLACK); // Black text
        cp.add(theSP);
        theSP.getViewport().add(theOutput);

        rootWindow.setVisible(true); // Make visible
        theInput.requestFocus();     // Focus is here
    }

    public void setController(BackDoorController c) {
        cont = c;
    }

    /**
     * Update the view
     * @param modelC   The observed model
     * @param arg      Specific args 
     */
    @Override
    public void update(Observable modelC, Object arg) {
        BackDoorModel model = (BackDoorModel) modelC;
        String message = (String) arg;
        theAction.setText(message);

        if (model.getBasket() != null) {
            theOutput.setText(model.getBasket().getDetails());
        } else {
            theOutput.setText("");
        }
        
        theInput.requestFocus();
    }
}
