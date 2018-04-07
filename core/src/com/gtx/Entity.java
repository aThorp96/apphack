package com.gtx;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.collision.BoundingBox;

public class Entity extends GameObject{

	protected EntityType entityType;
	protected Vector2 velocity;
	protected List<GameObject> obstacles = new ArrayList<GameObject>();
	
	public Entity(Vector2 position, Vector2 size, EntityType entityType) {
		super(position, size);
		this.entityType = entityType;
		this.velocity = new Vector2();
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
	
	/*private void handleCollisions() {
		BoundingBox box = getBoundingBox();
		for (GameObject obstacle : obstacles) {
			BoundingBox obsBox = obstacle.getBoundingBox();
			if (!getBoundingBox().intersects(obsBox)){
				continue;
			}
			
			float deltaX = 0;
			float deltaY = 0;
			if (box.max.x > obsBox.min.x) {
				deltaX = obsBox.min.x - box.max.x;
			}else if (box.min.x < obsBox.max.x) {
				deltaX = obsBox.max.x - box.min.x;
			}
			if (box.max.y > obsBox.min.y) {
				deltaY = obsBox.min.y - box.max.y;
			}else if (box.min.y < obsBox.max.y) {
				deltaY = obsBox.max.y - box.min.y;
			}

			if (box.max.x > obsBox.min.x && box.min.x < obsBox.max.x) {
				position.add(0, deltaY);
			} else if (box.max.y > obsBox.min.y && box.min.y < obsBox.min.y) {
				position.add(deltaX, 0);
			} else if (Math.abs(deltaY) < Math.abs(deltaX)) {
				position.add(0, deltaY);
			}else {
				position.add(deltaX, 0);
			}
			System.out.println(deltaX + ", " + deltaY);
			//position.add(deltaX, deltaY);
		}		
	}*/
	
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
			if (obstacle.getTileType() == TileType.WALL && 
					box.intersects(obstacle.getBoundingBox())){				
				collide(obstacle);
			}
		}
	}
	
	private Collection<Tile> getAdjacentTiles(Vector2 position, GameMap map) {
		int x = Math.round(position.x);
		int y = Math.round(position.y);
		int startx = x > 0 ? x - 1 : x;
		int endx = x < map.mapSize.x - 1 ? x + 1 : x;
		int starty = y > 0 ? y = 1 : y;
		int endy = y < map.mapSize.y - 1 ? y + 1 : x;
		Collection<Tile> adjacents = new ArrayList<Tile>();
		for (x = startx; x <= endx; x++) {
			for (y = starty; y<=endy; y++) {
				adjacents.add(map.map[x][y]);
			}
		}
		return adjacents;
	}

}
