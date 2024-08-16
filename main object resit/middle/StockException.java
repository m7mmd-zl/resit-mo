package middle;

/**
 * Exception thrown if there is an error in accessing the stock list.
 * 
 * @version 2.0
 */
public class StockException extends Exception
{
  private static final long serialVersionUID = 1L;

  /**
   * Constructs a new StockException with the specified detail message.
   * 
   * @param message the detail message
   */
  public StockException(String message)
  {
    super(message);
  }

  /**
   * Constructs a new StockException with the specified detail message and cause.
   * 
   * @param message the detail message
   * @param cause the cause of the exception
   */
  public StockException(String message, Throwable cause)
  {
    super(message, cause);
  }
}
