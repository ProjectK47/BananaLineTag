
public class ServerLoop extends Thread {
	
	Server server;
	
	public ServerLoop(Server s) {
		server = s;
	}
	
	public void run() {
		try {
			while (true) {
				for (Connection c : server.connections) {
					c.updateLocations();
				}
				Thread.sleep(50);
			}
		} catch (Exception e) {
		}
	}
	public void removePlayer(String name) {
		for (Connection c : server.connections) {
			if (!c.player.name.equals(name)) {
				c.removePlayer(name);
			}
		}
	}
	public void addPlayer(String name) {
		for (Connection c : server.connections) {
			if (!c.player.name.equals(name)) {
				c.addPlayer(name);
			}
		}
	}
}
