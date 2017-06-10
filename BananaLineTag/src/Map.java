import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class Map implements Serializable {
	
	private static final long serialVersionUID = -5245572493686594653L;

	public static BufferedImage generateIcon(Map map, int size) {
		BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		g.setColor(new Color(map.r, map.g, map.b));
		g.fillRect(0, 0, size, size);
		g.setColor(Color.black);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Stroke s = g2.getStroke();
		
		for (Line l : map.lines) {
			int cap = l.round ? BasicStroke.CAP_ROUND : BasicStroke.CAP_SQUARE;
			g2.setStroke(new BasicStroke(Math.max((int) ((size + size) / 2 * l.thickness), 1), cap, BasicStroke.JOIN_ROUND));
			g.setColor(new Color(l.r, l.g, l.b, l.a));
			g.drawLine((int) (size * l.x1), (int) (size * l.y1), (int) (size * l.x2), (int) (size * l.y2));
		}
		g2.setStroke(s);
		return image;
	}
	
	ArrayList<Line> lines = new ArrayList<Line>();
	
	int r = 255;
	int g = 255;
	int b = 255;
}

class Line implements Serializable {
	
	private static final long serialVersionUID = -2263006956721187930L;

	public Line() {}
	
	public Line(double x1, double y1, double x2, double y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
	
	
	double x1 = 0;
	double y1 = 0;
	
	double x2 = 0;
	double y2 = 0;

	int r = 255;
	int g = 255;
	int b = 255;
	int a = 255;
	
	boolean round = false;
	
	double thickness = 0.01;
	
}