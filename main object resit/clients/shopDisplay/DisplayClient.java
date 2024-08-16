package clients.shopDisplay;

import middle.MiddleFactory;
import middle.Names;
import middle.RemoteMiddleFactory;

import javax.swing.*;

/**
 * The standalone shop Display Client.
 * @version 2.0
 */
public class DisplayClient {
    public static void main(String[] args) {
        String stockURL = args.length < 1     // URL of stock RW
                        ? Names.STOCK_RW      // Default location
                        : args[0];            // Supplied location
        String orderURL = args.length < 2     // URL of order
                        ? Names.ORDER         // Default location
                        : args[1];            // Supplied location
        
        RemoteMiddleFactory mrf = new RemoteMiddleFactory();
        mrf.setStockRWInfo(stockURL);
        mrf.setOrderInfo(orderURL);
        displayGUI(mrf);                       // Create GUI
    }
    
    private static void displayGUI(MiddleFactory mf) {     
        JFrame window = new JFrame();
        window.setTitle("Pick Client MVC");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        DisplayModel model = new DisplayModel(mf);
        DisplayView view = new DisplayView(window, mf, 0, 0);
        DisplayController cont = new DisplayController(model, view);
        
        view.setController(cont);

        // Ensure the model is set in the view if required by your implementation
        // view.setModel(model); 

        model.addObserver(view); // Add observer to the model
        window.setVisible(true); // Display Screen 
    }
}
