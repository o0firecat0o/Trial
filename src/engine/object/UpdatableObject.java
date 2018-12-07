package engine.object;

import java.util.ArrayList;

public class UpdatableObject {

	public static ArrayList<UpdatableObject> AllUpdatableObject = new ArrayList<UpdatableObject>();

	private static ArrayList<UpdatableObject> DestroyNextFrameObjects = new ArrayList<>();

	public int ID;
	private static int IDcount = 0;

	// Destroy all the objects that is flagged as destroy;
	public static void DestroyAllDestroyNextFrameObjects() {
		for (int i = 0; i < DestroyNextFrameObjects.size(); i++) {
			DestroyNextFrameObjects.get(i).Destroy();
		}
		DestroyNextFrameObjects.clear();
	}

	// whether it will stay on stage after switch scence
	public boolean stay_on_stage = false;

	public UpdatableObject() {
		ID = IDcount++;
		AllUpdatableObject.add(this);
		Start();
	}

	// TODO: optimize?
	public static UpdatableObject getUpdatableObject(int UpdatableObjectID) {
		for (int i = 0; i < AllUpdatableObject.size(); i++) {
			if (AllUpdatableObject.get(i).ID == UpdatableObjectID) {
				return AllUpdatableObject.get(i);
			}
		}
		return null;
	}

	/**
	 * get wheter the gameObject still exist
	 * 
	 * @return
	 */
	public static <T extends UpdatableObject> boolean Exist(T updatableObject) {
		if (UpdatableObject.AllUpdatableObject.contains(updatableObject)) {
			return true;
		} else {
			return false;
		}
	}

	protected void Destroy() {
		AllUpdatableObject.remove(this);
	}

	public void InitDestroy() {
		DestroyNextFrameObjects.add(this);
	}

	public void Update() {

	}

	public void Start() {

	}

}
