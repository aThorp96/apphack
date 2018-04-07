package com.gtx;

import java.util.ArrayList;
import java.util.Collection;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class GameMap {
	
	private Tile[][] map;
	private Collection<Entity> entities;
	private Vector2 mapSize;
	
	public GameMap(Vector2 mapSize) {		
		entities = new ArrayList<Entity>();
		this.mapSize = mapSize;
		
		map = MapGenerator.generateMap((int)this.mapSize.x, (int)this.mapSize.y);
	}
	
	public void render(SpriteBatch batch) {
		
		for (int row = 0; row < map.length; row++) {
			for (int col = 0; col < map[0].length; col++) {
				map[row][col].render(batch);
			}
		}
		
		for (Entity entity : entities) {
			entity.render(batch);
		}
		
	}
	
	public void update(float deltaTime) {
		
		for ( Entity entity : entities ) {
			entity.update(deltaTime);
		}
		
	}
}
