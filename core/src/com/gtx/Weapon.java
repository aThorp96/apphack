package com.gtx;

import java.util.Collection;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.collision.BoundingBox;

public class Weapon {
	public static final double SWORD_LENGTH = 1.5;
	private int damage;
	private boolean ranged;
	
	public Weapon(int damage, boolean ranged) {
		this.damage = damage;
		this.ranged = ranged;
	}

	public void heroAttack(Vector2 position, Vector2 vector, Collection<Entity> entities) {
		if (!ranged) {
			vector.nor();
			vector.scl((float)1.5);
		} 
		for (Entity i : entities) {
			if (i instanceof Hero)
				continue;
			BoundingBox bb = i.getBoundingBox();
			Vector3 start = new Vector3(position, (float) 0.0);
			Vector3 end = new Vector3(vector, (float) 0.0).add(start);
			BoundingBox hitBox = new BoundingBox(start, end);
			attackPosition = new Vector2(end.x, end.y);
			attackerPosition = new Vector2(start.x, start.y);
			attacking = true;
			if (bb.intersects(hitBox)) {
				i.hit(damage);
			}
		}
	}
	
	public void enemyAttack(Vector2 position, Vector2 vector, Hero hero) {
		System.out.println("enemy attack");
		if (!ranged) {
			vector.nor();
			vector.scl((float)1.5);
		} 

		BoundingBox bb = hero.getBoundingBox();
		Vector3 start = new Vector3(position, (float) 0.0);
		Vector3 end = new Vector3(vector, (float) 0.0).add(start);
		BoundingBox hitBox = new BoundingBox(start, end);
		attackPosition = new Vector2(end.x, end.y);
		attackerPosition = new Vector2(start.x, start.y);
		attacking = true;
		frames = 0;
		if (bb.intersects(hitBox)) {
			hero.hit(damage);
		}
	}
	
	private Vector2 attackPosition;
	private Vector2 attackerPosition;
	private boolean attacking = false;
	private int frames = 0;
	private static final int ANIMATION_LENGTH = 25;
	private static TextureRegion[][] texture = TextureRegion.split(new Texture("../core/assets/swoosh.png"), 16, 16);
	
	public void render(SpriteBatch batch) {
		
		
		if (attacking) {
			TextureRegion tex = texture[(int)(4f*frames /ANIMATION_LENGTH) % 4][0];
			float width = 1;
			float height = 1;
			frames++;
			//batch.draw(tex, attackPosition.x - width/2, attackPosition.y - height/2, width, height);
			Vector2 diff = new Vector2(attackPosition).sub(attackerPosition);
			diff.nor();
			diff.scl(.7f);
			
			batch.draw(tex, attackPosition.x - diff.x, attackPosition.y - diff.y, 0, 0, width*2, height*2, 1, 1, (float) Math.toDegrees(Math.atan2(diff.y, diff.x) - 90), false);
		}
		
		if (frames > ANIMATION_LENGTH) {
			frames = 0;
			attacking = false;
		}
		
	}
	
}
