package engine.random;

import org.joml.Vector2f;

public class Random {

	private static java.util.Random random = new java.util.Random();
	private static java.util.Random random_Network;

	public static void SyncRandom(long l) {
		random_Network = new java.util.Random(l);
	}

	public static Vector2f RandomVector(float Max) {
		return new Vector2f(-Max / 2f + (float) Math.random() * Max, -Max / 2f + (float) Math.random() * Max);
	}

	public static int RandomInt(int min, int max) {
		return random.nextInt((max - min) + 1) + min;
	}

	public static float RandomFloat(float min, float max) {
		return min + random.nextFloat() * (max - min);
	}

	/**
	 * ONLY USE IN UPDATE THREAD
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static float TrueRandomInt(int min, int max) {
		if (random_Network != null) {
			return random_Network.nextInt((max - min) + 1) + min;
		} else {
			return RandomInt(min, max);
		}
	}

	public static float TrueRandomFloat(float min, float max) {
		if (random_Network != null) {
			return min + random_Network.nextFloat() * (max - min);
		} else {
			return RandomFloat(min, max);
		}
	}
}
