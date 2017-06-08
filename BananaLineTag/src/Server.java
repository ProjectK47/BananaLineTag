import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server extends Thread {
	
	public static void main(String[] args) {
		new Server().start();
	}
	
	public static int port = 43056;
	
	ServerSocket ss;
	
	ArrayList<Connection> connections = new ArrayList<Connection>();
	
	public void run() {
		try {
			ss = new ServerSocket(port);
			while (true) {
				if (Thread.interrupted()) {
					break;
				}
				Socket s = ss.accept();
				try {
					connections.add(new Connection(s, this));
				} catch (Exception e) {}
				
			}
		} catch (Exception e) {
		
		}
	}
	
}
