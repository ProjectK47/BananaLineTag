import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
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
	
	boolean[] keys = new boolean[65535];
	
	public void run() {
		
		login();
		
		initInput();
		
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
		try {
			while (true) {
				d.repaint(50L);
				boolean moved = false;
				if (keys[KeyEvent.VK_W]) {
					moved = true;
					self.y -= 0.005;
				}
				if (keys[KeyEvent.VK_A]) {
					moved = true;
					self.x -= 0.005;
				}
				if (keys[KeyEvent.VK_S]) {
					moved = true;
					self.y += 0.005;
				}
				if (keys[KeyEvent.VK_D]) {
					moved = true;
					self.x += 0.005;
				}
				if (moved) {
					out.writeInt(MOVE);
					out.writeDouble(self.x);
					out.writeDouble(self.y);
				}
				try {
					Thread.sleep(50);
				} catch (Exception e) {
				}
			}
		} catch (Exception e) {
		
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
				out.writeInt(MOVE);
				out.writeDouble(1);
				out.writeDouble(1);
				self.x = 1;
				self.y = 1;
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
