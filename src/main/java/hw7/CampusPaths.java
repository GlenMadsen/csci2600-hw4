package hw7;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;
import hw4.Graph;
import hw5.MarvelPaths;
import hw7.CampusParser;
import hw7.CampusView;
import javafx.util.Pair;
import java.lang.Math;

/**
 * Abstraction Function:
 * 
 * Representation Invariant for every MarvelPath Graph graph:
 * 
 */

public class CampusPaths {
    private Graph<String, Double> graph; // id's to distances
    private Map<String, String> List_Of_Buildings; // Id to building name
    private HashMap<String, Pair<Double, Double> > ID_to_Coord; 
    /**
	 * @param N/A
	 * @returns N/A
	 * @throws N/A
	 * @requires N/A
	 * @effects constructs a new Graph variable that is empty.
	 * @effects constructs a new List_Of_Buildings variable that is empty.
	 * @effects constructs a new ID_to_Coord variable that is empty.
	 * @modifies graph variable is created
	 * @modifies List_Of_Buildings is created
	 * @modifies ID_to_Coord is created
	 */
    public CampusPaths()
	{
		this.graph = new Graph<String, Double>();
		this.List_Of_Buildings = new HashMap<String, String>();
		this.ID_to_Coord = new HashMap<String, Pair<Double, Double>>();
		//checkRep();
	}
    public void createNewGraph(String Nodes, String Edges) // Reads a file and then constructs a graph of type Graph
	{
		//checkRep();  Probably wouldn't really checkRep here since graph is empty.
		try // Encapsulated within a try due to calling readData from CampusParser
		{  // Constructs necessary variables for readData for both files.
			Set<Pair<String, String>> BuildingEdges = new HashSet<Pair<String, String> >();
			Map<String, ArrayList<String>> Buildings = new HashMap<String, ArrayList<String>>();
			// Calls the Parser.
			CampusParser.readData(Nodes, Edges, BuildingEdges, Buildings);
			// Creates variables and iterators for parsing the data into the 3 data structures.
			Iterator<String> Buildings_it = Buildings.keySet().iterator();
			Iterator<Pair<String, String>> BuildingEdges_it = BuildingEdges.iterator();
			Pair<String, String> Campus_Edge;
			String id;
			String id2;
			double sum;
			double coord_temp;
			this.graph = new Graph<String, Double>(); // Creates the new graph, removing any previous elements			
			while(Buildings_it.hasNext()) // Adds nodes to graph for every Marvel Character found
			{
				id = Buildings_it.next();
		    	this.graph.addNode(id);
		    	this.List_Of_Buildings.put(id, Buildings.get(id).get(0));
		    	Pair<Double, Double> temp = new Pair<Double, Double>(Double.parseDouble(Buildings.get(id).get(1)),Double.parseDouble(Buildings.get(id).get(2)));
		    	this.ID_to_Coord.put(id, temp);
			}
			while(BuildingEdges_it.hasNext()) // Iterates through every Building Edge
			{
				sum = 0.0; // Resets the sum
				Campus_Edge = BuildingEdges_it.next(); // Finds the next edge pair
				id = Campus_Edge.getKey(); // Gets the 2 id's
				id2 = Campus_Edge.getValue();
				coord_temp = Double.parseDouble(Buildings.get(id).get(1));
				sum += coord_temp*coord_temp; 
				coord_temp = Double.parseDouble(Buildings.get(id).get(2));
				sum += coord_temp*coord_temp;
				sum = Math.sqrt(sum);
				sum = Math.sqrt(Math.pow(Double.parseDouble(Buildings.get(id).get(1))-Double.parseDouble(Buildings.get(id2).get(1)), 2.0)+Math.pow(Double.parseDouble(Buildings.get(id).get(2))-Double.parseDouble(Buildings.get(id2).get(2)), 2.0));
				this.graph.addEdge(id, id2, sum);
				this.graph.addEdge(id2, id, sum);
			}
			//checkRep();
		}
		catch (IOException e) // Catches any IOExceptions caused that were not caught in Parser
		{
			//checkRep();
			//e.printStackTrace(); I would print stack trace but Submitty says console should be empty
		}
	}
    
    /**
   	 * @param A String starting node which is an ID, this is assured since I find the ID before I call the method
   	 *        so no requires is required.
   	 * @param A String ending node which is an ID, this is assured since I find the ID before I call the method
   	 *        so no requires is required.
	 * @returns an ArrayList of Strings which is empty if a path was not found or other cases where it fails to create a path
	 * 			If a path was found then it returns an ArrayList of Strings which will be a size which is a multiple of three.
	 * 			The index mod 3 = 0 index is the first id, the id the path is going from
	 * 			The index mod 3 = 1 index is the second id, the id the path is going to
	 * 			The index mod 3 = 2 index is the distance in pixel units between the 1st and 2nd id.
	 * 			This continues so index 0, 3, 6, 9 are id1's, 1, 4, 7 are id2's, and 2, 5, 8, are distances.
	 * 			Since this is specified view understands and can parse it as such while upholding the design model.
	 * @throws N/A
	 * @requires N/A
	 * @effects N/A
	 * @modifies N/A
	*/
    // This implementation is nigh on identical to hw6, but the sum is being done differently slightly
    // and the path building is totally different so this is a distinct method. The graph representation
    // (not the class itself) has also changed enough to warrant this.
    public ArrayList<String> findPath(String node1, String node2)
	{ // Via Dijkstra's, returns the path with the lowest weight
		//checkRep();
    	ArrayList<String> path = new ArrayList<String>();
		String start = node1; // Creates new variables for the start and dest nodes, begins to check
		String dest = node2; // if they are in the graph, also initializes the path variable
		// Priority Queue and Comparator for sorting based off weight
		PriorityQueue<Pair<String, Double> > H = new PriorityQueue<Pair<String, Double>>(new Comparator<Pair<String, Double>>() {  

			public int compare(Pair<String, Double> p1, Pair<String, Double> p2) {
				if (p1.getValue() < p2.getValue())
                    return -1;
                else if (p1.getValue() > p2.getValue())
                    return 1;
                else
                    return 0;
			}      
        });
		
		// Hashmap of distances followed by a HashMap of Paths
		HashMap<String, Double> D = new HashMap<String, Double>();
		HashMap<String, Pair<String, Double>> P = new HashMap<String, Pair<String, Double> >();
		Iterator<String> node_it = this.graph.listNodes(); // iterates through all the nodes
		String node;
		String child;
		
		while(node_it.hasNext()) // Setting distances to infinity initially
		{
			node = node_it.next();
			D.put(node, Double.POSITIVE_INFINITY);
		}
		D.put(start, 0.00); // Adds the initial node with distance 0
		node_it = this.graph.listNodes(); // Adds all the nodes to the priority Queue
		
		while(node_it.hasNext())
		{
			node = node_it.next();
			Pair<String, Double> tempPair = new Pair<String, Double>(node, D.get(node));
			H.add(tempPair);
			P.put(node, null);
		}		
		// Some more variables, no point in creating them until we know we have to look for a path
		String Reversed;
		Double sum;
		String coord;
		String tempPair;
		// Performs Dijkstra's in a similar way to the way we did in lab, it is more efficient than the
		// method described in the hw6 html doc, though I do have to simulate a heapDict using pairs.
		while(!H.isEmpty()) // Performs Dijkstra's until it is empty
		{ // Gets the minimum value and puts the node, double into a pair, then remove the value
			tempPair = H.remove().getKey();
			if(tempPair.equals(dest)) // If dest=temp then it breaks, we've reached our destination
			{
				break;
			}
			Iterator<String> edge_it = this.graph.listChildren(tempPair);
			
			while(edge_it.hasNext()) // Otherwise it gets the edgeweights for each child and tries to go along
			{ 
				child = edge_it.next(); // Has to do some parsing of output of listChildren
				Reversed = MarvelPaths.ReverseString(child);
				child = Reversed.substring(Reversed.indexOf("(")+1);
				child = MarvelPaths.ReverseString(child);
				coord = Reversed.substring(Reversed.indexOf(")")+1, Reversed.indexOf("("));
				coord = MarvelPaths.ReverseString(coord); // Gets the weight which is the pixel distance
				sum = Double.parseDouble(coord); // Distance formula already applied so we can just use the value
				 // Checks to see if it should add to the Priority Queue
				if(D.get(child) > (D.get(tempPair) + sum))
				{
					D.put(child, D.get(tempPair) + sum); // Updates the distance in D
					P.put(child, new Pair<String, Double>(tempPair, D.get(tempPair))); // Updates the path list
					H.add(new Pair<String, Double>(child, D.get(child))); // Adds the new pair to H
				}
			}
		}
// From here on I build the necessary data structure for the view to be able to parse the data into whatever is needed
//--------------------------------------------------------------------------------------------------------		
		if(P.get(dest) == null) // If there is no path initially then it outputs the empty array as specified
		{
			return path;
		}
		String nxt = P.get(dest).getKey(); // Begins to recreate the path off P data by getting dest.
		ArrayList<String> Path = new ArrayList<String>();
		Path.add(dest);
		
		while(nxt != null) // Keeps looping until it hits null
		{
			if(nxt != null)
			{
				Path.add(nxt); // Adds the node to the path
			}
			if(P.get(nxt) == null)
			{
				break;
			}
			nxt = P.get(nxt).getKey(); // Gets the next node
		}
		// Now that it gone back through P, it needs to put the data in a better format again, which
		// it does below and into an ArrayList of Strings as specified.
		
		for(int i = Path.size()-1; i > 0; i--) // Go through it again but making the path,
		// path is a 1D ArrayList but specified as being essentially segments of 3, the first being
		// the first id, 2nd being the 2nd id, and third being the distance between the two, then
		// the next three are the same but for id2 and id3, etc, etc.
        // To get the distance between two nodes I use the difference in D, which keeps a running sum, 
		//leaving the actual weight	
		{ 	
			path.add(Path.get(i));
			path.add(Path.get(i-1));
			path.add(String.valueOf(D.get(Path.get(i-1))-D.get(Path.get(i))));
		}
		//checkRep();
		return path;
	}
    /**
   	 * @param N/A
	 * @returns an ArrayList of Strings corresponding to all the Buildings in the graph
	 * @throws N/A
	 * @requires N/A
	 * @effects N/A
	 * @modifies N/A
	*/
    public ArrayList<String> Listing_Buildings()
    {
    	Iterator<String> buildings = this.List_Of_Buildings.keySet().iterator();
    	ArrayList<String> Buildings = new ArrayList<String>();
    	String Building;
    	while(buildings.hasNext()) // Iterates through all of the id's and if the corresponding building name
    	{ // is "" then its an intersection and skips it, otherwise adds it to the list of buildings.
    		Building = buildings.next();
    		if(this.List_Of_Buildings.get(Building).equals(""))
    		{
    			continue;
    		}
    		Buildings.add(this.List_Of_Buildings.get(Building));
    	}
    	return Buildings;
    }
    /**
   	 * @param String
	 * @returns If is an id of an intersection or is not found as a building name then it returns an empty string,
	 * 			Otherwise then it finds it as a building name in the List_Of_Buildings and returns the building name
	 * @throws N/A
	 * @requires N/A
	 * @effects N/A
	 * @modifies N/A
	*/
    public String get_Building(String name)
    {
    	if(is_ID(name)) // If its an id then it returns either the "" for an intersection or the
    					// building name mapped to it by that id.
    	{
    		if(this.List_Of_Buildings.get(name).equals(""))
    		{
    	    	return "";
    		}
    		return this.List_Of_Buildings.get(name);
    	}
    	if(is_Building(name)) // If it is a building, but not an id, then it must already be a building
    						  // Name so it returns that name.
    	{
    		return name;
    	}
    	return ""; // If is neither then it returns an empty string.
    }
    /**
   	 * @param a String
	 * @returns True if the name is an id corresponding to a building or if it is a building name itself
	 *          False otherwise, for instance a name no in the file or an intersection ID.
	 * @throws N/A
	 * @requires N/A
	 * @effects N/A
	 * @modifies N/A
	*/
    public boolean is_Building(String name)
    {
    	if(name.equals("")) // If the name is an empty string its the name of an intersection so no-go
    	{
    		return false;
    	}
    	if(this.List_Of_Buildings.containsKey(name)) // If its in the list of buildings as a key
    	{ // aka as an id, as long as it is not an ID for an intersection then it is a building
        	if(this.List_Of_Buildings.get(name).equals(""))
        	{
        		return false;
        	}
        	return true;
    	}// Otherwise it tries to find the building name by searching through the list, if unfound then false
    	Iterator<String> buildings = this.List_Of_Buildings.keySet().iterator();
    	String Building;
    	while(buildings.hasNext())
    	{
    		Building = buildings.next();
    		if(this.List_Of_Buildings.get(Building).equals(name))
    		{
    			return true;
    		}
    	}
    	return false;
    }
    /**
   	 * @param String
	 * @returns a String of the corresponding building if its a building name, itself if it is a name,
	 *          or FALSE if otherwise.
	 * @throws N/A
	 * @requires N/A
	 * @effects N/A
	 * @modifies N/A
	*/
    public String get_ID(String name) // Uses methods is_Building and is_ID to determine which it is
    { // and therefore return the corresponding output, otherwise returns FALSE which should never happen.
      // in our cases.
    	if(is_Building(name))
    	{
    		String id;
    		Iterator<String> it = this.List_Of_Buildings.keySet().iterator();
    		while(it.hasNext())
    		{
    			id = it.next();
    			if(this.List_Of_Buildings.get(id).equals(name))
    			{
    				return id;
    			}
    		}
    	}
    	if(is_ID(name))
    	{
    		return name;
    	}
		return "FALSE";
    }
    public boolean is_ID(String name)
    {
    	if(this.List_Of_Buildings.containsKey(name))
    	{
    		return true;
    	}
    	return false;
    }
    /**
   	 * @param id1 is a String
   	 * @param id2 is a String
	 * @returns an angle representing the angle between id1 and id2 using x and y coordinates.
	 * @throws N/A
	 * @requires Both Strings to be valid IDs since no checking if it is valid is done in the method.
	 * @effects N/A
	 * @modifies N/A
	*/
    public Double find_Angle(String id1, String id2)
    { // I use atan2 in math to find the angle
      // its a very long equation but really its just doing y1-y2/x1-x2 and it works
    	double angle = Math.atan2(Double.valueOf(ID_to_Coord.get(id1).getValue())-Double.valueOf(ID_to_Coord.get(id2).getValue()), Double.valueOf(ID_to_Coord.get(id1).getKey())-Double.valueOf(ID_to_Coord.get(id2).getKey()));
    	angle = Math.toDegrees(angle);
    	return angle;
    }
    public static void main(String args[]) // Main Method, also the controller
	{
		String nodes_file = "data/RPI_map_data_Nodes.csv";//Hardcode in the files since that was asked
		String edge_file = "data/RPI_map_data_Edges.csv";
		CampusPaths CampusMap = new CampusPaths(); // Makes a new object and creates the graph
		CampusMap.createNewGraph(nodes_file, edge_file);
		String temp1; // Creates some variables and begins scanning input
		String temp2;
		String line;
        Scanner sc = new Scanner(System.in);
		while (true)
		{ // Depending on the command it calls different methods in view
			line = sc.nextLine();
			if(line.equals("b"))
			{
				System.out.print(CampusView.B_Command(CampusMap));
			}
			if(line.equals("r"))
			{
				temp1 = sc.nextLine();
				temp2 = sc.nextLine();
				System.out.println(CampusView.R_Command(temp1, temp2, CampusMap));
			}
			if(line.equals("q"))// For instance on q it exits the program, done by just ending the loop
			{
				break; // This line is part of the view
			}
			if(line.equals("m"))
			{
				System.out.println(CampusView.M_Command());
			}
			if(!line.equals("b") && !line.equals("r") && !line.equals("q") && !line.equals("m"))
			{
				System.out.print(CampusView.Other_Command());
			}
		}
		sc.close();
	}
}
