package hw9;

// Lots of includes to do everything
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import hw7.CampusModel;
import javafx.util.Pair;


// We didn't have to write ADT specifications and Rep invariants so there are none
public class Image_Panel extends JPanel{
	private static final long serialVersionUID = -399264903588448546L;// I was getting a serial warning
	private BufferedImage image; // Save the original image saved, modify the other one to avoid
	private Image drawn_image;   // Having the image get distorted
	final static int ORG_H = 2050; // I always cut the image so I need to div. by 2050, not 3400
	final static int ORG_W = 2175; // These are used for scaling
	int width;
	int height;
	boolean lines; // Determines in PaintComponent Whether to draw lines/circles.
	boolean circle;
	ArrayList<Pair<Double, Double>> points; // Points to draw lines
	Pair<Double, Double> start; // Start/End Points
	Pair<Double, Double> end;
	/**
	 * @param CampusModel object
	 * @returns N/A
	 * @throws N/A
	 * @requires N/A
	 * @modifies image, drawn_image, lines, circle, points 
	 * @effects Reads in an image using a set fileline, also sets lines/circle to false and points,
	 * star, and end to null
	 */
	// Constructor for my ImagePanel
	public Image_Panel(CampusModel CampusMap) {
		try { // Try Catch in case reading in the image fails
			image = ImageIO.read(new File("data/RPI_campus_map_2010_extra_nodes_edges.png"));
			image = image.getSubimage(0, 0, 2175, 2050);
			drawn_image = image; // Image is cropped and copied, variables initialized
			lines = false;
			circle = false;
			points = null;
			start = null;
			end = null;
		} 
		catch (IOException ex) {ex.printStackTrace();}
	}
	/**
	 * @param N/A
	 * @returns N/A
	 * @throws N/A
	 * @requires N/A
	 * @effects drawn_image is reset to a scaled and smoothed version of the original
	 * @modifies drawn_image
	 */
	public void Initialize() // Resets the drawn_image back to the original one
	{ // The most important bit is the smoothing so it is more legible
		drawn_image = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
	}
	/**
	 * @param Graphics g
	 * @returns N/A
	 * @throws N/A
	 * @requires N/A
	 * @effects changes width/height to the minimum of either of the window sizes, also paints image
	 * @modifies width/height also the panel as it draws, follows paintComponent specs
	 */
	@Override // Main function for painting, its a callback
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
		width = Math.min(this.getWidth(), this.getHeight()); // Find the correct width/height
		height = Math.min(this.getWidth(), this.getHeight());
        g.drawImage(drawn_image, 0, 0, Math.min(width, height), 
        		Math.min(width, height),this); // Draws the image as a square
        g2.setStroke(new BasicStroke(5));      // Sets the stroke, line thickness
        if(lines)
        {
        	g2.setColor(Color.BLUE); // I selected blue for the contrast it would provide
        	for(int i = 1; i < points.size(); i++)
        	{ // Draws the lines to make the path
        		g.drawLine(points.get(i-1).getKey().intValue(), points.get(i-1).getValue().intValue(), 
        				points.get(i).getKey().intValue(), points.get(i).getValue().intValue());
        	}
        	g2.setColor(Color.GREEN); // Green to indicate start
        	g2.setColor(Color.RED);  // Red to indicate end
        	lines = false; // The lines need to disappear next time a path is selected.
        }
        if(start != null) // If a start point has been added, draw it
        {
        	g2.setColor(Color.GREEN); // Green to indicate start, draws a hollow circle
        	g2.drawOval(start.getKey().intValue()-5, 
        			start.getValue().intValue()-5, 10, 10);
        }
        if(end != null) // If a end point has been added, draw it
        {
        	g2.setColor(Color.RED); // Green to indicate start
        	g2.drawOval(end.getKey().intValue()-5, 
        			end.getValue().intValue()-5, 10, 10);
        }
	}
	/**
	 * @param Pair<Double, Double> start, the start point coordinates
	 * @returns N/A
	 * @throws N/A
	 * @requires N/A
	 * @effects sets circle to true and alters the start point by scaling it to the correct size
	 * @modifies start and cirlce
	 */
	public void Set_Start(Pair<Double, Double> start)
	{ // Takes a point and sets it to the start, also rescales the points values to image size
		circle = true;
		Pair<Double, Double> new_point = new Pair<Double, Double>
		(start.getKey()*((float)width/ORG_W),start.getValue()*((float)height/ORG_H));
		this.start = new_point;
	}
	/**
	 * @param Pair<Double, Double> end, the end point coordinates
	 * @returns N/A
	 * @throws N/A
	 * @requires N/A
	 * @effects sets circle to true and alters the start point by scaling it to the correct size
	 * @modifies end and cirlce
	 */
	public void Set_End(Pair<Double, Double> end)
	{ // Same as Set_Start but for the end
		circle = true;
		Pair<Double, Double> new_point = new Pair<Double, Double>
		(end.getKey()*((float)width/ORG_W),end.getValue()*((float)height/ORG_H));
		this.end = new_point;
	}
	/**
	 * @param ArrayList<Pair<Double, Double>> points, the set of all points in order
	 * @returns N/A
	 * @throws N/A
	 * @requires N/A
	 * @effects sets circle to true and alters the start point by scaling it to the correct size
	 * @modifies end and circle
	 */
	// Function used to tell the class it can draw these lines
	public void Draw_lines(ArrayList<Pair<Double, Double>> points)
	{
		this.points = points;
		lines = true;
    	Scale_to_Image(width, height, ORG_W, ORG_H);
	}
	/**
	 * @param int width, height, original_w, and orginal_h, all variables used for scaling purposes
	 * @returns N/A
	 * @throws N/A
	 * @requires N/A
	 * @effects alters all the points by scaling them to the correct size and 
	 * 			setting points = new_points
	 * @modifies points
	 */
	// Scales the lines to current image size
	private void Scale_to_Image(int width, int height, int original_w, int original_h)
	{
		ArrayList<Pair<Double, Double>> new_points = new ArrayList<Pair<Double, Double>>();
		Pair<Double, Double> new_point; // Makes new array of points
		for(int i = 0; i < points.size(); i++) // proceeds to scale all old points and put them in new
		{
		new_point = new Pair<Double, Double>(points.get(i).getKey()*((float)width/ORG_W),
				points.get(i).getValue()*((float)height/ORG_H));
		new_points.add(new_point);
		}
		points = new_points; // Sets the old points to the new points
	}
	/**
	 * @param N/A
	 * @returns N/A
	 * @throws N/A
	 * @requires N/A
	 * @effects sets drawn_image back to image, sets lines to false, start to null, and end to null
	 * @modifies lines, drawn_image, start, and end
	 */
	// Resets Image Panel back to original state except for smoothing
	public void Reset()
	{
		drawn_image = image;
		lines = false;
		start = null;
		end = null;
	}
	/**
	 * @param N/A
	 * @returns N/A
	 * @throws N/A
	 * @requires N/A
	 * @effects sets start to null
	 * @modifies start
	 */
	public void ResetStart() {
		start = null;	// Resets the start point so it isn't drawn
	}
	/**
	 * @param N/A
	 * @returns N/A
	 * @throws N/A
	 * @requires N/A
	 * @effects sets end to null
	 * @modifies end
	 */
	public void ResetEnd() {
		end = null;	// Resets the end point so it isn't drawn
	}
}