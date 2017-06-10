import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Client extends Thread {
	
	public static void main(String[] args) {
		new Client().start();
	}
	
	Socket connection;
	
	DataInputStream in;
	DataOutputStream out;
	
	Player self = new Player();
	ArrayList<Player> players = new ArrayList<Player>();
	
	public void run() {
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
				DataInputStream in = new DataInputStream(s.getInputStream());
				DataOutputStream out = new DataOutputStream(s.getOutputStream());
				lw.switchToLoginMode();
				while (!lw.loginPressed) {
					Thread.sleep(100);
				}
				out.writeInt(SET_USERNAME);
				out.writeUTF(lw.username.getText());
				self.name = lw.username.getText();
				connection = s;
				break;
				
			} catch (Exception e) {
				lw.setMessage(e.getMessage());
			}
			lw.connectPressed = false;
		}
		lw.setVisible(false);
		ClientInputThread cit = new ClientInputThread(this);
		cit.start();
		
		
	}
	
	public static final int SET_USERNAME = 0;
	
}
