package engine.component.sound;

import org.lwjgl.openal.AL10;

import engine.object.Component;

public class Source extends Component {
	private int sourceId;

	public Source() {
		sourceId = AL10.alGenSources();
		AL10.alSourcef(sourceId, AL10.AL_GAIN, 1);
		AL10.alSourcef(sourceId, AL10.AL_PITCH, 1);
	}

	public void play(int buffer) {
		AL10.alSourcei(sourceId, AL10.AL_BUFFER, buffer);
		AL10.alSourcePlay(sourceId);
	}

	@Override
	public void Destroy() {
		AL10.alDeleteSources(sourceId);
		super.Destroy();
	}

	@Override
	protected void Update() {
		AL10.alSource3f(sourceId, AL10.AL_POSITION, gameObject.transform.position.x, gameObject.transform.position.y,
				0);
	}

	public Source setVolume(float Gain) {
		AL10.alSourcef(sourceId, AL10.AL_GAIN, Gain);
		return this;
	}

	public Source setPitch(float Pitch) {
		AL10.alSourcef(sourceId, AL10.AL_PITCH, Pitch);
		return this;
	}

	@Override
	protected void Start() {

	}
}
