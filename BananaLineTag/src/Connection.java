import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Connection extends Thread {
	
	Socket s;
	
	DataInputStream in;
	DataOutputStream out;
	
	Server server;
	
	Player player = new Player();
	
	boolean connected = true;
	boolean inGame = false;
	
	public Connection(Socket s, Server server) throws IOException {
		this.s = s;
		this.server = server;
		in = new DataInputStream(s.getInputStream());
		out = new DataOutputStream(s.getOutputStream());
	}
	
	public void run() {
		if (!connected) return;
		try {
			System.out.println("Login from: " + s.getRemoteSocketAddress());
			out.writeInt(SET_MAP);
			byte[] b = Utils.serCompress(server.map);
			out.writeInt(b.length);
			out.write(b);
			
			for (Connection c : server.connections) {
				if (c != this && c.inGame) {
					addPlayer(c.player.name);
				}
			}
			
			while (true) {
				
				int command = in.readInt();
				
				if (Thread.interrupted()) {
					break;
				}
				
				if (command == Client.SET_USERNAME) {
					setUsername(server.getUsableName(in.readUTF()));
					player.state = Player.STATE_UP;
				} else if (command == Client.MOVE) {
					double x = in.readDouble();
					double y = in.readDouble();
					move(x, y);
				}
			}
		} catch (Exception e) {
			System.out.println(s.getRemoteSocketAddress() + " disconnected.");
		} finally {
			try {
				s.close();
			} catch (IOException e) {}
		}
		server.disconnected(this);
	}
	
	public void setUsername(String name) {
		if (!connected) return;
		try {
			out.writeInt(USERNAME_SET);
			out.writeUTF(name);
			inGame = true;
			for (Connection c : server.connections) {
				if (c != this) {
					System.out.println(c.player.name+", "+name);
					c.removePlayer(player.name);
					c.addPlayer(name);
				}
			}
		} catch (IOException e) {
		}
		System.out.println("Set username of " + s.getRemoteSocketAddress() + " to " + name);
		player.name = name;
		
	}
	
	public void removePlayer(String name) {
		try {
			out.writeInt(REMOVE_PLAYER);
			out.writeUTF(name);
		} catch (IOException e) {
		}
	}
	
	public void addPlayer(String name) {
		try {
			out.writeInt(ADD_PLAYER);
			out.writeUTF(name);
		} catch (IOException e) {
		}
	}
	
	public void move(double x, double y) {
		player.x = x;
		player.y = y;
	}
	
	public void updateLocations() {
		if (!connected) return;
		for (Connection c : server.connections) {
			if (c != this) {
				try {
					out.writeInt(UPDATE_LOCATION);
					out.writeUTF(c.player.name);
					out.writeDouble(c.player.x);
					out.writeDouble(c.player.y);
				} catch (IOException e) {
				}
			}
		}
	}
	
	public void interrupt() {
		try {
			s.close();
		} catch (IOException e) {
		}
		super.interrupt();
	}
	
	/** String name */
	public static final int USERNAME_SET = 0;
	/** String player, double x, double y */
	public static final int UPDATE_LOCATION = 1;
	/** double x, double y */
	public static final int UPDATE_YOUR_LOCATION = 2;
	/** String player */
	public static final int ADD_PLAYER = 3;
	/** String player */
	public static final int REMOVE_PLAYER = 4;
	/** String player */
	@Deprecated
	public static final int DOWN = 5;
	/***/
	@Deprecated
	public static final int UP = 6;
	/** String name, int length, byte[length] Utils.imageToBytes(image) */
	public static final int SET_PLAYER_ICON = 7;
	/** int length, byte[length] Utils.serCompress(map) */
	public static final int SET_MAP = 8;
	/** String name, int state */
	public static final int SET_PLAYER_STATE = 9;
}
