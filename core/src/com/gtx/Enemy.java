package com.gtx;

import java.lang.Math;
import java.util.Collection;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Enemy extends Entity {

    public static final int ANIMATION_DIRECTIONS = 4;
	// Direction indicate degrees from north the character is pointing CW
	// This can range between 0 and 359.
	protected int direction;
	protected double frame;
	
	public Enemy(Vector2 position, Vector2 size, EntityType entityType) {
		super(position, size, entityType, new Weapon(1, false));
		direction = 3;
	}

	@Override
	public void render(SpriteBatch batch) {
		//note, this is getting the first tile
		//I am leaving room for animations
		if (!this.velocity.isZero()) {
			frame = (frame + 0.25) % ANIMATION_DIRECTIONS;
		} else {
			frame = 0;
		}
			
		batch.draw(entityType.getTextures()[(int) frame][direction], position.x - size.x/2, position.y - size.x/2, size.x, size.y);
	}

	public void update(float deltaTime, GameMap map) {
		super.update(deltaTime, map);
		// Determine which quartile the player is moving in.
		double angle = Math.toDegrees(Math.atan2(velocity.y, velocity.x));
		if (angle == 0 && velocity.x == 0) {
			direction = 3;
		} else if (angle <= 45 && angle >= -45) {
			direction = 0; // right
		} else if (angle <= 135 && angle > 0) {
			direction = 1; // up
		} else if (angle >= -135 && angle < 0) {
			direction = 3; // left
		} else {
			direction = 2; // down
		}
	
	}
	
}
