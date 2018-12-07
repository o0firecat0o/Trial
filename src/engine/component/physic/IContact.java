package engine.component.physic;

import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.*;

public interface IContact {
	public abstract void Contact(Contact contact, Fixture target);
}
