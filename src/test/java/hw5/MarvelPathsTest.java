package hw5;

import static org.junit.Assert.*;
import org.junit.Test;

public final class MarvelPathsTest {
	private final double JUNIT_DOUBLE_DELTA = 0.00001;
	
	@Test
	public void Test_Graph_Creation() // Passes the Representation Invariant on Creation (does not crash)
	{
		int x = 0;
		MarvelPaths Mar = new MarvelPaths();
		assertEquals(x, 0);
	}
	
	@Test
	public void Test_Reverse() // Checks if Reverse works
	{
		String test = "Hello";
		test = MarvelPaths.ReverseString(test);
		assertEquals("olleH", test);
		test = "racecar";
		test = MarvelPaths.ReverseString(test);
		assertEquals("racecar", test);
	}
	@Test
	public void One_Path() // Checks if a graph with one path between two points returns the correct output
	{
		String filename = "data/RS_Level1-10.csv";
		String answer;
		MarvelPaths Mar = new MarvelPaths();
		Mar.createNewGraph(filename);
		answer = Mar.findPath("Rusty", "Wyson");
		assertEquals("path from Rusty to Wyson:\n",answer.substring(0,answer.indexOf("\n")+1));
		assertEquals("Rusty to Seagull via Level 2",answer.substring(answer.indexOf("\n")+1,answer.indexOf("\n", answer.indexOf("\n")+1)));
		assertEquals("Seagull to Wyson via Level 3\n",answer.substring(answer.indexOf("\n", answer.indexOf("\n")+1)+1));
	}
	@Test
	public void No_Path() // Checks if a graph with no path between two points returns the correct output
	{
		String filename = "data/RS_Level1-10.csv";
		String answer;
		MarvelPaths Mar = new MarvelPaths();
		Mar.createNewGraph(filename);
		answer = Mar.findPath("Solus Dellagar", "Ochre Blamish Snail");
		assertEquals("path from Solus Dellagar to Ochre Blamish Snail:\n" + "no path found\n",answer);
	}
	@Test
	public void No_Path_Two()// Checks if a graph with no path between two points returns the correct output
	{
		String filename = "data/Alphabetical_Animals.csv";
		String answer;
		MarvelPaths Mar = new MarvelPaths();
		Mar.createNewGraph(filename);
		answer = Mar.findPath("Zephyr", "Alpaca");
		assertEquals("path from Zephyr to Alpaca:\n" + "no path found\n",answer);
	}
	@Test
	public void No_Node1() // Checks if a graph with no node1 returns the correct output
	{
		String filename = "data/RS_Level1-10.csv";
		String answer;
		MarvelPaths Mar = new MarvelPaths();
		Mar.createNewGraph(filename);
		answer = Mar.findPath("TzTok-Jad", "Chompy bird");
		assertEquals("unknown character TzTok-Jad\n",answer);
	}
	@Test
	public void No_Node2() // Checks if a graph with no node2 returns the correct output
	{
		String filename = "data/RS_Level1-10.csv";
		String answer;
		MarvelPaths Mar = new MarvelPaths();
		Mar.createNewGraph(filename);
		answer = Mar.findPath("Myre Blamish Snail", "Chaos Elemental");
		assertEquals("unknown character Chaos Elemental\n",answer);
	}
	@Test
	public void No_Node3() // Checks if a graph with no node1 returns the correct output
	{
		String filename = "data/RS_Level1-10.csv";
		String answer;
		MarvelPaths Mar = new MarvelPaths();
		Mar.createNewGraph(filename);
		answer = Mar.findPath("TzTok-Jad", "TzTok-Jad");
		assertEquals("unknown character TzTok-Jad\n",answer);
	}
	@Test
	public void Neither_Node() // Checks if a graph with neither node returns the correct output
	{
		String filename = "data/RS_Level1-10.csv";
		String answer;
		MarvelPaths Mar = new MarvelPaths();
		Mar.createNewGraph(filename);
		answer = Mar.findPath("TzTok-Jad", "Chaos Elemental");
		assertEquals("unknown character TzTok-Jad\n" + "unknown character Chaos Elemental\n",answer);
	}
	@Test
	public void Reflexive_Path2() // Checks if a graph with a reflexive edge returns the correct output
	{
		String filename = "data/RS_Level1-10.csv";
		String answer;
		MarvelPaths Mar = new MarvelPaths();
		Mar.createNewGraph(filename);
		answer = Mar.findPath("Ceolburg", "Ceolburg");
		assertEquals("path from Ceolburg to Ceolburg:\n",answer.substring(0,answer.indexOf("\n")+1));
	}
	@Test
	public void Alphabetical_Path() // Checks if a graph uses the correct alphabetical path
	{
		String filename = "data/Alphabetical_Animals.csv";
		String answer;
		MarvelPaths Mar = new MarvelPaths();
		Mar.createNewGraph(filename);
		answer = Mar.findPath("Alpaca", "Eagle");
		assertEquals("path from Alpaca to Eagle:\n"+"Alpaca to Bat via B\n"+"Bat to Chicken via C\n"+"Chicken to Drake via D\n"+"Drake to Eagle via E\n",answer);	
	}
	@Test
	public void There_and_Back_Again_Path() // Goes down a path, then goes back down the same path
	{
		String filename = "data/RS_Level1-10.csv";
		String answer;
		MarvelPaths Mar = new MarvelPaths();
		Mar.createNewGraph(filename);
		answer = Mar.findPath("Hygd", "Highwayman");
		assertEquals("path from Hygd to Highwayman:\n"+"Hygd to Gardener via Level 4\n"+"Gardener to Seagull via Level 3\n"+"Seagull to Goblin via Level 2\n"+"Goblin to Highwayman via Level 5\n",answer);	
		answer = Mar.findPath("Highwayman", "Hygd");
		assertEquals("path from Highwayman to Hygd:\n"+"Highwayman to Goblin via Level 5\n"+"Goblin to Seagull via Level 2\n" + "Seagull to Gardener via Level 3\n" + "Gardener to Hygd via Level 4\n",answer);	
	}
	@Test
	public void There_and_Back_Again_Again_Path() // Same as last test but different graph
	{
		String filename = "data/Alphabetical_Animals.csv";
		String answer;
		MarvelPaths Mar = new MarvelPaths();
		Mar.createNewGraph(filename);
		answer = Mar.findPath("Zephyr", "Railroad");
		assertEquals("path from Zephyr to Railroad:\n"+"Zephyr to Xylophone via Y\n"+"Xylophone to Why via X\n"+"Why to Violin via W\n"+"Violin to U via V\n"+"U to Trade Winds via U\n"+"Trade Winds to Stop via T\n" + "Stop to Railroad via S\n",answer);
		answer = Mar.findPath("Railroad", "Zephyr");
		assertEquals("path from Railroad to Zephyr:\n"+"Railroad to Stop via S\n"+"Stop to Trade Winds via T\n" + "Trade Winds to U via U\n" + "U to Violin via V\n" + "Violin to Why via W\n" + "Why to Xylophone via X\n" + "Xylophone to Zephyr via Y\n",answer);	
	}
	@Test
	public void Interconnected() // Checks if a graph uses the correct path (length 1 since fully connected)
	{ // Also it should be in alphabetical order, so always using the L path.
		String filename = "data/Alphabetical_Animals.csv";
		String answer;
		MarvelPaths Mar = new MarvelPaths();
		Mar.createNewGraph(filename);
		answer = Mar.findPath("Naughty", "Peace");
		assertEquals("path from Naughty to Peace:\n"+"Naughty to Peace via L\n",answer);
		answer = Mar.findPath("Limp", "Query");
		assertEquals("path from Limp to Query:\n"+"Limp to Query via L\n",answer);
		answer = Mar.findPath("Opulent", "Mice");
		assertEquals("path from Opulent to Mice:\n"+"Opulent to Mice via L\n",answer);
		
	}
	@Test
	public void New_Graph() // Checks if a graph can make a new graph and delete the old one correctly
	{
		String filename = "data/Alphabetical_Animals.csv";
		String answer;
		MarvelPaths Mar = new MarvelPaths();
		Mar.createNewGraph(filename);
		answer = Mar.findPath("Naughty", "Peace");
		assertEquals("path from Naughty to Peace:\n"+"Naughty to Peace via L\n",answer);
		filename = "data/RS_Level1-10.csv";
		Mar = new MarvelPaths();
		Mar.createNewGraph(filename);
		answer = Mar.findPath("Rusty", "Wyson");
		assertEquals("path from Rusty to Wyson:\n",answer.substring(0,answer.indexOf("\n")+1));
		assertEquals("Rusty to Seagull via Level 2",answer.substring(answer.indexOf("\n")+1,answer.indexOf("\n", answer.indexOf("\n")+1)));
		assertEquals("Seagull to Wyson via Level 3\n",answer.substring(answer.indexOf("\n", answer.indexOf("\n")+1)+1));
		answer = Mar.findPath("Naughty", "Peace");
		assertEquals("unknown character Naughty\n" + "unknown character Peace\n",answer);
	}
	@Test
	public void MarvelParser_Creation() // Checks if it handles a broken (bad format) file correctly (doesn't crash)
	{
		int x = 0;
		MarvelParser Mar = new MarvelParser();
		assertEquals(x, 0);	
	}
	@Test
	public void Broken_File() // Checks if it handles a broken (bad format) file correctly (doesn't crash)
	{
		String filename = "data/Broken_File.csv";
		int x = 0;
		MarvelPaths Mar = new MarvelPaths();
		Mar.createNewGraph(filename);
		assertEquals(x, 0);	
	}
	@Test
	public void Broken_File2() // Checks if it handles a broken (bad format) file correctly (doesn't crash)
	{
		String filename = "data/Broken_File_2.csv";
		int x = 0;
		MarvelPaths Mar = new MarvelPaths();
		Mar.createNewGraph(filename);
		assertEquals(x, 0);	
	}

	@Test
	public void Broken_File4() // Checks if it handles a broken (bad format) file correctly (doesn't crash)
	{
		String filename = "data/Broken_File_4.csv";
		int x = 0;
		MarvelPaths Mar = new MarvelPaths();
		Mar.createNewGraph(filename);
		assertEquals(x, 0);	
	}
}