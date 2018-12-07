package org.Stage;

import java.util.ArrayList;

import org.gamestate.MainGameState;
import org.joml.Vector2f;

import engine.component.graphic.SpriteRenderer;
import engine.component.graphic.Texture;
import engine.component.graphic.spriteRendererComponent.DefaultRender;
import engine.component.physic.EntityCategory;
import engine.component.physic.RigidBody;
import engine.component.physic.RigidFixutre;
import engine.component.physic.ShapeType;
import engine.main.Render;
import engine.math.Mathf;
import engine.object.Component;
import engine.object.GameObject;

public class Stage extends Component {

	@Override
	protected void Update() {
		buildStage();
		if (System.nanoTime()
				- SystemTime > frame * (60000000000f / MainGameState.midiReader.bpm * 2) * MainGameState.Offset2
						+ MainGameState.Offset) {
			scale *= 1.02f;
			frame++;
		}
		scale *= 0.9976f;
		scale = Mathf.clamp(scale, 134, 138);
		dynamicStageEffect.transform.setScale(scale);
	}

	ArrayList<GameObject> stageGO = new ArrayList<>();

	private void buildStage() {
		for (int i = 0; i < stageGO.size(); i++) {
			stageGO.get(i).InitDestroy();
		}
		stageGO.clear();
		for (int i = 0; i < 32; i++) {
			if (i == 16 || i == 0 || i == 8 || i == 24) {
				continue;
			}
			GameObject gameObject = new GameObject();
			gameObject.transform.setPosition(Mathf.Vector2fFromAngle((float) (2 * Math.PI * i / 32)).mul(6400));
			gameObject.transform.setRotation((float) (2 * Math.PI * i / 32));
			gameObject.AddComponent(new RigidBody());
			gameObject.AddComponent(new RigidFixutre(ShapeType.box, new Vector2f(6f, 1.6f),
					gameObject.GetComponent(RigidBody.class), new Vector2f(), 0));
			gameObject.GetComponent(RigidFixutre.class).setRestitution(0.9f);
			gameObject.GetComponent(RigidFixutre.class).setFriction(0);
			gameObject.GetComponent(RigidBody.class).setCategoryBits(EntityCategory.ROCK);
			gameObject.GetComponent(RigidBody.class).setMaskBits(EntityCategory.TEAM1);

			stageGO.add(gameObject);
		}
	}

	// used for the blipping effect
	long SystemTime;
	int frame = 0;

	GameObject staticStage;
	GameObject dynamicStageEffect;
	float scale = 134;

	@Override
	protected void Start() {

		staticStage = new GameObject();
		staticStage.AddComponent(new SpriteRenderer()).addSpriteRendererComponent(new DefaultRender());
		staticStage.transform.setPosition(0);
		staticStage.GetComponent(SpriteRenderer.class).SetTexture(Texture.getTexture("Stage2"));
		staticStage.transform.setScale(136f);

		dynamicStageEffect = new GameObject();
		dynamicStageEffect.AddComponent(new SpriteRenderer()).addSpriteRendererComponent(new DefaultRender())
				.SetFrameBuffer(Render.bloomFrameBuffer);
		dynamicStageEffect.transform.setPosition(0);
		dynamicStageEffect.GetComponent(SpriteRenderer.class).SetTexture(Texture.getTexture("Stage2"));
		dynamicStageEffect.transform.setScale(140f);

		SystemTime = System.nanoTime();

	}

	@Override
	public void Destroy() {
		staticStage.InitDestroy();
		dynamicStageEffect.InitDestroy();
		for (int i = 0; i < stageGO.size(); i++) {
			stageGO.get(i).InitDestroy();
		}
		stageGO.clear();
		super.Destroy();
	}

}
