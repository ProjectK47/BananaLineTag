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
				if (command == Connection.ADD_PLAYER) {
					Player p = new Player();
					p.name = c.in.readUTF();
					c.players.add(p);
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
				}
			}
		} catch (Exception e) {
			
		}
	}
}
