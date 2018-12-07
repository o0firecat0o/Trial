package engine.object;

import java.util.ArrayList;

public class GameObject extends UpdatableObject {

	public final Transform transform = new Transform();

	private String Tag = "Default";

	public boolean blockRayCast = false;

	public void setTag(String Tag) {
		this.Tag = Tag;
	}

	public String getTag() {
		return Tag;
	}

	public static ArrayList<GameObject> getGameObjectswithTag(String Name) {
		ArrayList<GameObject> returnList = new ArrayList<>();
		for (int i = 0; i < UpdatableObject.AllUpdatableObject.size(); i++) {
			UpdatableObject updatableObject = UpdatableObject.AllUpdatableObject.get(i);
			if (updatableObject instanceof GameObject) {
				GameObject gObject = (GameObject) updatableObject;
				if (gObject.getTag().equals(Name)) {
					returnList.add(gObject);
				}
			}
		}
		return returnList;
	}

	@Override
	public void Update() {
		super.Update();
		for (int i = 0; i < ComponentList.size(); i++) {
			ComponentList.get(i).Update();
		}
	}

	public void UpdateRender() {
		for (int i = 0; i < ComponentList.size(); i++) {
			ComponentList.get(i).UpdateRender();
		}
	}

	ArrayList<Component> ComponentList = new ArrayList<Component>();

	public <T extends Component> T AddComponent(T component) throws RuntimeException {
		if (HasComponent(component.getClass())) {
			throw new RuntimeException("Component already existed:" + component.getClass());
		}
		Component.toStartComponent.add(component);
		ComponentList.add(component);
		component.gameObject = this;
		component.Start();
		return component;
	}

	public <T extends Component> T AddComponentByForce(T component) {
		Component.toStartComponent.add(component);
		ComponentList.add(component);
		component.gameObject = this;
		component.Start();
		return component;
	}

	public boolean HasComponent(Class<?> componentClass) {
		for (Component component : ComponentList) {
			if (component.getClass().equals(componentClass))
				return true;
		}
		return false;
	}

	public boolean HasComponentExtendedFromClass(Class<?> componentClass) {
		for (Component component : ComponentList) {
			if (componentClass.isAssignableFrom(componentClass))
				return true;
		}
		return false;
	}

	public boolean HasComponentExtendedFromInterface(Class<?> interfaceClass) {
		for (Component component : ComponentList) {
			if (interfaceClass.isInstance(component))
				return true;
		}
		return false;
	}

	public <T extends Component> T GetComponent(Class<T> componentClass) {
		for (Component component : ComponentList) {
			if (component.getClass().equals(componentClass))
				return (T) component;
		}
		throw new RuntimeException("Missing Component: " + componentClass);
	}

	public <T extends Component> T GetComponentExtendedFromClass(Class<T> componentClass) {
		for (Component component : ComponentList) {
			if (componentClass.isAssignableFrom(component.getClass()))
				return (T) component;
		}
		throw new RuntimeException("Missing Component: " + componentClass);
	}

	public <T extends Component> T GetComponentExtendedFromInterface(Class<?> interfaceClass) {
		for (Component component : ComponentList) {
			if (interfaceClass.isInstance(component))
				return (T) component;
		}
		throw new RuntimeException("Missing Component: " + interfaceClass);
	}

	public <T extends Component> ArrayList<T> GetComponents(Class<T> componentClass) {
		ArrayList<T> arrayList = new ArrayList<>();
		for (Component component : ComponentList) {
			if (component.getClass().equals(componentClass)) {
				arrayList.add((T) component);
			}
		}
		return arrayList;
	}

	public boolean RemoveComponent(Class<?> componentClass) {
		for (Component component : ComponentList) {
			if (component.getClass().equals(componentClass)) {
				component.Destroy();
				return true;
			}
		}
		throw new RuntimeException(
				"There is no componentClass: " + componentClass + "when trying to remove the component");

	}

	@Override
	protected void Destroy() {
		if (ComponentList != null) {
			while (ComponentList.size() >= 1) {
				ComponentList.get(0).Destroy();
			}
		}
		super.Destroy();
	}

	@Override
	public void Start() {
		super.Start();
	}
}
