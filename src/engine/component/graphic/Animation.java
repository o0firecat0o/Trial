package engine.component.graphic;

import java.util.ArrayList;

import engine.object.Component;

public class Animation extends Component {

	private ArrayList<Integer> textureIDs = new ArrayList<>();

	private ArrayList<IAnimationFunction> Functions = new ArrayList<>();

	@Override
	protected void Update() {

		if (textureIDs.size() == 0) {
			return;
		}

		// run animation
		for (IAnimationFunction function : Functions) {
			if (timer == function.frame) {
				function.run(timer);
			}
		}

		// increase timer
		timer++;
		// reset timer if timer larger than the number of textures
		if (timer >= textureIDs.size()) {
			timer = 0;
		}

		gameObject.GetComponent(SpriteRenderer.class).SetTexture(textureIDs.get(timer));

	}

	private int timer = 0;

	@Override
	protected void Start() throws RuntimeException {
		if (!gameObject.HasComponent(SpriteRenderer.class)) {
			throw new RuntimeException("Add SpriteRenderer Class before adding Animation");
		}
	}

	public Animation Load(String Name) {
		for (int i = 1; i < 29644; i++) {
			if (Texture.hasTexture(Name + " (" + i + ")")) {
				textureIDs.add(Texture.getTexture(Name + " (" + i + ")"));
			} else {
				break;
			}
		}
		return this;
	}

	public void AddFunction(IAnimationFunction function, int frame) throws RuntimeException {
		function.frame = frame;
		Functions.add(function);
		if (frame > textureIDs.size()) {
			throw new RuntimeException("Frame out of texture IDs boundary");
		}
	}

	/**
	 * 
	 * @return return the number of frames the animation has
	 */
	public int returnLastFrame() {
		return textureIDs.size() - 1;
	}

}
