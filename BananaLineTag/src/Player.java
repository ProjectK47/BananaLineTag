import java.awt.image.BufferedImage;

public class Player {
	
	public Player() {
		icon = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		
	}

	String name = "";
	
	String downTo = "";
	
	double x = 0;
	double y = 0;
	
	double oldx = 0;
	double oldy = 0;
	
	int state = STATE_UP;
	
	BufferedImage icon;
	
	
	public static final int STATE_NOT_INGAME = 0;
	public static final int STATE_UP = 1;
	public static final int STATE_DOWN = 2;
	public static final int STATE_RPS = 3;
	
	public static final double PLAYER_SIZE = 0.030;
}
