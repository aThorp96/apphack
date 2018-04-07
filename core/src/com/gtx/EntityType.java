package com.gtx;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public enum EntityType {
	PLAYER(16,16,"../core/assets/TmpTiles.png");
	
	private TextureRegion[][] textures;
	
	private EntityType(int tileWidth, int tileHeight, String texturePath) {
		textures = TextureRegion.split( new Texture(texturePath) , tileWidth, tileHeight);
	}

	
	public TextureRegion[][] getTextures() {
		return textures;
	}
	
}
