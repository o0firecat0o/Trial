package engine.component.physic;

public enum EntityCategory {
	NULL(0x0000), DEFAULT(0x0001), TEAM1(0x0002), TEAM2(0x0004), TEAM1BULLET(0x0008), TEAM2BULLET(0x0010), TEAM1SHIELD(
			0x0020), TEAM2SHIELD(0x0040), HELPER(0x0080), ROCK(0x0100), ALL(0xFFFF);

	private final int value;

	private EntityCategory(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
