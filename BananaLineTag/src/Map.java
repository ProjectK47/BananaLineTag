import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;

public class Map implements Serializable {
	
	private static final long serialVersionUID = -5245572493686594653L;
	
	/**
	 * Draws a Map onto a BufferedImage for use as an icon.
	 * 
	 * @param map the Map to draw
	 * @param size the size at which to draw it
	 * @return the image
	 */
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
	
	/**
	 * Checks if a player is on the map. <br>
	 * (On a line, and on the screen)
	 * 
	 * @param p the Player to check
	 * @return if they are standing in a legal location
	 */
	public boolean onMap(Player p) {
		double s = Player.PLAYER_SIZE;
		if (p.x > 1 + s || p.x < 0 - s || p.y > 1 + s || p.y < 0 - s) {
			return false;
		}
		for (Line l : lines) {
			if (Utils.distanceToLineSegment(p.x, p.y, l.x1, l.y1, l.x2, l.y2) <= l.thickness + s) {
				return true;
			}
		}
		return false;
	}
	
	ArrayList<Line> lines = new ArrayList<Line>();
	
	/**
	 * Background color. Render only effect.
	 */
	int r = 255;
	int g = 255;
	int b = 255;
	
}

/**
 * Defines a line with arbitrary scale.
 */
class Line implements Serializable {
	
	private static final long serialVersionUID = -2263006956721187930L;
	
	/**
	 * Create a line from (0, 0) to (0, 0)
	 */
	public Line() {}
	
	/**
	 * Create a line from (x1, y1) to (x2, y2)
	 * 
	 * @param x1 the start x
	 * @param y1 the start y
	 * @param x2 the end x
	 * @param y2 the end y
	 */
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
	
	/**
	 * Line r g b a
	 * <p>
	 * Render only effect.
	 */
	int r = 255;
	int g = 255;
	int b = 255;
	int a = 255;
	
	/**
	 * Whether or not to round the line ends.
	 * <p>
	 * Render only effect.
	 */
	boolean round = false;
	
	double thickness = 0.01;
	
}