package hw6;
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
import hw5.MarvelParser;
import hw5.MarvelPaths;
import javafx.util.Pair;


/**
 * Abstraction Function:
 * MarvelPaths2 represents a graph of type and structure of the Graph datatype (as specified in Graph's
 * Abstraction function and otherwise (Also uses generics now)). Each node in MarvelPaths represents a character in a Marvel comic
 * book. Each edge is a double of 1 over the (and labeled as such) number of times both characters make an appearance 
 * (total books shared). Since the graph is directed, edges in both directions are created
 * (an edge from A to B & an edge from B to A), and reflexive edges are not allowed.
 * 
 * Representation Invariant for every MarvelPath Graph graph:
 * The same Representation Variant as the Graph class
 * && (each key in the keyset of graph is the first object of a pair found in filename (no duplicates))
 * && (each value for every node is a map where the keys are a set of other first objects found in filename 
 *	   who share an equal second object from the pair and has an Double, of the multiplicative inverse of
 *	   1 over the number second objects, as the value. This is true initially when the graph is read in;
 *	   later edges added are not bound in any way to the file, but share the same String to String with
 *	   Double label restriction.

 * 	In other words
 * 		*It is the same as a Graph
 * 		*Each key is a character from the Marvel Universe, or whatever file is read
 * 		*Initially Each edge is a shared comic book between two characters and is labeled as such either the
 * 			multiplicative inverse of the number of books
 * 		*Subsequent added edges are not bound to the file but share the same String to String with Double value
 *  	*Graph is using Strings for the nodes and Doubles for the edge labels
 */

public class MarvelPaths2 {
    private Graph<String, Double> graph;
    
    /**
	 * @param N/A
	 * @returns N/A
	 * @throws N/A
	 * @requires N/A
	 * @effects constructs a new Graph variable that is empty
	 * @modifies graph variable is created
	 */
    public MarvelPaths2()
	{
		this.graph = new Graph<String, Double>();
		//checkRep();
	}
    public void addNode(String nodeData) // Instance field so it gets these methods from Graph
    {
    	this.graph.addNode(nodeData);
    }

    public void addEdge(String parentNode, String childNode, Double edgeLabel)
    {
    	this.graph.addEdge(parentNode, childNode, edgeLabel);
    }
	public Iterator<String> listNodes()
	{
		return this.graph.listNodes();
	}
	public Iterator<String> listChildren(String parentNode)
	{
		return this.graph.listChildren(parentNode);
	}
    /** @param: filename The path to the "CSV" file that contains the <hero, book> pairs   
   * @returns: N\A
   * @requires: N\A                                                                                             
   * @modifies: MarvelPaths private variable graph, both its keyset and values
   * @effects: the graph variable has nodes added for every hero from the file and edges added between every
   * 		   hero with which they share a comic book with, also read in from the file.
   * @throws: N/A                                                                                
 */  
	public void createNewGraph(String filename) // Reads a file and then constructs a graph of type Graph
	{
		//checkRep();
		try // Encapsulated within a try due to calling readData from MarvelParser 
		{  // Constructs necessary variables for readData
			Map<String, Set<String>> charsInBooks = new HashMap<String,Set<String> >();
			// Above is a Map of comic books to a set of characters in the book
			// Below is a set of all characters found
			Set<String> chars = new HashSet<String>();
			MarvelParser.readData(filename,charsInBooks,chars);
			// x`System.out.println("Read "+chars.size()+" characters who appear in "+charsInBooks.keySet().size() +" books.");
			// Creates variables and iterators for parsing the data into character, book, and pairs
			Iterator<String> Marvel_char_it = chars.iterator();
			Iterator<String> Marvel_book_it = charsInBooks.keySet().iterator();
			Iterator<String> Marvel_pair_it_1;
			Iterator<String> Marvel_pair_it_2;
			String Marvel_character;
			String Marvel_book;
			String Marvel_pair_1;
			String Marvel_pair_2;
			this.graph = new Graph<String, Double>(); // Creates the new graph, removing any previous elements
			HashMap<Pair<String, String>, Double> Pseudo = new HashMap<Pair<String, String>, Double>();
			while(Marvel_char_it.hasNext()) // Adds nodes to graph for every Marvel Character found
			{
				Marvel_character = Marvel_char_it.next();
		    	this.graph.addNode(Marvel_character);
			}
			while(Marvel_book_it.hasNext()) // Iterates through every comic book
			{
				Marvel_book = Marvel_book_it.next();
				Marvel_pair_it_1 = charsInBooks.get(Marvel_book).iterator();
				while(Marvel_pair_it_1.hasNext()) // Iterates through every character
				{
					Marvel_pair_1 = Marvel_pair_it_1.next();
					Marvel_pair_it_2 = charsInBooks.get(Marvel_book).iterator();
					while(Marvel_pair_it_2.hasNext()) // Iterates through every character again,
					{ // So for every character, an edge is created from that character to every other
					  // character, and this is done in both directions.
					  // Also if trying to add an edge to itself, graph.addEdge handles the case and does
					  // not allow for reflexive edges, instead doing nothing.
						Marvel_pair_2 = Marvel_pair_it_2.next();
						if(!Marvel_pair_1.equals(Marvel_pair_2))
						{
							Pair<String,String> temp_pair = new Pair<String, String>(Marvel_pair_1,Marvel_pair_2);
//							if(Marvel_pair_1.equals("Alpaca"))
//							{
//								System.out.println(Marvel_pair_2);
//								System.out.println(Pseudo.get(temp_pair));
//							}
							if(Pseudo.get(temp_pair) == null)
							{
								Pseudo.put(temp_pair, 0.00);
							}
							Pseudo.put(temp_pair, 1.00+Pseudo.get(temp_pair));
//							if(Marvel_pair_1.equals("Alpaca"))
//							{
//								System.out.println(Marvel_pair_2 + "Testtttt");
//								Pair<String,String> temp_or = new Pair<String, String>(Marvel_pair_1,"Bat");
//								System.out.println(Pseudo.get(temp_or));
//							}
						}
					}
				}
			}
			Iterator<Pair<String, String>> Pseudo_it = Pseudo.keySet().iterator();
			Pair<String, String> Pseudo_nxt;
			while(Pseudo_it.hasNext())
			{
				Pseudo_nxt = Pseudo_it.next();
				this.graph.addEdge(Pseudo_nxt.getKey(), Pseudo_nxt.getValue(), 1/Pseudo.get(Pseudo_nxt));
//				this.graph.addEdge(Pseudo_nxt.getValue(), Pseudo_nxt.getKey(), 1/Pseudo.get(Pseudo_nxt));
//				if(Pseudo_nxt.getKey().equals("Alpaca"))
//				{
//					System.out.println(Pseudo_nxt.getValue());
//					System.out.println(1/Pseudo.get(Pseudo_nxt));
//				}
			}
		//	checkRep();
		}
		catch (IOException e) // Catches any exceptions caused by a bad file
		{
			//checkRep();
			e.printStackTrace();
		}
	}
	   /** @param: two Strings node1, node2 which represent the start and end locations respectively   
	   * @returns: A string which either states that the node(s) were not found in the Graph graph,
	   * 		   A string which states that no path between the points were found in the Graph
	   *           A string which implies that the start node is the same as the destination
	   *           A string which describes every step of the path, including the intermediary nodes and
	   *             the edge labels it traverses on from starting node to ending node.
	   * @requires: N\A                                                                                             
	   * @modifies: N\A
	   * @effects: N\A
	   * @throws: N\A                                                                                  
	 */ 
	public String findPath(String node1, String node2)
	{ // Via Dijkstra's, returns the path with the lowest weight
		//checkRep();
		String path = "";
		String temp_node;
		String start = node1; // Creates new variables for the start and dest nodes, begins to check
		String dest = node2; // if they are in the graph, also initializes the path variable
		int node1_present = 0;
		int node2_present = 0;
		Iterator<String> node_iter = this.graph.listNodes(); // Checks if nodes exist in the graph
		while(node_iter.hasNext())
		{
			temp_node = node_iter.next();
			if(temp_node.equals(node1))
			{
				node1_present++;
			}
			if(temp_node.equals(node2))
			{
				node2_present++;
			}
		}
		if(node1_present != 1 || node2_present != 1) // Otherwise returns specified output
		{ 
			if(node1.equals(node2))
			{
				path = path.concat("unknown character " + start + "\n");
				return path;
			}
			if(node1_present != 1)
			{ 
				path = path.concat("unknown character " + start + "\n");
			}
			if(node2_present != 1)
			{ 
				path = path.concat("unknown character " + dest + "\n");
			}
			return path;
		}
		if(start.equals(dest)) // If nodes are in path but are equal then returns path of weight 0
		{
			path = "path from ".concat(start).concat(" to ").concat(dest).concat(":\n");
			path = path.concat(String.format("total cost: %.3f\n", 0.00));
			return path;
		}
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
		// Hashmap of destinces followed by a HashMap of Paths
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
		node_it = this.graph.listChildren(start); // Adds all the nodes to the priority Q
//		while(node_it.hasNext())
//		{
//			node = node_it.next();
//			String Reversed = MarvelPaths.ReverseString(node);
//			node = Reversed.substring(Reversed.indexOf("(")+1);
//			node = MarvelPaths.ReverseString(node);
//		//	System.out.println(node);
//			Pair<String, Double> tempPair = new Pair<String, Double>(node, D.get(node));
//			H.add(tempPair);
//		}
		node_it = this.graph.listNodes(); // Adds all the nodes to the priority Q
		while(node_it.hasNext())
		{
			node = node_it.next();
			Pair<String, Double> tempPair = new Pair<String, Double>(node, D.get(node));
			H.add(tempPair);
			P.put(node, null);
		}		
		
		String Reversed;
		Double sum;
		String book_name;
		String tempPair;
		
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
				book_name = Reversed.substring(Reversed.indexOf(")")+1, Reversed.indexOf("("));
				book_name = MarvelPaths.ReverseString(book_name);
				child = Reversed.substring(Reversed.indexOf("(")+1);
				child = MarvelPaths.ReverseString(child);
				sum = Double.parseDouble(book_name);
				 // Checks to see if it should add to the Priority Queue
				
				if(D.get(child) > (D.get(tempPair) + sum))
				{
					D.put(child, D.get(tempPair) + sum); // Updates the distance
					P.put(child, new Pair<String, Double>(tempPair, D.get(tempPair))); // Updates the path list
					//H.remove(tempPair); // Creates new pair for Priority Queue and updates old one.
					H.add(new Pair<String, Double>(child, D.get(child)));
				}
			}
		}
//--------------------------------------------------------------------------------------------------------		
		path = "path from ".concat(start).concat(" to ").concat(dest).concat(":\n"); // Creates path start
		if(P.get(dest) == null) // If there is no path initially then outputs null
		{
			return path.concat("no path found\n");
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
			String temp_path = String.format(Path.get(i).concat(" to ").concat(Path.get(i-1)).concat(" with weight %.3f\n"), D.get(Path.get(i-1))-D.get(Path.get(i))
					);
			path = path.concat(temp_path);
		}
		path = path.concat(String.format("total cost: %.3f\n", D.get(dest)));
		return path; // Return path
	}
	

//	private void checkRep() throws RuntimeException
//	{// Only thing checkRep() does not check is for duplicate nodes, which is handled natively by the Set
//	 // class in Java so they will never be present.
//		if(graph == null)
//		{
//            throw new RuntimeException("graph == null");
//		}
//		Iterator<String> node_it = graph.listNodes();
//		Iterator<String> chil_it;
//		String node_key;
//		String chil_key;
//		while(node_it.hasNext())
//		{
//			node_key = node_it.next();
//			chil_it = graph.listChildren(node_key);
//			if(node_key == null)
//			{
//				throw new RuntimeException("A node's HashMap of it's children keyset is null");
//			}
//			while(chil_it.hasNext())
//			{
//				chil_key = chil_it.next();
//				if(chil_key == null)
//				{
//					throw new RuntimeException("The ArrayList<String> of edge labels for a child contains null");
//				}
//			}
//		}
//	}
//	public static void main(String[] arg)
//	{
//		String file = arg[0];
//		MarvelPaths2 Marvel = new MarvelPaths2();	
//		Marvel.createNewGraph(file);
//		//System.out.println(Marvel.findPath("Alpaca", "Bear"));
//		System.out.println(Marvel.findPath("SIF", "RAMBO"));
//	}
}