package clients.shopDisplay;

import middle.MiddleFactory;
import middle.OrderException;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * The visual display seen by customers (Change to graphical version)
 * Change to a graphical display
 */
public class DisplayView extends Canvas implements Observer {
    private static final long serialVersionUID = 1L;
    private Font font = new Font("Monospaced", Font.BOLD, 24);
    private int H = 300; // Height of window
    private int W = 400; // Width of window
    private String textToDisplay = "";
    private String feedbackMessage = ""; // New variable for feedback message
    private DisplayController cont = null;

    private JTextArea feedbackTextArea; // New text area for feedback
    private JButton submitButton; // Button to submit feedback

    /**
     * Construct the view
     * @param rpc Window in which to construct
     * @param mf Factor to deliver order and stock objects
     * @param x x-coordinate of position of window on screen
     * @param y y-coordinate of position of window on screen
     */
    public DisplayView(RootPaneContainer rpc, MiddleFactory mf, int x, int y) {
        Container cp = rpc.getContentPane(); // Content Pane
        Container rootWindow = (Container) rpc; // Root Window
        cp.setLayout(new BorderLayout()); // Border N E S W CENTER
        rootWindow.setSize(W, H); // Size of Window
        rootWindow.setLocation(x, y); // Position on screen

        // Set up feedback text area
        feedbackTextArea = new JTextArea(3, 20); // 3 rows, 20 columns
        feedbackTextArea.setLineWrap(true);
        feedbackTextArea.setWrapStyleWord(true);
        feedbackTextArea.setFont(font);
        feedbackTextArea.setForeground(Color.BLACK); // Black text for feedback
        feedbackTextArea.setBackground(Color.WHITE);
        JScrollPane feedbackScrollPane = new JScrollPane(feedbackTextArea);

        // Set up submit button
        submitButton = new JButton("Submit Feedback");
        submitButton.addActionListener(e -> submitFeedback());

        // Panel for feedback area and button
        JPanel feedbackPanel = new JPanel();
        feedbackPanel.setLayout(new BorderLayout());
        feedbackPanel.add(feedbackScrollPane, BorderLayout.CENTER);
        feedbackPanel.add(submitButton, BorderLayout.SOUTH);

        cp.add(feedbackPanel, BorderLayout.SOUTH); // Add feedback panel to the bottom

        rootWindow.add(this, BorderLayout.CENTER); // Add to root window
        rootWindow.setVisible(true); // Make visible
    }

    public void setController(DisplayController c) {
        cont = c;
    }

    /**
     * Called to update the display in the shop
     */
    @Override
    public void update(Observable aModelOfDisplay, Object arg) {
        // Code to update the graphical display with the current
        // state of the system
        // Orders awaiting processing
        // Orders being picked in the 'warehouse.
        // Orders awaiting collection

        try {
            Map<String, List<Integer>> res = ((DisplayModel) aModelOfDisplay).getOrderState();

            textToDisplay = "Orders in system" + "\n" +
                    "Waiting        : " + listOfOrders(res, "Waiting") +
                    "\n" +
                    "Being picked   : " + listOfOrders(res, "BeingPicked") +
                    "\n" +
                    "To Be Collected: " + listOfOrders(res, "ToBeCollected");
        } catch (OrderException err) {
            textToDisplay = "\n" + "** Communication Failure **";
        }
        repaint(); // Draw graphically
    }

    @Override
    public void update(Graphics g) { // Called by repaint
        drawScreen((Graphics2D) g); // Draw information on screen
    }

    /**
     * Redraw the screen double buffered
     * @param g Graphics context
     */
    @Override
    public void paint(Graphics g) { // When 'Window' is first
        //  shown or damaged
        drawScreen((Graphics2D) g); // Draw information on screen
    }

    private Dimension theAD; // Alternate Dimension
    private BufferedImage theAI; // Alternate Image
    private Graphics2D theAG; // Alternate Graphics

    public void drawScreen(Graphics2D g) { // Re draw contents
        Dimension d = getSize(); // Size of image

        if ((theAG == null) ||
                (d.width != theAD.width) ||
                (d.height != theAD.height)) { // New size
            theAD = d;
            theAI = (BufferedImage) createImage(d.width, d.height);
            theAG = theAI.createGraphics();
        }
        drawActualScreen(theAG); // draw
        g.drawImage(theAI, 0, 0, this); // Now on screen
    }

    /**
     * Redraw the screen
     * @param g Graphics context
     */
    public void drawActualScreen(Graphics2D g) { // Re draw contents
        g.setPaint(Color.white); // Paint Colour
        W = getWidth();
        H = getHeight(); // Current size

        g.setFont(font);
        g.fill(new Rectangle2D.Double(0, 0, W, H));

        // Draw state of system on display
        String lines[] = textToDisplay.split("\n");
        g.setPaint(Color.black);
        for (int i = 0; i < lines.length; i++) {
            g.drawString(lines[i], 0, 50 + 50 * i);
        }

        // Draw feedback message at the bottom
        g.setPaint(Color.red);
        g.drawString(feedbackMessage, 0, H - 30);
    }

    /**
     * Return a string of order numbers
     * @param map Contains the current state of the system
     * @param key The key of the list requested
     * @return As a string a list of order numbers.
     */
    private String listOfOrders(Map<String, List<Integer>> map, String key) {
        String res = "";
        if (map.containsKey(key)) {
            List<Integer> orders = map.get(key);
            for (Integer i : orders) {
                res += " " + i;
            }
        } else {
            res = "-No key-";
        }
        return res;
    }

    /**
     * Set feedback message
     * @param message The feedback message to display
     */
    public void setFeedbackMessage(String message) {
        feedbackMessage = message;
        repaint(); // Refresh the view
    }

    private void submitFeedback() {
        String feedback = feedbackTextArea.getText();
        if (cont != null) {
            cont.handleOrderCompletion(feedback);
            feedbackTextArea.setText(""); // Clear feedback text area
            JOptionPane.showMessageDialog(this, "Your message has been submitted. We appreciate your feedback.");
        } else {
            JOptionPane.showMessageDialog(this, "Unable to submit feedback. Please try again later.");
        }
    }
}
