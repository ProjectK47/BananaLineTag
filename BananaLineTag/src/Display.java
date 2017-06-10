import java.awt.Component;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JPanel;

public class Display extends JPanel {

	private static final long serialVersionUID = 1L;
	
	

}


class SquarePanel extends JPanel implements ComponentListener {
	
	private static final long serialVersionUID = 1L;
	
	public SquarePanel() {
		this.addComponentListener(this);
		this.add(display);
	}

	
	public void componentResized(ComponentEvent e) {
		double widthScaleFactor = (double) (this.getWidth()) / 1;
		double heightScaleFactor = (double) (this.getHeight()) / 1;
		double scaleFactor = (widthScaleFactor > heightScaleFactor ? heightScaleFactor : widthScaleFactor);
		
		int drawWidth = 0;
		int drawHeight = 0;
		
		drawWidth = (int) (scaleFactor * 1);
		
		drawHeight = (int) (scaleFactor * 1);
		
		//		if (widthScaleFactor > heightScaleFactor) {
		//			drawWidth = ((int) (heightScaleFactor * 480)) / (int) (44 * heightScaleFactor) * (int) (44 * heightScaleFactor);
		//			drawHeight = ((int) (heightScaleFactor * 360)) / (int) (44 * heightScaleFactor) * (int) (44 * heightScaleFactor);
		//		} else {
		//			drawWidth = ((int) (widthScaleFactor * 480)) / (int) (44 * widthScaleFactor) * (int) (44 * widthScaleFactor);
		//			drawHeight = ((int) (widthScaleFactor * 360)) / (int) (44 * widthScaleFactor) * (int) (44 * widthScaleFactor);
		//		}
		display.setBounds((this.getWidth()) / 2 - drawWidth / 2, (this.getHeight()) / 2 - drawHeight / 2, drawWidth, drawHeight);
		try {
			Component c = display.getComponent(0);
			c.setBounds(0, 0, drawWidth, drawHeight);
		} catch (Exception ex) {
			
		}
	}

	public void componentMoved(ComponentEvent e) {}

	public void componentShown(ComponentEvent e) {}

	public void componentHidden(ComponentEvent e) {}
	
	JPanel display = new JPanel();
	
}
