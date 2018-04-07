package com.gtx;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Entity extends GameObject{

	protected EntityType entityType;
	protected int hp;
	protected Vector2 velocity;
	protected Weapon weapon;
	
	public Entity(Vector2 position, Vector2 size, EntityType entityType) {
		super(position, size);
		this.entityType = entityType;
		this.velocity = new Vector2();
		weapon = null;
		hp = 10;
	}
	
	public Entity(Vector2 position, Vector2 size, EntityType entityType, Weapon weapon) {
		super(position, size);
		this.entityType = entityType;
		this.velocity = new Vector2();
		this.weapon = weapon;
		hp = 10;
	}

	@Override
	public void render(SpriteBatch batch) {
		//note, this is getting the first tile
		//I am leaving room for animations
		batch.draw(entityType.getTextures()[0][0], position.x - size.x/2, position.y - size.x/2, size.x, size.y);
	}

	public EntityType getEntityType() {
		return entityType;
	}
	
	public Vector2 getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector2 velocity) {
		this.velocity = velocity;
	}
	
	public void setVelocity(float x, float y) {
		this.velocity.set(x, y);
	}

	public void update(float deltaTime) {
		position.add(velocity.x * deltaTime, velocity.y * deltaTime);
	}
	
	public void hit(int damage) {
		hp -= damage;
	}
}
