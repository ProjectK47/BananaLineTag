import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class ClientInputThread extends Thread {
	
	Client c;
	
	public ClientInputThread(Client c) {
		this.c = c;
	}
	
	public void run() {
		try {
			while (true) {
				int command = c.in.readInt();
				//				System.out.println(command);
				if (command == Connection.ADD_PLAYER) {
					Player p = new Player();
					
					p.name = c.in.readUTF();
					c.players.add(p);
					System.out.println("Added Player");
				} else if (command == Connection.REMOVE_PLAYER) {
					String name = c.in.readUTF();
					for (Player p : c.players) {
						if (p.name.equalsIgnoreCase(name)) {
							c.players.remove(p);
							break;
						}
					}
				} else if (command == Connection.UPDATE_LOCATION) {
					String name = c.in.readUTF();
					double x = c.in.readDouble();
					double y = c.in.readDouble();
					for (Player p : c.players) {
						if (p.name.equalsIgnoreCase(name)) {
							p.x = x;
							p.y = y;
							break;
						}
					}
				} else if (command == Connection.UPDATE_YOUR_LOCATION) {
					double x = c.in.readDouble();
					double y = c.in.readDouble();
					c.self.x = x;
					c.self.y = y;
				} else if (command == Connection.USERNAME_SET) {
					c.self.name = c.in.readUTF();
				} else if (command == Connection.SET_MAP) {
					int size = c.in.readInt();
					byte[] b = new byte[size];
					c.in.read(b);
					c.map = (Map) Utils.deserDecompress(b);
					try {
						//Move the player to a valid location on the new Map.
						double x = c.map.lines.get(0).x1;
						double y = c.map.lines.get(0).y1;
						c.self.x = x;
						c.self.y = y;
						
						c.out.writeInt(Client.MOVE);
						c.out.writeDouble(x);
						c.out.writeDouble(y);
					} catch (ArrayIndexOutOfBoundsException e) {
					
					}
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
