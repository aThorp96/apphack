package com.gtx;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Tile extends GameObject{

	protected TileType tileType;
	
	public Tile(Vector2 position, Vector2 size, TileType tileType) {
		super(position, size);
		this.tileType = tileType;
	}

	@Override
	public void render(SpriteBatch batch) {
		batch.draw(tileType.getTexture(), position.x - size.x/2, position.y - size.x/2, size.x, size.y);
	}

	public TileType getTileType() {
		return tileType;
	}
}
