import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import javax.imageio.ImageIO;

/**
 * A <code>class</code> that contains many useful utilities.
 * 
 * @author Leif
 */
public final class Utils {
	
	/**
	 * No instances of <code>Utils</code> may be created.
	 */
	private Utils() {}
	
	/**
	 * Rotates a <code>BufferedImage</code> the specified number of degrees.
	 * 
	 * @param image the image to rotate
	 * @param amount the number of degrees to rotate it
	 * @return the rotated image
	 */
	public static BufferedImage rotate(BufferedImage image, double amount) {
		double locationX = image.getWidth() / 2;
		double locationY = image.getHeight() / 2;
		AffineTransform tx = AffineTransform.getRotateInstance(amount, locationX, locationY);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		return op.filter(image, null);
	}
	
	/**
	 * If n is less then min, returns min else returns n;
	 * 
	 * @param n the number to minimize
	 * @param min the minimum
	 * @return the minimized number
	 */
	public static int min(int n, int min) {
		if (n < min) {
			return min;
		}
		return n;
	}
	
	/**
	 * If n is greater then max, returns max else returns n;
	 * 
	 * @param n the number to maximize
	 * @param min the maximum
	 * @return the maximized number
	 */
	public static int max(int n, int max) {
		if (n > max) {
			return max;
		}
		return n;
	}
	
	/**
	 * Uses both Utils.min and Utils.max on n
	 * 
	 * @param n the number
	 * @param min the min value
	 * @param max the max value
	 * @return the minimized and maximized number
	 */
	public static int minMax(int n, int min, int max) {
		return max(min(n, min), max);
	}
	
	/**
	 * Uses both Utils.min and Utils.max on n
	 * 
	 * @param n the number
	 * @param min the min value
	 * @param max the max value
	 * @return the minimized and maximized number
	 */
	public static int maxMin(int n, int max, int min) {
		return minMax(n, min, max);
	}
	
	/**
	 * Removes the alpha channel from an <code>Image</code>.
	 * 
	 * @param img the image to make opaque
	 * @return the opaque version of the image
	 */
	public static BufferedImage makeOpaque(Image img) {
		BufferedImage image = toBufferedImage(img);
		if (image.getType() == BufferedImage.TYPE_INT_RGB) {
			return image;
		}
		BufferedImage imageNoAlpha = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
		imageNoAlpha.createGraphics().drawImage(img, 0, 0, image.getWidth(), image.getHeight(), null);
		return imageNoAlpha;
	}
	
	/**
	 * Adds an alpha channel to an <code>Image</code>.
	 * 
	 * @param img the image to add alpha
	 * @return the alpha version of the image
	 */
	public static BufferedImage makeAlpha(Image img) {
		BufferedImage image = toBufferedImage(img);
		if (image.getType() == BufferedImage.TYPE_4BYTE_ABGR) {
			return image;
		}
		BufferedImage imageNoAlpha = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		imageNoAlpha.createGraphics().drawImage(img, 0, 0, image.getWidth(), image.getHeight(), null);
		return imageNoAlpha;
	}
	
	/**
	 * Converts an <code>Image</code> to a <code>BufferedImage</code>.
	 * 
	 * @param img the Image to convert
	 * @return the Image as a BufferedImage
	 */
	public static BufferedImage toBufferedImage(Image img) {
		if (img instanceof BufferedImage) {
			return (BufferedImage) img;
		}
		
		// Create a buffered image with transparency
		BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		
		// Draw the image on to the buffered image
		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();
		
		// Return the buffered image
		return bimage;
	}
	
	/**
	 * Saves an <code>Object</code> in a file.
	 * 
	 * @param toSave the Object to save
	 * @param where the file to save it in
	 */
	public static void save(Object toSave, File where) {
		try {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(where));
			out.writeObject(toSave);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Saves a <code>String</code> to a file as plain text.
	 * 
	 * @param toSave the String to save
	 * @param where the file to save it in
	 */
	public static void save(String toSave, File where) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(where));
			out.write(toSave);
			out.flush();
			out.close();
		} catch (Exception e) {
		
		}
	}
	
	/**
	 * Saves an <code>ArrayList</code> of <code>Strings</code> as a plain text file with each String
	 * on a different line.
	 * 
	 * @param toSave the ArrayList of Strings to save
	 * @param where the file to save it in
	 */
	public static void save(ArrayList<String> toSave, File where) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(where));
			String last = toSave.remove(toSave.size() - 1);
			for (String str : toSave) {
				out.write(str);
				out.newLine();
			}
			out.write(last);
			out.flush();
			out.close();
		} catch (Exception e) {
		
		}
	}
	
	/**
	 * Attempts to load an <code>Object</code> from a file, returns <code>null</code> if the object
	 * failed to load.
	 * 
	 * @param where the File to load the Object from
	 * @return the loaded Object
	 */
	public static Object loadObject(File where) {
		Object out = null;
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(where));
			out = in.readObject();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}
	
	/**
	 * Attempts to read a <code>String</code> from the first line of the given plain text file,
	 * returns "" if if fails.
	 * 
	 * @param where the file to load the String from.
	 * @return the loaded String.
	 */
	public static String loadString(File where) {
		String out = "";
		try {
			BufferedReader in = new BufferedReader(new FileReader(where));
			out = in.readLine();
			in.close();
		} catch (Exception e) {
		
		}
		if (out == null) out = "";
		return out;
	}
	
	/**
	 * Attempts to load an <code>ArrayList</code> of <code>String</code> from the given plain text
	 * file. Failure results in an empty array.
	 * 
	 * @param where the file to load the text from
	 * @return the ArrayList of String
	 */
	public static ArrayList<String> loadStringList(File where) {
		ArrayList<String> out = new ArrayList<String>();
		try {
			BufferedReader in = new BufferedReader(new FileReader(where));
			String thisline = in.readLine();
			while (thisline != null) {
				out.add(thisline);
				thisline = in.readLine();
			}
			in.close();
		} catch (Exception e) {
		
		}
		return out;
	}
	
	/**
	 * Attempts to load a <code>String</code> from a plain text file, combining lines with spaces in
	 * between. Returns "" on failure.
	 * 
	 * @param where the file to load
	 * @return the file as a String
	 */
	public static String loadStrings(File where) {
		ArrayList<String> out = new ArrayList<String>();
		try {
			BufferedReader in = new BufferedReader(new FileReader(where));
			String thisline = in.readLine();
			while (thisline != null) {
				out.add(thisline);
				thisline = in.readLine();
			}
			in.close();
		} catch (Exception e) {
		
		}
		String str = "";
		for (String at : out) {
			str += at + " ";
		}
		return str;
	}
	
	/**
	 * Creates all the folders in the given path.
	 * 
	 * @param where the path to create
	 */
	public static void makeDir(File where) {
		where.mkdirs();
	}
	
	/**
	 * Converts the given <code>String</code> to an <code>int</code>, removing all non-number
	 * characters and combining numbers. Returns 0 if there are no number characters.
	 * 
	 * @param number the number to convert as a String
	 * @return the number from the String
	 */
	public static int toInt(String number) {
		int out = 0;
		for (int i = 0; i < number.length(); i++) {
			if (number.charAt(i) == '0') out = out * 10 + 0;
			if (number.charAt(i) == '1') out = out * 10 + 1;
			if (number.charAt(i) == '2') out = out * 10 + 2;
			if (number.charAt(i) == '3') out = out * 10 + 3;
			if (number.charAt(i) == '4') out = out * 10 + 4;
			if (number.charAt(i) == '5') out = out * 10 + 5;
			if (number.charAt(i) == '6') out = out * 10 + 6;
			if (number.charAt(i) == '7') out = out * 10 + 7;
			if (number.charAt(i) == '8') out = out * 10 + 8;
			if (number.charAt(i) == '9') out = out * 10 + 9;
		}
		
		if (number.length() > 1) if (number.charAt(0) == '-') return out * -1;
		return out;
	}
	
	/**
	 * Serializes an Object to an array of bytes.
	 * 
	 * @param toSer the Object to Serialize
	 * @return the Serialized Object as a byte array
	 */
	public static byte[] ser(Object toSer) {
		ByteArrayOutputStream o = new ByteArrayOutputStream();
		ObjectOutputStream out;
		try {
			out = new ObjectOutputStream(o);
			out.writeObject(toSer);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return o.toByteArray();
	}
	
	/**
	 * Deserializes an Object from the given byte array.
	 * 
	 * @param b the Object as bytes
	 * @return the Object
	 * @throws ClassNotFoundException
	 */
	public static Object deser(byte[] b) throws ClassNotFoundException {
		ByteArrayInputStream i = new ByteArrayInputStream(b);
		ObjectInputStream in;
		Object o = null;
		try {
			in = new ObjectInputStream(i);
			o = in.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return o;
	}
	
	/**
	 * Compresses an array of bytes.
	 * 
	 * @param data the bytes to compress
	 * @return the compressed bytes
	 * @throws IOException
	 */
	public static byte[] compress(byte[] data) throws IOException {
		Deflater deflater = new Deflater();
		deflater.setInput(data);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
		deflater.finish();
		byte[] buffer = new byte[1024];
		while (!deflater.finished()) {
			int count = deflater.deflate(buffer); // returns the generated code... index  
			outputStream.write(buffer, 0, count);
		}
		outputStream.close();
		byte[] output = outputStream.toByteArray();
		//System.out.println("Original: " + data.length / 1024.0 + " Kb");  
		//System.out.println("Compressed: " + output.length / 1024.0 + " Kb");  
		return output;
	}
	
	/**
	 * Decompresses an array of bytes.
	 * 
	 * @param data the compressed bytes to decompress.
	 * @return the decompressed array of bytes
	 * @throws IOException
	 * @throws DataFormatException
	 */
	public static byte[] decompress(byte[] data) throws IOException, DataFormatException {
		Inflater inflater = new Inflater();
		inflater.setInput(data);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
		byte[] buffer = new byte[1024];
		while (!inflater.finished()) {
			int count = inflater.inflate(buffer);
			outputStream.write(buffer, 0, count);
		}
		outputStream.close();
		byte[] output = outputStream.toByteArray();
		//System.out.println("Original: " + data.length / 1024.0 + " Kb");  
		//System.out.println("Decompressed: " + output.length / 1024.0 + " Kb");  
		return output;
	}
	
	/**
	 * Serializes an Object to an array of bytes, then compresses it. Like calling
	 * Utils.compress(Utils.ser(o))
	 * 
	 * @param toSer the Object to Serialize
	 * @return the Serialized Object as a byte array
	 */
	public static byte[] serCompress(Object o) throws IOException {
		return compress(ser(o));
	}
	
	/**
	 * Decompresses the given byte array, then deserializes an Object from the result. Like calling
	 * Utils.deser(Utils.decompress(b));
	 * 
	 * @param b the Object as bytes
	 * @return the Object
	 */
	public static Object deserDecompress(byte[] b) throws IOException, ClassNotFoundException, DataFormatException {
		return deser(decompress(b));
	}
	
	/**
	 * Converts the given <code>String</code> to an <code>int</code>, removing all non-number
	 * characters and combining numbers. Returns 0 if there are no number characters.
	 * 
	 * @param number the number to convert as a String
	 * @return the number from the String
	 */
	public static int toIntPos(String number) {
		int out = 0;
		for (int i = 0; i < number.length(); i++) {
			if (number.charAt(i) == '0') out = out * 10 + 0;
			if (number.charAt(i) == '1') out = out * 10 + 1;
			if (number.charAt(i) == '2') out = out * 10 + 2;
			if (number.charAt(i) == '3') out = out * 10 + 3;
			if (number.charAt(i) == '4') out = out * 10 + 4;
			if (number.charAt(i) == '5') out = out * 10 + 5;
			if (number.charAt(i) == '6') out = out * 10 + 6;
			if (number.charAt(i) == '7') out = out * 10 + 7;
			if (number.charAt(i) == '8') out = out * 10 + 8;
			if (number.charAt(i) == '9') out = out * 10 + 9;
		}
		
		return out;
	}
	
	/**
	 * Converts the given <code>String</code> to an <code>double</code>, removing all non-number
	 * characters and combining numbers. The resulting number then has its decimal point inserted.
	 * Returns 0 if there are no number characters.
	 * 
	 * @param number the number to convert as a String
	 * @return the number from the String
	 */
	public static double toDouble(String number) {
		double out = 0;
		int pointPlaces = 1;
		boolean point = false;
		for (int i = 0; i < number.length(); i++) {
			if (point) pointPlaces = pointPlaces * 10;
			if (number.charAt(i) == '0') out = out * 10 + 0;
			if (number.charAt(i) == '1') out = out * 10 + 1;
			if (number.charAt(i) == '2') out = out * 10 + 2;
			if (number.charAt(i) == '3') out = out * 10 + 3;
			if (number.charAt(i) == '4') out = out * 10 + 4;
			if (number.charAt(i) == '5') out = out * 10 + 5;
			if (number.charAt(i) == '6') out = out * 10 + 6;
			if (number.charAt(i) == '7') out = out * 10 + 7;
			if (number.charAt(i) == '8') out = out * 10 + 8;
			if (number.charAt(i) == '9') out = out * 10 + 9;
			if (number.charAt(i) == '.') point = true;
		}
		out = out / pointPlaces;
		if (number.charAt(0) == '-') return out * -1;
		return out;
	}
	
	/**
	 * Turns an image into bytes
	 * 
	 * @param image the image to use
	 * @return the image as bytes
	 * @throws IOException
	 */
	public static byte[] imageToBytes(BufferedImage image) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		DataOutputStream output = new DataOutputStream(out);
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		ImageIO.write(image, "png", buffer);
		output.writeInt(buffer.size());
		buffer.writeTo(output);
		return out.toByteArray();
		
	}
	
	/**
	 * Undoes imageToBytes(BufferedImage image)
	 * 
	 * @param b the image as bytes
	 * @return the original image
	 * @throws IOException
	 */
	public static BufferedImage imageFromBytes(byte[] b) throws IOException {
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(b));
		int size = in.readInt();
		byte[] buffer = new byte[size];
		in.readFully(buffer);
		return ImageIO.read(new ByteArrayInputStream(buffer));
	}
	
	/**
	 * Computes the distance from <code>(fromx, fromy) to (tox, toy)<code>
	 * 
	 * @param fromx the start x
	 * @param fromy the start y
	 * @param tox the end x
	 * @param toy the end y
	 * @return the distance
	 */
	public static double distance(double fromx, double fromy, double tox, double toy) {
		return Math.sqrt((tox - fromx) * (tox - fromx) + (toy - fromy) * (toy - fromy));
	}
	
	/**
	 * Computes the direction an object would point in to get from <code>(fromx, fromy)</code> to
	 * <code>(tox, toy)</code>
	 * 
	 * @param fromx the start x
	 * @param fromy the start y
	 * @param tox the end x
	 * @param toy the end y
	 * @return the direction
	 */
	public static double dir(int fromx, int fromy, int tox, int toy) {
		double deltaX = tox - fromx;
		double deltaY = toy - fromy;
		double rad = (Math.atan2(deltaY, deltaX));
		if ((rad * (180 / Math.PI)) < 0) {
			return 360 - ((rad * (180 / Math.PI)) + 360);
		} else if ((rad * (180 / Math.PI)) > 0) {
			return 360 - (rad * (180 / Math.PI));
		}
		return 0;
		
	}
	
	/**
	 * Returns the amount of X movement required to move 1 step in the direction <code>dir</code>.
	 * Multiply the result by the speed.
	 * 
	 * @param dir the direction
	 * @return the x movement
	 */
	public static double xMove(int dir) {
		return x[Math.abs(dir % 360)];
	}
	
	/**
	 * Returns the amount of Y movement required to move 1 step in the direction <code>dir</code>.
	 * Multiply the result by the speed.
	 * 
	 * @param dir the direction
	 * @return the y movement
	 */
	public static double yMove(int dir) {
		return y[Math.abs(dir % 360)];
	}
	
	/**
	 * Due to not understanding that java Math is in radians, these are the lists of directions.
	 */
	private static double[] x = { 1, 0.9998476951563913, 0.9993908270190958, 0.9986295347545738, 0.9975640502598242, 0.9961946980917455, 0.9945218953682733, 0.992546151641322, 0.9902680687415704, 0.9876883405951378, 0.984807753012208, 0.981627183447664, 0.9781476007338057, 0.9743700647852352, 0.9702957262759965, 0.9659258262890683, 0.9612616959383189, 0.9563047559630354, 0.9510565162951535, 0.9455185755993168, 0.9396926207859084, 0.9335804264972017, 0.9271838545667874, 0.9205048534524404, 0.9135454576426009, 0.9063077870366499, 0.898794046299167, 0.8910065241883679, 0.882947592858927, 0.8746197071393957, 0.8660254037844387, 0.8571673007021123, 0.848048096156426, 0.838670567945424, 0.8290375725550416, 0.8191520442889918, 0.8090169943749475, 0.7986355100472928, 0.788010753606722,
			0.7771459614569709, 0.7660444431189781, 0.754709580222772, 0.7431448254773942, 0.7313537016191706, 0.7193398003386512, 0.7071067811865476, 0.6946583704589974, 0.6819983600624985, 0.6691306063588583, 0.6560590289905074, 0.6427876096865394, 0.6293203910498376, 0.6156614753256583, 0.6018150231520484, 0.5877852522924732, 0.5735764363510462, 0.5591929034707469, 0.5446390350150272, 0.5299192642332049, 0.5150380749100544, 0.5000000000000001, 0.48480962024633717, 0.46947156278589086, 0.4539904997395468, 0.4383711467890774, 0.42261826174069944, 0.40673664307580015, 0.3907311284892739, 0.37460659341591196, 0.3583679495453004, 0.3420201433256688, 0.32556815445715676, 0.30901699437494745, 0.29237170472273677, 0.27563735581699916, 0.25881904510252074, 0.2419218955996679,
			0.22495105434386492, 0.20791169081775945, 0.19080899537654492, 0.17364817766693041, 0.15643446504023092, 0.1391731009600657, 0.12186934340514748, 0.10452846326765344, 0.08715574274765812, 0.06975647374412546, 0.052335956242943966, 0.03489949670250108, 0.017452406437283376, 6.123233995736766e-17, -0.017452406437283473, -0.03489949670250073, -0.05233595624294362, -0.06975647374412534, -0.08715574274765824, -0.10452846326765333, -0.12186934340514738, -0.13917310096006535, -0.15643446504023104, -0.1736481776669303, -0.1908089953765448, -0.20791169081775912, -0.2249510543438648, -0.24192189559966779, -0.25881904510252085, -0.27563735581699905, -0.29237170472273666, -0.30901699437494734, -0.3255681544571564, -0.3420201433256687, -0.35836794954530027, -0.37460659341591207,
			-0.3907311284892736, -0.4067366430758001, -0.42261826174069933, -0.4383711467890775, -0.45399049973954675, -0.46947156278589053, -0.48480962024633695, -0.4999999999999998, -0.5150380749100542, -0.5299192642332048, -0.5446390350150271, -0.5591929034707467, -0.5735764363510458, -0.587785252292473, -0.6018150231520484, -0.6156614753256582, -0.6293203910498373, -0.6427876096865393, -0.6560590289905075, -0.6691306063588582, -0.6819983600624984, -0.694658370458997, -0.7071067811865475, -0.7193398003386513, -0.7313537016191705, -0.743144825477394, -0.754709580222772, -0.7660444431189779, -0.7771459614569707, -0.7880107536067219, -0.7986355100472929, -0.8090169943749473, -0.8191520442889917, -0.8290375725550416, -0.8386705679454242, -0.848048096156426, -0.8571673007021121,
			-0.8660254037844387, -0.8746197071393957, -0.8829475928589268, -0.8910065241883678, -0.898794046299167, -0.9063077870366499, -0.9135454576426008, -0.9205048534524402, -0.9271838545667873, -0.9335804264972017, -0.9396926207859083, -0.9455185755993167, -0.9510565162951535, -0.9563047559630354, -0.9612616959383187, -0.9659258262890682, -0.9702957262759965, -0.9743700647852352, -0.9781476007338057, -0.981627183447664, -0.9848077530122081, -0.9876883405951377, -0.9902680687415703, -0.992546151641322, -0.9945218953682733, -0.9961946980917455, -0.9975640502598242, -0.9986295347545738, -0.9993908270190958, -0.9998476951563913, -1, -0.9998476951563913, -0.9993908270190958, -0.9986295347545738, -0.9975640502598243, -0.9961946980917455, -0.9945218953682734, -0.992546151641322,
			-0.9902680687415703, -0.9876883405951378, -0.984807753012208, -0.981627183447664, -0.9781476007338057, -0.9743700647852352, -0.9702957262759965, -0.9659258262890684, -0.9612616959383189, -0.9563047559630355, -0.9510565162951534, -0.9455185755993167, -0.9396926207859084, -0.9335804264972016, -0.9271838545667874, -0.9205048534524404, -0.9135454576426011, -0.90630778703665, -0.8987940462991671, -0.8910065241883681, -0.8829475928589269, -0.8746197071393959, -0.8660254037844386, -0.8571673007021123, -0.8480480961564261, -0.838670567945424, -0.8290375725550418, -0.819152044288992, -0.8090169943749475, -0.798635510047293, -0.7880107536067222, -0.7771459614569708, -0.7660444431189781, -0.7547095802227719, -0.7431448254773942, -0.7313537016191707, -0.7193398003386511,
			-0.7071067811865477, -0.6946583704589976, -0.6819983600624989, -0.6691306063588585, -0.6560590289905077, -0.6427876096865396, -0.6293203910498372, -0.6156614753256582, -0.6018150231520483, -0.5877852522924732, -0.5735764363510464, -0.5591929034707473, -0.544639035015027, -0.529919264233205, -0.5150380749100545, -0.5000000000000004, -0.48480962024633684, -0.46947156278589075, -0.4539904997395469, -0.43837114678907774, -0.42261826174069994, -0.4067366430758001, -0.3907311284892738, -0.3746065934159123, -0.3583679495453007, -0.3420201433256693, -0.32556815445715664, -0.30901699437494756, -0.29237170472273705, -0.2756373558169989, -0.25881904510252063, -0.2419218955996678, -0.22495105434386528, -0.20791169081775981, -0.19080899537654547, -0.17364817766693033, -0.15643446504023106,
			-0.13917310096006494, -0.12186934340514717, -0.10452846326765335, -0.08715574274765825, -0.06975647374412558, -0.052335956242944306, -0.03489949670250164, -0.017452406437283498, -1.8369701987210297e-16, 0.01745240643728313, 0.03489949670250128, 0.052335956242943946, 0.06975647374412522, 0.08715574274765789, 0.104528463267653, 0.12186934340514768, 0.13917310096006544, 0.15643446504023067, 0.17364817766692997, 0.19080899537654422, 0.20791169081775857, 0.2249510543438649, 0.24192189559966742, 0.2588190451025212, 0.2756373558169994, 0.29237170472273677, 0.30901699437494723, 0.3255681544571563, 0.3420201433256682, 0.35836794954529955, 0.37460659341591196, 0.3907311284892735, 0.4067366430758006, 0.4226182617406996, 0.4383711467890774, 0.45399049973954664, 0.4694715627858904,
			0.48480962024633645, 0.5, 0.515038074910054, 0.5299192642332047, 0.5446390350150266, 0.5591929034707462, 0.573576436351046, 0.5877852522924729, 0.6018150231520479, 0.6156614753256585, 0.6293203910498375, 0.6427876096865393, 0.656059028990507, 0.6691306063588578, 0.6819983600624979, 0.6946583704589966, 0.7071067811865475, 0.7193398003386509, 0.7313537016191707, 0.7431448254773942, 0.7547095802227719, 0.7660444431189779, 0.7771459614569706, 0.7880107536067216, 0.7986355100472928, 0.8090169943749473, 0.8191520442889916, 0.8290375725550414, 0.838670567945424, 0.8480480961564254, 0.8571673007021121, 0.8660254037844384, 0.8746197071393959, 0.882947592858927, 0.8910065241883678, 0.8987940462991673, 0.9063077870366497, 0.913545457642601, 0.9205048534524399, 0.9271838545667873,
			0.9335804264972015, 0.9396926207859084, 0.9455185755993165, 0.9510565162951535, 0.9563047559630357, 0.9612616959383187, 0.9659258262890683, 0.9702957262759965, 0.9743700647852351, 0.9781476007338056, 0.981627183447664, 0.9848077530122079, 0.9876883405951377, 0.9902680687415703, 0.992546151641322, 0.9945218953682733, 0.9961946980917455, 0.9975640502598243, 0.9986295347545738, 0.9993908270190958, 0.9998476951563913 };
	private static double[] y = { 0, -0.01745240643728351, -0.03489949670250097, -0.05233595624294383, -0.0697564737441253, -0.08715574274765817, -0.10452846326765346, -0.12186934340514748, -0.13917310096006544, -0.15643446504023087, -0.17364817766693033, -0.1908089953765448, -0.20791169081775931, -0.224951054343865, -0.24192189559966773, -0.25881904510252074, -0.27563735581699916, -0.29237170472273677, -0.3090169943749474, -0.32556815445715664, -0.3420201433256687, -0.35836794954530027, -0.374606593415912, -0.3907311284892737, -0.40673664307580015, -0.42261826174069944, -0.4383711467890774, -0.45399049973954675, -0.4694715627858908, -0.48480962024633706, -0.49999999999999994, -0.5150380749100542, -0.5299192642332049, -0.5446390350150271, -0.5591929034707469, -0.573576436351046,
			-0.5877852522924731, -0.6018150231520483, -0.6156614753256582, -0.6293203910498374, -0.6427876096865393, -0.6560590289905072, -0.6691306063588582, -0.6819983600624985, -0.6946583704589973, -0.7071067811865475, -0.7193398003386511, -0.7313537016191705, -0.7431448254773941, -0.7547095802227719, -0.766044443118978, -0.7771459614569708, -0.7880107536067219, -0.7986355100472928, -0.8090169943749473, -0.8191520442889917, -0.8290375725550416, -0.8386705679454239, -0.848048096156426, -0.8571673007021121, -0.8660254037844386, -0.8746197071393957, -0.8829475928589269, -0.8910065241883678, -0.898794046299167, -0.9063077870366499, -0.9135454576426009, -0.9205048534524403, -0.9271838545667874, -0.9335804264972017, -0.9396926207859083, -0.9455185755993167, -0.9510565162951535,
			-0.9563047559630354, -0.9612616959383189, -0.9659258262890683, -0.9702957262759965, -0.9743700647852352, -0.9781476007338056, -0.981627183447664, -0.984807753012208, -0.9876883405951378, -0.9902680687415703, -0.992546151641322, -0.9945218953682733, -0.9961946980917455, -0.9975640502598242, -0.9986295347545738, -0.9993908270190958, -0.9998476951563913, -1, -0.9998476951563913, -0.9993908270190958, -0.9986295347545738, -0.9975640502598242, -0.9961946980917455, -0.9945218953682734, -0.9925461516413221, -0.9902680687415704, -0.9876883405951377, -0.984807753012208, -0.981627183447664, -0.9781476007338057, -0.9743700647852352, -0.9702957262759965, -0.9659258262890683, -0.9612616959383189, -0.9563047559630355, -0.9510565162951536, -0.9455185755993168, -0.9396926207859084,
			-0.9335804264972017, -0.9271838545667874, -0.9205048534524404, -0.913545457642601, -0.90630778703665, -0.8987940462991669, -0.8910065241883679, -0.8829475928589271, -0.8746197071393959, -0.8660254037844388, -0.8571673007021123, -0.8480480961564261, -0.838670567945424, -0.8290375725550418, -0.819152044288992, -0.8090169943749475, -0.7986355100472928, -0.788010753606722, -0.7771459614569711, -0.7660444431189781, -0.7547095802227719, -0.7431448254773942, -0.7313537016191707, -0.7193398003386514, -0.7071067811865476, -0.6946583704589971, -0.6819983600624985, -0.6691306063588583, -0.6560590289905073, -0.6427876096865395, -0.6293203910498377, -0.6156614753256584, -0.6018150231520482, -0.5877852522924732, -0.5735764363510464, -0.5591929034707469, -0.5446390350150269,
			-0.5299192642332049, -0.5150380749100544, -0.49999999999999994, -0.48480962024633717, -0.4694715627858911, -0.45399049973954686, -0.4383711467890773, -0.4226182617406995, -0.40673664307580043, -0.39073112848927416, -0.37460659341591224, -0.3583679495453002, -0.3420201433256689, -0.325568154457157, -0.3090169943749475, -0.292371704722737, -0.27563735581699966, -0.258819045102521, -0.24192189559966773, -0.22495105434386475, -0.2079116908177593, -0.19080899537654497, -0.17364817766693025, -0.15643446504023098, -0.13917310096006574, -0.12186934340514755, -0.10452846326765373, -0.08715574274765864, -0.06975647374412552, -0.05233595624294381, -0.0348994967025007, -0.017452406437283435, -1.2246467991473532e-16, 0.017452406437283192, 0.03489949670250089, 0.05233595624294356,
			0.06975647374412483, 0.08715574274765794, 0.10452846326765305, 0.12186934340514774, 0.13917310096006552, 0.15643446504023076, 0.17364817766693047, 0.19080899537654475, 0.20791169081775907, 0.22495105434386498, 0.2419218955996675, 0.25881904510252035, 0.275637355816999, 0.2923717047227364, 0.30901699437494773, 0.3255681544571568, 0.34202014332566866, 0.35836794954530043, 0.374606593415912, 0.39073112848927355, 0.4067366430757998, 0.4226182617406993, 0.43837114678907707, 0.4539904997395463, 0.46947156278589086, 0.48480962024633695, 0.5000000000000001, 0.5150380749100542, 0.5299192642332048, 0.5446390350150271, 0.5591929034707467, 0.5735764363510458, 0.587785252292473, 0.601815023152048, 0.6156614753256578, 0.6293203910498376, 0.6427876096865393, 0.6560590289905075,
			0.6691306063588582, 0.6819983600624984, 0.6946583704589974, 0.7071067811865475, 0.7193398003386509, 0.7313537016191701, 0.743144825477394, 0.7547095802227717, 0.7660444431189779, 0.7771459614569711, 0.788010753606722, 0.7986355100472928, 0.8090169943749473, 0.8191520442889916, 0.8290375725550414, 0.838670567945424, 0.8480480961564258, 0.8571673007021121, 0.8660254037844384, 0.8746197071393959, 0.882947592858927, 0.8910065241883678, 0.8987940462991668, 0.9063077870366497, 0.913545457642601, 0.9205048534524403, 0.9271838545667873, 0.9335804264972016, 0.9396926207859082, 0.9455185755993168, 0.9510565162951535, 0.9563047559630353, 0.961261695938319, 0.9659258262890683, 0.9702957262759965, 0.9743700647852351, 0.9781476007338056, 0.9816271834476639, 0.984807753012208,
			0.9876883405951377, 0.9902680687415704, 0.9925461516413221, 0.9945218953682734, 0.9961946980917455, 0.9975640502598242, 0.9986295347545738, 0.9993908270190957, 0.9998476951563913, 1, 0.9998476951563913, 0.9993908270190958, 0.9986295347545738, 0.9975640502598243, 0.9961946980917455, 0.9945218953682734, 0.992546151641322, 0.9902680687415704, 0.9876883405951378, 0.9848077530122081, 0.9816271834476641, 0.9781476007338058, 0.9743700647852352, 0.9702957262759966, 0.9659258262890682, 0.9612616959383188, 0.9563047559630354, 0.9510565162951536, 0.945518575599317, 0.9396926207859085, 0.9335804264972021, 0.9271838545667874, 0.9205048534524405, 0.9135454576426008, 0.9063077870366498, 0.898794046299167, 0.891006524188368, 0.8829475928589271, 0.8746197071393961, 0.8660254037844386,
			0.8571673007021123, 0.8480480961564262, 0.8386705679454243, 0.8290375725550421, 0.8191520442889918, 0.8090169943749476, 0.7986355100472932, 0.7880107536067218, 0.7771459614569708, 0.7660444431189781, 0.7547095802227722, 0.7431448254773947, 0.731353701619171, 0.7193398003386517, 0.7071067811865477, 0.6946583704589976, 0.6819983600624983, 0.6691306063588581, 0.6560590289905074, 0.6427876096865396, 0.6293203910498378, 0.6156614753256588, 0.6018150231520483, 0.5877852522924732, 0.5735764363510464, 0.5591929034707473, 0.544639035015027, 0.5299192642332058, 0.5150380749100545, 0.5000000000000004, 0.48480962024633684, 0.4694715627858908, 0.45399049973954697, 0.43837114678907696, 0.4226182617407, 0.40673664307580015, 0.39073112848927466, 0.3746065934159123, 0.35836794954530077,
			0.34202014332566855, 0.32556815445715753, 0.3090169943749476, 0.29237170472273627, 0.2756373558169998, 0.2588190451025207, 0.24192189559966787, 0.22495105434386534, 0.20791169081775987, 0.19080899537654467, 0.17364817766693127, 0.15643446504023112, 0.13917310096006588, 0.12186934340514811, 0.10452846326765342, 0.08715574274765832, 0.06975647374412476, 0.05233595624294437, 0.03489949670250082, 0.01745240643728445 };
}
