package com.gtx;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public enum TileType {
	GROUND(1,4),
	WALL(0,0);
	
	private static final int TILE_TEXTURE_SIZE = 16;
	private static final String TILE_TEXTURE_PATH = "RocksFull.png";
	
	private static TextureRegion[][] tileTextures;
	
	private TextureRegion tileTexture;
	private int textureIndexRow;
	private int textureIndexCol;
	
	private TileType(int textureIndexRow, int textureIndexCol) {
		this.textureIndexCol = textureIndexCol;
		this.textureIndexRow = textureIndexRow;
	}
	
	static {
		
		tileTextures = TextureRegion.split( new Texture(TILE_TEXTURE_PATH) , TILE_TEXTURE_SIZE, TILE_TEXTURE_SIZE);
		
		for (TileType tile : TileType.values()) {
			tile.tileTexture = tileTextures[tile.textureIndexRow][tile.textureIndexCol];
		}
	}
	
	public TextureRegion getTexture() {
		return tileTexture;
	}
	
}
