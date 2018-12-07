package org.gamestate;

import java.util.HashMap;
import java.util.Map;

import org.Stage.Paddle;
import org.Stage.Pong;
import org.joml.Vector2f;
import org.joml.Vector3f;

import engine.component.graphic.MovieTexture;
import engine.component.graphic.SpriteRenderer;
import engine.component.graphic.spriteRendererComponent.DefaultRender;
import engine.component.midiReader.MidiReader;
import engine.component.sound.Sound;
import engine.component.sound.Source;
import engine.gamestate.IGameState;
import engine.object.GameObject;
import engine.object.UIObject;
import engine.object.UIObject.UIPositions;

public class MainGameState implements IGameState {

	GameObject gameObject;

	@Override
	public void Update() {
		try {
			gameObject.GetComponent(SpriteRenderer.class)
					.SetTexture(gameObject.GetComponent(MovieTexture.class).getTextureID());
		} catch (Exception e) {
			System.out.println(e);
		}

		float f = (System.nanoTime() - SystemTime) / 1000000;
		while (true) {
			if (midiReader.Keys.size() == 0) {
				break;
			}
			if (f > midiReader.Keys.get(0).time * Offset2 + Offset) {
				for (Map.Entry<Integer, Paddle> entry : Id_paddle_dictionary.entrySet()) {
					int trackId = entry.getKey();
					Paddle paddle = entry.getValue();
					if (trackId == midiReader.Keys.get(0).track) {
						float wholeDifference = midiReader.MaxNotesOfTrack.get(trackId)
								- midiReader.MinNotesOfTrack.get(trackId);
						float noteDifference = midiReader.Keys.get(0).notes - midiReader.MinNotesOfTrack.get(trackId);
						float ratio = noteDifference / wholeDifference;
						paddle.PlayNotes(ratio);
					}
				}
				midiReader.Keys.remove(0);
			} else {
				break;
			}
		}
		if (f > Offset && y) {
//			GameObject gameObject = new GameObject();
//			gameObject.AddComponent(new Source());
//			gameObject.GetComponent(Source.class).play(Sound.getSound("KDAMIDI"));
//			gameObject.GetComponent(Source.class).setVolume(2f);
//			y = false;
		}
	}

	boolean y = true;

	// offset between the MIDI file start time and the movie start time
	public static int Offset = 15600;
	// 3100 for imaging warrior
	// 15600 for rise
	// 0 for stronger than you
	// 3400 for KDA

	// the bpm offset between the MIDI file and the movie song file
	// = BPM of the midi/actual BPM of the mp4
	public static float Offset2 = 1.00338f;
	// 1.00338f for rise

	@Override
	public void Stop() {

	}

	long SystemTime;
	public static MidiReader midiReader;

	GameObject stage;

	HashMap<Integer, Paddle> Id_paddle_dictionary = new HashMap<>();

	@Override
	public void Init() {

		gameObject = new UIObject(UIPositions.Centered, new Vector2f(), 0);
		gameObject.transform.setScale(new Vector2f(12.8f, 7.2f));
		gameObject.AddComponent(new SpriteRenderer()).addSpriteRendererComponent(new DefaultRender());
		gameObject.AddComponent(new MovieTexture("Rise.MP4"));
		gameObject.AddComponent(new Source());
		gameObject.GetComponent(Source.class).play(Sound.getSound("Rise"));
		gameObject.GetComponent(Source.class).setVolume(0.2f);

		midiReader = new MidiReader();
		midiReader.Read("Rise.mid");

		SystemTime = System.nanoTime();
		;

		stage = new GameObject();
		stage.AddComponent(new org.Stage.Stage());

		GameObject paddle = new GameObject();
		Id_paddle_dictionary.put(0, paddle.AddComponent(new Paddle((float) Math.PI, new Vector3f(0.47f, 0.1f, 0.7f))));
		GameObject paddle2 = new GameObject();
		Id_paddle_dictionary.put(1, paddle2.AddComponent(new Paddle(0, new Vector3f(1f, 0.4f, 0.3f))));
		// Warior 2
		// Rise 0 + 1
		// StrongerThanYou 0 + 1

		GameObject pong = new GameObject();
		pong.AddComponent(new Pong(1, new Vector3f(0.8f, 0.1f, 0)));
		GameObject pong2 = new GameObject();
		pong2.AddComponent(new Pong(2, new Vector3f(0.4f, 0.8f, 0)));
		GameObject pong3 = new GameObject();
		pong3.AddComponent(new Pong(3, new Vector3f(0.2f, 0.0f, 0.8f)));
	}

}
