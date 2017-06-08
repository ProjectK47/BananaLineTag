
import java.util.HashMap;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class RestrictedField extends JTextField {

	private static final long serialVersionUID = -2247916497202537334L;
	char[] allowed = new char[0];
	boolean lowerCase = false;
	int maxLength = 0;
	HashMap<Character, Character> swaps = new HashMap<Character, Character>();
	public RestrictedField(int length, char[] c, int lengthMax) {
		super(length);
		maxLength = lengthMax;
		allowed = c;
		this.getDocument().addDocumentListener(new DocumentListener() {
			private void fix() {
				new Thread() {
					public void run() {
						String text = RestrictedField.this.getText();
						String results = "";
						if (lowerCase) {
							results = results.toLowerCase();
						}
						for (int i = 0; i<text.length(); i++) {
							char c = text.charAt(i);
							if (lowerCase) {
								String s = ""+c;
								c = s.toLowerCase().charAt(0);
							}
							boolean contains = false;
							for (char at : allowed) {
								if (at == c) {
									contains = true;
									break;
								}
							}
							if (contains && !swaps.containsKey(c)) {
								results += c;
							}
							if (swaps.containsKey(c)) {
								results += swaps.get(c);
							}
						}
						if (maxLength>-1 && results.length()>maxLength) {
							results = results.substring(0, maxLength);
						}
						if (!text.equals(results) && (!lowerCase)) {
							int pos = RestrictedField.this.getCaretPosition();
							RestrictedField.this.setText(results);
							if (pos<results.length()) {
							RestrictedField.this.setCaretPosition(pos);
							} else if (results.length()>0) {
								RestrictedField.this.setCaretPosition(results.length());
							}
						}
						if (!text.equals(results) && (lowerCase) && text.equalsIgnoreCase(results)) {
							int pos = RestrictedField.this.getCaretPosition()+1;
							RestrictedField.this.setText(results);
							if (pos<results.length()) {
							RestrictedField.this.setCaretPosition(pos);
							} else if (results.length()>0) {
								RestrictedField.this.setCaretPosition(results.length());
							}
						}
						if (!text.equals(results) && (lowerCase) && !text.equalsIgnoreCase(results)) {
							int pos = RestrictedField.this.getCaretPosition()+1;
							RestrictedField.this.setText(results);
							if (pos<results.length()) {
							RestrictedField.this.setCaretPosition(pos);
							} else if (results.length()>0) {
								RestrictedField.this.setCaretPosition(results.length());
							}
						}
						
						
					}
				}.start();
			}
			public void insertUpdate(DocumentEvent e) {
				fix();
			}
			public void removeUpdate(DocumentEvent e) {
				fix();
			}
			public void changedUpdate(DocumentEvent e) {
				fix();
			}
		});
	}
	
}
class Restrict {
	public static String restrict(String text, char[] chars, int maxLength) {
		String results = "";
		for (int i = 0; i<text.length(); i++) {
			char c = text.charAt(i);
			boolean contains = false;
			for (char at : chars) {
				if (at == c) {
					contains = true;
					break;
				}
			}
			if (contains) {
				results += c;
			}
		}
		if (maxLength>-1 && results.length()>maxLength) {
			results = results.substring(0, maxLength);
		}
		return results;
	}
}