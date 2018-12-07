package engine.component.physic;

import java.util.ArrayList;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.joml.Vector2f;

import engine.math.Mathf;
import engine.math.Maths;
import engine.object.Component;

public class RigidFixutre extends Component implements IContact {

	@Override
	protected void Update() {
		if (fixture.getBody() == null) {
			gameObject.InitDestroy();
			return;
		}

		// rotate the offset by the parent angle
		Vector2f rotatedOffset = Mathf.rotateVector(new Vector2f(offset.x * 100, offset.y * 100),
				fixture.getBody().getAngle());

		// this is the true position on the world
		Vector2f position = new Vector2f(fixture.getBody().getPosition().x * RigidBody.RATIO + rotatedOffset.x,
				fixture.getBody().getPosition().y * RigidBody.RATIO + rotatedOffset.y);

		gameObject.transform.position.x = position.x;
		gameObject.transform.position.y = position.y;
		gameObject.transform.rotation = fixture.getBody().getAngle() + rotation;
	}

	@Override
	protected void Start() {

	}

	@Override
	public void Destroy() {
		IContactComponent.clear();
		IContactComponent = null;
		fixture.setUserData(null);
		if (fixture.getBody() != null) {
			fixture.getBody().destroyFixture(fixture);
		}
		fixture = null;
		rigidBody = null;
		super.Destroy();
	}

	ArrayList<IContact> IContactComponent = new ArrayList<IContact>();

	Fixture fixture;
	public RigidBody rigidBody;
	private final float rotation;

	/**
	 * offset relative to the parent body world location Expressed in physical
	 * location
	 */
	final Vector2f offset = new Vector2f();

	@Override
	public void Contact(org.jbox2d.dynamics.contacts.Contact contact, Fixture target) {
		IContactComponent.forEach(component -> component.Contact(contact, target));
	}

	public void RegisterContactCallBack(IContact iContact) {
		IContactComponent.add(iContact);
	}

	public void UnRegisterContactCallBack(IContact iContact) {
		IContactComponent.remove(iContact);
	}

	/**
	 * create the rigidbody BEFORE the fixture
	 */
	public RigidFixutre(ShapeType shapeType, Vector2f size, RigidBody rigidBody, Vector2f offset, float rotation) {
		super();

		this.rigidBody = rigidBody;
		this.rotation = rotation;

		if (shapeType == ShapeType.circle) {
			ChangeFixture(ConstructCircleShape(size, offset, rotation));
		} else if (shapeType == ShapeType.box) {
			Vector2f[] vertices = new Vector2f[4];
			vertices[0] = new Vector2f(0, 0);
			vertices[1] = new Vector2f(1, 0);
			vertices[2] = new Vector2f(1, 1);
			vertices[3] = new Vector2f(0, 1);
			ChangeFixture(ConstructShape(size, offset, vertices, new Vector2f(1, 1), rotation));
		} else if (shapeType == ShapeType.triangle) {
			// Shape of the triangle
			// ....O
			// ...OO
			// ..O.O
			// .O..O
			// OOOOO
			Vector2f[] vertices = new Vector2f[3];
			vertices[0] = new Vector2f(0, 0);
			vertices[1] = new Vector2f(1, 0);
			vertices[2] = new Vector2f(1, 1);
			ChangeFixture(ConstructShape(size, offset, vertices, new Vector2f(1, 1), rotation));
		} else {
			System.err.println("Shape Does not Exist @RigidFixture, Constructor()");
		}

	}

	/**
	 * create the rigidbody BEFORE the fixture
	 */
	public RigidFixutre(Vector2f[] vertices, Vector2f spriteSize, Vector2f size, RigidBody rigidBody, Vector2f offset,
			float rotation) {
		super();

		this.rigidBody = rigidBody;
		this.rotation = rotation;

		ChangeFixture(ConstructShape(size, offset, vertices, spriteSize, rotation));
	}

	public <shape extends Shape> RigidFixutre ChangeFixture(shape S) {
		// remove the original fixture from the rigidBody
		rigidBody.RemoveFixture(this);

		// Create a new fixture
		FixtureDef bodyFixture = new FixtureDef();
		bodyFixture = new FixtureDef();
		bodyFixture.density = 1;

		// assign the shape
		bodyFixture.shape = S;

		rigidBody.AddFixture(this, bodyFixture);
		return this;
	}

	private Shape ConstructCircleShape(Vector2f size, Vector2f offset, float rotation) {
		this.offset.x = (offset.x / RigidBody.RATIO);
		this.offset.y = (offset.y / RigidBody.RATIO);

		CircleShape cShape = new CircleShape();
		cShape.m_radius = size.x;

		cShape.m_p.x = this.offset.x;
		cShape.m_p.y = this.offset.y;

		return cShape;
	}

	/**
	 * 
	 * make irregular shape if you have the vertices
	 * 
	 * @param shapeType
	 * @param size
	 * @param offset
	 * @param vertices   the vertices in term of pixel, for example, (16,80), check
	 *                   in paint.net
	 * @param spriteSize the sprite size of the sprite, example, 160x160 pixels
	 * @param rotation   this rotation only means the rotation with respect to the
	 *                   parent rigidBody
	 * 
	 *                   if you want to apply rotation to the whole gameObject, you
	 *                   should apply rotation to the parent rigidBody directly
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private <shape extends Shape> shape ConstructShape(Vector2f size, Vector2f offset, Vector2f[] vertices,
			Vector2f spriteSize, float rotation) {

		this.offset.x = (offset.x / RigidBody.RATIO);
		this.offset.y = (offset.y / RigidBody.RATIO);

		// create the shape
		PolygonShape bodyShape = new PolygonShape();

		bodyShape.m_vertexCount = vertices.length;

		// rotate the vertices
		for (int i = 0; i < vertices.length; i++) {
			vertices[i] = Mathf.rotateVectorAroundPoint(vertices[i], rotation,
					new Vector2f(spriteSize.x / 2, spriteSize.y / 2));
		}

		// assign the vertices
		for (int i = 0; i < vertices.length; i++) {
			bodyShape.m_vertices[i].set(
					size.x * (vertices[i].x - spriteSize.x / 2) / (spriteSize.x / 2) + this.offset.x,
					size.y * (vertices[i].y - spriteSize.y / 2) / (spriteSize.y / 2) + this.offset.y);
		}

		// normalize the vertices
		for (int i = 0; i < vertices.length; i++) {
			vertices[i].x = (2 * vertices[i].x / spriteSize.x - 1);
			vertices[i].y = (2 * vertices[i].y / spriteSize.y - 1);
		}
		// assign the normals
		Vector2f[] normals = new Vector2f[vertices.length];
		normals = Maths.createNormalfromVertices(vertices);

		for (int i = 0; i < vertices.length; i++) {

			bodyShape.m_normals[i].set(normals[i].x, normals[i].y);
		}

		// bodyShape.m_centroid.setZero();

		return (shape) bodyShape;
	}

	/**
	 * My mask
	 */
	int CategoryBits = 0x0001;
	/**
	 * The mask which I will collide with
	 */
	int MaskBits = 0xFFFF;

	/**
	 * Set my mask
	 * 
	 * @param entityCategory
	 * @return
	 */
	public RigidFixutre setCategoryBits(EntityCategory entityCategory) {
		CategoryBits = entityCategory.getValue();
		fixture.m_filter.categoryBits = CategoryBits;
		return this;
	}

	public RigidFixutre setMaskBits(EntityCategory entityCategory) {
		MaskBits = entityCategory.getValue();
		fixture.m_filter.maskBits = MaskBits;
		return this;
	}

	public RigidFixutre addMaskBits(EntityCategory entityCategory) {
		MaskBits = MaskBits | entityCategory.getValue();
		fixture.m_filter.maskBits = MaskBits;
		return this;
	}

	public int returnCategoryBits() {
		return CategoryBits;
	}

	/**
	 * set the bouncy between 0 and 1
	 * 
	 * @param restitution
	 * @return
	 */
	public RigidFixutre setRestitution(float restitution) {
		fixture.setRestitution(restitution);
		return this;
	}

	public RigidFixutre setFriction(float friction) {
		fixture.setFriction(friction);
		return this;
	}
}
