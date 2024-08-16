package middle;

import dbAccess.StockR;
import dbAccess.StockRW;
import orders.Order;

/**
 * Provide access to middle tier components.
 * Now only one instance of each middle tier object is created
 * 
 * @version 2.1
 */
public class LocalMiddleFactory implements MiddleFactory
{
  private static StockR  aStockR  = null;
  private static StockRW aStockRW = null;
  private static Order   aOrder   = null;

  /**
   * Return an object to access the database for read only access.
   * All users share this same object.
   */
  public StockReader makeStockReader() throws StockException
  {
    if (aStockR == null) {
      try {
        aStockR = new StockR();
      } catch (Exception e) {
        throw new StockException("Failed to create StockR instance: " + e.getMessage(), e);
      }
    }
    return aStockR;
  }

  /**
   * Return an object to access the database for read/write access.
   * All users share this same object.
   */
  public StockReadWriter makeStockReadWriter() throws StockException
  {
    if (aStockRW == null) {
      try {
        aStockRW = new StockRW();
      } catch (Exception e) {
        throw new StockException("Failed to create StockRW instance: " + e.getMessage(), e);
      }
    }
    return aStockRW;
  }

  /**
   * Return an object to access the order processing system.
   * All users share this same object.
   */
  public OrderProcessing makeOrderProcessing() throws OrderException
  {
    if (aOrder == null) {
      try {
        aOrder = new Order();
      } catch (Exception e) {
        throw new OrderException("Failed to create Order instance: " + e.getMessage(), e);
      }
    }
    return aOrder;
  }
}
