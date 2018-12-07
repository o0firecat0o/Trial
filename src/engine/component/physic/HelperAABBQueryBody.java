package engine.component.physic;

import java.util.ArrayList;

import org.jbox2d.callbacks.QueryCallback;
import org.jbox2d.dynamics.Fixture;

public class HelperAABBQueryBody implements QueryCallback {

	ArrayList<Fixture> fixtureList = new ArrayList<>();

	@Override
	public boolean reportFixture(Fixture fixture) {
		fixtureList.add(fixture);
		return true; // keep on continue until find all fixtures
	}

	public void Destory() {
		fixtureList.clear();
		fixtureList = null;
	}

}
