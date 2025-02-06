
import java.util.Arrays;
import java.util.Scanner;

// The city consists of a grid of 9 X 9 City Blocks

// Streets are east-west (1st street to 9th street)
// Avenues are north-south (1st avenue to 9th avenue)

// Example 1 of Interpreting an address:  "34 4th Street"
// A valid address *always* has 3 parts.
// Part 1: Street/Avenue residence numbers are always 2 digits (e.g. 34).
// Part 2: Must be 'n'th or 1st or 2nd or 3rd (e.g. where n => 1...9)
// Part 3: Must be "Street" or "Avenue" (case insensitive)

// Use the first digit of the residence number (e.g. 3 of the number 34) to determine the avenue.
// For distance calculation you need to identify the the specific city block - in this example 
// it is city block (3, 4) (3rd avenue and 4th street)

// Example 2 of Interpreting an address:  "51 7th Avenue"
// Use the first digit of the residence number (i.e. 5 of the number 51) to determine street.
// For distance calculation you need to identify the the specific city block - 
// in this example it is city block (7, 5) (7th avenue and 5th street)
//
// Distance in city blocks between (3, 4) and (7, 5) is then == 5 city blocks
// i.e. (7 - 3) + (5 - 4) 

public class CityMap
{
  // Checks for string consisting of all digits
  // An easier solution would use String method matches()
  private static boolean allDigits(String s)
  {
    for (int i = 0; i < s.length(); i++)
      if (!Character.isDigit(s.charAt(i)))
        return false;
    return  true;
  }

  // Get all parts of address string
  // An easier solution would use String method split()
  // Other solutions are possible - you may replace this code if you wish
  private static String[] getParts(String address)
  {
    String parts[] = new String[3];
    
    if (address == null || address.length() == 0)
    {
      parts = new String[0];
      return parts;
    }
    int numParts = 0;
    Scanner sc = new Scanner(address);
    while (sc.hasNext())
    {
      if (numParts >= 3)
        parts = Arrays.copyOf(parts, parts.length+1);

      parts[numParts] = sc.next();
      numParts++;
    }
    if (numParts == 1)
      parts = Arrays.copyOf(parts, 1);
    else if (numParts == 2)
      parts = Arrays.copyOf(parts, 2);
    return parts;
  }

  // Checks for a valid address
  public static boolean validAddress(String address)
  {
    // Fill in the code
    // Make use of the helper methods above if you wish
    // There are quite a few error conditions to check for 
    // e.g. number of parts != 3
    // Check if the address is null or empty
    if (address == null || address.isEmpty()){
        return false;
    }
    // Split the address into parts
    String[] parts = getParts(address);

    // Check if the address has exactly 3 parts
    if (parts.length != 3){
        return false;
    }
    // Check if the residence number consists of digits
    if (!allDigits(parts[0])){
      return false;
    }
    // Check if the street/avenue part is in the correct format
    if (!parts[1].matches("\\d(?:st|nd|rd|th)")){
      return false;
    }
    // Check if the street/avenue type is either "Street" or "Avenue" (case insensitive)
    if (!parts[2].equalsIgnoreCase("Street") && !parts[2].equalsIgnoreCase("Avenue")){
      return false;
    }
    return true;
  } 

  // Computes the city block coordinates from an address string
  // returns an int array of size 2. e.g. [3, 4] 
  // where 3 is the avenue and 4 the street
  // See comments at the top for a more detailed explanation
  public static int[] getCityBlock(String address)
  {
    int[] block = {-1, -1};
    // Fill in the code

    validAddress(address);
    String[] parts = getParts(address);

    // Get avenue and street from the address
    int avenue = 0;
    int street = 0;
    if (parts[2].equalsIgnoreCase("Street")) {
      avenue = Character.getNumericValue(parts[0].charAt(0));
      street = Integer.parseInt(parts[1].substring(0, parts[1].length() - 2));
    } 
  else if (parts[2].equalsIgnoreCase("Avenue")) {
      avenue = Integer.parseInt(parts[1].substring(0, parts[1].length() - 2));
      street = Character.getNumericValue(parts[0].charAt(0));
    } 

    block[0] = avenue;
    block[1] = street;
    return block;
  }
  
  // Calculates the distance in city blocks between the 'from' address and 'to' address
  // Hint: be careful not to generate negative distances
  
  // This skeleton version generates a random distance
  // If you do not want to attempt this method, you may use this default code
  public static int getDistance(String from, String to)
  {
    // Fill in the code or use this default code below. If you use
    // the default code then you are not eligible for any marks for this part
    validAddress(from);
    validAddress(to);

    int[] fromBlock = getCityBlock(from);
    int[] toBlock = getCityBlock(to);

    int distance = Math.abs(toBlock[0] - fromBlock[0]) + Math.abs(toBlock[1] - fromBlock[1]);

    return distance;
  }
}
