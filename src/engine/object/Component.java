package engine.object;

import java.util.ArrayList;

public abstract class Component {
	protected abstract void Update();

	public void UpdateRender() {

	}

	public void StartRender() {

	}

	protected abstract void Start();

	public GameObject gameObject;

	protected void Destroy() {
		gameObject.ComponentList.remove(this);
	}

	public static ArrayList<Component> toStartComponent = new ArrayList<>();

	public static void addStartComponent(Component component) {
		toStartComponent.add(component);
	}
}
