import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Connection extends Thread {
	
	Socket s;
	
	DataInputStream in;
	DataOutputStream out;
	
	Server server;
	
	public Connection(Socket s, Server server) throws IOException {
		this.s = s;
		this.server = server;
		in = new DataInputStream(s.getInputStream());
		out = new DataOutputStream(s.getOutputStream());
	}
	
	public void run() {
		try {
			
		} catch (Exception e) {
			
		}
		server.connections.remove(this);
	}
	
}
