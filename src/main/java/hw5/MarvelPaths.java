package hw5;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import hw4.Graph;


/**
 * Abstraction Function:
 * MarvelPaths represents a graph of type and structure of the Graph datatype (as specified in Graph's
 * Abstraction function and otherwise). Each node in MarvelPaths represents a character in a Marvel comic
 * book. Each edge is a comic book (and labeled as such) where both characters make an appearance. Since
 * the graph is directed, edges in both directions are created (an edge from A to B & an edge from B to A),
 * and reflexive edges are not allowed.
 * 
 * Representation Invariant for every MarvelPath Graph graph:
 * The same Representation Variant as the Graph class
 * && (each key in the keyset of graph is the first object of a pair found in filename (no duplicates))
 * && (each value for every node is a map where the keys are a set of other first objects found in filename 
 *	   who share an equal second object from the pair and has an ArrayList of second objects as a value.

 * 	In other words
 * 		*It is the same as a Graph
 * 		*Each key is a character from the Marvel Universe, or whatever file is read
 * 		*Each edge is a shared comic book between two characters and is labeled as such.
 * 		*Graph is using Strings for the nodes and Strings for the edge labels
 */

public class MarvelPaths {
    private Graph<String, String> graph;
    
    /**
	 * @param N/A
	 * @returns N/A
	 * @throws N/A
	 * @requires N/A
	 * @effects constructs a new Graph variable that is empty
	 * @modifies graph variable is created
	 */
    public MarvelPaths()
	{
		this.graph = new Graph<String, String>();
		checkRep();
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
		checkRep();
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
			this.graph = new Graph<String, String>(); // Creates the new graph, removing any previous elements
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
							this.graph.addEdge(Marvel_pair_1, Marvel_pair_2, Marvel_book);
						}
					}
				}
			}
			checkRep();
		}
		catch (IOException e) // Catches any exceptions caused by a bad file
		{
			checkRep();
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
	public String findPath(String node1, String node2) // Determines the shortest path from node1 to node2
	{ // Via BFS, returns the path, if multiple, then which ever is first in lexicographic ordering. 
		checkRep();
		String start = node1; // Creates new variables for the start and dest nodes, begins to check
		String dest = node2; // if they are in the graph, also initializes the path variable
		String node;
		String path = "";
		
		int node1_present = 0;
		int node2_present = 0;
		Iterator<String> node_it = this.graph.listNodes(); // Checks for node1 and node2's presence in the
		while(node_it.hasNext()) // graph
		{
			node = node_it.next();
			if(node.equals(node1))
			{
				node1_present++;
			}
			if(node.equals(node2))
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
		// At this point both nodes must be in the graph, so we set path to what it should be and initialize
		path = "path from " + start + " to " + dest + ":\n"; // The rest of the variables, like the queue.
		String n;
		String child;
		String character_name;
		String book_name;
		String Reversed;
		Queue<String> q = new LinkedList<String>(); // The queue for BFS
		Map<String, ArrayList<String>> M = new HashMap<String, ArrayList<String> >(); 
		// Mapping of nodes to paths
		q.add(start);
		ArrayList<String> empty = new ArrayList<String>();
		M.put(start, empty);
		while(!q.isEmpty()) // Iterates through the queue
		{
			n = q.peek();
			q.poll();
			if (n.equals(dest)) // Returns the path once the destination is found
			{
				for(int i = 0; i < M.get(n).size(); i++) // Concatenates the ArrayList of individual
				{ // edges into one string, then returns it
					path = path.concat(M.get(n).get(i)).concat("\n");
				}
				checkRep();
				return path;
			}
			Iterator<String> edge_it = this.graph.listChildren(n); // Gets an iterator for the edges of n
			while(edge_it.hasNext())
			{ // Parses the output of list children into character name and comic book name
				child = edge_it.next(); // to do this easily it first reverses the name
				Reversed = ReverseString(child);
				book_name = Reversed.substring(Reversed.indexOf(")")+1, Reversed.indexOf("("));
				character_name = Reversed.substring(Reversed.indexOf("(")+1);
				book_name = ReverseString(book_name);
				character_name = ReverseString(character_name);
				if(M.containsKey(character_name)) // Checks if the character is already visited
				{
					continue;
				}
				if(!M.containsKey(character_name)) // Otherwise adds a path to it and adds it to M and q
				{
					ArrayList<String> temp = new ArrayList<String>();
					temp.addAll(M.get(n));
					temp.add(n + " to " + character_name + " via " + book_name);
					M.put(character_name, temp);
					q.add(character_name);
				}
			}
		}
		checkRep();
		path = path.concat("no path found\n"); // If no paths are found then this point is reached, all vertices visited,
		return path; // and "no path found" returned".
	}
	   /** @param: A String called child
	   * @returns: The String but reversed, so the last character is first, 2nd to last is 2nd, etc. until
	   * 			the first character is last.
	   * @requires: N\A                                                                                             
	   * @modifies: N\A
	   * @effects: N\A
	   * @throws: N\A                                                                               
	 */ 
	public static String ReverseString(String child) // Static Function used to reverse strings
	{
		String reversed = "";
		for(int i = child.length(); i > 0; i--)
		{
			reversed = reversed.concat(child.substring(i-1, i));
		}
		return reversed;
	}
	private void checkRep() throws RuntimeException
	{// Only thing checkRep() does not check is for duplicate nodes, which is handled natively by the Set
	 // class in Java so they will never be present.
		if(graph == null)
		{
            throw new RuntimeException("graph == null");
		}
		Iterator<String> node_it = graph.listNodes();
		Iterator<String> chil_it;
		String node_key;
		String chil_key;
		while(node_it.hasNext())
		{
			node_key = node_it.next();
			chil_it = graph.listChildren(node_key);
			if(node_key == null)
			{
				throw new RuntimeException("A node's HashMap of it's children keyset is null");
			}
			while(chil_it.hasNext())
			{
				chil_key = chil_it.next();
				if(chil_key == null)
				{
					throw new RuntimeException("The ArrayList<String> of edge labels for a child contains null");
				}
			}
		}
	}
	public static void main(String[] arg)
	{
		String file = arg[0];
		MarvelPaths Marvel = new MarvelPaths();	
		Marvel.createNewGraph(file);
		System.out.println(Marvel.findPath("CALLISTO", "SIF"));
	}
}