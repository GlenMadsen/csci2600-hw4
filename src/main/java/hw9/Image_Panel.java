package hw9;

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

public class Image_Panel extends JPanel{
	private static final long serialVersionUID = -399264903588448546L;
	private BufferedImage image;
	private Image drawn_image;
	double Zoom_Level;
	final static int ORG_H = 2050; //3400??
	final static int ORG_W = 2175;
	int width;
	int height;
	boolean lines;
	ArrayList<Pair<Double, Double>> points;
	public Image_Panel(CampusModel CampusMap) {
		try {                
			image = ImageIO.read(new File("data/RPI_campus_map_2010_extra_nodes_edges.png"));
			image = image.getSubimage(0, 0, 2175, 2050);
			Zoom_Level = 1.0;
			drawn_image = image;
			lines = false;
			points = null;
		} 
		catch (IOException ex) {}	
	}
	public void Initialize()
	{
		drawn_image = image.getScaledInstance(width, height, image.SCALE_SMOOTH);
	}
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		width = Math.min(this.getWidth(), this.getHeight());
		height = Math.min(this.getWidth(), this.getHeight());
        int total_width = (int)(width - 2*(width/2 - (width/2)*(1/Zoom_Level)));
        int total_height = (int)(height - 2*(height/2 - (height/2)*(1/Zoom_Level)));
        g.drawImage(drawn_image, 0, 0, Math.min(total_width, total_height),
            Math.min(total_width, total_height),this);
		if(lines)
		{
			Scale_to_Image(total_width, total_height, ORG_W, ORG_H);
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.BLUE);
            g2.setStroke(new BasicStroke(5));
            for(int i = 1; i < points.size(); i++)
			{
            	//System.out.println(points.get(i-1).getKey().intValue() 
            	//		+ "   " + points.get(i).getKey().intValue());
				g.drawLine(points.get(i-1).getKey().intValue(), points.get(i-1).getValue().intValue(), 
						points.get(i).getKey().intValue(), points.get(i).getValue().intValue());
			}
          g2.drawOval(points.get(0).getKey().intValue()-5, 
        		  points.get(0).getValue().intValue()-5, 10, 10);
          g2.drawOval((points.get(points.size()-1).getKey()).intValue()-5, 
        		  points.get(points.size()-1).getValue().intValue()-5, 10, 10);
          lines = false;
		}
		}
	public void Decrease_Zoom()
	{
		if(Zoom_Level > 0)
		{
			Zoom_Level *= 1.1;
		}
	}
	public void Increase_Zoom()
	{
		if(Zoom_Level > 0)
		{
			Zoom_Level = Zoom_Level/1.1;
		}
	}
	public void Draw_lines(ArrayList<Pair<Double, Double>> points)
	{
		this.points = points;
		lines = true;
	}
	public void Scale_to_Image(int width, int height, int original_w, int original_h)
	{
		ArrayList<Pair<Double, Double>> new_points = new ArrayList<Pair<Double, Double>>();
		Pair<Double, Double> new_point;
		for(int i = 0; i < points.size(); i++)
		{
			new_point = new Pair<Double, Double>
			(points.get(i).getKey()*((float)width/ORG_W),points.get(i).getValue()*((float)height/ORG_H));
			new_points.add(new_point);
			//System.out.println(ORG_W);
		}
		points = new_points;
	}
	public void Fit_Zoom(int x1, int y1, int x2, int y2)
	{
		drawn_image = image.getSubimage(Math.min(x1,x2), Math.min(y1,y2),
				Math.min(Math.abs(x2-x1), Math.abs(y2-y1)),Math.min(Math.abs(x2-x1), Math.abs(y2-y1))); 
		drawn_image = drawn_image.getScaledInstance((int)(Math.abs(x1-x2)),(int)(Math.abs(y1-y2)), 
				drawn_image.SCALE_SMOOTH);

	}
	public void Reset_Zoom()
	{
		Zoom_Level = 1.0;
		drawn_image = image;
		lines = false;
	}
}