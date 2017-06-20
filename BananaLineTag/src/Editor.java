import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Editor extends JPanel implements MouseMotionListener, MouseListener {
	
	private static final long serialVersionUID = 1L;
	
	public static void main(String[] args) {
		JFileChooser fc = new JFileChooser();
		JFrame frame = new JFrame();
		Editor e = new Editor();
		SquarePanel sp = new SquarePanel();
		sp.display.add(e);
		frame.add(sp);
		frame.setVisible(true);
		JDialog options = new JDialog(frame);
		options.add(e.options);
		options.setVisible(true);
		options.setSize(e.options.getPreferredSize());
		frame.setSize(e.options.getPreferredSize());
		fc.showOpenDialog(frame);
		try {
			e.template = ImageIO.read(fc.getSelectedFile());
		} catch (Exception ex) {
		
		}
		e.updateSettings();
		
	}
	
	Map map = new Map();
	
	double startX = 0;
	double startY = 0;
	double atX = 0;
	double atY = 0;
	double thickness = 0.01;
	Color c = new Color(0, 0, 0);
	boolean round = false;
	
	boolean dragging = false;
	
	BufferedImage template;
	
	JPanel options = new JPanel();
	
	JColorChooser background = new JColorChooser();
	JColorChooser newLine = new JColorChooser();
	JCheckBox roundedLines = new JCheckBox();
	JSlider slider = new JSlider(1, 150);
	
	/**
	 * Creates a new editor
	 */
	public Editor() {
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		JTabbedPane tp = new JTabbedPane();
		
		JPanel cap = new JPanel();
		cap.setLayout(new BoxLayout(cap, BoxLayout.Y_AXIS));
		cap.add(new JLabel("Rounded line ends"));
		cap.add(roundedLines);
		cap.add(new JLabel("Thickness"));
		cap.add(slider);
		((JButton) cap.add(new JButton("Save"))).addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.showSaveDialog(Editor.this);
				File file = fc.getSelectedFile();
				try {
					DataOutputStream out = new DataOutputStream(new FileOutputStream(file));
					byte[] b = Utils.serCompress(map);
					out.writeInt(b.length);
					out.write(b);
					out.close();
				} catch (Exception ex) {
				
				}
			}
			
		});
		((JButton) cap.add(new JButton("Open"))).addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.showOpenDialog(Editor.this);
				File file = fc.getSelectedFile();
				try {
					DataInputStream in = new DataInputStream(new FileInputStream(file));
					byte[] b = new byte[in.readInt()];
					in.read(b);
					Map m = (Map) Utils.deserDecompress(b);
					map = m;
					background.setColor(new Color(m.r, m.g, m.b));
					in.close();
					
					updateSettings();
					repaint();
				} catch (Exception ex) {
				
				}
			}
			
		});
		
		tp.addTab("Background Color", background);
		tp.addTab("New Line Color", newLine);
		tp.addTab("Options", cap);
		
		options.add(tp);
		background.setColor(Color.white);
		newLine.setColor(Color.BLACK);
		
		background.getSelectionModel().addChangeListener(new ChangeListener() {
			
			public void stateChanged(ChangeEvent e) {
				updateSettings();
				repaint();
			}
			
		});
		this.setFocusable(true);
		this.addKeyListener(new KeyListener() {
			
			public void keyTyped(KeyEvent e) {}
			
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
					if (map.lines.size() > 0) {
						map.lines.remove(map.lines.size() - 1);
						updateSettings();
						repaint();
					}
				}
			}
			
			public void keyReleased(KeyEvent e) {}
			
		});
		
	}
	/**
	 * Resets the current draw info to what is selected in the chooser panel.
	 */
	public void updateSettings() {
		round = roundedLines.isSelected();
		c = newLine.getColor();
		Color back = background.getColor();
		map.r = back.getRed();
		map.g = back.getGreen();
		map.b = back.getBlue();
		thickness = slider.getValue() / 1000.0;
		
		Container c = SwingUtilities.getAncestorOfClass(SquarePanel.class, this);
		if (c != null) {
			c.setBackground(back);
		}
	}
	/**
	 * Renders the current map, along with any other lines that are being drawn.
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	protected void paintComponent(Graphics g) {
		g.setColor(new Color(map.r, map.g, map.b));
		g.fillRect(0, 0, getWidth(), getHeight());
		g.drawImage(template, 0, 0, getWidth(), getHeight(), null);
		g.setColor(Color.black);
		g.drawRect(0, 0, getWidth(), getHeight());
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Stroke s = g2.getStroke();
		
		for (Line l : map.lines) {
			int cap = l.round ? BasicStroke.CAP_ROUND : BasicStroke.CAP_SQUARE;
			g2.setStroke(new BasicStroke(Math.max((int) ((getWidth() + getHeight()) / 2 * l.thickness), 1), cap, BasicStroke.JOIN_ROUND));
			g.setColor(new Color(l.r, l.g, l.b, l.a));
			g.drawLine((int) (getWidth() * l.x1), (int) (getHeight() * l.y1), (int) (getWidth() * l.x2), (int) (getHeight() * l.y2));
		}
		
		if (dragging) {
			int cap = round ? BasicStroke.CAP_ROUND : BasicStroke.CAP_SQUARE;
			g2.setStroke(new BasicStroke(Math.max((int) ((getWidth() + getHeight()) / 2 * thickness), 1), cap, BasicStroke.JOIN_BEVEL));
			g.setColor(c);
			g.drawLine((int) (getWidth() * startX), (int) (getHeight() * startY), (int) (getWidth() * atX), (int) (getHeight() * atY));
		}
		g2.setStroke(s);
	}
	/**
	 * Start new line.
	 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 */
	public void mouseDragged(MouseEvent e) {
		updateSettings();
		if (!dragging) {
			dragging = true;
			startX = (double) e.getX() / getWidth();
			startY = (double) e.getY() / getHeight();
		}
		atX = (double) e.getX() / getWidth();
		atY = (double) e.getY() / getHeight();
		repaint();
	}
	/**
	 * End new line.
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) {
		updateSettings();
		if (dragging) {
			dragging = false;
			Line l = new Line(startX, startY, (double) e.getX() / getWidth(), (double) e.getY() / getHeight());
			l.r = c.getRed();
			l.g = c.getGreen();
			l.b = c.getBlue();
			l.a = c.getAlpha();
			l.round = round;
			l.thickness = thickness;
			map.lines.add(l);
			repaint();
		}
	}
	
	public void mouseMoved(MouseEvent e) {}
	
	public void mouseClicked(MouseEvent e) {}
	
	public void mousePressed(MouseEvent e) {}
	
	public void mouseEntered(MouseEvent e) {}
	
	public void mouseExited(MouseEvent e) {}
	
}
