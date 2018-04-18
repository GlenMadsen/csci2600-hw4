package hw7;

import java.io.*;

import hw7.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class CampusPathsTest { // Rename to the name of your "main" class

	/**
	 * @param file1 
	 * @param file2
	 * @return true if file1 and file2 have the same content, false otherwise
	 * @throws IOException
	 */	
	/* compares two text files, line by line */
	private static boolean compare(String file1, String file2) throws IOException {
		BufferedReader is1 = new BufferedReader(new FileReader(file1)); // Decorator design pattern!
		BufferedReader is2 = new BufferedReader(new FileReader(file2));
		String line1, line2;
		boolean result = true;
		while ((line1=is1.readLine()) != null) {
			// System.out.println(line1);
			line2 = is2.readLine();
			if (line2 == null) {
				System.out.println(file1+" longer than "+file2);
				result = false;
				break;
			}
			if (!line1.equals(line2)) {
				System.out.println("Lines: "+line1+" and "+line2+" differ.");
				result = false;
				break;
			}
		}
		if (result == true && is2.readLine() != null) {
			System.out.println(file1+" shorter than "+file2);
			result = false;
		}
		is1.close();
		is2.close();
		return result;		
	}
	
	private void runTest(String filename) throws IOException {
		InputStream in = System.in; 
		PrintStream out = System.out;				
		String inFilename = "data/"+filename+".test"; // Input filename: [filename].test here  
		String expectedFilename = "data/"+filename+".expected"; // Expected result filename: [filename].expected
		String outFilename = "data/"+filename+".out"; // Output filename: [filename].out
		BufferedInputStream is = new BufferedInputStream(new FileInputStream(inFilename));
		System.setIn(is); // redirects standard input to a file, [filename].test 
		PrintStream os = new PrintStream(new FileOutputStream(outFilename));
		System.setOut(os); // redirects standard output to a file, [filename].out 
		CampusPaths.main(null); // Call to YOUR main. May have to rename.
		System.setIn(in); // restores standard input
		System.setOut(out); // restores standard output
		assertTrue(compare(expectedFilename,outFilename)); 
		// TODO: More informative file comparison will be nice.
		
	}
	private void runTest2(String filename, String[] other_files) throws IOException {
		InputStream in = System.in; 
		PrintStream out = System.out;				
		String inFilename = "data/"+filename+".test"; // Input filename: [filename].test here  
		String expectedFilename = "data/"+filename+".expected"; // Expected result filename: [filename].expected
		String outFilename = "data/"+filename+".out"; // Output filename: [filename].out
		BufferedInputStream is = new BufferedInputStream(new FileInputStream(inFilename));
		System.setIn(is); // redirects standard input to a file, [filename].test 
		PrintStream os = new PrintStream(new FileOutputStream(outFilename));
		System.setOut(os); // redirects standard output to a file, [filename].out
		//System.out.println(files == null);
		CampusPaths.main(other_files); // Call to YOUR main. May have to rename.
		System.setIn(in); // restores standard input
		System.setOut(out); // restores standard output
		assertTrue(compare(expectedFilename,outFilename)); 
		// TODO: More informative file comparison will be nice.
		
	}
	
	@Test // Tests Listing Buildings using given output from the given files
	public void testListBuildings() throws IOException {
		runTest("test1");
	}
	@Test // Tests finding a path using the sample output
	public void testFindPath() throws IOException {
		runTest("test2");
	}
	@Test // Tests a longer paths on the graph
	public void testLongPath() throws IOException {
		runTest("test3");
	}
	@Test // Tests Disconnected paths using nodes 58 and 86 on a few cases
	public void testDisconnectedPath() throws IOException {
		runTest("test4");
	}
	@Test // Tests finding paths using intersections, either primarily through them or 
		  // calling path with them and checking if the correct output is returned
	public void testInterSectionPath() throws IOException {
		runTest("test5");
	}
	@Test // Tests if various close paths both work and give the shortest path when many are available
	public void testClosePath() throws IOException {
		runTest("test6");
	}
	@Test // Tests all 8 angles, but if it does a node 1 to node 2 which goes North, also checks if
		  // node 2 to node 1 goes South, since it takes the same path but in reverse, so 16 tests.
	public void testAnglePath() throws IOException {
		runTest("test7");
	}
	@Test // Tests things like empty strings or spaces when calling functions and wrong letters
	public void testInvalidInputPath() throws IOException {
		runTest("test8");
	}
	@Test // Tests the Parser by using a broken file, specifically the building one.
	public void testNewFile1() throws IOException {
		String[] files = {"data/hw6_Test_Buildings.csv","data/hw6_Test_Edges.csv"};
		runTest2("test9", files);
	}
	@Test // Tests another part of the parser by using a broken building file in a different way
	public void testNewFile2() throws IOException {
		String[] files = {"data/hw6_Test2_Buildings.csv","data/hw6_Test_Edges.csv"};
		runTest2("test10", files);
	}
	@Test // Tests another part of the parser with a broken edge file
	public void testNewFile3() throws IOException {
		String[] files = {"data/RPI_map_data_Nodes.csv","data/hw6_Test_Edges.csv"};
		runTest2("test11", files);	
	}
	@Test // Tests creating the objects and if a file isnt found in the first place.
	public void testDifferent() throws IOException {
		CampusParser Parser = new CampusParser();
		CampusView View = new CampusView();
		CampusPaths Paths = new CampusPaths();
		try
		{
		runTest("test12");
		}
		catch (FileNotFoundException e)
		{
			//e.printStackTrace(); I would print here but Submitty wants the stackTrace empty
		}
	}
}
