package hw7;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import hw4.Graph;
import hw5.MarvelPaths;
import hw7.CampusParser;
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
	 * @effects constructs a new Graph variable that is empty
	 * @modifies graph variable is created
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
		try // Encapsulated within a try due to calling readData from MarvelParser 
		{  // Constructs necessary variables for readData
			Set<Pair<String, String>> BuildingEdges = new HashSet<Pair<String, String> >();
			Map<String, ArrayList<String>> Buildings = new HashMap<String, ArrayList<String>>();
			CampusParser.readData(Nodes, Edges, BuildingEdges, Buildings);
			// Creates variables and iterators for parsing the data into character, book, and pairs
			Iterator<String> Buildings_it = Buildings.keySet().iterator();
			Iterator<Pair<String, String>> BuildingEdges_it = BuildingEdges.iterator();
			Pair<String, String> Campus_Edge;
			String id;
			String id2;
			double sum;
			this.graph = new Graph<String, Double>(); // Creates the new graph, removing any previous elements			
			while(Buildings_it.hasNext()) // Adds nodes to graph for every Marvel Character found
			{
				id = Buildings_it.next();
		    	this.graph.addNode(id);
		    	this.List_Of_Buildings.put(id, Buildings.get(id).get(0));
		    	Pair<Double, Double> temp = new Pair<Double, Double>(Double.parseDouble(Buildings.get(id).get(1)),Double.parseDouble(Buildings.get(id).get(2)));
		    	this.ID_to_Coord.put(id, temp);
			}
			while(BuildingEdges_it.hasNext()) // Iterates through every comic book
			{
				sum = 0.0;
				Campus_Edge = BuildingEdges_it.next();
				id = Campus_Edge.getKey();
				id2 = Campus_Edge.getValue();
				//System.out.println(id);
				//System.out.println(Buildings.get(id));
				Double temp = Double.parseDouble(Buildings.get(id).get(1));
				sum += temp*temp; 
				temp = Double.parseDouble(Buildings.get(id).get(2));
				sum += temp*temp;
				sum = Math.sqrt(sum);
				sum = Math.sqrt(Math.pow(Double.parseDouble(Buildings.get(id).get(1))-Double.parseDouble(Buildings.get(id2).get(1)), 2.0)+Math.pow(Double.parseDouble(Buildings.get(id).get(2))-Double.parseDouble(Buildings.get(id2).get(2)), 2.0));
				this.graph.addEdge(id, id2, sum);
				this.graph.addEdge(id2, id, sum);
			}
//			Iterator<String> iter = this.graph.listNodes();
//			int count = 0;
//			while(iter.hasNext())
//			{
//				System.out.println(iter.next());
//				count++;
//			}
//			if(count == 0)
//			{
//			while(true)
//			{
//				 count+=1;
//			}
//			}
			//checkRep();
		}
		catch (IOException e) // Catches any IOExceptions caused that were not caught in Parser
		{
			//checkRep();
			//e.printStackTrace(); I would print stack trace but Submitty says console should be empty
		}
	}
    
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
		// method described in the html doc, though I do have to simulate a heapDict using pairs.
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
				child = edge_it.next(); // Has to do some parsing of output
				Reversed = MarvelPaths.ReverseString(child);
				child = Reversed.substring(Reversed.indexOf("(")+1);
				child = MarvelPaths.ReverseString(child);
				coord = Reversed.substring(Reversed.indexOf(")")+1, Reversed.indexOf("("));
				coord = MarvelPaths.ReverseString(coord); // Gets the weight
				//sum = Double.parseDouble(coord)*Double.parseDouble(coord);
				sum = Double.parseDouble(coord);
				 // Checks to see if it should add to the Priority Queue
				
				if(D.get(child) > (D.get(tempPair) + sum))
				{
					D.put(child, D.get(tempPair) + sum); // Updates the distance in D
					P.put(child, new Pair<String, Double>(tempPair, D.get(tempPair))); // Updates the path list
					H.add(new Pair<String, Double>(child, D.get(child))); // Adds the new pair to H
				}
			}
		}
// From here on I build the path since Dijkstra's is done and the shortest path may or may not exist
//--------------------------------------------------------------------------------------------------------		
//		path = "path from ".concat(start).concat(" to ").concat(dest).concat(":\n"); // Creates path start
		if(P.get(dest) == null) // If there is no path initially then outputs null
		{
			//checkRep();
//			return path.concat("no path found\n");
			return path;
		}
		String nxt = P.get(dest).getKey(); // Begins to recreate the path off Prev data
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
		
		for(int i = Path.size()-1; i > 0; i--) // Go through it again but making 'path'
		{ // To get the distance between two nodes I use the difference in D, which keeps a running sum, leaving the actual weight
			//String temp_path = String.format(Path.get(i).concat(" to ").concat(Path.get(i-1)).concat(" with weight %.3f\n"), D.get(Path.get(i-1))-D.get(Path.get(i))
			//		);
			//path = path.concat(temp_path);a
			path.add(Path.get(i));
			path.add(Path.get(i-1));
			path.add(String.valueOf(D.get(Path.get(i-1))-D.get(Path.get(i))));
		}
		//path = path.concat(String.format("total cost: %.3f\n", D.get(dest)));
		//checkRep();
		return path;
	}
    
    public ArrayList<String> Listing_Buildings()
    {
    	Iterator<String> buildings = this.List_Of_Buildings.keySet().iterator();
    	ArrayList<String> Buildings = new ArrayList<String>();
    	String Building;
    	while(buildings.hasNext())
    	{
    		Building = buildings.next();
    		if(this.List_Of_Buildings.get(Building).equals(""))
    		{
    			continue;
    		}
    		Buildings.add(this.List_Of_Buildings.get(Building));
    		//System.out.println("who's there?");
    		//System.out.println(this.List_Of_Buildings.get(Building));

    	}
    	return Buildings;
    }
    public String get_Building(String name)
    {
    	if(is_ID(name))
    	{
    		if(this.List_Of_Buildings.get(name).equals(""))
    		{
    	    	return "";
    		}
    		return this.List_Of_Buildings.get(name);
    	}
    	if(is_Building(name))
    	{
    		return name;
    	}
    	return "";
    }
    public boolean is_Building(String name)
    {
    	if(name.equals(""))
    	{
    		return false;
    	}
    	if(this.List_Of_Buildings.containsKey(name))
    	{
        	if(this.List_Of_Buildings.get(name).equals(""))
        	{
        		return false;
        	}
        	return true;
    	}
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
    public String get_ID(String name)
    {
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
    
    public String find_Direction(String id1, String id2)
    {
    	double angle = Math.atan2(Double.valueOf(ID_to_Coord.get(id1).getValue())-Double.valueOf(ID_to_Coord.get(id2).getValue()), Double.valueOf(ID_to_Coord.get(id1).getKey())-Double.valueOf(ID_to_Coord.get(id2).getKey()));
    	angle = Math.toDegrees(angle);
    //	System.out.println("coord 1: " + ID_to_Coord.get(id1).getValue());
    	if(angle < 0)
    	{
    		angle += 360;
    	}
    	//System.out.println("angle:" + angle);
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