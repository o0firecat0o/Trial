package engine.main;

public class Main {

	private static int width = 1280;
	private static int height = 720;

	private Thread RenderThread;
	private Thread LogicThread;

	public static final Object lock = new Object();

	public static void main(String[] args) {
		new Main().start();
	}

	private void start() {
		RenderThread = new Thread(new Render(), "Render");
		RenderThread.start();
		LogicThread = new Thread(new Logic(), "Logic");
		LogicThread.start();

	}

	// return the width of the screen
	public static int getWidth() {
		return width;
	}

	// return the height of the screen
	public static int getHeight() {
		return height;
	}

	public static void setWidth(int width) {
		Main.width = width;
	}

	public static void setHeight(int height) {
		Main.height = height;
	}
}
