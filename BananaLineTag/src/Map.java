import java.io.Serializable;
import java.util.ArrayList;

public class Map implements Serializable {
	
	private static final long serialVersionUID = -5245572493686594653L;

	ArrayList<Line> lines = new ArrayList<Line>();
	
	int r = 255;
	int g = 255;
	int b = 255;
}

class Line implements Serializable {
	
	private static final long serialVersionUID = -2263006956721187930L;

	public Line() {}
	
	public Line(double x1, double y1, double x2, double y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
	
	
	double x1 = 0;
	double y1 = 0;
	
	double x2 = 0;
	double y2 = 0;

	int r = 255;
	int g = 255;
	int b = 255;
	int a = 255;
	
	boolean round = false;
	
	double thickness = 0.01;
	
}