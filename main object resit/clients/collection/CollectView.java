package clients.collection;

import middle.MiddleFactory;
import middle.OrderProcessing;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Implements the Customer view.
 * @author  Mike Smith University of Brighton
 * @version 1.0
 */

public class CollectView implements Observer {
    private static final String COLLECT = "Collect";
    
    private static final int H = 300;       // Height of window pixels
    private static final int W = 400;       // Width  of window pixels

    private final JLabel      theAction  = new JLabel();
    private final JTextField  theInput   = new JTextField();
    private final JTextArea   theOutput  = new JTextArea();
    private final JScrollPane theSP      = new JScrollPane();
    private final JButton     theBtCollect = new JButton(COLLECT);
    
    private OrderProcessing   theOrder = null;
    private CollectController cont     = null;

    /**
     * Construct the view
     * @param rpc   Window in which to construct
     * @param mf    Factor to deliver order and stock objects
     * @param x     x-cordinate of position of window on screen 
     * @param y     y-cordinate of position of window on screen  
     */
    public CollectView(RootPaneContainer rpc, MiddleFactory mf, int x, int y) {
        try {
            theOrder = mf.makeOrderProcessing(); // Process order
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

        // Add Collect Button
        theBtCollect.setBounds(16, 25 + 60 * 0, 80, 40);
        theBtCollect.setOpaque(true);
        theBtCollect.setBackground(buttonColor);
        theBtCollect.setBorderPainted(false);
        theBtCollect.setFont(buttonFont);
        theBtCollect.addActionListener(e -> cont.doCollect(theInput.getText()));
        cp.add(theBtCollect);

        // Add Action Label
        theAction.setBounds(110, 25, 270, 20);
        theAction.setText("");
        theAction.setForeground(Color.BLACK); // Black text
        cp.add(theAction);

        // Add Input Field
        theInput.setBounds(110, 50, 270, 40);
        theInput.setText("");
        theInput.setBackground(new Color(0xFAFAFA)); // Off-white
        theInput.setForeground(Color.BLACK); // Black text
        cp.add(theInput);

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

    public void setController(CollectController c) {
        cont = c;
    }

    /**
     * Update the view
     * @param modelC   The observed model
     * @param arg      Specific args 
     */
    @Override
    public void update(Observable modelC, Object arg) {
        CollectModel model = (CollectModel) modelC;
        String message = (String) arg;
        theAction.setText(message);
        
        theOutput.setText(model.getResponce());
        theInput.requestFocus(); // Focus is here
    }
}
