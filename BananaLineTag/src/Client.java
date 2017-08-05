import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JFrame;

public class Client extends Thread {
	
	public static void main(String[] args) {
		new Client().start();
	}
	
	Socket connection;
	
	DataInputStream in;
	DataOutputStream out;
	
	ClientInputThread cit;
	
	Map map;
	
	Player self = new Player();
	ArrayList<Player> players = new ArrayList<Player>();
	
	SquarePanel sp;
	Display d;
	double stamina = 1;
	
	boolean[] keys = new boolean[65535];
	
	public void run() {
		while (true) {
			login();//open login window and wait to be connected to a server
			
			initInput();
			
			initFrame();
			
			try {
				//// client tick loop
				while (true) {
					
					d.repaint(50L);
					d.stamina = stamina;
					
					handleMovement();
					
					try {
						Thread.sleep(50);
					} catch (Exception e) {
					}
				}
			} catch (Exception e) {
			
			}
		}
		
	}
	
	private void initFrame() {
		JFrame frame = new JFrame();
		frame.setSize(500, 500);
		sp = new SquarePanel();
		d = new Display(map, players, self);
		sp.display.add(d);
		frame.add(sp);
		frame.setVisible(true);
		sp.setBackground(new Color(map.r, map.g, map.b));
		
		sp.setFocusable(true);
		sp.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				keys[e.getKeyCode()] = true;
			}
			
			public void keyTyped(KeyEvent e) {}
			
			public void keyReleased(KeyEvent e) {
				keys[e.getKeyCode()] = false;
			}
		});
		d.addMouseListener(new MouseListener() {
			
			public void mouseClicked(MouseEvent e) {}
			
			public void mousePressed(MouseEvent e) {
				clickPlayer(d.getPlayerForClick(e.getX(), e.getY()));
			}
			
			public void mouseReleased(MouseEvent e) {}
			
			public void mouseEntered(MouseEvent e) {}
			
			public void mouseExited(MouseEvent e) {}
			
		});
	}
	
	public void clickPlayer(Player p) {
		if (p != null) {
		
		}
	}
	
	/**
	 * Check for any keys being pressed, and tell the server is the player moved.
	 * 
	 * @throws IOException if the stream to the server is closed
	 */
	private void handleMovement() throws IOException {
		boolean moved = false;
		boolean wasOnMap = map.onMap(self);
		double wasX = self.x;
		double wasY = self.y;
		
		//// check keys
		
		if (keys[KeyEvent.VK_W] || keys[KeyEvent.VK_UP]) {
			moved = true;
			self.y -= 0.003 + 0.004 * stamina;
		}
		if (keys[KeyEvent.VK_A] || keys[KeyEvent.VK_LEFT]) {
			moved = true;
			self.x -= 0.003 + 0.004 * stamina;
		}
		if (keys[KeyEvent.VK_S] || keys[KeyEvent.VK_DOWN]) {
			moved = true;
			self.y += 0.003 + 0.004 * stamina;
		}
		if (keys[KeyEvent.VK_D] || keys[KeyEvent.VK_RIGHT]) {
			moved = true;
			self.x += 0.003 + 0.004 * stamina;
		}
		
		//// handle movement by forcing player to stay on map
		
		if (moved) {
			if (map.onMap(self)) {
				out.writeInt(MOVE);
				out.writeDouble(self.x);
				out.writeDouble(self.y);
				stamina = Math.max(stamina - 0.015, 0);
			} else if (wasOnMap) {
				double toX = self.x;
				self.x = wasX;
				if (map.onMap(self)) {
					out.writeInt(MOVE);
					out.writeDouble(self.x);
					out.writeDouble(self.y);
					return;
				}
				self.x = toX;
				self.y = wasY;
				if (map.onMap(self)) {
					out.writeInt(MOVE);
					out.writeDouble(self.x);
					out.writeDouble(self.y);
					return;
				}
				self.x = wasX;
				
			}
		} else {
			stamina = Math.min(stamina + 0.1, 1);
		}
		
	}
	
	/**
	 * Starts the ClientInputThread and waits for a Map to be received, then returns.
	 */
	public void initInput() {
		cit = new ClientInputThread(this);
		cit.start();
		while (map == null) {
			try {
				Thread.sleep(50);
			} catch (Exception e) {
			}
		}
	}
	
	/**
	 * Opens the Login Window and returns once the user is connected to a server
	 */
	public void login() {
		LoginWindow lw = new LoginWindow();
		lw.ipAddress.setText("localhost");
		lw.port.setText("43056");
		lw.switchToConnectMode();
		lw.setVisible(true);
		lw.setSize(400, 200);
		while (true) {
			try {
				while (!lw.connectPressed) {
					Thread.sleep(100);
				}
				Socket s = new Socket(lw.ipAddress.getText(), Integer.parseInt(lw.port.getText()));
				in = new DataInputStream(s.getInputStream());
				out = new DataOutputStream(s.getOutputStream());
				lw.switchToLoginMode();
				while (!lw.loginPressed) {
					Thread.sleep(100);
				}
				out.writeInt(SET_USERNAME);
				out.writeUTF(lw.username.getText());
				self.name = lw.username.getText();
				connection = s;
				break;
				
			} catch (Exception e) {
				lw.setMessage(e.getMessage());
			}
			lw.connectPressed = false;
		}
		lw.setVisible(false);
	}
	
	/** String name */
	public static final int SET_USERNAME = 0;
	/** int length, byte[length] Utils.imageToBytes(image) */
	public static final int SET_ICON = 1;
	/** double x, double y */
	public static final int MOVE = 2;
	
}
