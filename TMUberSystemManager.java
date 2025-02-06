

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/*
 * 
 * This class contains the main logic of the system.
 * 
 *  It keeps track of all users, drivers and service requests (RIDE or DELIVERY)
 * 
 */
public class TMUberSystemManager
{
  private ArrayList<User>   users;
  private ArrayList<Driver> drivers;

  private ArrayList<TMUberService> serviceRequests; 

  public double totalRevenue; // Total revenues accumulated via rides and deliveries
  
  // Rates per city block
  private static final double DELIVERYRATE = 1.2;
  private static final double RIDERATE = 1.5;
  // Portion of a ride/delivery cost paid to the driver
  private static final double PAYRATE = 0.1;

  //These variables are used to generate user account and driver ids
  int userAccountId = 900;
  int driverId = 700;

  public TMUberSystemManager()
  {
    users   = new ArrayList<User>();
    drivers = new ArrayList<Driver>();
    serviceRequests = new ArrayList<TMUberService>(); 
    
    TMUberRegistered.loadPreregisteredUsers(users);
    TMUberRegistered.loadPreregisteredDrivers(drivers);
    
    totalRevenue = 0;
  }

  // General string variable used to store an error message when something is invalid 
  // (e.g. user does not exist, invalid address etc.)  
  // The methods below will set this errMsg string and then return false
  String errMsg = null;

  public String getErrorMessage()
  {
    return errMsg;
  }
  
  // Given user account id, find user in list of users
  // Return null if not found
  public User getUser(String accountId)
  {
    // Fill in the code
    for (User user : users) {
      if (user.getAccountId().equals(accountId)) {
          return user;
      }
  }
    return null;
  }
  
  // Check for duplicate user
  private boolean userExists(User user)
  {
    // Fill in the code
    for (User user2 : users) {
      if (user2.equals(user)) {
          return true;
      }
  }
    return false;
  }
  
 // Check for duplicate driver
 private boolean driverExists(Driver driver)
 {
   // Fill in the code
   for (Driver driver2 : drivers) {
    if (driver2.equals(driver)) {
        return true;
    }
  }
   return false;
 }
  
  // Given a user, check if user ride/delivery request already exists in service requests
  private boolean existingRequest(TMUberService req)
  {
    // Fill in the code
    for (TMUberService request : serviceRequests) {
      if (request.equals(req)) {
          return true;
      }
    }
    return false;
  }

  // Calculate the cost of a ride or of a delivery based on distance 
  private double getDeliveryCost(int distance)
  {
    return distance * DELIVERYRATE;
  }

  private double getRideCost(int distance)
  {
    return distance * RIDERATE;
  }

  // Go through all drivers and see if one is available
  // Choose the first available driver
  // Return null if no available driver
  private Driver getAvailableDriver()
  {
    // Fill in the code
    for (Driver driver : drivers) {
      if (driver.getStatus() == Driver.Status.AVAILABLE) {
          return driver;
      }
    }
    return null;
  }

  // Print Information (printInfo()) about all registered users in the system
  public void listAllUsers()
  {
    System.out.println();
    
    for (int i = 0; i < users.size(); i++)
    {
      int index = i + 1;
      System.out.printf("%-2s. ", index);
      users.get(i).printInfo();
      System.out.println(); 
    }
  }

  // Print Information (printInfo()) about all registered drivers in the system
  public void listAllDrivers()
  {
    // Fill in the code
    System.out.println();
    
    for (int i = 0; i < drivers.size(); i++)
    {
      int index = i + 1;
      System.out.printf("%-2s. ", index);
      drivers.get(i).printInfo();
      System.out.println(); 
    }
  }

  // Print Information (printInfo()) about all current service requests
  public void listAllServiceRequests()
  {
    // Fill in the code
    System.out.println();
    
    for (int i = 0; i < serviceRequests.size(); i++)
    {
      int index = i + 1;
      System.out.printf("%-2s. ", index);
      serviceRequests.get(i).printInfo();
      System.out.println(); 
    }
  }

  // Add a new user to the system
  public boolean registerNewUser(String id, String name, String address, double wallet)
  {
    // Fill in the code. Before creating a new user, check paramters for validity
    // See the assignment document for list of possible erros that might apply
    // Write the code like (for example):
    // if (address is *not* valid)
    // {
    //    set errMsg string variable to "Invalid Address "
    //    return false
    // }
    // If all parameter checks pass then create and add new user to array list users
    // Make sure you check if this user doesn't already exist!
    if (id == null || id.isEmpty()) {
      errMsg = "Invalid ID";
      return false;
  }
    if (name == null || name.isEmpty()) {
      errMsg = "Invalid User Name";
      return false;
    }
    if (address == null || address.isEmpty()) {
      errMsg = "Invalid User Address";
      return false;
    }
    if (wallet < 0) {
      errMsg = "Invalid Money in Wallet";
      return false;
    }
    User newUser = new User(id, name, address, wallet);
    if (userExists(newUser)) {
      errMsg = "User Already Exists in System";
      return false;
    }
    users.add(newUser);
    return true;
  }

  // Add a new driver to the system
  public boolean registerNewDriver(String id, String name, String carModel, String carLicencePlate)
  {
    // Fill in the code - see the assignment document for error conditions
    // that might apply. See comments above in registerNewUser
    if (id == null || id.isEmpty()) {
      errMsg = "Invalid ID";
      return false;
  }
    if (name == null || name.isEmpty()) {
      errMsg = "Invalid Driver Name";
      return false;
  }
  if (carModel == null || carModel.isEmpty()) {
      errMsg = "Invalid Car Model";
      return false;
  }
  if (carLicencePlate == null || carLicencePlate.isEmpty()) {
      errMsg = "Invalid Car Licence Plate";
      return false;
  }

  Driver newDriver = new Driver(id, name, carModel, carLicencePlate);

  if (driverExists(newDriver)) {
      errMsg = "Driver Already Exists in System";
      return false;
  }

  drivers.add(newDriver);
    return true;
  }

  // Request a ride. User wallet will be reduced when drop off happens
  public boolean requestRide(String accountId, String from, String to)
  {
    // Check for valid parameters
    if (accountId == null || from == null || to == null) {
      errMsg = "Invalid Request #";
      return false;
  }
    if (!CityMap.validAddress(from) || !CityMap.validAddress(to)) {
      errMsg = "Invalid Address";
      return false;
  }
	// Use the account id to find the user object in the list of users
    User user = getUser(accountId);
    if (user == null) {
        errMsg = "User Account Not Found";
        return false;
    }
    // Get the distance for this ride
    // Note: distance must be > 1 city block!
    int distance = CityMap.getDistance(from, to);
    if (distance <= 1) {
        errMsg = "Insufficient Travel Distance";
        return false;
    }
    // Find an available driver
    Driver driver = getAvailableDriver();
    if (driver == null) {
        errMsg = "No Drivers Available";
        return false;
    }
    // Create the TMUberRide object
    TMUberRide ride = new TMUberRide(driver, from, to, user, distance, getRideCost(distance));
    // Check if existing ride request for this user - only one ride request per user at a time!
    if (existingRequest(ride)) {
      errMsg = "User Already Has Ride Request";
      return false;
    }
    // Change driver status
    driver.setStatus(Driver.Status.DRIVING);
    // Add the ride request to the list of requests
    serviceRequests.add(ride);
    // Increment the number of rides for this user
    user.addRide();
    return true;
  }

  // Request a food delivery. User wallet will be reduced when drop off happens
  public boolean requestDelivery(String accountId, String from, String to, String restaurant, String foodOrderId)
  {
    // See the comments above and use them as a guide
     // Check for valid parameters
     if (accountId == null || from == null || to == null || restaurant == null || foodOrderId == null) {
      errMsg = "Invalid Request #";
      return false;
  }
  if (!CityMap.validAddress(from) || !CityMap.validAddress(to)) {
    errMsg = "Invalid Address";
    return false;
}

  // Use the account id to find the user object in the list of users
  User user = getUser(accountId);
  if (user == null) {
      errMsg = "User Account Not Found";
      return false;
  }

  
  // Calculate the distance for this delivery
  int distance = CityMap.getDistance(from, to);
  if (distance <= 1) {
      errMsg = "Insufficient Travel Distance";
      return false;
  }

  // Find an available driver
  Driver driver = getAvailableDriver();
  if (driver == null) {
      errMsg = "No Drivers Available";
      return false;
  }

  // Create the TMUberDelivery object
  TMUberDelivery delivery = new TMUberDelivery(driver, from, to, user, distance, getDeliveryCost(distance), restaurant, foodOrderId);

  // Check if existing delivery request for this user at the same restaurant with the same food order id
  if (existingRequest(delivery)) {
      errMsg = "User Already Has Delivery Request at Restaurant with this Food Order";
      return false;
  }

  // Change driver status
  driver.setStatus(Driver.Status.DRIVING);

  // Add the delivery request to the list of requests
  serviceRequests.add(delivery);

  // Increment the number of deliveries for this user
  user.addDelivery();
    return true;
  }


  // Cancel an existing service request. 
  // parameter int request is the index in the serviceRequests array list
  public boolean cancelServiceRequest(int request)
  {
    // Check if valid request #
    if (request < 0 || request >= serviceRequests.size()) {
      errMsg = "Invalid Request #";
      return false;
  }
    // Remove request from list
    TMUberService cancelledRequest = serviceRequests.remove(request);
    // Also decrement number of rides or number of deliveries for this user
    // since this ride/delivery wasn't completed
    if (cancelledRequest instanceof TMUberRide) {
      ((TMUberRide) cancelledRequest).getUser().subRide();
  } else if (cancelledRequest instanceof TMUberDelivery) {
      ((TMUberDelivery) cancelledRequest).getUser().subDelivery();
  }

    return true;
  }
  
  // Drop off a ride or a delivery. This completes a service.
  // parameter request is the index in the serviceRequests array list
  public boolean dropOff(int request)
  {
    // See above method for guidance
    // Check if valid request #
    if (request < 0 || request >= serviceRequests.size()) {
      errMsg = "Invalid Request #";
      return false;
  }
    // Get the service request
    TMUberService completedService = serviceRequests.get(request);

    // Get the cost for the service and add to total revenues
    double cost = completedService.getCost();
    totalRevenue += cost; 
    // Pay the driver
    Driver driver = completedService.getDriver();
    double fee = cost * PAYRATE;
    driver.pay(fee);
    // Deduct driver fee from total revenues
    totalRevenue -= fee;
    // Change driver status
    driver.setStatus(Driver.Status.AVAILABLE);
    // Deduct cost of service from user
    completedService.getUser().payForService(cost);

    // Remove the completed service request from the list
    serviceRequests.remove(request);
    return true;
  }


  
  
  public void sortByUserName()
  {
    Collections.sort(users, new NameComparator());// Sort users by name
    listAllUsers();// Then list all users
  }

  // Helper class for method sortByUserName
  private class NameComparator implements Comparator<User> {
    @Override
    public int compare(User user1, User user2) {
        return user1.getName().compareTo(user2.getName());
    }
}

  // Sort users by number amount in wallet
  // Then ist all users
  public void sortByWallet()
  {
    Collections.sort(users, new UserWalletComparator());
    listAllUsers();
  }
  // Helper class for use by sortByWallet
  private class UserWalletComparator implements Comparator<User> {
    @Override
    public int compare(User user1, User user2) {
        return Double.compare(user2.getWallet(), user1.getWallet());
    }
  
  }

  // Sort trips (rides or deliveries) by distance
  // Then list all current service requests
  public void sortByDistance()
  {
    Collections.sort(serviceRequests, new DistanceComparator());
    listAllServiceRequests();
}

// Helper class for sorting service requests by distance
private class DistanceComparator implements Comparator<TMUberService> {
    @Override
    public int compare(TMUberService service1, TMUberService service2) {
        return Integer.compare(service1.getDistance(), service2.getDistance());
    }
  }

}
