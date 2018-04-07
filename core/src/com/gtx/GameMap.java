package com.gtx;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class GameMap implements InputProcessor{
	
	Tile[][] map;
	Collection<Entity> entities;
	Vector2 mapSize;
	
	private HashSet<Integer> pressedKeys;
	
	public GameMap(Vector2 mapSize) {
		pressedKeys = new HashSet<Integer>();
		
		
		this.mapSize = mapSize;
		
		map = MapGenerator.generateMap((int)this.mapSize.x, (int)this.mapSize.y, (int) new Date().getTime());
		
		Hero hero = new Hero(new Vector2(), new Vector2(0.9f,0.9f), EntityType.PLAYER);
		
		int maxNumOfHostiles = 15;
		entities = MapGenerator.placeEntities(map, hero, maxNumOfHostiles);
		
	}
	
	private List<Vector2> getFloorTiles(){
		List<Vector2> floorTiles = new ArrayList<Vector2>();
		for (int row = 0; row < map.length; row++) {
			for (int col = 0; col < map[0].length; col++) {
				if (map[row][col].getTileType() == TileType.GROUND) {
					floorTiles.add(new Vector2(col, row));
				}
			}
		}
		return floorTiles;
	}
	private <T> T randomFromList(List<T> list) {
		return list.remove(new Random().nextInt(list.size()));
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
			
			if (entity.getEntityType() == EntityType.PLAYER) {
				applyInputToPlayer(entity);
				Hero hero = (Hero) entity;
				hero.update(deltaTime, this);
			}
			entity.update(deltaTime, this);
		}
	}

	
	private void applyInputToPlayer(Entity player) {		
		
		int speed = 4;
		
		if (pressedKeys.contains(Keys.W)) {
			player.setVelocity(player.getVelocity().x, speed);
		}
		if (pressedKeys.contains(Keys.S)) {
			player.setVelocity(player.getVelocity().x, -speed);
		}
		if (pressedKeys.contains(Keys.A)) {
			player.setVelocity(-speed, player.getVelocity().y);
		}
		if (pressedKeys.contains(Keys.D)) {
			player.setVelocity(speed, player.getVelocity().y);
		}
		
		if (!(pressedKeys.contains(Keys.W) || pressedKeys.contains(Keys.S))) {
			player.setVelocity(player.getVelocity().x, 0);
		}
		
		if (!(pressedKeys.contains(Keys.A) || pressedKeys.contains(Keys.D))) {
			player.setVelocity(0, player.getVelocity().y);
		}
	}
	
	
	
	@Override
	public boolean keyDown(int keycode) {
		
		pressedKeys.add(keycode);

		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		
		pressedKeys.remove(keycode);
		
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public Collection<Entity> getEntities() {
		return this.entities;
	}
}
