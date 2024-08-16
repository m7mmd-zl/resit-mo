package catalogue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Formatter;
import java.util.Locale;

public class Basket extends ArrayList<Product> implements Serializable {
    private static final long serialVersionUID = 1;
    private int theOrderNum = 0; // Order number
    private double discount = 0.0; // Discount percentage, default is 0

    public Basket() {
        theOrderNum = 0;
    }

    public void setOrderNum(int anOrderNum) {
        theOrderNum = anOrderNum;
    }

    public int getOrderNum() {
        return theOrderNum;
    }

    @Override
    public boolean add(Product pr) {
        return super.add(pr);
    }

    public boolean updateProductQuantity(String productNum, int newQuantity) {
        for (Product product : this) {
            if (product.getProductNum().equals(productNum)) {
                product.setQuantity(newQuantity);
                return true;
            }
        }
        return false;
    }

    public void applyDiscount(double discountRate) {
        this.discount = discountRate;
    }

    public void applyLoyaltyCoupon() {
        for (Product product : this) {
            int quantity = product.getQuantity();
            if (quantity > 1) {
                int freeItems = quantity / 2; // Calculate the number of free items
                product.setQuantity(quantity + freeItems); // Apply the buy one get one free offer
            }
        }
    }

    public String getDetails() {
        Locale uk = Locale.UK;
        StringBuilder sb = new StringBuilder(256);
        Formatter fr = new Formatter(sb, uk);
        String csign = (Currency.getInstance(uk)).getSymbol();
        double total = 0.00;

        if (theOrderNum != 0) {
            fr.format("Order number: %03d\n", theOrderNum);
        }

        if (this.size() > 0) {
            for (Product pr : this) {
                int number = pr.getQuantity();
                fr.format("%-7s", pr.getProductNum());
                fr.format("%-14.14s ", pr.getDescription());
                fr.format("(%3d) ", number);
                fr.format("%s%7.2f", csign, pr.getPrice() * number);
                fr.format("\n");
                total += pr.getPrice() * number;
            }

            if (discount > 0) {
                fr.format("----------------------------\n");
                fr.format("Discount Applied            ");
                fr.format("%s-%7.2f\n", csign, total * discount);
                total = total * (1 - discount);
            }

            fr.format("----------------------------\n");
            fr.format("Total                       ");
            fr.format("%s%7.2f\n", csign, total);
            fr.close();
        }
        return sb.toString();
    }
}
