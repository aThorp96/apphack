package com.gtx;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public abstract class GameObject {
	
	protected Vector2 position;
	protected Vector2 size;
	
	public GameObject(Vector2 position, Vector2 size) {
		this.position = position;
		this.size = size;
	}
	
	public Vector2 getPosition() {
		return position;
	}

	public void setPosition(Vector2 position) {
		this.position = position;
	}

	public Vector2 getSize() {
		return size;
	}

	public abstract void render(SpriteBatch batch);
	
	public BoundingBox getBoundingBox() {
		return new BoundingBox( new Vector3(position.x - size.x/2, position.y - size.y/2,0) , new Vector3(position.x + size.x/2, position.y + size.y/2,0));
	}
	
	
	
}
