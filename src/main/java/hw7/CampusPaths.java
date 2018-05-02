package hw7;
import hw7.CampusView;
import java.util.Scanner;

//This class does not represent an ADT
public class CampusPaths {
    public static void main(String args[]) // Main Method, also the controller
	{
    	String nodes_file;
    	String edge_file;
    	if(args != null)
    	{
    		nodes_file = args[0];// Take other files for arguments so I can test other files
    		edge_file = args[1];
    	}
    	else
    	{
    		nodes_file = "data/RPI_map_data_Nodes.csv";//Hardcode in the files since that was asked
    		edge_file = "data/RPI_map_data_Edges.csv";
    	}
		CampusModel CampusMap = new CampusModel(); // Makes a new object and creates the graph
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