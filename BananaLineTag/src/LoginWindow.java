import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginWindow extends JFrame {
	
	private static final long serialVersionUID = -9187482309670565975L;
	
	public static void main(String[] args) throws Exception {
		LoginWindow lw = new LoginWindow();
		lw.switchToConnectMode();
		lw.setVisible(true);
		lw.setSize(100, 100);
		while (true) {
			while (!lw.connectPressed) {
				Thread.sleep(100);
			}
			lw.switchToLoginMode();
			while (!lw.loginPressed) {
				Thread.sleep(100);
			}
			lw.switchToConnectMode();
		}
	}
	
	JButton connect = new JButton("Connect");
	JTextField ipAddress = new JTextField(20);
	RestrictedField port = new RestrictedField(10, new char[] { '1', '2', '3', '4', '5', '6', '7', '8', '9', '0' }, 5);
	JLabel connectMassage = new JLabel();
	
	JTextField username = new RestrictedField(10, new char[] { '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',  'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '-'}, 10);
	JButton login = new JButton("Login");
	JLabel loginMessage = new JLabel();
	
	volatile boolean loginPressed = false;
	volatile boolean connectPressed = false;
	
	public LoginWindow() {
		connect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				connectPressed = true;
			}
		});
		login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loginPressed = true;
			}
		});
	}
	
	public void setMessage(String text) {
		loginMessage.setText(text);
		connectMassage.setText(text);
	}
	
	public void switchToConnectMode() {
		connectPressed = false;
		this.getContentPane().removeAll();
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		this.add(new JLabel("IP Address:"));
		this.add(ipAddress);
		Dimension d = new Dimension(10000, connect.getMaximumSize().height);
		ipAddress.setMaximumSize(d);
		this.add(new JLabel("Port:"));
		this.add(port);
		port.setMaximumSize(d);
		this.add(connect);
		this.add(connectMassage);
		connectMassage.setText("");
		this.revalidate();
		this.repaint(50L);
	}
	
	public void switchToLoginMode() {
		loginPressed = false;
		this.getContentPane().removeAll();
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		this.add(new JLabel("Username:"));
		this.add(username);
		Dimension d = new Dimension(10000, login.getMaximumSize().height);
		username.setMaximumSize(d);
		this.add(login);
		this.add(loginMessage);
		loginMessage.setText("");
		this.revalidate();
		this.repaint(50L);
	}
	
}