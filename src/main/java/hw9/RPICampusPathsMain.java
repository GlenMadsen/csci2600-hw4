package hw9;

/*
 * GridBagLayoutDemo.java requires no other files.
 */
 
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import hw7.CampusModel;
import hw7.CampusView;
import javafx.util.Pair;
 
public class RPICampusPathsMain {
    final static boolean shouldFill = true;
    final static boolean shouldWeightX = true;
    final static boolean RIGHT_TO_LEFT = false;
 
    public static void addComponentsToPane(Container pane,  CampusModel CampusMap) 
    {
    	String HelpText = "To Calculate a Path Perform the Following Steps:\n"
 		       + "1. Input your starting location in the left-most textfield either by typing the building id,"
 		       + "building name, or by selecting it from the drop down menu. If selected from the drop down"
 		       + "menu, be sure to press 'Select'.\n 2. Do the same for your destination in the right-most"
 		       + "box.\n Press Calculate to calculate the path. If you wish to exit press 'Cancel'.";
 	
    	String[] choices = CampusView.B_Command(CampusMap).split("\n");
//    	for(int i = 0; i < choices.length; i++)
//    	{
//    		choices[i] = choices[i].substring(0, choices[i].indexOf(","));
//    	}

    	if (RIGHT_TO_LEFT) {
    		pane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
    	}

    	pane.setLayout(new GridBagLayout());
    	GridBagConstraints c = new GridBagConstraints();
    	if (shouldFill) {
    		//natural height, maximum width
    		c.fill = GridBagConstraints.HORIZONTAL;
    	}

    	JTextField Start = new JTextField(6);
    	if (shouldWeightX) {
    		c.weightx = 0.5;
    	}
    	c.fill = GridBagConstraints.HORIZONTAL;
    	c.gridx = 0;
    	c.gridy = 1;
    	pane.add(Start, c);

    	JTextField End = new JTextField(6);
    	c.fill = GridBagConstraints.HORIZONTAL;
    	c.weightx = 0.5;
    	c.gridx = 1;
    	c.gridy = 1;
    	pane.add(End, c);

    	JLabel titleLabel = new JLabel("RPI Campus Map Path-Finder", SwingConstants.CENTER);
    	titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 24));
    	c.fill = GridBagConstraints.HORIZONTAL;
    	c.weightx = .9;
    	c.gridx = 0;
    	c.gridy = 0;
    	pane.add(titleLabel, c);


    	final JComboBox<String> Starting = new JComboBox<String>(choices);
    	if (shouldWeightX) {
    		c.weightx = 0.5;
    	}
    	c.fill = GridBagConstraints.HORIZONTAL;
    	c.gridx = 0;
    	c.gridy = 2;
    	pane.add(Starting, c);
    	
    	final JComboBox<String> Ending = new JComboBox<String>(choices);
    	c.fill = GridBagConstraints.HORIZONTAL;
    	c.gridx = 1;
    	c.gridy = 2;
    	pane.add(Ending, c);

		JButton Select_Start = new JButton("Select Start");
    	if (shouldWeightX) {
    		c.weightx = 0.5;
    	}
    	c.fill = GridBagConstraints.HORIZONTAL;
    	c.gridx = 0;
    	c.gridy = 3;
    	pane.add(Select_Start, c);
    	Select_Start.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) {
				String choice = 
						choices[Starting.getSelectedIndex()].substring(0, 
								choices[Starting.getSelectedIndex()].indexOf(","));
				Start.setText(choice);
			}
		});
    	
		JButton Select_End = new JButton("Select Destination");
    	c.fill = GridBagConstraints.HORIZONTAL;
    	c.gridx = 1;
    	c.gridy = 3;
    	pane.add(Select_End, c);
		Select_End.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) {
				String choice = 
						choices[Ending.getSelectedIndex()].substring(0, 
								choices[Ending.getSelectedIndex()].indexOf(","));
				End.setText(choice);
			}
		});
		
		Image_Panel i_panel = new Image_Panel(CampusMap);

		JButton Fit_Zoom = new JButton("Fit Line");
		if (shouldWeightX) {
    		c.weightx = 0.5;
    	}
    	c.fill = GridBagConstraints.HORIZONTAL;
    	c.gridx = 0;
    	c.gridwidth = 2;   //2 columns wide

    	c.gridy = 6;
    	pane.add(Fit_Zoom, c);
    	Fit_Zoom.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) {
				i_panel.Fit_Zoom(1500,1200,900,800);
				i_panel.repaint();
			}
		});
    	
		JButton Cancel = new JButton("Cancel");
        c.weightx = 0.5;
    	c.fill = GridBagConstraints.HORIZONTAL;
    	c.gridx = 2;
    	c.gridwidth = 2;   //2 columns wide

    	c.gridy = 6;
    	pane.add(Cancel, c);
    	Cancel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
    	
		JButton Help = new JButton("?");
		c.weightx = 0.1;
    	c.fill = GridBagConstraints.HORIZONTAL;
    	c.gridx = 2;
    	c.gridy = 0;
    	pane.add(Help, c);
    	Help.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) {
		        JOptionPane.showMessageDialog(null, HelpText, "Help Box: " + "Help", JOptionPane.INFORMATION_MESSAGE);
			}
		});
    	
    	
    	c.fill = GridBagConstraints.BOTH;
    	//c.ipadx = 1024;      //make this component tall
    	c.ipady = 560;
    	c.weightx = 1.0;
    	c.gridwidth = 5;
    	c.gridx = 0;
    	c.gridy = 4;
    	c.insets = new Insets(0,55,0,0);  //top padding
    	pane.add(i_panel, c);
    	
    	JButton Calculate = new JButton("Calculate");
    	c.fill = GridBagConstraints.HORIZONTAL;
    	c.ipady = 0;       //reset to default
    	//c.weighty = 1.0;   //request any extra vertical space
    	//c.anchor = GridBagConstraints.PAGE_END; //bottom of space
    	c.insets = new Insets(0,0,0,0);  //top padding
    	c.gridx = 0;       //aligned with button 2
    	c.gridwidth = 2;
    	c.gridy = 5;       //third row
    	pane.add(Calculate, c);
    	Calculate.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) {
				String Path = CampusView.R_Command(Start.getText(), End.getText(), CampusMap);
				String[] Lines = Path.split("\n");
				String temp;
				Pair<Double, Double> placeholder;
				ArrayList<Pair<Double, Double>> points = new ArrayList<Pair<Double, Double>>();
				placeholder = new Pair<Double, Double>
				(CampusMap.get_Coord(CampusMap.get_ID(Start.getText())).getKey(),
						CampusMap.get_Coord(CampusMap.get_ID(Start.getText())).getValue());				
				points.add(placeholder);
				for(int i = 1; i < Lines.length-1; i++)
				{
					temp = Lines[i].substring(Lines[i].indexOf("(")+1, Lines[i].indexOf(")"));
					if(temp.contains("Intersection"))
					{
						temp = temp.substring(temp.indexOf(" ")+1);
					}
					//placeholder = CampusMap.get_Coord(CampusMap.get_ID(temp));
					placeholder = new Pair<Double, Double>
					(CampusMap.get_Coord(CampusMap.get_ID(temp)).getKey(),
							CampusMap.get_Coord(CampusMap.get_ID(temp)).getValue());
					points.add(placeholder);
				}
				i_panel.Draw_lines(points);
				i_panel.repaint();
				//System.out.println(Path);
				//System.out.println("Calculating Route with: " + Start.getText() + " and " + End.getText());
			}
		});
    	
    	JButton Reset = new JButton("Reset");
    	c.fill = GridBagConstraints.HORIZONTAL;
    	c.ipady = 0;       //reset to default
    	//c.weighty = 1.0;   //request any extra vertical space
    	//c.anchor = GridBagConstraints.PAGE_END; //bottom of space
    	//c.insets = new Insets(10,0,0,0);  //top padding
    	c.gridx = 2;       //aligned with button 2
    	c.gridy = 5;       //third row
    	pane.add(Reset, c);
    	Reset.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) {
				i_panel.Reset_Zoom();
				i_panel.repaint();
				Start.setText("");
				End.setText("");
				Starting.setSelectedIndex(0);
				Ending.setSelectedIndex(0);
				i_panel.Initialize();
			}
		});
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
    	//Create and set up the window.
    	JFrame frame = new JFrame("GridBagLayoutDemo");
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.setPreferredSize(new Dimension(768,768));;
    	String nodes_file = "data/RPI_map_data_Nodes.csv";//Hardcode in the files since that was asked
    	String edge_file = "data/RPI_map_data_Edges.csv";
    	CampusModel CampusMap = new CampusModel(); // Makes a new object and creates the graph
    	CampusMap.createNewGraph(nodes_file, edge_file);

    	//Set up the content pane.
    	addComponentsToPane(frame.getContentPane(), CampusMap);

    	//Display the window.
    	frame.pack();
    	frame.setVisible(true);
    }

    public static void main(String[] args) {
    	//Schedule a job for the event-dispatching thread:
    	//creating and showing this application's GUI.
    	createAndShowGUI();
    }
}