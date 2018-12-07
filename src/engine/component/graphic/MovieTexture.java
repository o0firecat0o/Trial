package engine.component.graphic;

import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glTexImage2D;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL11;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import engine.object.Component;

public class MovieTexture extends Component {

	@Override
	public void UpdateRender() {
		// check whether the next frame is ready
		if (System.currentTimeMillis() >= originalTime + frame * 1000f / fps) {
			frame++;
		} else {
			return;
		}
		// play a movie frame
		cap.read(mat);

		// modify the texture accordingly
		int numbChannels = mat.channels();
		int frameSize = mat.rows() * mat.cols();

		GL11.glBindTexture(GL_TEXTURE_2D, Texture.getTexture(textureName));

		byte[] byteBuffer = new byte[frameSize * numbChannels];
		mat.get(0, 0, byteBuffer);

		ByteBuffer buffer = engine.utils.BufferUtils.createByteBuffer(byteBuffer);

		glTexImage2D(GL_TEXTURE_2D, 0, GL11.GL_RGB, mat.cols(), mat.rows(), 0, 0x80E0, GL_UNSIGNED_BYTE, buffer);

		GL11.glBindTexture(GL_TEXTURE_2D, 0);

		super.UpdateRender();
	}

	VideoCapture cap;
	public String textureName;
	Mat mat = new Mat();
	double fps;
	double time;
	long originalTime = System.currentTimeMillis();
	int frame = 0;

	public int getTextureID() {
		if (Texture.hasTexture(textureName)) {
			return Texture.getTexture(textureName);
		} else {
			return 0;
		}
	}

	public MovieTexture(String location) {
		cap = new VideoCapture();

		cap.open(location);

		if (cap.isOpened() == false) {
			System.out.println(
					"Error loading the video, check whether the video exist, or the library of opencv_ffmpeg400_64 exist");
		} else {
			System.out.println("Loaded video with name: " + location + ", it has "
					+ (int) cap.get(Videoio.CAP_PROP_FRAME_COUNT) + " frames");
		}

		fps = cap.get(Videoio.CAP_PROP_FPS);
		System.out.println();

		// reserve an unused texture name
		textureName = Texture.getUnusedTextureName();
	}

	@Override
	public void StartRender() {
		// create a texture to use
		Texture.createTexture(textureName);

		GL11.glBindTexture(GL_TEXTURE_2D, Texture.getTexture(textureName));

		GL11.glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		GL11.glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

		GL11.glBindTexture(GL_TEXTURE_2D, 0);

		super.StartRender();
	}

	@Override
	protected void Update() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void Start() {
		// TODO Auto-generated method stub

	}
}
