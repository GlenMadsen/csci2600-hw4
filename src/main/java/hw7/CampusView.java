package hw7;

import java.util.ArrayList;

// This class does not represent an ADT
public class CampusView 
{
	/**
   	 * @param CampusModel model object campusMap
	 * @returns String representing a list of all the buildings in the form of Building Name, id in
	 * 			lexicographic order.
	 * @throws N/A
	 * @requires N/A
	 * @effects N/A
	 * @modifies N/A
	*/
	public static String B_Command(CampusModel campusMap)
	{ // B_Command calls CampusMap method Listing_Buildings which by specification returns an
	  // ArrayList of all the Building Names, not including the empty strings of inersections, and an
	  // empty ArrayList if there are no Buildings.
		ArrayList<String> Buildings = new ArrayList<String>(campusMap.Listing_Buildings());
		Buildings.sort(null); // Sorts the ArrayList
		String next_B = "";
		for(int i = 0; i < Buildings.size(); i++) // Constructs the string which, as the view, it has
		{ // knowledge of how it should be constructed, and even though it does not know the structure
		  // of CampusMap's objects, it knows Listing_Buildings returns an ArrayList of buildings which
		  // is all it needs
			next_B = next_B.concat(Buildings.get(i));
			next_B = next_B.concat(",");
			next_B = next_B.concat(campusMap.get_ID(Buildings.get(i)));
			next_B = next_B.concat("\n");
		}
		return next_B;
	}
	/**
   	 * @param CampusModel model object CampusMap
   	 * @param String start which is the starting building name or id, must correspond to a building not an intersection
   	 * @param String end which is the starting building name or id, must correspond to a building not an intersection
	 * @returns: A string which either states that the node(s) were not found in the Graph graph,
	 * 		     A string which states that no path between the points were found in the Graph
	 *           A string which implies that the start node is the same as the destination
	 *           A string which describes directions over every step of the path, including the 
	 *             intermediary nodes and the direction you must walk at each step. Then it outputs
	 *             the total pixel distance walked.
	 * @throws N/A
	 * @requires N/A
	 * @effects N/A
	 * @modifies N/A
	*/
	public static String R_Command(String start, String end, CampusModel campusMap) 
	{  // Starts outputting the correct ouput
		String path = "";
		double path_L = 0.00;
		path = path.concat("First building id/name, followed by Enter: ");
		path = path.concat("Second building id/name, followed by Enter: ");
		// Checks if either start or end is not a building, if it isn't then it has to return the 
		// the specified output, of which there are a few cases
		// Is Building just returns a boolean of whether not the id maps to a non-empty building name
		// or if it is in the list of building names, so it is a 'get' method for the view from the model
		if(!campusMap.is_Building(start) || !campusMap.is_Building(end))
		{
			if(start.equals(end))
			{
				return path.concat("Unknown building: [" + start + "]");
			}
			if(!campusMap.is_Building(start) && !campusMap.is_Building(end))
			{
    	    	path = path.concat("Unknown building: [" + start + "]" + "\n");
    	    	path = path.concat("Unknown building: [" + end + "]");
    	    	return path;
			}
			if(!campusMap.is_Building(start))
			{
    	    	path = path.concat("Unknown building: [" + start + "]");
			}
			if(!campusMap.is_Building(end))
			{
    	    	path = path.concat("Unknown building: [" + end + "]");
			}
			return path; // Since they aren't building names it stops the path construction and returns
		} // Get building takes building or id name and returns the corresponding building name, a 'get' method.
		String temp1 = campusMap.get_Building(start); // Some variable which won't be needed unless
		String temp2 = campusMap.get_Building(end);   // they are actually buildings, so initialization is delayed.
		if(temp1.equals(temp2)) // Returns specified output if they are equal
		{
			path = path.concat("Path from " + temp1 + " to " + temp2 + ":\n");
			path = path.concat("Total distance: 0.000 pixel units.");
			return path;
		}
		String id1 = campusMap.get_ID(start); // Gets the id's using a 'getter' method from the model
		String id2 = campusMap.get_ID(end);
		String angle;
		ArrayList<String> P = new ArrayList<String>(campusMap.findPath(id1, id2)); // Uses the model's
		// findPath which returns an array list of (id1, id2, Distance between the two, id2, id3, ...)
		// and since it just returns the raw data, of which there is enough to construct a path,
		// it upholds the MVC design archetype.
		if(P.isEmpty()) // Checks if the path is empty, which means no path, and returns specified output
		{ // This is specified in the findPath that an empty ArrayList represents no path.
			path = path.concat("There is no path from " +  temp1 + " to " + temp2 + ".");
			return path;
		}
		path = path.concat("Path from " + temp1 + " to " + temp2 + ":\n"); 
		// Starts building the path based on specifications, iterates through P in segments of three
	    // Since that is how the data is stored and concatenates the path correctly depending on if
		// the id's in P are buildings or intersections. Also finds the direction which is done by
		// calling a model method to get the angle since it needs values found in the model, and then
		// with the angle parsing that double into a direction in the view.
		for(int i = 0; i < P.size(); i += 3)
		{
			if(campusMap.is_Building(P.get(i+1)))
			{
				temp1 = "(".concat(campusMap.get_Building(P.get(i+1))).concat(")");
			}
			if(!campusMap.is_Building(P.get(i+1)))
			{
				temp1 = "(Intersection ".concat(campusMap.get_ID(P.get(i+1))).concat(")");
			}
			angle = findDirection(campusMap.find_Angle(P.get(i), P.get(i+1)));
			path = path.concat("\tWalk " + angle + " to " +  temp1 + "\n");
			path_L += Double.valueOf(P.get(i+2));
		} // After the path is built and total distance found it outputs such.
		path = path.concat(String.format("Total distance: %.3f pixel units.", path_L));
		return path;
	}
	/**
   	 * @param N/A
	 * @returns String representing all possible and valid commands for the controller.
	 * @throws N/A
	 * @requires N/A
	 * @effects N/A
	 * @modifies N/A
	*/
	public static String M_Command() // Lists all the commands
	{
		return "The Commands List: 'b', 'r', 'q', 'm'.";

	}
	/**
   	 * @param N/A
	 * @returns String stating command was unknown to the controller.
	 * @throws N/A
	 * @requires N/A
	 * @effects N/A
	 * @modifies N/A
	*/
	public static String Other_Command() // Says its an unknown option
	{
		return "Unknown option\n";
	}
	/**
   	 * @param an Double called angle with that can be used to find the direction, should be 
   	 *        between 0 and 360 but handles other values
	 * @returns String stating the parsed direction or unknown if it is unable to (shouldn't happen).
	 * @throws N/A
	 * @requires N/A
	 * @effects N/A
	 * @modifies N/A
	*/
	public static String findDirection(Double angle)
	{   // Just a bunch of if statements with values for the angle and their corresponding direction
		// They look a bit strange because I shift the angle if negative to positive below.
    	if(angle < 0)
    	{
    		angle += 360;
    	}
		if(angle >= 67.5 && angle < 112.5)
    	{
    		return "North";
    	}
    	if(angle >= 22.5 && angle < 67.5)
    	{
    		return "NorthWest";
    	}
    	if(angle >= 337.5 || angle < 22.5)
    	{
    		return "West";
    	}
    	if(angle >= 292.5 && angle < 337.5)
    	{
    		return "SouthWest";
    	}
    	if(angle >= 247.5 && angle < 292.5)
    	{
    		return "South";
    	}
    	if( angle >= 202.5 && angle < 247.5)
    	{
    		return "SouthEast";
    	}
    	if(angle >= 157.5 && angle < 202.5)
    	{
    		return "East";
    	}
    	if(angle >= 112.5 && angle < 157.5)
    	{
    		return "NorthEast";
    	}
    	return "Unknown Angle";
	}
}