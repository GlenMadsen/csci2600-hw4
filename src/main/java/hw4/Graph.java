package hw4;

import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

import java.util.*;

/**
 * Abstraction Function:
 * Graph represents a directed labeled multigraph where each key (a String) in the nodes keySet is a node 
 * in the graph and each nodes' keys map to a HashMap of all of it's children as keys (Strings) and an 
 * ArrayList<String> as a list of edgeLabels for every edge. There should be no duplicate nodes but 
 * duplicate edges between nodes are allowed.
 * 
 * So there is a set of nodes (possibly empty) in the first level of the map, with each node containing
 * a HashMap with set of its children (nodes it directs to in a one way manner and possibly empty) as keys. 
 * This maps to an array of all of the edges from the parentnode to the childnode, which are labeled via
 * the edgeLabels. This represents the directed labeled multigraph.
 * 
 * Representation Invariant for every Graph g:
 * (g.nodes !=  null)
 * && (g.nodes.containsKey(null) == false) 
 * && (g.nodes.keySet() != null)
 * && (g.nodes.values() != null)
 * && (g.nodes.containsValue(null))
 * && (for all keys in keyset g.nodes.get(key).containsKey(null) == false)
 * && (for all keys in keyset g.nodes.get(key).keySet() != null) 
 * && (for all keys in keyset g.nodes.get(key).values() != null);
 * && (for all keys in keyset g.nodes.get(key).get(key in other keyset).contains(null) == false);
 * && (for all keys in keyset of g there are no duplicate keys)

 * 	In other words
 * 		*The nodes is not null
 *  	*The keyset of nodes does not contain null and is not null.
 *  	*The values of the nodes HashMap is not null and does not contain null.
 *  	*The keyset of every HashMap mapped to by a key in nodes does not contain null and is not null.
 *  	*The values of every HashMap mapped to by a key in nodes is not null and does not contain null.
 */

public class Graph {

	private HashMap<String, HashMap<String, ArrayList<String>>> nodes = new HashMap<String, HashMap<String, ArrayList<String> > >();

	/**
	 * @param N/A
	 * @returns N/A
	 * @throws N/A
	 * @requires N/A
	 * @effects constructs a new HashMap<String, HashMap<String, ArrayList<String> > > equal to null
	 * @modifies nodes variable is created
	 */
	public Graph()
	{
		this.nodes = new HashMap<String, HashMap<String, ArrayList<String> > >();
		checkRep(); // Only checks Rep after ADT is created.
	}


	/**
	 * @param nodeData is the label of the new node
	 * @returns N/A
	 * @throws N/A
	 * @requires nodeData to be a non-null String
	 * @effects places a new HashMap<String, ArrayList<String> > equal to null in 
	 * nodes values with a key of nodeData mapped to it. 
	 * If nodeData is already in the keySet of nodes then nothing is modified.
	 * @modifies nodes values and keySet if nodeData is not in the keySet of nodes.keySet()
	 */
	public void addNode(String nodeData)
	{ // Does not have a check if nodeData is null, but checkRep handles it by throwing an expception.
		checkRep(); // checkRep is not commented out since addNode changes the ADT.
		if (!this.nodes.containsKey(nodeData))
		{
			HashMap<String, ArrayList<String> > temp = new HashMap<String, ArrayList<String> >();
			this.nodes.put(nodeData, temp);
		}
		checkRep();
	}


	/**
	 * @param parentNode is the String key of the nodes HashMap, childNode is the key to be added or a 
	 * key already of the HashMap mapped to by parentNode, and edgeLabel is the string to be added to either
	 * a prior existing or new ArrayList<String> mapped to by childNode in the HashMap mapped to 
	 * by the parentNode key in the nodes HashMap. 
	 * @returns N/A
	 * @throws N/A
	 * @requires parentNode, childNode, and edgeLabel to be a non-null String. If either parentNode or
	 * childNode do not exist in the keySet of nodes then nothing is modified.
	 * @effects If either the nodes keySet does not contains parentNode or childNode the it does nothing
	 * else:
	 * If the childNode exists in the keySet of the nodes.get(parentNode).keySet() HashMap then 
	 * it adds a String edgeLabel to the ArrayList mapped by the childNode in the values of that HashMap.
	 * 
	 * If the childNode does not exist in the keySet of the nodes.get(parentNode).keyset() HashMap, then
	 * it adds a String childNode to that hashmaps keyset and maps it to a new ArrayList<String>
	 * containing only the string edgeLabel.
	 * 
	 * Any duplicate edges going from the same parentNode to childNode with the same edgeLabel will still
	 * be added to the ArrayList<String> so duplicates are allowed.
	 * 
	 * @modifies If the nodes keySet contains parentNode and childNode, it either modifies
	 * the ArrayList<String> in the values of the HashMap mapped by nodes.get(parentNode) if 
	 * childNode already exists in nodes.get(parentNode).keySet()
	 * or it modifies ArrayList<String> in the values of the HashMap mapped by nodes.get(parentNode)
	 * and the corresponding keySet of the Hashmap
	 */
	public void addEdge(String parentNode, String childNode, String edgeLabel)
	{// Add edge modifies, so checks Rep at the start and end, adds a new node if keyset contains both nodes
		checkRep();
		if(this.nodes.containsKey(parentNode) && this.nodes.containsKey(childNode))
		{
			if(this.nodes.get(parentNode).containsKey(childNode))
			{
				this.nodes.get(parentNode).get(childNode).add(edgeLabel);
			}
			else
			{
				ArrayList <String> temp = new ArrayList<String> (1); // Does not check if edgelabel is null
				temp.add(edgeLabel);						// Would violate invariant, handled by checkRep
				this.nodes.get(parentNode).put(childNode, temp);
			}
		}
		checkRep();
	}
	/**
	 * @param N/A
	 * @returns returns an iterator to a new TreeSet containing the keySet of nodes
	 * This iterator returns the nodes in lexicographical (alphabetical) order. 
	 * @throws N/A
	 * @requires
	 * @effects N/A
	 * @modifies N/A
	 */
	public Iterator<String> listNodes()
	{ // As listNode does not modify anything, it has checkReps commented out, sorts using a new TreeSet
		//CheckRep();
		Set<String> list_of_nodes = new TreeSet<String>();
		list_of_nodes.addAll(this.nodes.keySet());
		Iterator<String> graph_it = list_of_nodes.iterator();
		checkRep();
		//CheckRep();
		return graph_it;
	}
	/**
	 * @param 
	 * @returns returns If parentNode is not in the keySet then it returns null, if the keySet is empty
	 * then it returns null. If parentNode is in the keySet then it returns an iterator to a new TreeSet 
	 * containing each edgeLabel in every ArrayList<String> mapped to by each key in the keySet of the
	 * HashMap mapped to by the parentNode.
	 * 
	 * The iterator returns the list of childNode(edgeLabel) in lexicographical (alphabetical) order 
	 * by node name and secondarily by edge label.  childNode(edgeLabel) means there is an edge with label 
	 * edgeLabel from parentNode to childNode. This includes reflexive edges and duplicate edges, listing
	 * each edge as a seperate entry.
	 * @throws N/A
	 * @requires parentNode to be a non-null String
	 * @effects N/A
	 * @modifies N/A
	 */
	public Iterator<String> listChildren(String parentNode)
	{ // Lists all the children of a parent node, commented out check reps as it shouldn't modify anything
		//CheckRep();
		if(this.nodes.keySet().isEmpty()) // Specified to return null if keyset is empty
		{
			return null;
		}
		if(this.nodes.get(parentNode) != null) // Iterates through keyset and array length for a node
		{ // Concatenates the strings, sorts them, then returns an iterator of this new sorted list.
			ArrayList<String> list_of_children = new ArrayList<String>();
			Iterator<String> key_it = this.nodes.get(parentNode).keySet().iterator();
			while(key_it.hasNext())
			{
				String curr_key = key_it.next();
				int arr_length = this.nodes.get(parentNode).get(curr_key).size();
				for(int i = 0; i < arr_length; i++)
				{
					list_of_children.add(curr_key.concat("(").concat(this.nodes.get(parentNode).get(curr_key).get(i).concat(")")));
				}
			}
			list_of_children.sort(null);
			Iterator<String> graph_it = list_of_children.iterator();
			//CheckRep();
			return graph_it;
		}
		else // Also if the item is not in the key set
		{	
			//CheckRep();
			return null;
		}
	}
	
	/**
     * Checks that the representation invariant holds
	 * @param N/A
	 * @returns N/A
	 * @throws Runtime Exceptions if representation invariant is violated.
	 * @requires N/A
	 * @effects N/A
	 * @modifies N/A
	 */
	private void checkRep() throws RuntimeException
	{// Only thing checkRep() does not check is for duplicate nodes, which is handled natively by the Set
	 // class in Java so they will never be present.
		if(this.nodes == null)
		{
            throw new RuntimeException("nodes == null");
		}
		if(this.nodes.containsKey(null))
		{
            throw new RuntimeException("node's keySet contains null");
		}
		if(this.nodes.keySet() == null)
		{
            throw new RuntimeException("nodes' keySet == null");
		}
		if(this.nodes.values() == null)
		{
            throw new RuntimeException("node's HashMap == null");
		}
		if(this.nodes.containsValue(null))
		{
            throw new RuntimeException("a key in nodes maps to null");
		}
		Iterator<String> node_it = this.nodes.keySet().iterator();
		Iterator<String> chil_it;
		String node_key;
		String chil_key;
		while(node_it.hasNext())
		{
			node_key = node_it.next();
			if(this.nodes.get(node_key).keySet() == null)
			{
				throw new RuntimeException("A node's HashMap of it's children keyset is null");
			}
			if(this.nodes.get(node_key).containsKey(null))
			{
				throw new RuntimeException("A node's HashMap of it's children keyset contains null");
			}
			chil_it = this.nodes.get(node_key).keySet().iterator();
			while(chil_it.hasNext())
			{
				chil_key = chil_it.next();
				if(this.nodes.get(node_key).get(chil_key) == null)
				{
					throw new RuntimeException("The ArrayList<String> of edge labels for a child is null");
				}
				if(this.nodes.get(node_key).get(chil_key).contains(null))
				{
					throw new RuntimeException("The ArrayList<String> of edge labels for a child contains null");
				}
			}
		}
	}
}
