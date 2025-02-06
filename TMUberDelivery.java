
/*
 * 
 * This class simulates a food delivery service for a simple Uber app
 * 
 * A TMUberDelivery is-a TMUberService with some extra functionality
 */
public class TMUberDelivery extends TMUberService
{
  public static final String TYPENAME = "DELIVERY";
 
  private String restaurant; 
  private String foodOrderId;
   
   // Constructor to initialize all inherited and new instance variables 
  public TMUberDelivery(Driver driver, String from, String to, User user, int distance, double cost,
                        String restaurant, String order)
  {
    // Fill in the code - make use of the super method
    super(driver, from, to, user, distance, cost);
    this.restaurant = restaurant;
    this.foodOrderId = order;
    
  }
 
  
  public String getServiceType()
  {
    return TYPENAME;
  }
  public String getRestaurant()
  {
    return restaurant;
  }
  public void setRestaurant(String restaurant)
  {
    this.restaurant = restaurant;
  }
  public String getFoodOrderId()
  {
    return foodOrderId;
  }
  public void setFoodOrderId(String foodOrderId)
  {
    this.foodOrderId = foodOrderId;
  }
  /*
   * Two Delivery Requests are equal if they are equal in terms of TMUberServiceRequest
   * and the restaurant and food order id are the same  
   */
  public boolean equals(Object other)
  {
    // First check to see if other is a Delivery type
    if (!(other instanceof TMUberDelivery)){
        return false;
    }
    // Cast other to a TMUService reference and check type
    TMUberService otherService = (TMUberService) other;
    // If not a delivery, return false
    if (!TYPENAME.equals(otherService.getServiceType())){
        return false;
    }
    // If this and other are deliveries, check to see if they are equal
    return super.equals(otherService) && restaurant.equals(((TMUberDelivery) other).restaurant) && foodOrderId.equals(((TMUberDelivery) other).foodOrderId);
  }
  /*
   * Print Information about a Delivery Request
   */
  public void printInfo()
  {
    // Fill in the code
    // Use inheritance to first print info about a basic service request
    super.printInfo();
    // Then print specific subclass info
    System.out.printf("\nRestaurant: %-9s Food Order #: %-3s", restaurant, foodOrderId); 
  }
}
