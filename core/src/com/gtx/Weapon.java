package com.gtx;

import java.util.Collection;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.collision.BoundingBox;

public class Weapon {
	
	private int damage;
	private boolean ranged;
	
	public Weapon(int damage, boolean ranged) {
		this.damage = damage;
		this.ranged = ranged;
	}

	public void attack(Vector2 position, Vector2 vector, Collection<Entity> entities) {
		if (!ranged) {
			vector.nor();
			vector.scl((float)1.5);
		} 
		for (Entity i : entities) {
			BoundingBox bb = i.getBoundingBox();
			Vector3 start = new Vector3(position, (float) 0.0);
			Vector3 end = new Vector3(position, (float) 0.0);
			if (Intersector.intersectRayBoundsFast( new Ray(start, end), bb)) {
				i.hit(damage);
			}
		}
	}
	
	
}
