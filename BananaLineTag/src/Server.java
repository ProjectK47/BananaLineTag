import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JFileChooser;

public class Server extends Thread {
	
	public static void main(String[] args) {
		args = new String[] { "/users/leif/basketball court.bltm" };
		if (args.length == 0) {
			JFileChooser fc = new JFileChooser();
			fc.showOpenDialog(null);
			File file = fc.getSelectedFile();
			try {
				DataInputStream in = new DataInputStream(new FileInputStream(file));
				byte[] b = new byte[in.readInt()];
				in.read(b);
				Map m = (Map) Utils.deserDecompress(b);
				in.close();
				new Server(m).start();
			} catch (Exception ex) {
			
			}
		} else {
			File file = new File(args[0]);
			try {
				DataInputStream in = new DataInputStream(new FileInputStream(file));
				byte[] b = new byte[in.readInt()];
				in.read(b);
				Map m = (Map) Utils.deserDecompress(b);
				in.close();
				new Server(m).start();
			} catch (Exception ex) {
			
			}
			
		}
		System.out.println("Server started.");
	}
	
	/**
	 * New Server using the specified Map.
	 * 
	 * @param m the Map to use
	 */
	public Server(Map m) {
		map = m;
	}
	
	public static int port = 43056;
	
	ServerLoop sl;
	ServerSocket ss;
	Map map;
	ArrayList<Connection> connections = new ArrayList<Connection>();
	
	/**
	 * Creates a name that is not in use, by appending underscores to the end repeatedly.
	 * 
	 * @param name the base name
	 * @return the name with added underscores
	 */
	public String getUsableName(String name) {
		boolean isInUse = false;
		for (Connection c : connections) {
			if (c.player.name.equalsIgnoreCase(name)) {
				isInUse = true;
			}
		}
		if (isInUse) {
			return getUsableName(name + "_");
		} else {
			return name;
		}
	}
	
	public void run() {
		sl = new ServerLoop(this);
		sl.start();
		try {
			ss = new ServerSocket(port);
			while (true) {
				if (Thread.interrupted()) {
					break;
				}
				Socket s = ss.accept();
				try {
					Connection c = new Connection(s, this);
					connections.add(c);
					c.start();
				} catch (Exception e) {
				}
				
			}
		} catch (Exception e) {
		
		}
	}
	
	/**
	 * Disconnects a user, and messages all other inGame clients of their departure.
	 * 
	 * @param c the Connection that is to be disconnected
	 */
	public void disconnected(Connection c) {
		c.interrupt();
		c.connected = false;
		this.connections.remove(c);
		if (c.inGame) {
			for (Connection co : connections) {
				co.removePlayer(c.player.name);
			}
		}
	}
	
}
