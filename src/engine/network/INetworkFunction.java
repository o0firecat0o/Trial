package engine.network;

public abstract class INetworkFunction {
	public abstract void run(Object[] objects);

	public int ID;

	public INetworkFunction() {
		ID = MPNetworkManager.RegisterFunction(this);
	}
}
