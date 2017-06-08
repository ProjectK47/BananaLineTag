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
	
	public Connection(Socket s, Server server) throws IOException {
		this.s = s;
		this.server = server;
		in = new DataInputStream(s.getInputStream());
		out = new DataOutputStream(s.getOutputStream());
	}
	
	public void run() {
		try {
			System.out.println("Login from: "+s.getRemoteSocketAddress());
			while (true) {
				int command = in.readInt();
				
				if (command == Client.SET_USERNAME) {
					setUsername(server.getUsableName(in.readUTF()));
				}
			}
		} catch (Exception e) {
			System.out.println(s.getRemoteSocketAddress()+" disconnected.");
		}
		server.disconnected(this);
	}
	
	public void setUsername(String name) {
		try {
			out.writeInt(USERNAME_SET);
			out.writeUTF(name);
		} catch (IOException e) {}
		System.out.println("Set username of "+s.getRemoteSocketAddress()+" to "+name);
		player.name = name;
		
	}
	
	
	public static final int USERNAME_SET = 0;
	
	
}
