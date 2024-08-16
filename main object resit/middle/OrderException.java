package middle;

/**
 * Exception thrown if there is an error in the order processing system.
 * 
 * @version 2.0
 */
public class OrderException extends Exception
{
  private static final long serialVersionUID = 2L;

  /**
   * Constructs a new OrderException with the specified detail message.
   * 
   * @param message the detail message
   */
  public OrderException(String message)
  {
    super(message);
  }

  /**
   * Constructs a new OrderException with the specified detail message and cause.
   * 
   * @param message the detail message
   * @param cause the cause of the exception
   */
  public OrderException(String message, Throwable cause)
  {
    super(message, cause);
  }
}
