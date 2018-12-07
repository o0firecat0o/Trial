package engine.timer;

import java.util.ArrayList;

public class Timer {
	private ITimerFunction iTimerFunction;
	private int Threshold = 60;
	private int timer = 0;

	public static ArrayList<Timer> timerArrays = new ArrayList<>();

	public Timer(int Threshold, ITimerFunction iTimerFunction) {
		this.Threshold = Threshold;
		this.iTimerFunction = iTimerFunction;
		timerArrays.add(this);
	}

	public void Update() {
		timer++;
		if (timer > Threshold) {
			timer = 0;
			iTimerFunction.run();
		}
	}

	public void Destroy() {
		iTimerFunction = null;
		timerArrays.remove(this);
	}

	public void SetThreshold(int Threshold) {
		this.Threshold = Threshold;
	}

	public void SetTime(int Time) {
		timer = Time;
	}
}
