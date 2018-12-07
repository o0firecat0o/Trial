package engine.component;

import java.awt.*;
import java.awt.image.BufferedImage;

import engine.object.Component;

public class NaiveScreenCapturer extends Component {

	Robot robot;
	Rectangle screensize;

	int width;
	int height;

	@Override
	protected void Update() {

	}

	@Override
	protected void Start() {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}

		screensize = new Rectangle(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
	}

	public BufferedImage getImage() {
		return robot.createScreenCapture(screensize);
	}

	// File outputfile = new File("image.jpg");
	// try {
	// ImageIO.write(image, "jpg", outputfile);
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
}
