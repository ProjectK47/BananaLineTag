import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;

import javax.swing.JPanel;

public class Display extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	Map map;
	
	ArrayList<Player> players;
	
	Player self;
	
	public Display(Map m, ArrayList<Player> ps, Player self) {
		map = m;
		players = ps;
		this.self = self;
	}
	
	protected void paintComponent(Graphics g) {
		
		//// setup
		
		g.setColor(new Color(map.r, map.g, map.b));
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.black);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Stroke s = g2.getStroke();
		
		//// draw map
		
		for (Line l : map.lines) {
			int cap = l.round ? BasicStroke.CAP_ROUND : BasicStroke.CAP_SQUARE;
			g2.setStroke(new BasicStroke(Math.max((int) ((getWidth() + getHeight()) / 2 * l.thickness), 1), cap, BasicStroke.JOIN_ROUND));
			g.setColor(new Color(l.r, l.g, l.b, l.a));
			g.drawLine((int) (getWidth() * l.x1), (int) (getHeight() * l.y1), (int) (getWidth() * l.x2), (int) (getHeight() * l.y2));
		}
		
		g2.setStroke(s); //// reset stroke
		
		//// draw players
		
		// setup
		
		g.setFont(new Font("Copperplate", Font.PLAIN, (int) (getHeight() * Player.PLAYER_SIZE)));
		g.setColor(Color.black);
		FontMetrics fm = g.getFontMetrics();
		
		g2.setStroke(new BasicStroke(Math.round(getHeight() * 0.005)));
		
		for (Player p : players) {
			
			// draw one player
			
			int width = (int) (getWidth() * Player.PLAYER_SIZE);
			int height = (int) (getHeight() * Player.PLAYER_SIZE);
			
			int x = (int) (getWidth() * p.oldx) - width / 2;
			int y = (int) (getHeight() * p.oldy) - height / 2;
			
			g.drawImage(p.icon, x, y, width, height, null);
			
			// draw range
			
			if (p.state == Player.STATE_UP) {
				g.setColor(new Color(0, 127, 255, 127));
				
				g.drawOval(x + width / 2 - width * 2, y + height / 2 - height * 2, width * 4, height * 4);
			}
			
			// draw label
			
			x = (int) (getWidth() * p.oldx) - fm.stringWidth(p.name) / 2;
			y = (int) (getHeight() * p.oldy) - height / 2 - fm.getDescent();
			if (p.state == Player.STATE_UP) {
				g.setColor(Color.BLACK);
			} else if (p.state == Player.STATE_DOWN) {
				g.setColor(new Color(0, 0, 0, 127));
			} else if (p.state == Player.STATE_RPS) {
				g.setColor(new Color(0, 0, 127, 127));
			}
			g.drawString(p.name, x, y);
			
			p.oldx = (p.x + p.oldx + p.oldx) / 3;
			p.oldy = (p.y + p.oldy + p.oldy) / 3;
			
		}
		
		//// draw self
		
		g2.setStroke(new BasicStroke(Math.round(getHeight() * 0.005)));
		
		int width = (int) (getWidth() * Player.PLAYER_SIZE);
		int height = (int) (getHeight() * Player.PLAYER_SIZE);
		
		int x = (int) (getWidth() * self.oldx) - width / 2;
		int y = (int) (getHeight() * self.oldy) - height / 2;
		
		g.drawImage(self.icon, x, y, width, height, null);
		
		// draw range
		
		g.setColor(new Color(0, 255, 0, 127));
		
		g.drawOval(x + width / 2 - width * 2, y + height / 2 - height * 2, width * 4, height * 4);
		
		// draw label
		
		x = (int) (getWidth() * self.oldx) - fm.stringWidth(self.name) / 2;
		y = (int) (getHeight() * self.oldy) - height / 2 - fm.getDescent();
		g.setColor(Color.GREEN);
		
		g.drawString(self.name, x, y);
		
		self.oldx = (self.x + self.oldx) / 2;
		self.oldy = (self.y + self.oldy) / 2;
		
	}
	
	public Player getPlayerForClick(int cx, int cy) {
		for (Player p : players) {
			
			// get x y width and height
			
			int width = (int) (getWidth() * Player.PLAYER_SIZE);
			int height = (int) (getHeight() * Player.PLAYER_SIZE);
			
			int x = (int) (getWidth() * p.oldx) - width / 2;
			int y = (int) (getHeight() * p.oldy) - height / 2;
			
			if (cx > x && cx < x + width && cy > y && cy < y + height) {
				return p;
			}
			
		}
		return null;
	}
	
}

//class Point {
//	double x;
//	double y;
//}

class SquarePanel extends JPanel implements ComponentListener {
	
	private static final long serialVersionUID = 1L;
	
	public SquarePanel() {
		this.addComponentListener(this);
		this.add(display);
	}
	
	public void componentResized(ComponentEvent e) {
		double widthScaleFactor = (double) (this.getWidth()) / 1;
		double heightScaleFactor = (double) (this.getHeight()) / 1;
		double scaleFactor = (widthScaleFactor > heightScaleFactor ? heightScaleFactor : widthScaleFactor);
		
		int drawWidth = 0;
		int drawHeight = 0;
		
		drawWidth = (int) (scaleFactor * 1);
		
		drawHeight = (int) (scaleFactor * 1);
		
		//		if (widthScaleFactor > heightScaleFactor) {
		//			drawWidth = ((int) (heightScaleFactor * 480)) / (int) (44 * heightScaleFactor) * (int) (44 * heightScaleFactor);
		//			drawHeight = ((int) (heightScaleFactor * 360)) / (int) (44 * heightScaleFactor) * (int) (44 * heightScaleFactor);
		//		} else {
		//			drawWidth = ((int) (widthScaleFactor * 480)) / (int) (44 * widthScaleFactor) * (int) (44 * widthScaleFactor);
		//			drawHeight = ((int) (widthScaleFactor * 360)) / (int) (44 * widthScaleFactor) * (int) (44 * widthScaleFactor);
		//		}
		display.setBounds((this.getWidth()) / 2 - drawWidth / 2, (this.getHeight()) / 2 - drawHeight / 2, drawWidth, drawHeight);
		try {
			Component c = display.getComponent(0);
			c.setBounds(0, 0, drawWidth, drawHeight);
		} catch (Exception ex) {
		
		}
	}
	
	public void componentMoved(ComponentEvent e) {}
	
	public void componentShown(ComponentEvent e) {}
	
	public void componentHidden(ComponentEvent e) {}
	
	JPanel display = new JPanel();
	
}
