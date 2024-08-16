package clients.shopDisplay;

import middle.MiddleFactory;
import middle.OrderException;
import middle.OrderProcessing;

import java.util.List;
import java.util.Map;
import java.util.Observable;

/**
 * Implements the Model of the display client
 */
public class DisplayModel extends Observable {
    private OrderProcessing theOrder;

    /**
     * Set up initial connection to the order processing system
     * @param mf Factory to return an object to access the order processing system
     */
    public DisplayModel(MiddleFactory mf) {
        try {
            theOrder = mf.makeOrderProcessing(); // Initialize OrderProcessing
        } catch (Exception e) {
            // Log error if OrderProcessing cannot be initialized
            System.err.println("Error initializing OrderProcessing: " + e.getMessage());
            e.printStackTrace(); // Print stack trace for debugging
        }
        // Start background thread to periodically update the display
        new Thread(this::backgroundRun).start();
    }

    /**
     * Run as a thread in background to continually update the display
     */
    private void backgroundRun() {
        while (true) {
            try {
                Thread.sleep(2000); // Wait for 2 seconds
                setChanged(); // Indicate that the model has changed
                notifyObservers(); // Notify observers (e.g., the view)
            } catch (InterruptedException e) {
                // Handle thread interruption
                System.err.println("Background thread interrupted: " + e.getMessage());
                Thread.currentThread().interrupt(); // Restore interrupted status
            }
        }
    }

   
    public synchronized Map<String, List<Integer>> getOrderState() throws OrderException {
        return theOrder.getOrderState();
    }

  
    public void handleFeedback(String feedback) {
        // Implement feedback handling logic here
        System.out.println("Feedback received: " + feedback);
    }
}
