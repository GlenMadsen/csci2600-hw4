package hw6;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;

import hw4.GraphWrapper;

public final class MarvelPaths2Test {
	private final double JUNIT_DOUBLE_DELTA = 0.00001;
	
	@Test
	public void Test_Graph_Creation() // Passes the Representation Invariant on Creation (does not crash)
	{
		int x = 0;
		MarvelPaths2 Mar = new MarvelPaths2();
		assertEquals(x, 0);
	}
	@Test
	public void One_Path() // Checks if a graph with one path between two points returns the correct output
	{
		String filename = "data/RS_Level1-10.csv";
		String answer;
		MarvelPaths2 Mar = new MarvelPaths2();
		Mar.createNewGraph(filename);
		answer = Mar.findPath("Rusty", "Wyson");
		assertEquals("path from Rusty to Wyson:\n",answer.substring(0,answer.indexOf("\n")+1));
		assertEquals("Rusty to Seagull with weight 1.000",answer.substring(answer.indexOf("\n")+1,answer.indexOf("\n", answer.indexOf("\n")+1)));
		assertEquals("Seagull to Wyson with weight 1.000\ntotal cost: 2.000\n",answer.substring(answer.indexOf("\n", answer.indexOf("\n")+1)+1));
	}
	@Test
	public void No_Path() // Checks if a graph with no path between two points returns the correct output
	{
		String filename = "data/RS_Level1-10.csv";
		String answer;
		MarvelPaths2 Mar = new MarvelPaths2();
		Mar.createNewGraph(filename);
		answer = Mar.findPath("Solus Dellagar", "Ochre Blamish Snail");
		assertEquals("path from Solus Dellagar to Ochre Blamish Snail:\n" + "no path found\n",answer);
	}
	@Test
	public void No_Path_Two()// Checks if a graph with no path between two points returns the correct output
	{
		String filename = "data/Alphabetical_Animals.csv";
		String answer;
		MarvelPaths2 Mar = new MarvelPaths2();
		Mar.createNewGraph(filename);
		answer = Mar.findPath("Zephyr", "Alpaca");
		assertEquals("path from Zephyr to Alpaca:\n" + "no path found\n",answer);
	}
	@Test
	public void No_Node1() // Checks if a graph with no node1 returns the correct output
	{
		String filename = "data/RS_Level1-10.csv";
		String answer;
		MarvelPaths2 Mar = new MarvelPaths2();
		Mar.createNewGraph(filename);
		answer = Mar.findPath("TzTok-Jad", "Chompy bird");
		assertEquals("unknown character TzTok-Jad\n",answer);
	}
	@Test
	public void No_Node2() // Checks if a graph with no node2 returns the correct output
	{
		String filename = "data/RS_Level1-10.csv";
		String answer;
		MarvelPaths2 Mar = new MarvelPaths2();
		Mar.createNewGraph(filename);
		answer = Mar.findPath("Myre Blamish Snail", "Chaos Elemental");
		assertEquals("unknown character Chaos Elemental\n",answer);
	}
	@Test
	public void No_Node3() // Checks if a graph with no node1 returns the correct output
	{
		String filename = "data/RS_Level1-10.csv";
		String answer;
		MarvelPaths2 Mar = new MarvelPaths2();
		Mar.createNewGraph(filename);
		answer = Mar.findPath("TzTok-Jad", "TzTok-Jad");
		assertEquals("unknown character TzTok-Jad\n",answer);
	}
	@Test
	public void Neither_Node() // Checks if a graph with neither node returns the correct output
	{
		String filename = "data/RS_Level1-10.csv";
		String answer;
		MarvelPaths2 Mar = new MarvelPaths2();
		Mar.createNewGraph(filename);
		answer = Mar.findPath("TzTok-Jad", "Chaos Elemental");
		assertEquals("unknown character TzTok-Jad\n" + "unknown character Chaos Elemental\n",answer);
	}
	@Test
	public void Reflexive_Path2() // Checks if a graph with a reflexive edge returns the correct output
	{
		String filename = "data/RS_Level1-10.csv";
		String answer;
		MarvelPaths2 Mar = new MarvelPaths2();
		Mar.createNewGraph(filename);
		answer = Mar.findPath("Ceolburg", "Ceolburg");
		assertEquals("path from Ceolburg to Ceolburg:\n",answer.substring(0,answer.indexOf("\n")+1));
	}
	@Test
	public void Alphabetical_Path() // Checks if a graph uses the correct alphabetical path
	{
		String filename = "data/Alphabetical_Animals.csv";
		String answer;
		MarvelPaths2 Mar = new MarvelPaths2();
		Mar.createNewGraph(filename);
		answer = Mar.findPath("Alpaca", "Eagle");
		assertEquals("path from Alpaca to Eagle:\n"+"Alpaca to Bat with weight 1.000\n"+"Bat to Chicken with weight 1.000\n"+"Chicken to Drake with weight 1.000\n"+"Drake to Eagle with weight 1.000\ntotal cost: 4.000\n",answer);	
	}
	@Test
	public void There_and_Back_Again_Path() // Goes down a path, then goes back down the same path
	{
		String filename = "data/RS_Level1-10.csv";
		String answer;
		MarvelPaths2 Mar = new MarvelPaths2();
		Mar.createNewGraph(filename);
		answer = Mar.findPath("Hygd", "Highwayman");
		assertEquals("path from Hygd to Highwayman:\n"+"Hygd to Gardener with weight 1.000\n"+"Gardener to Seagull with weight 1.000\n"+"Seagull to Goblin with weight 1.000\n"+"Goblin to Highwayman with weight 1.000\ntotal cost: 4.000\n",answer);	
		answer = Mar.findPath("Highwayman", "Hygd");
		assertEquals("path from Highwayman to Hygd:\n"+"Highwayman to Goblin with weight 1.000\n"+"Goblin to Seagull with weight 1.000\n" + "Seagull to Gardener with weight 1.000\n" + "Gardener to Hygd with weight 1.000\ntotal cost: 4.000\n",answer);	
	}
	@Test
	public void There_and_Back_Again_Again_Path() // Same as last test but different graph
	{
		String filename = "data/Alphabetical_Animals.csv";
		String answer;
		MarvelPaths2 Mar = new MarvelPaths2();
		Mar.createNewGraph(filename);
		answer = Mar.findPath("Zephyr", "Railroad");
		assertEquals("path from Zephyr to Railroad:\n"+"Zephyr to Xylophone with weight 1.000\n"+"Xylophone to Why with weight 1.000\n"+"Why to Violin with weight 1.000\n"+"Violin to U with weight 1.000\n"+"U to Trade Winds with weight 1.000\n"+"Trade Winds to Stop with weight 1.000\n" + "Stop to Railroad with weight 1.000\ntotal cost: 7.000\n",answer);
		answer = Mar.findPath("Railroad", "Zephyr");
		assertEquals("path from Railroad to Zephyr:\n"+"Railroad to Stop with weight 1.000\n"+"Stop to Trade Winds with weight 1.000\n" + "Trade Winds to U with weight 1.000\n" + "U to Violin with weight 1.000\n" + "Violin to Why with weight 1.000\n" + "Why to Xylophone with weight 1.000\n" + "Xylophone to Zephyr with weight 1.000\ntotal cost: 7.000\n",answer);	
	}
	@Test
	public void Interconnected() // Checks if a graph uses the correct path (length 1 since fully connected)
	{ // Also it should be in alphabetical order, so always using the L path.
		String filename = "data/Alphabetical_Animals.csv";
		String answer;
		MarvelPaths2 Mar = new MarvelPaths2();
		Mar.createNewGraph(filename);
		answer = Mar.findPath("Naughty", "Peace");
		assertEquals("path from Naughty to Peace:\n"+"Naughty to Peace with weight 0.167\ntotal cost: 0.167\n",answer);
		answer = Mar.findPath("Limp", "Query");
		assertEquals("path from Limp to Query:\n"+"Limp to Query with weight 0.167\ntotal cost: 0.167\n",answer);
		answer = Mar.findPath("Opulent", "Mice");
		assertEquals("path from Opulent to Mice:\n"+"Opulent to Mice with weight 0.167\ntotal cost: 0.167\n",answer);
		
	}
	@Test
	public void New_Graph() // Checks if a graph can make a new graph and delete the old one correctly
	{
		String filename = "data/Alphabetical_Animals.csv";
		String answer;
		MarvelPaths2 Mar = new MarvelPaths2();
		Mar.createNewGraph(filename);
		answer = Mar.findPath("Naughty", "Peace");
		assertEquals("path from Naughty to Peace:\n"+"Naughty to Peace with weight 0.167\ntotal cost: 0.167\n",answer);
		filename = "data/RS_Level1-10.csv";
		Mar = new MarvelPaths2();
		Mar.createNewGraph(filename);
		answer = Mar.findPath("Rusty", "Wyson");
		assertEquals("path from Rusty to Wyson:\n",answer.substring(0,answer.indexOf("\n")+1));
		assertEquals("Rusty to Seagull with weight 1.000",answer.substring(answer.indexOf("\n")+1,answer.indexOf("\n", answer.indexOf("\n")+1)));
		assertEquals("Seagull to Wyson with weight 1.000\ntotal cost: 2.000\n",answer.substring(answer.indexOf("\n", answer.indexOf("\n")+1)+1));
		answer = Mar.findPath("Naughty", "Peace");
		assertEquals("unknown character Naughty\n" + "unknown character Peace\n",answer);
	}
	@Test
	public void Large_Graph()
	{
		String filename = "data/Larger_Test.csv";
		MarvelPaths2 Mar = new MarvelPaths2();
		Mar.createNewGraph(filename);
		String answer = Mar.findPath("Drake", "Naga");
		assertEquals("path from Drake to Naga:\nDrake to Wyvern with weight 0.333\nWyvern to Naga with weight 0.333\ntotal cost: 0.667\n",answer);
		Mar.addEdge("Dragon", "Naga", 1/4.00);
		answer = Mar.findPath("Drake", "Naga");
		assertEquals("path from Drake to Naga:\nDrake to Dragon with weight 0.333\nDragon to Naga with weight 0.250\ntotal cost: 0.583\n",answer);
		Mar.addNode("Wyrm");
		Mar.addEdge("Naga", "Wyrm", 1/3.00);
		answer = Mar.findPath("Drake", "Wyrm");
		assertEquals("path from Drake to Wyrm:\nDrake to Dragon with weight 0.333\nDragon to Naga with weight 0.250\nNaga to Wyrm with weight 0.333\ntotal cost: 0.917\n",answer);
	}
	@Test
	public void Graph_List_Checks_Tech() // Lists two children in alphabetical order by edge
	{
		MarvelPaths2 Mar = new MarvelPaths2();
		Mar.addNode("Apple");
		Mar.addNode("Google");
		Mar.addEdge("Apple", "Google", 1/99.999999999999999);
		Iterator<String> Mar_it = Mar.listChildren("Apple");
		assertEquals(Mar_it.next(), "Google(0.01)");
		Mar.addNode("Ebay");
		Mar.addNode("Amazon");
		Mar.addEdge("Apple", "Amazon", 1/99.9999999999999999);
		Mar.addEdge("Amazon", "Ebay", 1.00);
		Mar.addEdge("Google", "Ebay", 1.00);
		Mar_it = Mar.listChildren("Apple");
		assertEquals(Mar_it.next(), "Amazon(0.01)");
		Mar_it = Mar.listChildren("Google");
		assertEquals(Mar_it.next(), "Ebay(1.0)");
		Mar_it = Mar.listNodes();
		assertEquals(Mar_it.next(), "Amazon");
		assertEquals(Mar_it.next(), "Apple");
		assertEquals(Mar_it.next(), "Ebay");
		assertEquals(Mar_it.next(), "Google");
		String answer = Mar.findPath("Apple", "Ebay");
		assertEquals("path from Apple to Ebay:\nApple to Amazon with weight 0.010\nAmazon to Ebay with weight 1.000\ntotal cost: 1.010\n",answer);
	}
	@Test
	public void Broken_File() // Checks if it handles a broken (bad format) file correctly (doesn't crash)
	{
		String filename = "data/Broken_File.csv";
		int x = 0;
		MarvelPaths2 Mar = new MarvelPaths2();
		Mar.createNewGraph(filename);
		assertEquals(x, 0);	
	}
	@Test
	public void Broken_File2() // Checks if it handles a broken (bad format) file correctly (doesn't crash)
	{
		String filename = "data/Broken_File_2.csv";
		int x = 0;
		MarvelPaths2 Mar = new MarvelPaths2();
		Mar.createNewGraph(filename);
		assertEquals(x, 0);	
	}

	@Test
	public void Broken_File4() // Checks if it handles a broken (bad format) file correctly (doesn't crash)
	{
		String filename = "data/Broken_File_4.csv";
		int x = 0;
		MarvelPaths2 Mar = new MarvelPaths2();
		Mar.createNewGraph(filename);
		assertEquals(x, 0);	
	}
}