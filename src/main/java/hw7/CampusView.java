package hw7;

import java.util.ArrayList;
import java.util.Scanner;

public class CampusView 
{
	public static String B_Command(CampusPaths CampusMap)
	{
		ArrayList<String> Buildings = new ArrayList<String>(CampusMap.Listing_Buildings());
		Buildings.sort(null);
		String next_B = "";
		for(int i = 0; i < Buildings.size(); i++)
		{
			next_B = next_B.concat(Buildings.get(i));
			next_B = next_B.concat(",");
			next_B = next_B.concat(CampusMap.get_ID(Buildings.get(i)));
			next_B = next_B.concat("\n");
		}
		return next_B;
	}
	public static String R_Command(String start, String end, CampusPaths CampusMap) 
	{
		System.out.println("First building id/name, followed by Enter: ");
		System.out.println("Second building id/name, followed by Enter: ");
		String path = "";
		// surround with an if and return
		if(!CampusMap.is_Building(start) || !CampusMap.is_Building(end))
		{
			if(start.equals(end))
			{
				return "Unknown building: [" + start + "]";
			}
			if(!CampusMap.is_Building(start) && !CampusMap.is_Building(end))
			{
    	    	path = path.concat("Unknown building: [" + start + "]" + "\n");
    	    	path = path.concat("Unknown building: [" + end + "]");
    	    	return path;
			}
			if(!CampusMap.is_Building(start))
			{
    	    	path = path.concat("Unknown building: [" + start + "]");
			}
			if(!CampusMap.is_Building(end))
			{
    	    	path = path.concat("Unknown building: [" + end + "]");
			}
			return path;
		}
		String temp1 = CampusMap.get_Building(start);
		String temp2 = CampusMap.get_Building(end);
		if(temp1.equals(temp2))
		{
			path = path.concat("Path from " + temp1 + " to " + temp2 + ":\n");
			path = path.concat("Total distance: 0.000 pixel units.");
			return path;
		}
		path = path.concat("Path from " + temp1 + " to " + temp2 + ":\n");
		String id1 = CampusMap.get_ID(start);
		String id2 = CampusMap.get_ID(end);
		ArrayList<String> P = new ArrayList<String>(CampusMap.findPath(id1, id2));
		double path_L = 0.00;
		if(P.isEmpty())
		{
			path = path.concat("There is no path from " +  temp1 + " to " + temp2 + ".");
			return path;
		}
		for(int i = 0; i < P.size(); i += 3)
		{
			if(CampusMap.is_Building(P.get(i+1)))
			{
				temp1 = "(".concat(CampusMap.get_Building(P.get(i+1))).concat(")");
			}
			if(!CampusMap.is_Building(P.get(i+1)))
			{
				temp1 = "(Intersection ".concat(CampusMap.get_ID(P.get(i+1))).concat(")");
			}
			path = path.concat("\tWalk " + CampusMap.find_Direction(P.get(i), P.get(i+1)) + " to " +  temp1 + "\n");
			path_L += Double.valueOf(P.get(i+2));
		}
		//path = path.concat("Total edge weight of: " + String.valueOf(path_L));
		path = path.concat(String.format("Total distance: %.3f pixel units.", path_L));
		return path;
	}
	public static String M_Command()
	{
		return "The Commands List: 'b', 'r', 'q', 'm'.";

	}
	private static String Other_Command() 
	{
		return "Unknown option\n";
	}
	public static void main(String args[])
	{
		String nodes_file = "data/RPI_map_data_Nodes.csv";
		String edge_file = "data/RPI_map_data_Edges.csv";
		CampusPaths CampusMap = new CampusPaths();
		CampusMap.createNewGraph(nodes_file, edge_file);
		String temp1;
		String temp2;
		String line;
        Scanner sc = new Scanner(System.in);
		while (true)
		{
			line = sc.nextLine();
			if(line.equals("b"))
			{
				System.out.print(B_Command(CampusMap));
			}
			if(line.equals("r"))
			{
				temp1 = sc.nextLine();
				temp2 = sc.nextLine();
				System.out.println(R_Command(temp1, temp2, CampusMap));
			}
			if(line.equals("q"))
			{
				break;
			}
			if(line.equals("m"))
			{
				System.out.println(M_Command());
			}
			if(!line.equals("b") && !line.equals("r") && !line.equals("q") && !line.equals("m"))
			{
				System.out.print(Other_Command());
			}
		}
		sc.close();
	}
}