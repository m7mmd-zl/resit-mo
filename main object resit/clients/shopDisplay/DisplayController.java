package clients.shopDisplay;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The controller for handling interactions between the view and model.
 */
public class DisplayController {
    private DisplayModel model;
    private DisplayView view;

    /**
     * Constructor
     * @param model The model
     * @param view  The view from which the interaction came
     */
    public DisplayController(DisplayModel model, DisplayView view) {
        this.model = model;
        this.view = view;
    }

    /**
     * Method to handle the feedback after an order is collected
     * @param feedback The feedback message to display
     */
    public void handleOrderCompletion(String feedback) {
        model.handleFeedback(feedback); // Optionally handle feedback in the model
        view.setFeedbackMessage("Thank you for shopping! Is there any feedback? " + feedback);
    }
}
