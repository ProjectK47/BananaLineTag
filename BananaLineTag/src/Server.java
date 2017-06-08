import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server extends Thread {
	
	public static void main(String[] args) {
		new Server().start();
		System.out.println("Server started.");
	}
	
	public static int port = 43056;
	
	ServerSocket ss;
	
	ArrayList<Connection> connections = new ArrayList<Connection>();
	
	/**
	 * Creates a name that is not in use, by appending underscores to the end repeatedly.
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
			return getUsableName(name+"_");
		} else {
			return name;
		}
	}
	
	public void run() {
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
				} catch (Exception e) {}
				
			}
		} catch (Exception e) {
		
		}
	}
	
	public void disconnected(Connection c) {
		this.connections.remove(c);
	}
	
}
