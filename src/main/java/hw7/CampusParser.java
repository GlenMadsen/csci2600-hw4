package hw7;

import java.util.*;
import java.io.*;
import javafx.util.Pair;


public class CampusParser {
	/** @param: Nodes The path to the "CSV" file that contains the <Building Name, id, x coordinate, y coordinate> lines
	    @param: Edges, the path to the "CSV" file that contains the <id1,id2> pairs                                                                                             
        @param: BuildingEdges The Set that stores parsed <id1, id2> edge pairs;
        	    usually an empty Set on call.
        @param: Buildings, the map that stores parsed coordinate and name information,
        		in the form of id keys to an ArrayList of (Building Name, x coordinate, y coordinate)
        @effects: adds parsed <id, ArrayList<String>(BuildingName, x coordinate, y coordinate)>
        		  	values to the map Buildings, first item as the key and the second as the value.
        		  adds parsed id1 to id2 edge pairs to Set BuildingEdges
        @throws: IOException if file cannot be read (wrong format) or if the file is not a CSV file                                                                                     
	 */
    public static void readData(String Nodes, String Edges, Set<Pair<String, String>> BuildingEdges, Map<String, ArrayList<String>> Buildings) 
    		throws IOException {
    	BufferedReader reader = new BufferedReader(new FileReader(Nodes));
        String building;
        String id;
        String x;
        String y;
    	String line;
        String[] lines; // Create necessary variables
        while ((line = reader.readLine()) != null) // Loop through the file lines of Nodes
        {
             int i = line.indexOf(","); // Check if the file line is okay, aka 3 commas
             if ((i == -1)) {
            	// throw new IOException("File "+Nodes+" not a CSV (Building Name, id, x, y) file.");
             }  
             int count = 0;
             for(int j = 0; j < line.length(); j++)
             {
            	 if(line.charAt(j)==',')
            	 {
            		 count++;
            	 }
             }
             if(count != 3) // If not throw exceptions
             {
            	 //throw new IOException("File "+Nodes+" not a CSV (Building Name, id, x, y) file.");
             }
             lines = line.split(","); // Split the file by commas and reset variables
             building = lines[0]; // Set variables to the proper values
             id = lines[1];
             x = lines[2];
             y = lines[3];
             ArrayList<String> t = new ArrayList<String>(); // Pack them into an Array List
             t.add(building);
             t.add(x);
             t.add(y);
             Buildings.put(id, t); // Put the ArrayList into Buildings
        }
        reader.close(); // Close the reader and open a new one for Edges
        reader = new BufferedReader(new FileReader(Edges));
        while ((line = reader.readLine()) != null) 
        {
             int i = line.indexOf(","); // If "CSV" file is invalid throw an exception
             if ((i == -1)) {
            	 //throw new IOException("File "+Nodes+" not a CSV (Building Name, id, x, y) file.");
             }
             lines = line.split(","); // Split the line, create a pair, add the pair to BuildingEdges
             Pair<String, String> edge = new Pair<String, String>(lines[0], lines[1]);
             BuildingEdges.add(edge);
        }
        reader.close();
    }
}
