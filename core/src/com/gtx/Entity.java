package com.gtx;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.collision.BoundingBox;

public class Entity extends GameObject{

	protected EntityType entityType;
	protected int hp;
	protected Vector2 velocity;
	protected List<GameObject> obstacles = new ArrayList<GameObject>();
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

	public void update(float deltaTime, GameMap map) {
		float oldx = position.x;
		float oldy = position.y;
		position.add(velocity.x * deltaTime, velocity.y * deltaTime);
		int x = (int)Math.round(position.x);
		int y = (int)Math.round(position.y);
		if(handleCollisions(deltaTime, map, oldx, oldy)) {
			position.add(0, velocity.y * deltaTime);
			if(handleCollisions(deltaTime, map, oldx, oldy)) {
				position.add(velocity.x * deltaTime, 0);
				handleCollisions(deltaTime, map, oldx, oldy);
			}
		}
		
	}
	private boolean handleCollisions(float deltaTime, GameMap map, float oldx, float oldy) {
		checkCollisions(map);
		if (obstacles.size() > 0) {
			position.set(oldx, oldy);
			obstacles.clear();
			return true;
		}
		return false;
	}
	

	public void collide(GameObject obstacle) {
		obstacles.add(obstacle);
	}
	private void checkCollisions(GameMap map) {
		BoundingBox box = getBoundingBox();
		for (Entity obstacle : map.entities) {
			if (obstacle == this) {
				continue;
			}
			if (box.intersects(obstacle.getBoundingBox())){
				collide(obstacle);
			}
		}
		for (Tile obstacle : getAdjacentTiles(position, map)) {
			if (obstacle.getTileType() == TileType.WALL) {
				if( box.intersects(obstacle.getBoundingBox())){	
					collide(obstacle);
				}
			}
		}
	}
	
	private Collection<Tile> getAdjacentTiles(Vector2 position, GameMap map) {
		int x = (int)Math.floor(position.x);
		int y = (int)Math.floor(position.y);
		Collection<Tile> adjacents = new ArrayList<Tile>();
		
		for (int i = x - 1 >= 0 ? x-1 : x; i <= x+1; i++) {
			for (int j = y - 1 >= 0 ? y-1 : y; j <= y + 1; j++) {
				adjacents.add(map.map[j][i]);
			}
		}
		return adjacents;
	}

	public void hit(int damage) {
		hp -= damage;
	}
	
	public int getSpeed() {
		return 4;
	}
}
